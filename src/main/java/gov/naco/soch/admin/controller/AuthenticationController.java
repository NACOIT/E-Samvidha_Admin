package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.AuthenticationService;
import gov.naco.soch.admin.service.CaptchaService;
import gov.naco.soch.admin.service.SSOAuthService;
import gov.naco.soch.admin.service.SochSystemConfigService;
import gov.naco.soch.constant.SSOAuthConstants;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LoginRequest;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.SSOResponseDto;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.repository.UserAuthRepository;
import gov.naco.soch.security.JwtTokenUtil;
import gov.naco.soch.security.UserAuthenticationToken;
import gov.naco.soch.util.CommonConstants;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthenticationService authService;

	@Autowired
	SSOAuthService ssoAuthService;

	@Autowired
	Environment environment;

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private UserAuthRepository userAuthRepository;
	
	@Autowired
	private SochSystemConfigService sochSystemConfigService;

	@Value("${captchaEnabled}")
	private boolean captchaEnabled;

	@Value("${captchaEnabledForMobile}")
	private boolean captchaEnabledForMobile;
	
	@Value("${ssoEnabled}")
	private boolean ssoEnabled;

	@Value("${isArtVlBackDateEntryAllowed}")
	private boolean isArtVlBackDateEntryAllowed;

	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	private static BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponseDto login(@RequestBody String data) {
		LoginRequest loginUser = new LoginRequest();
		int captchaValid = 2; // 2= Invalid.

		try {
			String decodedString = new String(Base64.getDecoder().decode(data));

			String[] credentials = decodedString.split(":");

			loginUser.setUsername(credentials[0]);
			loginUser.setPassword(credentials[1]);
			if (captchaEnabled) {
				String captchaId = credentials[2];
				String captchaAnswer = credentials[3];
				captchaValid = captchaService.validateCaptcha(Long.parseLong(captchaId), captchaAnswer);
				if (captchaValid == 0) {
					captchaService.updateCaptchaAsUsed(Long.parseLong(captchaId));
				}
			} else {
				captchaValid = 0;
			}
		} catch (Exception ex) {
			logger.error("Exception in validating captcha -> ", ex);
		}
		if (captchaValid == 2) {
			logger.error("captchaValid ->{} ", captchaValid);
			throw new BadCredentialsException("Invalid captcha");
		} else if (captchaValid == 1) {
			logger.error("captchaValid ->{} ", captchaValid);
			throw new BadCredentialsException("Expired captcha");
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
		LoginResponseDto user = authService.findLoginUserDetails(loginUser.getUsername());
		if (user == null || user.getUserId() == null) {
			throw new BadCredentialsException("Invalid user");
		}
		if (user.getRoleId().equals(RoleEnum.PARAMEDICAL_STAFF.getRole())
				|| user.getRoleId()
						.equals(RoleEnum.PARAMEDICAL_STAFF_Lab_Technician_counsellor_staff_nurse_ANM_.getRole())
				|| user.getRoleId().equals(RoleEnum.OST_NURSE.getRole())
				|| user.getRoleId().equals(RoleEnum.TI_CENTRE_ORW.getRole())
				|| user.getRoleId().equals(RoleEnum.TI_CENTRE_PE.getRole())) {
			throw new BadCredentialsException("Invalid user");
		}
		final String token = jwtTokenUtil.generateToken(user);

		authenticate(user.getUserId(), user.getUserName(), loginUser.getPassword(), token, user.getAccessCodes());
		user.setToken(token);
		authService.setLoginTimeAndToken(user);
		authService.insertLoginLog(user,1L);
		
		// To set SSO session ID -[START]
		try {
			if (ssoEnabled) {
			//if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("uat") || env.equalsIgnoreCase("Perf2")))) {
				SSOResponseDto ssoResponseDto = new SSOResponseDto();
				try {
					ssoResponseDto = ssoAuthService
							.registeruserForSSO(SSOAuthConstants.SSO_APP_NAME, user.getUserName()).getBody();
					if (ssoResponseDto.getId() != null && !ssoResponseDto.getId().isEmpty()) {
						user.setSsoSessionId(ssoResponseDto.getId());
					}
					// END
				} catch (Exception e) {
					logger.error("Exception in SSO user registration !",e);
					return user;
				}
			}
		} catch (Exception e) {
			return user;
		}
		user.setIsArtVlBackDateEntryAllowed(isArtVlBackDateEntryAllowed);
		return user;
	}

	@RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
	public LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {

		String token = jwtTokenUtil.getTokenFromHeader(request.getHeader(CommonConstants.HEADER_STRING));
		LoginResponseDto user = authService.getLoginUserDetailsFromToken(token);
		final String newToken = jwtTokenUtil.generateToken(user);
		user.setToken(newToken);
		return user;
	}

	private void authenticate(Long userId, String username, String password, String token, List<String> accessCodes) {

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
			} else {
				logger.error("Exception in authenticate method for the user->{}", username, e);
				throw e;
			}
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	// To Test rempoved once tested.
	@DeleteMapping("/delete/sid/{sessionId}")
	public ResponseEntity<Boolean> deleteRegisteredSession(@PathVariable("sessionId") String sessionId) {
		Boolean isDeleted = true;
		try {
			isDeleted = ssoAuthService.deleteRegisteredSession(sessionId);
		} catch (Exception e) {
			isDeleted = false;
		}
		return new ResponseEntity<Boolean>(isDeleted, HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public boolean logout() {
		return authService.logout();
	}



	@RequestMapping(value = "/disclaimer", method = RequestMethod.GET)
	public Boolean disclaimer() {
		return authService.disclaimer();
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
