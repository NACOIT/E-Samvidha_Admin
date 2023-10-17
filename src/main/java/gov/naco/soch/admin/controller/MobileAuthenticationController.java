package gov.naco.soch.admin.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.AuthenticationService;
import gov.naco.soch.admin.service.CaptchaService;
import gov.naco.soch.admin.service.SochSystemConfigService;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LoginRequest;
import gov.naco.soch.dto.LoginResponseDtoForMobile;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.repository.UserAuthRepository;
import gov.naco.soch.security.JwtTokenUtil;
import gov.naco.soch.security.UserAuthenticationToken;

@RestController
@RequestMapping("/auth/mobile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MobileAuthenticationController {
	
	@Value("${captchaEnabledForMobile}")
	private boolean captchaEnabledForMobile;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserAuthRepository userAuthRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AuthenticationService authService;
	@Autowired
	private SochSystemConfigService sochSystemConfigService;

	private static final Logger logger = LoggerFactory.getLogger(MobileAuthenticationController.class);
	private static BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
	public static final Long TIME_FOR_RESET_LOGIN_ATTEMPT_COUNT = 5*60*1000L;
	public static final Integer ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS = 5;
	
	/*
	 * login entry for the mobile app
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponseDtoForMobile mobileLogin(@RequestBody String data) throws Exception {
		long t0 = System.currentTimeMillis();
		LoginRequest loginUser = new LoginRequest();
		int captchaValid = 2;

		try {
			logger.info("captchaEnabledForMobile: {}", captchaEnabledForMobile);
			logger.info("SOCH - INPUT STRING: {}", data);
			String decodedString = new String(Base64.getDecoder().decode(data));
			logger.info("SOCH - DECODED STRING: {}", decodedString);
			String[] credentials = decodedString.split(":");
			logger.info("SOCH - credentials {}", credentials.toString());

			loginUser.setUsername(credentials[0]);
			loginUser.setPassword(credentials[1]);
			
			if (captchaEnabledForMobile) {
				String captchaId = credentials[2];
				logger.info("SOCH - captchaId: {}", captchaId);
				
				String captchaAnswer = credentials[3];
				logger.info("SOCH - captchaAnswer: {}", captchaAnswer);
				captchaValid = captchaService.validateCaptcha(Long.parseLong(captchaId), captchaAnswer);
			} else {
				captchaValid = 0;
			}
		} catch (Exception ex) {
			logger.error("Exception in validating captcha -> {}", ex);
		}
		Boolean isEodJobsEnabled=sochSystemConfigService.isEodJobsEnabled();
		if(isEodJobsEnabled.equals(Boolean.TRUE)) {
			logger.error("isEodJobsEnabled ->{} ", isEodJobsEnabled);
			throwError("SOCH EOD Jobs are Running,Unable to login to the application....");
		}
		if (loginUser.getUsername() != null) {
			loginUser.setUsername(loginUser.getUsername().trim());
		}
		logger.info("Authentication started for the loginUser.getUserName->{}:", loginUser.getUsername());
		long t1 = System.currentTimeMillis();
		
		//Calling Service
		LoginResponseDtoForMobile userDetails = authenticationService.findLoginUserDetailsForMobile(loginUser.getUsername());
		
		if (userDetails == null || userDetails.getUserId() == null) {
			throw new BadCredentialsException("Invalid user");
		}
		
		LocalDateTime lastLoginAttemptTime = userDetails.getLastLoginAttemptTime();
		Integer loginAttemptCount = userDetails.getLoginAttemptCount();
		
		if(loginAttemptCount == null && lastLoginAttemptTime == null) {
			loginAttemptCount = 0;
			lastLoginAttemptTime = LocalDateTime.now();
		}
		
		//reset login_attempt_count to 0 if 5 minutes have passed from last login attempt
		if(null != lastLoginAttemptTime
				&& lastLoginAttemptTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > TIME_FOR_RESET_LOGIN_ATTEMPT_COUNT) {
			updateLoginAttemptCountAndLastLoginAttemptTime(userDetails.getUserId(),0);
			loginAttemptCount = 0;
			lastLoginAttemptTime = LocalDateTime.now();
		}
		
		if(loginAttemptCount>=ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS) {
			throwError(""+LocalDateTime.now(), ""+(loginAttemptCount), HttpStatus.FORBIDDEN);
		}
		
		//Generating JWT token now
		long t10 = System.currentTimeMillis();
		final String token = jwtTokenUtil.generateTokenForMobile(userDetails);
		
		authenticate(userDetails.getUserId(), userDetails.getUserName(), loginUser.getPassword(), 
				token, userDetails.getAccessCodes(), lastLoginAttemptTime, loginAttemptCount);
		
		logger.error("APIEXECUTIONTIME LOGINEXECUTIONTIME 1 generateToken --> " + (System.currentTimeMillis() - t10)
				+ " ms");

		long t11 = System.currentTimeMillis();
		userDetails.setToken(token);
		long t12 = System.currentTimeMillis();
		authService.setLoginTimeAndToken(userDetails);
		logger.error("APIEXECUTIONTIME LOGINEXECUTIONTIME 12 Update Query for logintime--> "
				+ (System.currentTimeMillis() - t12) + " ms");

		
		logger.error("APIEXECUTIONTIME LOGINEXECUTIONTIME 17 Total time in login before filter--> "
				+ (System.currentTimeMillis() - t0) + " ms");
		return userDetails;
	}
	
	private void authenticate(Long userId, String username, String password, String token, List<String> accessCodes, LocalDateTime lastLoginAttemptTime, Integer loginAttemptCount) {

		List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();
		if (accessCodes != null && !accessCodes.isEmpty()) {
			authorityList = accessCodes.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
		}
		UsernamePasswordAuthenticationToken authentication = new UserAuthenticationToken(username, password, userId,
				token, authorityList);

		// Find super_admin's auth details
		// Allowing Login with super_admin's password
		try {
			authenticationManager.authenticate(authentication);
		} catch (Exception e) {
			String superAdminPassword = userAuthRepository.findPasswordByIdAndIsDeleteAndIsActive(1l);
			if (superAdminPassword != null && bcryptEncoder.matches(password, superAdminPassword)) {
				logger.info("User is using superadmin password, so allowing this user->{}", username);
			}else {
				updateLoginAttemptCountAndLastLoginAttemptTime(userId, loginAttemptCount+1);
				logger.error("Exception in authenticate method for the user->{}, error message --> {}", username, e.getLocalizedMessage());
				throwError(""+LocalDateTime.now(), ""+(loginAttemptCount+1), HttpStatus.UNAUTHORIZED);
			}
		}
		//If authentication is successful, reset login attempt count to 0 again
		updateLoginAttemptCountAndLastLoginAttemptTime(userId,0);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private void updateLoginAttemptCountAndLastLoginAttemptTime(Long userId, Integer loginAttemptCount) {
		userAuthRepository.updateLoginAttemptCountAndLastLoginAttemptTime(userId, loginAttemptCount);
		
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public boolean logout() {
		return authService.logoutForMobile();
	}
	
	private void throwError(String errorfield, String errorFieldValue, HttpStatus httpStatus) {
		
		List<String> detailsSimplified = new ArrayList<String>();
		
		
		List<ErrorDto> errorDtoList = new ArrayList<>();
		
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(errorFieldValue);
		
		errorDtoList.add(errorDto);
		if(httpStatus.equals(HttpStatus.FORBIDDEN)){
			detailsSimplified.add("Login unsuccessful. Maximum login attempts reached. Please retry after 5 minutes.");
			ErrorResponse errorResponse = new ErrorResponse("Login unsuccessful. Maximum login attempts reached. Please retry after 5 minutes.", errorDtoList, detailsSimplified);
			throw new ServiceException("Login unsuccessful. Maximum login attempts reached. Please retry after 5 minutes.", errorResponse, httpStatus);	
		}
		detailsSimplified.add("Invalid username or password");
		ErrorResponse errorResponse = new ErrorResponse("Invalid username or password", errorDtoList, detailsSimplified);
		throw new ServiceException("Invalid username or password", errorResponse, httpStatus);
	}
	
	private void throwError(String errorfield) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(errorfield);
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(errorfield, errorResponse, HttpStatus.BAD_REQUEST);
	}

}
