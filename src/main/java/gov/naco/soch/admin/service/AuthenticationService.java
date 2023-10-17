package gov.naco.soch.admin.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import gov.naco.soch.admin.feign.NotificationFeignClient;
import gov.naco.soch.admin.util.OtpGenerator;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.LoginResponseDtoForMobile;
import gov.naco.soch.dto.MenuItemDto;
import gov.naco.soch.dto.SendOtpRequestDto;
import gov.naco.soch.entity.LoginLog;
import gov.naco.soch.entity.MenuMaster;
import gov.naco.soch.entity.OtpEntity;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.projection.BeneficiaryProjectionForMobile;
import gov.naco.soch.projection.MobileRegisteredFacilityDetailsProjection;
import gov.naco.soch.projection.MobileUserDetailsProjection;
import gov.naco.soch.projection.UserDetailsProjection;
import gov.naco.soch.projection.UserRoleAccessProjection;
import gov.naco.soch.repository.BeneficiaryRepository;
import gov.naco.soch.repository.LoginLogRepository;
import gov.naco.soch.repository.MenuMasterRepository;
import gov.naco.soch.repository.MobileBeneficiaryRepository;
import gov.naco.soch.repository.MobileOtpRepository;
import gov.naco.soch.repository.UserAuthRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.security.JwtTokenUtil;
import gov.naco.soch.security.UserAuthenticationToken;
import gov.naco.soch.util.UserUtils;
import io.jsonwebtoken.Claims;

@Service
@Transactional
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserMasterRepository userRepository;

	@Autowired
	private UserAuthRepository userAuthRepository;

	@Autowired
	private MenuMasterRepository menuMasterRepository;
	@Autowired
	BeneficiaryRepository beneficiaryRepository;
	@Autowired
	MobileBeneficiaryRepository mobileBeneficiaryRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
    @Autowired
    OtpGenerator otpGenerator;
    @Autowired
    MobileOtpRepository mobileOtpRepository;
