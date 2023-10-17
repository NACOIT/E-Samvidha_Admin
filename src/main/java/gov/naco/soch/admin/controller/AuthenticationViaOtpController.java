package gov.naco.soch.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.AuthenticationService;
import gov.naco.soch.admin.service.SochSystemConfigService;
import gov.naco.soch.admin.service.UserService;
import gov.naco.soch.dto.LoginResponseDtoForOTPLogin;
import gov.naco.soch.dto.SendOtpRequestDto;
import gov.naco.soch.dto.SendOtpResponseDto;
import gov.naco.soch.dto.UserAuthDto;
import gov.naco.soch.dto.ValidateOtpRequestDto;
import gov.naco.soch.projection.BeneficiaryProjectionForMobile;
import gov.naco.soch.security.JwtTokenUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationViaOtpController {
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	UserService userService;
	@Autowired
	private SochSystemConfigService sochSystemConfigService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationViaOtpController.class);
	
	@RequestMapping(value = "/send-otp", method = RequestMethod.POST)
	public SendOtpResponseDto sendOtp(@RequestBody SendOtpRequestDto sendOtpDto) throws Exception {
		SendOtpResponseDto sendOtpResponseDto = null;
		if (sendOtpDto.getEvent().equalsIgnoreCase("change-number")) {
			sendOtpResponseDto = userService.createOtpForUserForMobile(sendOtpDto.getMobileNumber());
			return sendOtpResponseDto;
		}else if (sendOtpDto.getEvent().equalsIgnoreCase("beneficiary-login")){
			authenticationService.checkIfUserExists(sendOtpDto);
			sendOtpResponseDto = userService.createOtpForUserForMobile(sendOtpDto.getMobileNumber());
		}else if (sendOtpDto.getEvent().equalsIgnoreCase("forgot-password")){
			Boolean otpSentFlag = userService.createOtpForUserForMobileForgotPassword(sendOtpDto);
			if(otpSentFlag) {
				sendOtpResponseDto = new SendOtpResponseDto();
				sendOtpResponseDto.setMessage("OTP Successfully Sent");
				sendOtpResponseDto.setUserName(sendOtpDto.getUserName());
				sendOtpResponseDto.setStatusCode("200");
				sendOtpResponseDto.setOtpId(1L);
				
			}
		}
		return sendOtpResponseDto;
	}
	
	@RequestMapping(value = "/validate-otp", method = RequestMethod.POST)
	public LoginResponseDtoForOTPLogin login(@RequestBody ValidateOtpRequestDto validateOtpRequestDto) {
		LoginResponseDtoForOTPLogin loginResponseDtoForOTPLogin = new LoginResponseDtoForOTPLogin();
		UserAuthDto userAuthDto = null;
		boolean artStatus = false;
		String mobileNumber = validateOtpRequestDto.getMobileNumber();
		String otp = validateOtpRequestDto.getOtp();
		Long otpId = validateOtpRequestDto.getOtpId();
		String fcmToken = validateOtpRequestDto.getFcmToken();
		String deviceOsType=validateOtpRequestDto.getDeviceOsType();
		Boolean isEodJobsEnabled=sochSystemConfigService.isEodJobsEnabled();
		if(isEodJobsEnabled.equals(Boolean.TRUE)) {
			logger.error("isEodJobsEnabled ->{} ", isEodJobsEnabled);
			loginResponseDtoForOTPLogin.setCode("200");
			loginResponseDtoForOTPLogin.setMessage("SOCH EOD Jobs are Running,Unable to login to the application....");
		}else {
			boolean validOtp = authenticationService.validateOtp(otpId, otp, mobileNumber);
			if(!validOtp) {
				throw new BadCredentialsException("Invalid OTP. Please try again");
			}
			
			
			loginResponseDtoForOTPLogin.setCode("200");
			loginResponseDtoForOTPLogin.setMessage("OTP validated: success");
			if(validateOtpRequestDto.getEvent().equalsIgnoreCase("change-number")) {
				return loginResponseDtoForOTPLogin;
			}
			BeneficiaryProjectionForMobile beneficiaryDetails = authenticationService.findBenefiaryDetailsByMobileNo(mobileNumber);
			
			boolean updateToken=userService.updateBeneficiaryIdAndFcmDeviceTokenAndType(beneficiaryDetails.getBeneficiaryId(),fcmToken, deviceOsType, mobileNumber);
			
//			Map<String, Object> registeredFacilitiesDetails = authenticationService.getRegisteredFacilityTypes(beneficiaryDetails.getBeneficiaryId());
			final String token = jwtTokenUtil.generateTokenForLoginViaOtp(beneficiaryDetails);
//			if(((List)registeredFacilitiesDetails.get("ART")).size()>0) {
//				artStatus = true;
//			}

			loginResponseDtoForOTPLogin.setToken(token);
			loginResponseDtoForOTPLogin.setBeneficiaryDetails(beneficiaryDetails);
//			loginResponseDtoForOTPLogin.setArtStatus(artStatus);
//			loginResponseDtoForOTPLogin.setRegisteredFacilities(registeredFacilitiesDetails);
		}
		
		return loginResponseDtoForOTPLogin;
	}



}
