package gov.naco.soch.admin.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.controller.UserController;
import gov.naco.soch.admin.dto.UserSearchResponseDto;
import gov.naco.soch.admin.util.NotificationUtil;
import gov.naco.soch.criteria.SearchCriteria;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.LoginResponseDtoForMobile;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.SendOtpRequestDto;
import gov.naco.soch.dto.SendOtpResponseDto;
import gov.naco.soch.dto.TypologyUserMappingDto;
import gov.naco.soch.dto.UpdatePasswordDto;
import gov.naco.soch.dto.UserAuthDto;
import gov.naco.soch.dto.UserDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.entity.Division;
import gov.naco.soch.entity.DivisionAdminDivisionMapping;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.FacilityType;
import gov.naco.soch.entity.OtpEntity;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.TypologyMaster;
import gov.naco.soch.entity.TypologyUserMapping;
import gov.naco.soch.entity.UserAuth;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.entity.UserRoleMapping;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.enums.NotificationEventIdEnum;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.AccessSettingsMapper;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.UserMapperUtil;
import gov.naco.soch.projection.UserListProjection;
import gov.naco.soch.repository.BeneficiaryRepository;
import gov.naco.soch.repository.DivisionAdminDivisionMappingRepository;
import gov.naco.soch.repository.DivisionRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.FacilityTypeRepository;
import gov.naco.soch.repository.MobileOtpRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.repository.TypologyRepository;
import gov.naco.soch.repository.TypologyUserMappingRepository;
import gov.naco.soch.repository.UserAuthRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.repository.UserRoleMappingRepository;
import gov.naco.soch.specifications.UserSpecification;
import gov.naco.soch.util.CommonConstants;
import gov.naco.soch.util.UserUtils;

@Transactional
@Service
public class UserService {
	@Value("${otpExpiryInSeconds:120}")
	private long secondsBeforeOtpExpiry;

	@Value("${accessKey}")
	private String accessKey;

	@Value("${exportRecordsLimit}")
	private Integer exportRecordsLimit;
	
	@Value("${appleAppStoreReviewEnabled}")
	private boolean appleAppStoreReviewEnabled;
	
	@Value("${appleAppStoreReviewMobileNumber}")
	private String appleAppStoreReviewMobileNumber;
	
	@Value("${validateByOtpEnabled}")
	private boolean validateByOtpEnabled;

	@Autowired
	private UserMasterRepository userRepository;
    @Autowired
    MobileOtpRepository mobileOtpRepository;
	@Autowired
	private BeneficiaryRepository beneficiaryRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private TypologyUserMappingRepository typologyUserMappingRepository;

	@Autowired
	private DivisionAdminDivisionMappingRepository divisionAdminDivisionMappingRepository;

	@Autowired
	private UserAuthRepository userAuthRepository;

	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private FacilityTypeRepository facilityTypeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TypologyRepository typologyRepository;
	@Autowired
	UserLdapService userLdapService;

	@Autowired
	private NotificationEventRepository notificationEventRepository;