//    @Autowired
//    NotificationFeignClient notificationFeignClient;
    @Autowired
    private LoginLogRepository loginLogRepository;

	private List<UserDetailsProjection> userDetailList;

	private List<String> accessCodes;

	@Value("${disclaimerFrequencyDays}")
	private Long disclaimerFrequencyDays;
	
	@Value("${validateByOtpEnabled}")
	private boolean validateByOtpEnabled;
	
	@Value("${appleAppStoreReviewEnabled}")
	private boolean appleAppStoreReviewEnabled;
	
	@Value("${appleAppStoreReviewOtp}")
	private String appleAppStoreReviewOtp;
	
	@Value("${appleAppStoreReviewMobileNumber}")
	private String appleAppStoreReviewMobileNumber;

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	public static final Long TIME_FOR_MOBILE_OTP_EXPIRY = 2*60*1000L;

	private static BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	public LoginResponseDto findLoginUserDetails(String username) {
		logger.debug("findLoginUserDetails:Going to fetch the user details for username->{}:", username);
		LoginResponseDto loginUserDetails = new LoginResponseDto();
		loginUserDetails.setUserName(username);
		List<UserDetailsProjection> userDetailList = userRepository.findUserDetails(username);
		logger.debug("User details fetched for username->{}:", username);
		if (!userDetailList.isEmpty()) {
			UserDetailsProjection userDetails = userDetailList.get(0);
			logger.info("User details fetched for username->{}:userDetailList.get(0)->{}:", username, userDetails);
			Long userId = userDetails.getUserId();
			loginUserDetails.setUserId(userDetails.getUserId());
			loginUserDetails.setFirstname(userDetails.getFirstname());
			loginUserDetails.setLastname(userDetails.getLastname());
			loginUserDetails.setContact(userDetails.getContact());
			loginUserDetails.setDivisionId(userDetails.getDivisionId());
			loginUserDetails.setFacilityTypeId(userDetails.getFacilityTypeId());
			loginUserDetails.setFacilityId(userDetails.getFacilityId());
			loginUserDetails.setFacilityName(userDetails.getFacilityName());
			loginUserDetails.setFacilityCode(userDetails.getFacilityCode());
			loginUserDetails.setFacilityNumber(userDetails.getFacilityNumber());
			loginUserDetails.setDesignationId(userDetails.getDesignationId());
			loginUserDetails.setDesignation(userDetails.getDesignation());
			loginUserDetails.setUserName(userDetails.getUsername());
			loginUserDetails.setRoleId(userDetails.getRoleId());
			loginUserDetails.setRoleName(userDetails.getRoleName());
			loginUserDetails.setLastLogin(userDetails.getLastLoginTime());
			loginUserDetails.setFacilityCbStatus(userDetails.getFacilityCbStatus());
			loginUserDetails.setStateId(userDetails.getStateId() != null ? userDetails.getStateId() : null);
			loginUserDetails.setStateAlernateName(
					userDetails.getStateAlernateName() != null ? userDetails.getStateAlernateName() : null);
			loginUserDetails.setDistrictId(userDetails.getDistrictId() != null ? userDetails.getDistrictId() : null);
			loginUserDetails.setDistrictAlernateName(
					userDetails.getDistrictAlernateName() != null ? userDetails.getDistrictAlernateName() : null);
			loginUserDetails.setStateName(userDetails.getStateName());
			loginUserDetails.setDistrictName(userDetails.getDistrictName());
			loginUserDetails.setEmail(userDetails.getEmail());
			loginUserDetails.setSacsId(userDetails.getSacsId());

			LocalDateTime loginTime = LocalDateTime.now();
			String activeKey = loginTime.toString() + new SecureRandom().nextInt(99999);

			loginUserDetails.setCurrentLoginTime(loginTime);
			loginUserDetails.setActiveToken(bcryptEncoder.encode(activeKey));

			if (userDetails.getLastDisclaimerShownDate() != null) {
				Long noOfDays = ChronoUnit.DAYS.between(userDetails.getLastDisclaimerShownDate(), loginTime);
				if (noOfDays != null && noOfDays <= disclaimerFrequencyDays) {
					loginUserDetails.setShowConfidentialityAgreement(Boolean.FALSE);
				} else {
					loginUserDetails.setShowConfidentialityAgreement(Boolean.TRUE);
				}
			} else {
				loginUserDetails.setShowConfidentialityAgreement(Boolean.TRUE);
			}

			logger.debug("loginUserDetails constructed->{}:", loginUserDetails);

			List<UserRoleAccessProjection> userRoleAccessList = getUserRoleAccessByUserId(userId);
			List<String> accessCodes = getUserRolesAccess(userRoleAccessList);
			logger.debug("accessCodes fetched for username->{}:accessCodes->{}:", username, accessCodes);
			loginUserDetails.setAccessCodes(accessCodes);
			List<MenuMaster> menuItems = getMenuItems(userRoleAccessList);
			List<MenuItemDto> menuItemsDto = menuItems.stream().map(m -> {
				MenuItemDto menuItemDto = new MenuItemDto();
				menuItemDto.setId(m.getId());
				menuItemDto.setMenu(m.getMenuName());
				menuItemDto.setRouterLink(m.getRouteLink());
				return menuItemDto;
			}).collect(Collectors.toList());
			loginUserDetails.setMenuItems(menuItemsDto);
		} else {
			logger.warn("User details NOT FOUND for username->{}:", username);
		}
		logger.debug("findLoginUserDetails:Returning for username->{}:", username);
		return loginUserDetails;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> userDetailList = userRepository.findBasicUserDetails(username);
		// List<UserDetailsProjection> userDetailList = this.userDetailList;
		if (userDetailList.isEmpty()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		UserDetailsProjection userDetails = userDetailList.get(0);
		// Long userId = userDetails.getUserId();
		// List<UserRoleAccessProjection> userRoleAccessList =
		// getUserRoleAccessByUserId(userId);
		// List<String> accessCodes = getUserRolesAccess(userRoleAccessList);
		// List<String> accessCodes = this.accessCodes;
		// List<SimpleGrantedAuthority> authorityList = accessCodes.stream().map(r ->
		// new SimpleGrantedAuthority(r))
		// .collect(Collectors.toList());

		// password field to be set
		// return new User(userDetails.getUsername(), userDetails.getPassword(),
		// authorityList);
		return new User(userDetails.getUsername(), userDetails.getPassword(), new ArrayList<>());
	}

	public LoginResponseDto getLoggedInUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = ((UserAuthenticationToken) authentication).getName();
		return findLoginUserDetails(username);
	}

	public LoginResponseDto getLoginUserDetailsFromToken(String token) {

		LoginResponseDto loginUserDetails = new LoginResponseDto();
		String username = jwtTokenUtil.getUsernameFromToken(token);

		Claims tokenClaims = jwtTokenUtil.getClaimsFromToken(token);
		List<String> accessCodes = (List<String>) tokenClaims.get("accessCodes");
		Long userId = ((Integer) tokenClaims.get("userId")).longValue();
		String firstname = (String) tokenClaims.get("firstname");
		String lastname = (String) tokenClaims.get("lastname");
		Long roleId = ((Integer) tokenClaims.get("roleId")).longValue();
		String roleName = (String) tokenClaims.get("roleName");
		Long divisionId = ((Integer) tokenClaims.get("divisionId")).longValue();
		Long facilityTypeId = ((Integer) tokenClaims.get("facilityTypeId")).longValue();
		Optional<Integer> facilityIdOpt = Optional.ofNullable((Integer) tokenClaims.get("facilityId"));
		Long facilityId = facilityIdOpt.isPresent() ? facilityIdOpt.get().longValue() : null;
		Long designationId = ((Integer) tokenClaims.get("designationId")).longValue();
		String designation = ((String) tokenClaims.get("designation"));

		loginUserDetails.setUserId(userId);
		loginUserDetails.setFirstname(firstname);
		loginUserDetails.setLastname(lastname);
		loginUserDetails.setDivisionId(divisionId);
		loginUserDetails.setFacilityTypeId(facilityTypeId);
		loginUserDetails.setFacilityId(facilityId);
		loginUserDetails.setDesignationId(designationId);
		loginUserDetails.setDesignation(designation);
		loginUserDetails.setUserName(username);
		loginUserDetails.setRoleId(roleId);
		loginUserDetails.setRoleName(roleName);
		loginUserDetails.setAccessCodes(accessCodes);
		return loginUserDetails;
	}

	private List<MenuMaster> getMenuItems(List<UserRoleAccessProjection> userRoleAccessList) {
		List<String> accessCode = userRoleAccessList.stream().map(r -> r.getAccessCode()).collect(Collectors.toList());
		List menuList = menuMasterRepository.findMenuByAccessList(accessCode);
		return menuList;
	}

	private List<UserRoleAccessProjection> getUserRoleAccessByUserId(Long userId) {
		List<UserRoleAccessProjection> list = userRepository.getUserRoleAccessByUserId(userId);
		return list;
	}

	private List<String> getUserRolesAccess(List<UserRoleAccessProjection> userRoleAccessList) {

		List<String> accessCodes = new ArrayList<>();
		if (!userRoleAccessList.isEmpty()) {
			accessCodes = userRoleAccessList.stream().map(UserRoleAccessProjection::getAccessCode)
					.collect(Collectors.toList());

		}
		return accessCodes;
	}

	public void setLoginTimeAndToken(LoginResponseDto user) {
		/*
		 * UserAuth userAuthDetails =
		 * userAuthRepository.findByUserMaster(user.getUserId());
		 * userAuthDetails.setLastLoginTime(user.getCurrentLoginTime());
		 * userAuthDetails.setActiveToken(user.getActiveToken());
		 * userAuthRepository.save(userAuthDetails);
		 */
		logger.info(
				"GOING to setLoginTimeAndToken --> user.getUserId()-->{}:user.getUserName()-->{}:user.getActiveToken()-->{}:",
				user.getUserId(), user.getUserName(), user.getActiveToken());
		userAuthRepository.updateLastLoginTime(user.getUserId(), user.getActiveToken());
		logger.info(
				"Prcessed setLoginTimeAndToken --> user.getUserId()-->{}:user.getUserName()-->{}:user.getActiveToken()-->{}:",
				user.getUserId(), user.getUserName(), user.getActiveToken());
	}

	public boolean logout() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		insertLoginLog(currentUser,2L);
		userAuthRepository.deleteUserActiveToken(currentUser.getUserId());
		logger.info("Prcessed logout --> currentUser.getUserId()-->{}:currentUser.getUserName()-->{}:",
				currentUser.getUserId(), currentUser.getUserName());
		return true;
	}

	public Boolean disclaimer() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		logger.info("GOING to setLastDisclaimerShownDate --> currentUser.getUserId()-->{}:", currentUser.getUserId());
		userRepository.updateLastDisclaimerShownDate(currentUser.getUserId());
		logger.info("Processed setLastDisclaimerShownDate --> currentUser.getUserId()-->{}:", currentUser.getUserId());
		return true;
	}
	
	public LoginResponseDtoForMobile findLoginUserDetailsForMobile(String username) {
		logger.debug("findLoginUserDetailsForMobile:Going to fetch the user details for username->{}:", username);
		LoginResponseDtoForMobile loginResponseDtoForMobile = new LoginResponseDtoForMobile();
		loginResponseDtoForMobile.setUserName(username);
		long t2 = System.currentTimeMillis();
		List<MobileUserDetailsProjection> userDetailList = userRepository.findUserDetailsForMobile(username);
		// this.userDetailList = userDetailList;
		logger.error("APIEXECUTIONTIME LOGINEXECUTIONTIME 2 Userdetails fetch Query --> "
				+ (System.currentTimeMillis() - t2) + " ms");
		logger.info("User details fetched for username->{}:", username);
		if (!userDetailList.isEmpty()) {
			MobileUserDetailsProjection userDetails = userDetailList.get(0);
			Long userId = userDetails.getUserId();
			logger.error("User details fetched for username->{}:userDetailList.get(0)->{}:", username, userDetails);
			loginResponseDtoForMobile.setUserId(userDetails.getUserId());
			loginResponseDtoForMobile.setFirstname(userDetails.getFirstname());
			loginResponseDtoForMobile.setLastname(userDetails.getLastname());
			loginResponseDtoForMobile.setRoleId(userDetails.getRoleId());
			loginResponseDtoForMobile.setRoleName(userDetails.getRoleName());
			loginResponseDtoForMobile.setUserName(userDetails.getUsername());
			loginResponseDtoForMobile.setFacilityTypeId(userDetails.getFacilityTypeId());
			loginResponseDtoForMobile.setFacilityId(userDetails.getFacilityId());
			loginResponseDtoForMobile.setDesignation(userDetails.getDesignation());
			loginResponseDtoForMobile.setDivisionId(userDetails.getDivisionId());
			loginResponseDtoForMobile.setDesignationId(userDetails.getDesignationId());
			loginResponseDtoForMobile.setOrwCode(userDetails.getOrwCode());
			loginResponseDtoForMobile.setFacilityCbStatus(userDetails.getFacilityCbStatus());
			loginResponseDtoForMobile.setLastLoginAttemptTime(userDetails.getLastLoginAttemptTime());
			loginResponseDtoForMobile.setLoginAttemptCount(userDetails.getLoginAttemptCount());
			
			LocalDateTime loginTime = LocalDateTime.now();
			String activeKey = loginTime.toString() + new SecureRandom().nextInt(99999);

			loginResponseDtoForMobile.setActiveToken(bcryptEncoder.encode(activeKey));
			
			List<UserRoleAccessProjection> userRoleAccessList = getUserRoleAccessByUserId(userId);
			// logger.error("APIEXECUTIONTIME LOGINEXECUTIONTIME 3 --> "+
			// (System.currentTimeMillis() - t3) + " ms");
			long t4 = System.currentTimeMillis();
			List<String> accessCodes = getUserRolesAccess(userRoleAccessList);
			// this.accessCodes = accessCodes;
			// logger.error("APIEXECUTIONTIME 4 --> "+ (System.currentTimeMillis() - t4) + "
			// ms");
			logger.debug("accessCodes fetched for username->{}:accessCodes->{}:", username, accessCodes);
			loginResponseDtoForMobile.setAccessCodes(accessCodes);
		}
		logger.info("findLoginUserDetails:Returning for username->{}:", username);
		return loginResponseDtoForMobile;

	}
	
	public void setLoginTimeAndToken(LoginResponseDtoForMobile loginResponseDtoForMobile) {

		logger.info(
				"GOING to setLoginTimeAndToken --> user.getUserId()-->{}:user.getUserName()-->{}:user.getActiveToken()-->{}:",
				loginResponseDtoForMobile.getUserId(), loginResponseDtoForMobile.getUserName(), loginResponseDtoForMobile.getActiveToken());
		userAuthRepository.updateLastLoginTime(loginResponseDtoForMobile.getUserId(), loginResponseDtoForMobile.getActiveToken());
		logger.error(
				"Prcessed setLoginTimeAndToken --> user.getUserId()-->{}:user.getUserName()-->{}:user.getActiveToken()-->{}:",
				loginResponseDtoForMobile.getUserId(), loginResponseDtoForMobile.getUserName(), loginResponseDtoForMobile.getActiveToken());
	}
	
	public boolean logoutForMobile() {
		LoginResponseDtoForMobile currentUser = UserUtils.getLoggedInUserDetailsForMobile();
		userAuthRepository.deleteUserActiveToken(currentUser.getUserId());
		logger.info("Processed logout --> currentUser.getUserId()-->{}:currentUser.getUserName()-->{}:",
				currentUser.getUserId(), currentUser.getUserName());
		return true;
	}
	
	public boolean checkIfUserExists(SendOtpRequestDto sendOtpDto) throws Exception {
		List<Object[]> beneficiaryList = new ArrayList<>();
		/*
		 * appleAppStoreReviewEnabled has been added to fulfill App Store requirement of
		 * providing some hard-coded login for beneficiary app on 22nd March 2021
		 */
		if(appleAppStoreReviewEnabled && sendOtpDto.getMobileNumber().equalsIgnoreCase(appleAppStoreReviewMobileNumber)) {
			return true;
		}
		beneficiaryList = beneficiaryRepository.findByMobileNumber(sendOtpDto.getMobileNumber());
		if(beneficiaryList.size() == 1) {
			return true;
		}else if(beneficiaryList.size() > 1) {
			throwError("Method: checkIfUserExists", "Multiple records exist for the mobile number provided. Please contact admin for assistance.", HttpStatus.BAD_REQUEST);
		}else {
			throwError("Method: checkIfUserExists", "Mobile Number does not exist in database", HttpStatus.BAD_REQUEST);
		}
		return false;
	}

//	public SendOtpResponseDto sendOtp(String mobileNumber) {
//		SendOtpResponseDto sendOtpResponseDto = new SendOtpResponseDto();
//		String otp = otpGenerator.generateOtp(4);
//		Long otpId = saveOtp(mobileNumber, otp);
//		//String responseFromNoticationService = sendOtpViaNotificationService(mobileNumber, otp);
//		//commented above line to bypass sending any OTP - after code merge for UAT
//		String responseFromNoticationService = "2000";
//		if(responseFromNoticationService.contains("2000")) {
//			sendOtpResponseDto.setMessage("OTP Successfully Sent");
//			sendOtpResponseDto.setMobileNumber(mobileNumber);
//			sendOtpResponseDto.setStatusCode("200");
//			sendOtpResponseDto.setOtpId(otpId);
//		}else {
//			logger.info("[Exception] in sending OTP: {}", responseFromNoticationService);
//			throwError("Method: sendOtp", "[Exception] in sending OTP:->>> " + responseFromNoticationService, HttpStatus.UNAUTHORIZED);
//			//throw new BadCredentialsException("[Exception] in sending OTP: " + responseFromNoticationService);
//		}
//		return sendOtpResponseDto;
//	}

//	private String sendOtpViaNotificationService(String mobileNumber, String otp) {
//		String response = null;
//		String messageText =  "OTP from Soch Mobile App: " + otp;
//		Map<String, Object> otpMap = new HashMap<String, Object>();
//		otpMap.put("mobileNumber", "+91" + mobileNumber);
//		otpMap.put("messageText", messageText);
//		try {
//		 response = notificationFeignClient.sendOtpNotification(otpMap).getBody();
//		}catch(Exception ex) {
//			logger.info("[Exception] Occurred in feching response from Notification Service");
//			throw ex;
//		}
//		logger.info("Response from Notification Service: {}", response);
//		return response;
//	}