	@Autowired
	private NotificationUtil notificationUtil;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	// function to select all user details from database
	public UserSearchResponseDto getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortType) {

		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		UserSearchResponseDto userResponse = new UserSearchResponseDto();
		List<UserListProjection> userList = new ArrayList<>();
		Page<UserListProjection> userPage = null;
		Optional<List> userListOptional = null;
		int userCount = 0;

		if (currentUser != null && currentUser.getFacilityId() != null) {
			if (currentUser.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()) {
				userCount = userRepository.findCountOfTotalRecord(currentUser.getUserId());
				userPage = userRepository.findAllUserList(currentUser.getUserId(), pageable);
				userListOptional = Optional.ofNullable(userPage.getContent());
			} else {
				userCount = userRepository.findCountOfTotalRecord(currentUser.getUserId(), currentUser.getFacilityId());
				userPage = userRepository.findAllUserList(currentUser.getUserId(), currentUser.getFacilityId(),
						pageable);
				userListOptional = Optional.ofNullable(userPage.getContent());
			}
		}
		if (userListOptional.isPresent()) {
			userList = userListOptional.get();
		}
		List<UserMasterDto> userMasterDtos = UserMapperUtil.mapUserListProjectionListToUserMasterDtoList(userList);
		userResponse.setActualRecordCount(userCount);
		userResponse.setGivenRecordCount(userMasterDtos.size());
		userResponse.setUserMasterList(userMasterDtos);
		return userResponse;
	}

	public List<UserDto> getAllFacilityUsers(Long facilityId) {

		List<UserMaster> users = (List<UserMaster>) userRepository.findAllByFacilityIdAndIsDelete(facilityId,
				Boolean.FALSE);
		List<UserDto> userDtoList = UserMapperUtil.mapToUserDtoList(users);
		Collections.sort(userDtoList);
		return userDtoList;
	}

	// function to add user details to database

	public UserMasterDto saveUser(Long facilityTypeId, UserMasterDto userMasterDto) {
		userMasterDto.setEmail(userMasterDto.getEmail() != null ? userMasterDto.getEmail().toLowerCase() : null);
		userMasterDto.setUserName(userMasterDto.getUserName() != null ? userMasterDto.getUserName().trim() : null);
		UserMaster user = new UserMaster();
		UserAuth userAuth = new UserAuth();
		String password = null;
		userMasterDto.setIsDelete(false);

		user = UserMapperUtil.mapToUserMaster(userMasterDto);

		boolean isEdit = false;

		Long count = userAuthRepository.existsByUsernameIgnoreCase(userMasterDto.getUserName());

		if (userMasterDto.getId() != null && userMasterDto.getId() != 0) {
			isEdit = true;
			if (count.equals(0L)) {
				userRepository.save(user);
			} else {
				Optional<UserMaster> userMaster = userRepository.findById(user.getId());
				user = UserMapperUtil.mapToUserMaster(userMasterDto, userMaster.get());

			}
		} else {
			isEdit = false;
			if (!count.equals(0L)) {
				String errorfield = "username";
				throwError(errorfield, userMasterDto.getUserName());
			}

		}
		if (facilityTypeId != null && !facilityTypeId.equals(FacilityTypeEnum.BLOOD_BANK.getFacilityType())
				&& !facilityTypeId.equals(FacilityTypeEnum.TRANSPORTER.getFacilityType())) {
			if (user.getId() == null) {
				userAuth = UserMapperUtil.mapToUserAuth(facilityTypeId, userMasterDto);
				password = userMasterDto.getPassword();
				userAuth.setUserMaster(user);
				user.setUserAuths(userAuth);
			}
		}

		try {
			Optional<Facility> facility = facilityRepository.findById(userMasterDto.getFacilityId());
			user.setFacility(facility.get());
		} catch (NoSuchElementException e) {
			List<ErrorDto> errorDtoList = new ArrayList<>();
			List<String> detailsSimplified = new ArrayList<String>();

			ErrorDto errorDto = new ErrorDto();
			errorDto.setDescription("Facility does not exist");
			errorDtoList.add(errorDto);
			detailsSimplified.add(errorDto.getDescription());
			ErrorResponse errorResponse = new ErrorResponse("Validation Failed", errorDtoList, detailsSimplified);
			throw new ServiceException("Facility does not exist", errorResponse, HttpStatus.BAD_REQUEST);
		}

		Optional<Division> division = divisionRepository.findById(userMasterDto.getDivisionId());
		user.setDivision(division.get());

		Optional<FacilityType> facilityType = facilityTypeRepository.findById(userMasterDto.getFacilityTypeId());
		user.setFacilityType(facilityType.get());

		Set<UserRoleMapping> userRoleMappingSet = new HashSet<UserRoleMapping>();
		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
		roleDtoList = userMasterDto.getRoleDto();
		
		Set<TypologyUserMapping> typologyUserMappingSet = new HashSet<TypologyUserMapping>();
		List<TypologyUserMappingDto> typologyUserMappingDtoList = new ArrayList<TypologyUserMappingDto>();
		typologyUserMappingDtoList = userMasterDto.getTypologyUserMappingDto();

		UserMaster userEntity = userRepository.save(user);

		if (isEdit) {
			userRoleMappingRepository.deleteByUserMasterId(userMasterDto.getId());
		}
		boolean isPe = false;
		boolean isOrw = false;
		for (RoleDto roleDto : roleDtoList) {
			Optional<Role> role = roleRepository.findById(roleDto.getId());
			UserRoleMapping userRoleMapping = new UserRoleMapping();
			userRoleMapping.setCreatedBy(userEntity.getCreatedBy());
			userRoleMapping.setCreatedTime(userEntity.getCreatedTime());
			userRoleMapping.setModifiedBy(userEntity.getModifiedBy());
			userRoleMapping.setModifiedTime(userEntity.getModifiedTime());
			userRoleMapping.setIsActive(userEntity.getIsActive());
			userRoleMapping.setIsDelete(userEntity.getIsDelete());
			userRoleMapping.setRole(role.get());
			userRoleMapping.setUserMaster(userEntity);
			userRoleMappingSet.add(userRoleMapping);
			isPe = AccessSettingsMapper.findIsPeFromAccessSettings(role.get(), isPe);
			isOrw = AccessSettingsMapper.findIsOrwFromAccessSettings(role.get(), isOrw);
		}
		if(typologyUserMappingDtoList!=null) {
			for (TypologyUserMappingDto typologyUserMappingDto : typologyUserMappingDtoList) {
				if (typologyUserMappingDto.getId() != null) {
					Optional<TypologyMaster> typology = typologyRepository.findById(typologyUserMappingDto.getTypologyId());
					TypologyUserMapping typologyUserMapping = new TypologyUserMapping();
					typologyUserMapping.setIsActive(typologyUserMappingDto.isActive());
					typologyUserMapping.setIsDelete(typologyUserMappingDto.isDelete());
					typologyUserMapping.setUserMaster(userEntity);
					typologyUserMapping.setTypologyMaster(typology.get());
					typologyUserMapping.setId(typologyUserMappingDto.getId());
					typologyUserMappingRepository.save(typologyUserMapping);
				} else if (typologyUserMappingDto.getId() == null && !typologyUserMappingDto.isDelete()){
					Optional<TypologyMaster> typology = typologyRepository.findById(typologyUserMappingDto.getTypologyId());
					TypologyUserMapping typologyUserMapping = new TypologyUserMapping();
					typologyUserMapping.setIsActive(typologyUserMappingDto.isActive());
					typologyUserMapping.setIsDelete(typologyUserMappingDto.isDelete());
					typologyUserMapping.setUserMaster(userEntity);
					typologyUserMapping.setTypologyMaster(typology.get());
					typologyUserMappingRepository.save(typologyUserMapping);
				}
			}
		}
		
		userRoleMappingRepository.saveAll(userRoleMappingSet);
		Optional<TypologyMaster> typology = null;
		if (userMasterDto.getTypologyId() != null) {
			typology = typologyRepository.findById(userMasterDto.getTypologyId());
		}

		if (isPe) {
			DecimalFormat df = new DecimalFormat("0000");
			String peCode = "PE" + df.format(userEntity.getId());
			userEntity.setPeCode(peCode);
//			if (typology != null) {
//				userEntity.setTypology(typology.get());
//			}
			userRepository.save(userEntity);
		}
		if (isOrw) {
			DecimalFormat df = new DecimalFormat("0000");
			String orwCode = "ORW" + df.format(userEntity.getId());
			userEntity.setOrwCode(orwCode);
//			if (typology != null) {
//				userEntity.setTypology(typology.get());
//			}
			userRepository.save(userEntity);
		}
		userEntity.setUserRoleMappings(userRoleMappingSet);
		userMasterDto = UserMapperUtil.mapToUserMasterDto(userEntity);
		userMasterDto.setPassword(password);
		if (isEdit) {
			userMasterDto.setIsEdit(true);
		} else {
			userMasterDto.setIsEdit(false);
		}

		// Start Sync to LDAP-[START]
		try {
			userLdapService.saveUserToLdapServer(userMasterDto, roleDtoList);
		} catch (Exception e) {
			logger.error(
					"----------------LDAP User Exception------------------- user name: " + userMasterDto.getUserName(),
					e);
		}
		// Start Sync to LDAP-[END]
		return userMasterDto;
	}

	/*
	 * public UserMasterDto prepareRegistrationDetailsForSavedUser(UserMasterDto
	 * userMasterDto) { String password =
	 * UserMapperUtil.generateUserPassword(userMasterDto.getUserName());
	 * userMasterDto.setPassword(password); return userMasterDto;
	 *
	 * }
	 */

	// function to delete user details from database
	public String deleteUser(Long userId) {
		Optional<UserMaster> userMaster = userRepository.findById(userId);
		UserMaster user = userMaster.get();
		UserAuth userAuth = null;
		if (user.getUserAuths() != null) {
			userAuth = user.getUserAuths();
		}
		user.setIsDelete(true);
		userAuth.setIsDelete(true);
		userRepository.save(user);
		userAuthRepository.save(userAuth);
		try {
			userLdapService.deleteUserFromLdap(userAuth);
		} catch (Exception e) {
			logger.error("--------LDAP User delete -----" + userAuth.getUsername(), e);
		}
		if (!user.getUserRoleMappings().isEmpty()) {
			Set<UserRoleMapping> mappings = user.getUserRoleMappings();
			mappings.forEach(action -> {
				action.setIsDelete(Boolean.TRUE);
			});
			userRoleMappingRepository.saveAll(mappings);
		}
		if (!user.getDivisionAdminDivisionMappings().isEmpty()) {
			Set<DivisionAdminDivisionMapping> mappings = user.getDivisionAdminDivisionMappings();
			mappings.forEach(action -> {
				action.setIsDelete(Boolean.TRUE);
			});
			divisionAdminDivisionMappingRepository.saveAll(mappings);
		}
		return userMaster.get().getFirstname();
	}

	// Method to throw error
	private void throwError(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(CommonConstants.VALIDATION_FAILED, errorDtoList,
				detailsSimplified);
		throw new ServiceException(Constants.DUPLICATE_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}

	public Set<UserMasterDto> searchAllUsers(List<SearchCriteria> searchCriteria) {
		UserSpecification userSpecification = new UserSpecification(new SearchCriteria("is_delete", "=", "false"));
		// int counter=0;
		// Specification.where(userSpecification);
		// for (SearchCriteria tempCriteria : searchCriteria) {
		// UserSpecification temp= new UserSpecification(new
		// SearchCriteria(tempCriteria.getKey(), ":", tempCriteria.getValue()));
		// if(counter==0) {
		// Specification.and(temp);
		// }else {
		//
		// }
		// counter++;
		// }

		// UserSpecification spec= new UserSpecification(new
		// SearchCriteria(searchCriteria.getKey(), ":", searchCriteria.getValue()));
		// System.out.println(spec.toString());
		List<UserMaster> users = (List<UserMaster>) userRepository.findAll();
		return UserMapperUtil.mapToUserMasterDtoSet(users);
	}

	public UserMasterDto getUserIdByName(String name) {
		UserMaster user = new UserMaster();
		UserMasterDto userDto = new UserMasterDto();
		user = userRepository.findByName(name);
		userDto = UserMapperUtil.mapToUserDto(user);
		return userDto;

	}

	public UserMasterDto getUserProfileInfoById(Long id) {
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		UserMaster userMaster = null;
		if (loginResponseDto != null) {
			if (loginResponseDto.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()) {
				userMaster = userRepository.findByUserId(id);
			} else {
				Optional<UserMaster> userMasterOpt = userRepository.findByIdAndFacilityIdAndIsDelete(id,
						loginResponseDto.getFacilityId(), false);
				if (userMasterOpt.isPresent()) {
					userMaster = userMasterOpt.get();
				}
			}
		}
		UserMasterDto userDto = new UserMasterDto();
		if (userMaster == null) {
			throwErrorManually("Provided User ID not allowed", "FORBIDDEN");
		} else {
			userDto = UserMapperUtil.mapToUserMasterDto(userMaster);
		}
		if (userDto != null && userDto.getUserAuthsDto() != null) {
			userDto.getUserAuthsDto().setPassword(null);
		}
		return userDto;
	}

	public UserMasterDto updateUserProfile(@Valid UserMasterDto userDto) {
		UserMaster userMaster = new UserMaster();
		userMaster = userRepository.findById(userDto.getId()).get();
		userMaster = UserMapperUtil.mapToUserMasterForUpdateProfile(userDto, userMaster);
		userMaster = userRepository.save(userMaster);
		return userDto;
	}

	public UserAuthDto updateUserPassword(@Valid UserAuthDto userDto) {

		UserAuth user = userAuthRepository.findUserAuthByUserId(userDto.getId());
		String existingPswd = user.getPassword();
		String newPswd = userDto.getPassword();

		// Checking entered current password and password in db is equal or not
		if (userDto.getCurrentPassword() == null
				|| !bcryptEncoder.matches(userDto.getCurrentPassword(), existingPswd)) {
			throw new ServiceException("Incorrect current password", null, HttpStatus.CONFLICT);
		}

		// Checking entered New password and password in db is equal or not
		if (!bcryptEncoder.matches(newPswd, existingPswd)) {
			user.setPassword(bcryptEncoder.encode(newPswd));
			userAuthRepository.save(user);
		} else {
			throw new ServiceException("New password same as old password. Please enter a new one.", null,
					HttpStatus.CONFLICT);
		}
		return userDto;
	}

	public UserAuthDto changeForgotPassword(@Valid UserAuthDto userDto) {

		boolean userInvalid = false;
		boolean otpInvalid = false;
		boolean otpExpired = false;
		long totalSeconds = 0;
		UserAuth userAuth = null;

		try {

			userAuth = userAuthRepository.findByUserName(userDto.getUsername());
			if (userAuth != null) {

				UserMaster userMaster = userAuth.getUserMaster();

				if (userMaster != null) {
					Long userId = userMaster.getId();
					if (userId != null && userId.longValue() > 0) {

						if (userDto.getOtp() != null && userAuth.getOtpGenerated() != null
								&& !"".equalsIgnoreCase(userDto.getOtp())
								&& (userDto.getOtp().trim().equalsIgnoreCase(userAuth.getOtpGenerated().trim()))) {

							LocalDateTime currentTime = LocalDateTime.now();
							LocalDateTime otpGeneratedTime = userAuth.getOtpGeneratedTime();
							if (otpGeneratedTime != null) {
								totalSeconds = otpGeneratedTime.until(currentTime, ChronoUnit.SECONDS);

								if (totalSeconds <= secondsBeforeOtpExpiry) {

									userAuth.setPassword(bcryptEncoder.encode(userDto.getPassword()));
									userAuthRepository.save(userAuth);
									// Send mail
									HashMap<String, Object> placeholderMap = new HashMap<>();

									logger.info("Notifying User after changing password user");
									notificationEventRepository.findByEventIdAndIsEnabled(
											Long.parseLong(
													NotificationEventIdEnum.CHANGE_PASSWORD_FOR_USER.getEventId()),
											true).ifPresent(x -> {
												if (accessKey != null) {
													placeholderMap.put("accessKey", accessKey.trim());
												}
												placeholderMap.put("recipient_specific", userDto.getUsername());
												List<String> emails = new ArrayList<>();
												emails.add(userMaster.getEmail());
												placeholderMap.put(
														CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER,
														emails);
												List<String> phoneNumbers = new ArrayList<String>();
												phoneNumbers.add(userMaster.getMobileNumber());
												placeholderMap.put(
														CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
														phoneNumbers);
												String eventId = "";
												eventId = NotificationEventIdEnum.CHANGE_PASSWORD_FOR_USER.getEventId();
												try {
													notificationUtil.sendNotfication(eventId, true, true, false,
															placeholderMap);

												} catch (Exception e) {
													logger.error("Mail not sent!", e);
												}
											});

									logger.info("End of Notifying User after changing password");

								} else {
									otpExpired = true;
								}
							}

						} else {
							otpInvalid = true;
						}

					} else {
						userInvalid = true;
					}
				} else {
					userInvalid = true;
				}
			}
		} catch (Exception e) {
			logger.error("Following Error occurred while updating password", e);
		}

		if (userInvalid) {
			throw new ServiceException("Invalid User Name", null, HttpStatus.NOT_FOUND);
		}

		if (otpInvalid) {
			throw new ServiceException("OTP entered is Invalid.Please generate a new OTP", null, HttpStatus.CONFLICT);
		}

		if (otpExpired) {
			throw new ServiceException("OTP Expired.Please generate a new OTP", null, HttpStatus.CONFLICT);
		}

		return userDto;
	}
	
	

	/**
	 * Noral Search in User List
	 *
	 * @param searchValue
	 * @param sortType
	 * @param sortBy
	 * @return
	 */
	public UserSearchResponseDto getUserListByNormalSearch(String searchValue, Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		// Getting current login user details
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		UserSearchResponseDto searchResponse = new UserSearchResponseDto();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		List<UserListProjection> userList = new ArrayList<>();
		Page<UserListProjection> userPage = null;
		Optional<List> userListOptional = null;
		searchValue = '%' + searchValue.trim() + '%';
		int actualRecordCount = 0;

		if (currentUser != null && currentUser.getFacilityId() != null) {
			if (currentUser.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()) {
				actualRecordCount = userRepository.userNormalSearchActualRecordCount(searchValue,
						currentUser.getUserId());
				userPage = userRepository.userNormalSearch(searchValue, currentUser.getUserId(), pageable);
				userListOptional = Optional.ofNullable(userPage.getContent());
			} else {
				actualRecordCount = userRepository.userNormalSearchBasedOnFacilityActualRecordCount(searchValue,
						currentUser.getFacilityId(), currentUser.getUserId());
				userPage = userRepository.userNormalSearchBasedOnFacility(searchValue, currentUser.getFacilityId(),
						currentUser.getUserId(), pageable);
				userListOptional = Optional.ofNullable(userPage.getContent());

			}
		}
		if (userListOptional.isPresent()) {
			userList = userListOptional.get();
		}
		List<UserMasterDto> userMasterDtos = UserMapperUtil.mapUserListProjectionListToUserMasterDtoList(userList);
		searchResponse.setActualRecordCount(actualRecordCount);
		searchResponse.setGivenRecordCount(userList.size());
		searchResponse.setUserMasterList(userMasterDtos);
		return searchResponse;
	}

	public UserSearchResponseDto getUserListByAdvancedSearch(Map<String, String> searchValue, Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		// Getting current login user details
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		UserSearchResponseDto searchResponse = new UserSearchResponseDto();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Object[]> userList = new ArrayList<>();
		int actualRecordCount = 0;
		List<String> searchAndActualCountQueries = UserMapperUtil.queryCreaterForUserAdvanceSearch(searchValue,
				currentUser, sortBy, sortType);
		if (!searchAndActualCountQueries.isEmpty()) {
			userList = userRepository.userAdvanceSearch(searchAndActualCountQueries.get(0), searchValue,
					currentUser.getUserId(), currentUser.getFacilityId(), pageable);
			actualRecordCount = userRepository.userActualCount(searchAndActualCountQueries.get(1), searchValue,
					currentUser.getUserId(), currentUser.getFacilityId());
		}
		List<UserMasterDto> userMasterDtos = UserMapperUtil.mapUserListObjectToUserMasterDtoList(userList);
		searchResponse.setActualRecordCount(actualRecordCount);
		searchResponse.setGivenRecordCount(userList.size());
		searchResponse.setUserMasterList(userMasterDtos);
		return searchResponse;
	}

	/**
	 * Fetch all Users list for current log in facility
	 *
	 * @return
	 */
	public List<FacilityUserDto> getUserListForCurrentFacility() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<Object[]> userList = userRepository.findUserByFacilityId(currentUser.getFacilityId());
		List<FacilityUserDto> facilityUserList = UserMapperUtil.mapUserListToFacilityUserDtoList(userList);
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.ART_FACILITY.getFacilityType()) {
			facilityUserList = facilityUserList.stream()
					.filter(user -> user.getRole_id() == RoleEnum.ART_CARE_COORDINATOR.getRole()
							|| user.getRole_id() == RoleEnum.ART_COUNSELLOR.getRole()
							|| user.getRole_id() == RoleEnum.ART_MEDICAL_OFFICER.getRole()
							|| user.getRole_id() == RoleEnum.ART_STAFF_NURSE.getRole()
							|| user.getRole_id() == RoleEnum.ART_PHARMACIST.getRole()
							|| user.getRole_id() == RoleEnum.ART_LAB_TECHNICIAN.getRole())
					.collect(Collectors.toList());
		} else {
			facilityUserList = facilityUserList.stream()
					.filter(user -> user.getRole_id() != RoleEnum.ART_DATA_MANAGER.getRole()
							&& user.getRole_id() != RoleEnum.SACEP_COORDINATOR.getRole())
					.collect(Collectors.toList());
		}
		return facilityUserList;
	}

	public UserMasterDto getUserByUserId(Long userId) {
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		Optional<UserMaster> userOpt = null;
		if (loginResponseDto != null) {
			if (loginResponseDto.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()) {
				userOpt = userRepository.findByIdAndIsDelete(userId, false);
			} else if (loginResponseDto.getFacilityTypeId() == FacilityTypeEnum.SACS.getFacilityType()) {
				userOpt = userRepository.findByIdAndFacilityIdOrSacsIdAndIsDelete(userId,
						loginResponseDto.getFacilityId());
			} else {
				userOpt = userRepository.findByIdAndFacilityIdAndIsDelete(userId, loginResponseDto.getFacilityId(),
						false);
			}
		}
		UserMasterDto userDto = new UserMasterDto();
		if (userOpt.isPresent()) {
			userDto = UserMapperUtil.mapToUserMasterDto(userOpt.get());
		}
		return userDto;
	}

	public boolean resetPassword(String userName) {
		UserAuth user = userAuthRepository.findByUserName(userName);
		if (user == null) {
			throw new ServiceException("Username not exist!", null, HttpStatus.NOT_FOUND);
		}
		String newPassword = "";
		boolean result = false;
		newPassword = UserMapperUtil.generateUserPassword(userName);
		newPassword = UserMapperUtil.encryptPassword(newPassword);
		user.setPassword(newPassword);
		try {
			userAuthRepository.save(user);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public String updateUserPasswordForMobile(@Valid UpdatePasswordDto updatePasswordDto) {
		LoginResponseDtoForMobile loginResponseDtoForMobile = UserUtils.getLoggedInUserDetailsForMobile();
		UserAuth user = userAuthRepository.findUserAuthByUserId(loginResponseDtoForMobile.getUserId());
		String existingPswd = user.getPassword();
		String newPswd = updatePasswordDto.getNewPassword();

		// Checking entered current password and password in db is equal or not
		if (updatePasswordDto.getCurrentPassword() == null
				|| !bcryptEncoder.matches(updatePasswordDto.getCurrentPassword(), existingPswd)) {
			throwError("Method: updateUserPasswordForMobile", "Invalid current password.", HttpStatus.BAD_REQUEST);
		}

		// Checking entered New password and password in db is equal or not
		if (!bcryptEncoder.matches(newPswd, existingPswd)) {
			user.setPassword(bcryptEncoder.encode(newPswd));
			userAuthRepository.save(user);
		} else {
			throwError("Method: updateUserPasswordForMobile", "New password same as old password. Please enter a new one.", HttpStatus.BAD_REQUEST);
		}
		return "password updated successfully";
	}

	String generateOtp(int otpLength)	{

		String numbers = "0123456789";
		Random rndm_method = new Random();
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < otpLength; i++) {
			otp.append(numbers.charAt(rndm_method.nextInt(numbers.length())));
		}
		return otp.toString();
	}

	public boolean createOtpForUser(String userName) {
		Integer otpUpdateCount = 0;
		boolean otpUpdateStatus = false;
		boolean userInvalid = false;
		try {

			Long userId = userAuthRepository.findUserIdByUserNameAndIsDeleteAndIsActive(userName);
			if (userId != null && userId.longValue() > 0) {
				UserMaster userMaster = userRepository.findByUserId(userId);
				if (userMaster != null) {
					String otpGenerated = generateOtp(6);
					// logger.info(otpGenerated);
					if (otpGenerated != null && otpGenerated.length() > 0) {
						otpUpdateCount = userAuthRepository.updateOtpAndGeneratedTime(otpGenerated, LocalDateTime.now(),
								userId);
						if (otpUpdateCount != null && otpUpdateCount.intValue() > 0) {
							otpUpdateStatus = true;
							HashMap<String, Object> placeholderMap = new HashMap<>();

							long minutesBeforeExpiry = secondsBeforeOtpExpiry / 60;
							logger.info("Notifying User after generating OTP");
							notificationEventRepository
									.findByEventIdAndIsEnabled(
											Long.parseLong(NotificationEventIdEnum.OTP_GENERATED.getEventId()), true)
									.ifPresent(x -> {
										if (accessKey != null) {
											placeholderMap.put("accessKey", accessKey.trim());
										}
										placeholderMap.put("recipient_specific", userName);
										placeholderMap.put("otp", otpGenerated);
										List<String> emails = new ArrayList<>();
										emails.add(userMaster.getEmail());
										placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER,
												emails);
										List<String> phoneNumbers = new ArrayList<String>();
										phoneNumbers.add(userMaster.getMobileNumber());
										placeholderMap.put(
												CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
												phoneNumbers);
										placeholderMap.put("expiry_time", minutesBeforeExpiry);
										String eventId = "";
										eventId = NotificationEventIdEnum.OTP_GENERATED.getEventId();
										try {
											notificationUtil.sendNotfication(eventId, true, true, false,
													placeholderMap);

										} catch (Exception e) {
											logger.error("Mail not sent!", e);
										}
									});
							logger.info("Notifying User after generating OTP");

						}
					}
				}

			} else {
				userInvalid = true;
			}

		} catch (Exception e) {
			logger.error("Following Error occurred while generating Otp", e);
			e.printStackTrace();
			return false;
		}
		if (userInvalid) {
			throw new ServiceException("Invalid User Name", null, HttpStatus.NOT_FOUND);
		}
		return otpUpdateStatus;
	}
	
	public boolean createOtpForUserForMobileForgotPassword(SendOtpRequestDto sendOtpDto) {
		Integer otpUpdateCount = 0;
		boolean otpUpdateStatus = false;
		boolean userInvalid = false;
		String userName = sendOtpDto.getUserName();
		if(StringUtils.isEmpty(userName)){
			throwError("Method: createOtpForUserForMobileForgotPassword", "User Name is mandatory for forgot password request", HttpStatus.BAD_REQUEST);
		}
		try {

			Long userId = userAuthRepository.findUserIdByUserNameAndIsDeleteAndIsActive(userName);
			if (userId != null && userId.longValue() > 0) {
				UserMaster userMaster = userRepository.findByUserId(userId);
				if (userMaster != null) {
					String otpGenerated = generateOtp(4);
					// logger.info(otpGenerated);
					if (otpGenerated != null && otpGenerated.length() > 0) {
						otpUpdateCount = userAuthRepository.updateOtpAndGeneratedTime(otpGenerated, LocalDateTime.now(),
								userId);
						if (otpUpdateCount != null && otpUpdateCount.intValue() > 0) {
							otpUpdateStatus = true;
							HashMap<String, Object> placeholderMap = new HashMap<>();

							long minutesBeforeExpiry = secondsBeforeOtpExpiry / 60;
							logger.info("Notifying User after generating OTP");
							notificationEventRepository
									.findByEventIdAndIsEnabled(
											Long.parseLong(NotificationEventIdEnum.OTP_GENERATED.getEventId()), true)
									.ifPresent(x -> {
										if (accessKey != null) {
											placeholderMap.put("accessKey", accessKey.trim());
										}
										placeholderMap.put("recipient_specific", userName);
										placeholderMap.put("otp", otpGenerated);
										List<String> emails = new ArrayList<>();
										emails.add(userMaster.getEmail());
										placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER,
												emails);
										List<String> phoneNumbers = new ArrayList<String>();
										phoneNumbers.add(userMaster.getMobileNumber());
										placeholderMap.put(
												CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
												phoneNumbers);
										placeholderMap.put("expiry_time", minutesBeforeExpiry);
										String eventId = "";
										eventId = NotificationEventIdEnum.OTP_GENERATED.getEventId();
										try {
											notificationUtil.sendNotfication(eventId, false, true, false,
													placeholderMap);

										} catch (Exception e) {
											
											logger.error("SMS for mobile forgot password not sent!", e);
											//throwError("Method: createOtpForUserForMobileForgotPassword", "SMS for mobile forgot password not sent!", HttpStatus.BAD_REQUEST);
										}
									});
							logger.info("Notifying User after generating OTP");

						}
					}
				}

			} else {
				userInvalid = true;
			}

		} catch (Exception e) {
			logger.error("Following Error occurred while generating Otp", e);
			e.printStackTrace();
			return false;
		}
		if (userInvalid) {
			throwError("Method: createOtpForUserForMobileForgotPassword", "Invalid User Name", HttpStatus.BAD_REQUEST);
		}
		return otpUpdateStatus;
	}

	private void throwErrorManually(String errorString, String errorType) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorType);
		errorDto.setDescription(errorString);
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(errorString, errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	public boolean updateBeneficiaryIdAndFcmDeviceTokenAndType(Long beneficiaryId, String deviceToken,String deviceOsType,String mobileNumber) {
		try {
			int update=beneficiaryRepository.updateBeneficiaryIdAndFcmDeviceTokenAndType(beneficiaryId, deviceToken, deviceOsType, mobileNumber);
			if(update>0) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			return false;
		}
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
	
	public SendOtpResponseDto createOtpForUserForMobile(String mobileNumber) {
		SendOtpResponseDto sendOtpResponseDto = new SendOtpResponseDto();

		try {
			if(appleAppStoreReviewEnabled && mobileNumber.equalsIgnoreCase(appleAppStoreReviewMobileNumber)) {
				sendOtpResponseDto.setMessage("OTP Successfully Sent");
				sendOtpResponseDto.setMobileNumber(appleAppStoreReviewMobileNumber);
				sendOtpResponseDto.setStatusCode("200");
				sendOtpResponseDto.setOtpId(1L);
				
				return sendOtpResponseDto;
			}
			String otpGenerated = generateOtp(4);
			Long otpId = saveOtp(mobileNumber, otpGenerated);
			if (otpGenerated != null && otpGenerated.length() > 0) {
				HashMap<String, Object> placeholderMap = new HashMap<>();

				long minutesBeforeExpiry = secondsBeforeOtpExpiry / 60;
				logger.info("Notifying User after generating OTP");
				notificationEventRepository.findByEventIdAndIsEnabled(
						Long.parseLong(NotificationEventIdEnum.MOBILE_SEND_OTP.getEventId()), true).ifPresent(x -> {
				if (accessKey != null) {
					placeholderMap.put("accessKey", accessKey.trim());
				}
				placeholderMap.put("recipient_specific", mobileNumber);
				placeholderMap.put("otp", otpGenerated);
				List<String> emails = new ArrayList<>();
				placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER, emails);
				List<String> phoneNumbers = new ArrayList<String>();
				phoneNumbers.add(mobileNumber);
				placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
						phoneNumbers);
				placeholderMap.put("expiry_time", minutesBeforeExpiry);
				String eventId = "";
				eventId = NotificationEventIdEnum.MOBILE_SEND_OTP.getEventId();
				try {
					notificationUtil.sendNotfication(eventId, false, true, false, placeholderMap);

				} catch (Exception e) {
					throwErrorForMobile("Method: sendOtp", "[Exception] in sending OTP:->>> " + e.getLocalizedMessage(),
							HttpStatus.UNAUTHORIZED);
				}
			});

		sendOtpResponseDto.setMessage("OTP Successfully Sent");
		sendOtpResponseDto.setMobileNumber(mobileNumber);
		sendOtpResponseDto.setStatusCode("200");
		sendOtpResponseDto.setOtpId(otpId);
		logger.info("Notifying User after generating OTP");

			}

		} catch (Exception e) {
			logger.error("Following Error occurred while generating Otp for Mobile", e);
			e.printStackTrace();
			logger.info("[Exception] in sending OTP for Mobile: {}", e.getLocalizedMessage());
			throwErrorForMobile("Method: sendOtp", "[Exception] in sending OTP:->>> " + e.getLocalizedMessage(),
					HttpStatus.UNAUTHORIZED);
		}
		return sendOtpResponseDto;
	}
	
	private Long saveOtp(String mobileNumber, String otp) {
		OtpEntity entity = mobileOtpRepository.findByMobileNumber(mobileNumber);
		if(entity == null) {
			entity = new OtpEntity();
			entity.setOtp(otp);
			entity.setMobileNumber(mobileNumber);
			entity.setOtpGeneratedTime(LocalDateTime.now());
			entity.setIsActive(true);
			entity.setIsDelete(false);
			entity.setCreatedBy(0);
			entity.setCreatedTime(LocalDateTime.now());
			entity.setModifiedBy(0);
			entity.setModifiedTime(LocalDateTime.now());
			
			entity = mobileOtpRepository.save(entity);
		}else {
			entity.setOtp(otp);
			entity.setOtpGeneratedTime(LocalDateTime.now());
			entity.setModifiedTime(LocalDateTime.now());
			entity = mobileOtpRepository.save(entity);
		}
		logger.info("Saving OTP [{}] in table for mobile number: [{}]", otp, mobileNumber);
		
		return entity.getId();
		
		
	}
	
	private void throwErrorForMobile(String errorfield, String errorFieldValue, HttpStatus httpStatus) {
		
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
}