//	private Long saveOtp(String mobileNumber, String otp) {
//		OtpEntity entity = mobileOtpRepository.findByMobileNumber(mobileNumber);
//		if(entity == null) {
//			entity = new OtpEntity();
//			entity.setOtp(otp);
//			entity.setMobileNumber(mobileNumber);
//			entity.setOtpGeneratedTime(LocalDateTime.now());
//			entity.setIsActive(true);
//			entity.setIsDelete(false);
//			entity.setCreatedBy(0);
//			entity.setCreatedTime(LocalDateTime.now());
//			entity.setModifiedBy(0);
//			entity.setModifiedTime(LocalDateTime.now());
//			
//			entity = mobileOtpRepository.save(entity);
//		}else {
//			entity.setOtp(otp);
//			entity.setOtpGeneratedTime(LocalDateTime.now());
//			entity.setModifiedTime(LocalDateTime.now());
//			entity = mobileOtpRepository.save(entity);
//		}
//		logger.info("Saving OTP [{}] in table for mobile number: [{}]", otp, mobileNumber);
//		
//		return entity.getId();
//		
//		
//	}

	public boolean validateOtp(Long otpId, String otp, String mobileNumber) {
		logger.info("Entering in validateOtp method");
		if(appleAppStoreReviewEnabled 
				&& mobileNumber.equalsIgnoreCase(appleAppStoreReviewMobileNumber)
				&& otp.equalsIgnoreCase(appleAppStoreReviewOtp)) {
			logger.info("As appleAppStoreReviewEnabled flag is enabled, so returning true for validateOtp");
			return true;
		}
		
		if(validateByOtpEnabled) {
			logger.info("validateByOtpEnabled is enabled");
			OtpEntity entity = mobileOtpRepository.validateOtp(otpId, otp, mobileNumber);
			if (entity != null
					&& !(entity.getOtpGeneratedTime().until(LocalDateTime.now(), ChronoUnit.MILLIS) > TIME_FOR_MOBILE_OTP_EXPIRY)) {
				return true;
			}else{
				throwError("Method: validateOtp", "The OTP entered is not valid or has expired", HttpStatus.UNAUTHORIZED);
			}
			return false;
		}else {
			logger.info("validateByOtpEnabled is disabled");
			//This is just for testing, need to remove this check after testing completes.
			if(otp.equalsIgnoreCase("8694")) {
				return true;
			}else {
				throwError("Method: validateOtp", "The OTP entered is not valid or has expired", HttpStatus.UNAUTHORIZED);
			}
			throwError("Method: validateOtp", "The validateByOtpEnabled is disabled", HttpStatus.UNAUTHORIZED);
			return false;
		}
	}

	public BeneficiaryProjectionForMobile findBenefiaryDetailsByMobileNo(String mobileNumber) {
		return beneficiaryRepository.findBenefiaryDetailsByMobileNo(mobileNumber).get(0);
	}

public Map<String, Object> getRegisteredFacilityTypes(Long beneficiaryId) {
		
		Map<String, Object> registeredFacilitiesMap = new HashMap<String, Object>();
		List<MobileRegisteredFacilityDetailsProjection> ictcCentres = mobileBeneficiaryRepository.findIctcCentre(beneficiaryId);
		List<MobileRegisteredFacilityDetailsProjection> artCentres = mobileBeneficiaryRepository.findArtCentre(beneficiaryId);
		List<MobileRegisteredFacilityDetailsProjection> tiCentres = mobileBeneficiaryRepository.findTiCentre(beneficiaryId);
		List<MobileRegisteredFacilityDetailsProjection> dsrcCentres = mobileBeneficiaryRepository.findDsrcCentre(beneficiaryId);
		List<MobileRegisteredFacilityDetailsProjection> ostCentres = mobileBeneficiaryRepository.findOstCentre(beneficiaryId);
		
		if(ictcCentres!=null) {
			registeredFacilitiesMap.put("ICTC", ictcCentres);
		}
		if(artCentres!=null) {
			registeredFacilitiesMap.put("ART", artCentres);
		}
		if(tiCentres!=null) {
			registeredFacilitiesMap.put("TI", tiCentres);
		}
		if(dsrcCentres!=null) {
			registeredFacilitiesMap.put("DSRC", dsrcCentres);
		}
		if(ostCentres!=null) {
			registeredFacilitiesMap.put("OST", ostCentres);
		}
		
		return registeredFacilitiesMap;
	}

private void throwError(String errorfield, String errorFieldValue, HttpStatus httpStatus) {
	
	List<String> detailsSimplified = new ArrayList<String>();
	detailsSimplified.add("Class: " + this.getClass().getName());
	detailsSimplified.add(errorfield);
	detailsSimplified.add(errorFieldValue);
	
	List<ErrorDto> errorDtoList = new ArrayList<>();
	ErrorDto errorDto = new ErrorDto();
	errorDto.setField(errorfield);
	errorDto.setDescription(errorFieldValue);
	errorDtoList.add(errorDto);
	
	ErrorResponse errorResponse = new ErrorResponse(errorFieldValue, errorDtoList, detailsSimplified);
	throw new ServiceException(errorFieldValue, errorResponse, httpStatus);
}

public void insertLoginLog(LoginResponseDto user, Long loginStatus) {
	
	LoginLog loginLog=new LoginLog();
	UserMaster userMaster=new UserMaster();
	userMaster.setId(user.getUserId());
	loginLog.setUserMaster(userMaster);
	loginLog.setDateTime(LocalDateTime.now());
	loginLog.setLoginStatus(loginStatus);
	loginLogRepository.save(loginLog);
}
	 
}
