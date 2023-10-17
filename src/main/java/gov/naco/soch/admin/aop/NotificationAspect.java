package gov.naco.soch.admin.aop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.naco.soch.dto.DesignationDto;
import gov.naco.soch.dto.DivisionDto;
import gov.naco.soch.dto.FacilityDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.ProductDto;
import gov.naco.soch.dto.RegimenDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.dto.TypologyDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.entity.Designation;
import gov.naco.soch.entity.Division;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.UserAuth;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.enums.NotificationEventIdEnum;
import gov.naco.soch.mapper.UserMapperUtil;
import gov.naco.soch.repository.DesignationRepository;
import gov.naco.soch.repository.DivisionRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.repository.UserAuthRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.util.CommonConstants;
import gov.naco.soch.util.UserUtils;

/*
  Working : 1)give the full quality name of the method of the controller,after which a notification needs to be send.
            2) 1) should be given as a @Pointcut (refer the example @Pointcut saveUser(){})
            3) Advice given is @AfterReturning,which means only after successful execution of the method given in the 
            @Pointcut the method annotated with  @AfterReturning will be invoked
            4)give the arguments expected by the method as seen in the example method written here :sendNotification(JoinPoint joinPoint,Object body) 
 */

@Aspect
@Component
public class NotificationAspect {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private NotificationEventRepository notificationEventRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private UserAuthRepository userAuthRepository;

	@Autowired
	private RoleRepository roleRepository;

	// private static HashMap<String, Object> placeholderMap = new HashMap<>();

	@Value("${notification.emailServiceUrl}")
	private String EMAIL_ENDPOINTURL;
	@Value("${notification.smsServiceUrl}")
	private String SMS_ENDPOINTURL;
	@Value("${notification.whatsappServiceUrl}")
	private String WHATSAPP_ENDPOINTURL;
	@Value("${notification.webuserNotificationUrl}")
	private String WEB_ENDPOINTURL;
	@Value("${accessKey}")
	private String accessKey;

	private static final Logger logger = LoggerFactory.getLogger(NotificationAspect.class);

	@Pointcut("execution(* gov.naco.soch.admin.controller.UserController.saveUser(..))")
	public void saveUser() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.UserController.deleteUser(..))")
	public void deleteUser() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.DivisionController.addDivision(..))")
	public void addDivision() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.DivisionController.deleteDivision(..))")
	public void deleteDivision() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.RoleController.addRole(..))")
	public void saveRole() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.RoleController.deleteRole(..))")
	public void deleteRole() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.DesignationController.saveDesignation(..))")
	public void saveDesignation() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.DesignationController.deleteDesignation(..))")
	public void deleteDesignation() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.FacilityController.addFacility(..))")
	public void saveFacility() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.FacilityController.deleteFacility(..))")
	public void deleteFacility() {

	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.SacsController.addSacs(..))")
	public void addSacs() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.LabsController.addLabs(..))")
	public void addLabs() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.LabsController.deleteLabs(..))")
	public void deleteLabs() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.ProcurementAgentController.deleteProcurementAgent(..))")
	public void deleteProcurementAgent() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.SacsController.deleteSacs(..))")
	public void deleteSacs() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.SupplierController.deleteSupplier(..))")
	public void deleteSupplier() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.RegionalWarehouseController.deleteRegionalWarehouse(..))")
	public void deleteRegionalWarehouse() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.SupplierController.addSupplier(..))")
	public void addSupplier() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.RegionalWarehouseController.addRegionalWarehouse(..))")
	public void addRegionalWarehouse() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.FacilityController.addAnyFacility(..))")
	public void addAnyFacility() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.LacController.addLacFacility(..))")
	public void saveLac() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.LacController.deleteLacFacility(..))")
	public void deleteLac() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.ProductController.saveProduct(..))")
	public void saveProduct() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.UserController.resetPassword(..))")
	public void resetPassword() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.RegimenController.saveRegimen(..))")
	public void saveRegimen() {
	}

	@Pointcut("execution(* gov.naco.soch.admin.controller.ProcurementAgentController.addProcurementAgent(..))")
	public void addProcurementAgent() {
	}
	/*
	 * @Pointcut("execution(* gov.naco.soch.admin.controller.UserController.prepareRegistrationDetailsForSavedUser(..))"
	 * ) public void prepareRegistrationDetailsForSavedUser() {
	 * 
	 * }
	 */

	@AfterReturning(pointcut = "saveUser()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveUser(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_USER.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof UserMasterDto && responseBody instanceof UserMasterDto) {
						UserMasterDto request = (UserMasterDto) requestBody;
						// Change to avoid notification on updation. @Author : Rishad Basheer
						if (request.getId() == null || request.getId() == 0) {
							// placeholderMap = new HashMap<>();
							UserMasterDto response = (UserMasterDto) responseBody;
							/* Send registration details */
							String eventId = null;
							if (!StringUtils.isBlank(response.getEmail())) {
								logger.info("Sending registration details to the user-->response.getEmail()-->{}:",
										response.getEmail());
								List<String> toEmailsList = new ArrayList<>();
								toEmailsList.add(response.getEmail());
								placeholderMap.put("username", response.getUserName());
								placeholderMap.put("password", response.getPassword());
								placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER,
										response.getFirstname());
								placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER,
										toEmailsList);
								List<String> toPhoneList = new ArrayList<String>();
								toPhoneList.add(response.getMobileNumber());
								placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
										toPhoneList);
								eventId = NotificationEventIdEnum.SEND_REGISTRATION_DETAILS_TO_USER.getEventId();
								// Web user notification
								placeholderMap.put(CommonConstants.WEB_EVENT_ID, eventId);
								placeholderMap.put(CommonConstants.WEB_USER_ID, response.getId());
								placeholderMap.put(CommonConstants.WEB_FINAL_URL, "finalUrl");
								placeholderMap.put("accessKey", accessKey);
								sendNotfication(eventId, placeholderMap, true, true, false, true);
								response.setPassword(null);
							}
							// placeholderMap = new HashMap<>();
							placeholderMap.put("username", request.getFirstname());
							placeholderMap.put("userid", response.getId());
							placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY,
									response.getFacilityId());
							// Web user notification
							placeholderMap.put(CommonConstants.WEB_EVENT_ID, eventId);
							placeholderMap.put(CommonConstants.WEB_FINAL_URL, "finalUrl");
							placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
							eventId = NotificationEventIdEnum.ADD_USER.getEventId();
							sendNotfication(eventId, placeholderMap, true, false, false, true);
						}
					}
				});
	}

	@AfterReturning(pointcut = "deleteUser()&& args(.., @PathVariable userId)")
	@Async
	public void sendNotificationFordeleteUser(JoinPoint joinPoint, Long userId) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_USER.getEventId()), true)
				.ifPresent(x -> {
					// placeholderMap.put("username", userName);
					// Code change (Bug-fix on 08/07/20)
					UserMaster userMaster = userMasterRepository.findById(userId).get();
					if (userMaster != null) {
						String userName = userMaster.getFirstname();
						if (userMaster.getLastname() != null) {
							userName += " " + userMaster.getLastname();
						}
						placeholderMap.put("username", userName);
						if (userMaster.getFacility() != null) {
							placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY,
									userMaster.getFacility().getId());
						}
					} else
						System.out.println("userMaster is null");
					placeholderMap.put("userid", userId);
					String eventId = NotificationEventIdEnum.DELETE_USER.getEventId();
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					sendNotfication(eventId, placeholderMap, true, false, false, false);
				});
	}

	@AfterReturning(pointcut = "addDivision()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveDivision(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_DIVISION.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof DivisionDto && responseBody instanceof DivisionDto) {
						DivisionDto request = (DivisionDto) requestBody;
						// Change to avoid notification on updation. @Author : Rishad Basheer
						// if (request.getId() == null || request.getId() == 0) {
						DivisionDto response = (DivisionDto) responseBody;
						placeholderMap.put("divisionname", request.getName());
						placeholderMap.put("divisionid", response.getId());
						String eventId = NotificationEventIdEnum.ADD_DIVISION.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, false);
						// }
					}
				});
	}

	@AfterReturning(pointcut = "deleteDivision()&& args(.., @PathVariable divisionId)")
	@Async
	public void sendNotificationFordeleteDivision(JoinPoint joinPoint, Long divisionId) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_DIVISION.getEventId()), true)
				.ifPresent(x -> {
					// Code change (Bug-fix on 08/07/20)
					Division division = divisionRepository.findById(divisionId).get();
					String divisionName = division.getName();
					placeholderMap.put("divisionname", divisionName);
					placeholderMap.put("divisionid", divisionId);
					String eventId = NotificationEventIdEnum.DELETE_DIVISION.getEventId();
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					sendNotfication(eventId, placeholderMap, true, false, false, false);
				});
	}

	@AfterReturning(pointcut = "saveRole()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveRole(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_ROLE.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof RoleDto && responseBody instanceof RoleDto) {
						RoleDto request = (RoleDto) requestBody;
						// Change to avoid notification on updation. @Author : Rishad Basheer
						if (request.getId() == null || request.getId() == 0) {
							RoleDto response = (RoleDto) responseBody;
							placeholderMap.put("rolename", request.getName());
							placeholderMap.put("roleid", response.getId());
							String eventId = NotificationEventIdEnum.ADD_ROLE.getEventId();
							placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
							sendNotfication(eventId, placeholderMap, true, false, false, false);
						}
					}
				});
	}

	@AfterReturning(pointcut = "deleteRole()&& args(.., @PathVariable roleId)")
	@Async
	public void sendNotificationForDeleteRole(JoinPoint joinPoint, Long roleId) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_ROLE.getEventId()), true)
				.ifPresent(x -> {
					Role role = roleRepository.findById(roleId).get();
					placeholderMap.put("rolename", role.getName());
					placeholderMap.put("roleid", roleId);
					String eventId = NotificationEventIdEnum.DELETE_ROLE.getEventId();
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					sendNotfication(eventId, placeholderMap, true, false, false, false);
				});
	}

	@AfterReturning(pointcut = "saveDesignation()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveDesignation(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_DESIGNATION.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof DesignationDto && responseBody instanceof DesignationDto) {
						DesignationDto request = (DesignationDto) requestBody;
						// Change to avoid notification on updation. @Author : Rishad Basheer
						if (request.getId() == null || request.getId() == 0) {
							DesignationDto response = (DesignationDto) responseBody;
							placeholderMap.put("designationtitle", request.getTitle());
							placeholderMap.put("designationid", response.getId());
							String eventId = NotificationEventIdEnum.ADD_DESIGNATION.getEventId();
							placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
							sendNotfication(eventId, placeholderMap, true, false, false, false);
						}
					}
				});
	}

	@AfterReturning(pointcut = "deleteDesignation()&& args(.., @PathVariable designationId)")
	@Async
	public void sendNotificationForDeleteDesignation(JoinPoint joinPoint, Long designationId) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository.findByEventIdAndIsEnabled(
				Long.parseLong(NotificationEventIdEnum.DELETE_DESIGNATION.getEventId()), true).ifPresent(x -> {
					// Code change (Bug-fix on 08/07/20)
					Designation designation = designationRepository.findById(designationId).get();
					String title = designation.getTitle();
					placeholderMap.put("designationtitle", title);
					placeholderMap.put("designationid", designationId);
					String eventId = NotificationEventIdEnum.DELETE_DESIGNATION.getEventId();
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					sendNotfication(eventId, placeholderMap, true, false, false, false);

				});
	}

	@AfterReturning(pointcut = "saveFacility()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveFacility(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof FacilityDto && responseBody instanceof FacilityDto) {
						FacilityDto request = (FacilityDto) requestBody;
						FacilityDto response = (FacilityDto) responseBody;
						placeholderMap.put("facilityname", request.getName());
						placeholderMap.put("facilityid", response.getId());
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.ADD_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);
					}
				});
	}

	@AfterReturning(pointcut = "deleteFacility()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteFacility(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "deleteLabs()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteLabs(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "deleteProcurementAgent()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteProcurementAgent(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "deleteSacs()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteSacs(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "deleteSupplier()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteSupplier(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "deleteRegionalWarehouse()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteRegionalWarehouse(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_FACILITY.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("facilityname", response.getName());
						placeholderMap.put("facilityid", facilityId);
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_FACILITY.getEventId();
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "addSacs()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddSacs(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_SACS.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("sacsname", sacsFacilityDtoReq.getName());
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoReq.getId());
		}
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});
	}

	@AfterReturning(pointcut = "addLabs()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddLabs(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_LABS.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("labname", sacsFacilityDtoReq.getName());
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoReq.getId());
		}
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});
	}

	@AfterReturning(pointcut = "addSupplier()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddSupplier(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_SUPPLIER.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("suppliername", sacsFacilityDtoReq.getName());
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoReq.getId());
		}
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});
	}

	@AfterReturning(pointcut = "addRegionalWarehouse()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddRegionalWarehouse(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_RWH.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("rwhname", sacsFacilityDtoReq.getName());
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoReq.getId());
		}
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});
	}

	@AfterReturning(pointcut = "addAnyFacility()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddAnyFacility(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_SACS_FACILITY.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("facilityname", sacsFacilityDtoReq.getName());
			if (sacsFacilityDtoReq.getFacilityTypeName() != null) {
				logger.info("Sendng Notification to facility with facility type-->{}:",
						sacsFacilityDtoReq.getFacilityTypeName());
				placeholderMap.put("facilitytype", sacsFacilityDtoReq.getFacilityTypeName());
			}
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoRes = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoRes.getId());
		}
		logger.info("!!!!!Setting placeHolderMap with access key!!!!!! -->{}:", accessKey);
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});

		String eventId3 = NotificationEventIdEnum.NEW_TARGET_TO_TI.getEventId();
		if (requestBody instanceof SacsFacilityDto) {

			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			List<TypologyDto> oldTypology = sacsFacilityDtoReq.getOldTypology();
			List<TypologyDto> typologyDtos = sacsFacilityDtoReq.getTypology();
			if (oldTypology != null && !oldTypology.isEmpty() && typologyDtos != null && !typologyDtos.isEmpty()) {
				List<String> strings = new ArrayList<String>(oldTypology.size());
				for (TypologyDto object : oldTypology) {
					strings.add(object.getTypologyName());
				}

				List<String> strings1 = new ArrayList<String>(typologyDtos.size());
				for (TypologyDto object : typologyDtos) {
					strings1.add(object.getTypologyName());
				}
				Collections.sort(strings);
				Collections.sort(strings1);
				boolean checkString = strings.equals(strings1);
				boolean checkString1 = strings1.equals(strings);
				if (!checkString || !checkString1) {
					// placeholderMap.put("tiCenterName",
					// sacsFacilityDtoResponse.getTiCenterName());
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId3), true)
							.ifPresent(x -> {
								sendNotfication(eventId3, placeholderMap, true, false, false, true);
							});
				}
			}
		}

		String eventId4 = NotificationEventIdEnum.PARENT_OST_DEACTIVATION.getEventId();
		if (requestBody instanceof SacsFacilityDto && responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto response = (SacsFacilityDto) responseBody;
			List<MasterDto> oldParentOst = response.getOldParentOsts();
			List<MasterDto> newParentOsts = response.getParentOsts();
			placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
			if (oldParentOst.size() > newParentOsts.size()) {
				notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId4), true).ifPresent(x -> {
					sendNotfication(eventId4, placeholderMap, true, false, false, true);
				});
			}
		}

		// String eventId2 = NotificationEventIdEnum.ADD_FACILITY.getEventId();
		// if (requestBody instanceof SacsFacilityDto) {
		// SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
		// placeholderMap.put("facilityname", sacsFacilityDtoReq.getName());
		// }
		// if (responseBody instanceof SacsFacilityDto) {
		// SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
		// placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY,
		// sacsFacilityDtoReq.getId());
		// }
		//
		// notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId2),
		// true).ifPresent(x -> {
		// sendFacilityCreationNotification(requestBody, eventId2);
		// });
	}

	private void sendFacilityCreationNotification(Object requestBody, String eventId, Object responseBody,
			HashMap<String, Object> placeholderMap) {
		if (responseBody instanceof SacsFacilityDto) {
			// SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			SacsFacilityDto sacsFacilityDtoRes = (SacsFacilityDto) responseBody;
			if (sacsFacilityDtoRes.getIsEdit() == false) {
				sendNotfication(eventId, placeholderMap, true, false, false, true);
			}

			/* Send registration details for the created users with SACS */
			UserMasterDto primaryUser = sacsFacilityDtoRes.getPrimaryUser();
			placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
			sendFacilityUserRegistrationNotfication(primaryUser, placeholderMap);

			UserMasterDto alternateUser = sacsFacilityDtoRes.getAlternateUser();
			if (alternateUser != null) {
				sendFacilityUserRegistrationNotfication(alternateUser, placeholderMap);
			}

		}
	}

	private void sendFacilityUserRegistrationNotfication(UserMasterDto userMasterDto,
			HashMap<String, Object> placeholderMap) {
		if (userMasterDto.getEmail() != null && !StringUtils.isBlank(userMasterDto.getEmail())) {
			List<String> toEmailsList = new ArrayList<>();
			toEmailsList.add(userMasterDto.getEmail());
			placeholderMap.put("username", userMasterDto.getUserName());
			placeholderMap.put("password", userMasterDto.getPassword());
			placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER,
					userMasterDto.getFirstname());
			placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER, toEmailsList);
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, userMasterDto.getFacilityId());
			placeholderMap.put(CommonConstants.WEB_USER_ID, userMasterDto.getId());
			String eventId = NotificationEventIdEnum.SEND_REGISTRATION_DETAILS_TO_USER.getEventId();
			placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
			if (userMasterDto.getIsEdit() == false) {
				sendNotfication(eventId, placeholderMap, true, false, false, true);
			}
			// response.setPassword(null);
		}
	}

	@AfterReturning(pointcut = "saveLac()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForsaveLac(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_LAC_CST.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody instanceof SacsFacilityDto) {
						SacsFacilityDto request = (SacsFacilityDto) requestBody;
						placeholderMap.put("lacname", request.getName());

					}
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY,
								sacsFacilityDtoReq.getId());
					}
					String eventId = NotificationEventIdEnum.ADD_LAC_CST.getEventId();
					// sendNotfication(eventId);
					placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
					sendNotfication(eventId, placeholderMap, true, false, false, true);

				});
	}

	@AfterReturning(pointcut = "deleteLac()&& args(.., @PathVariable facilityId)", returning = "responseBody")
	@Async
	public void sendNotificationForDeleteLac(JoinPoint joinPoint, Long facilityId, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.DELETE_LAC_CST.getEventId()), true)
				.ifPresent(x -> {
					if (responseBody instanceof SacsFacilityDto) {
						SacsFacilityDto response = (SacsFacilityDto) responseBody;
						placeholderMap.put("lacname", response.getName());
						placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, response.getId());
						String eventId = NotificationEventIdEnum.DELETE_LAC_CST.getEventId();
						// sendNotfication(eventId);
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						sendNotfication(eventId, placeholderMap, true, false, false, true);

					}
				});
	}

	@AfterReturning(pointcut = "saveProduct()&& args(.., @RequestBody requestBody)")
	@Async
	public void sendNotificationForsaveProduct(JoinPoint joinPoint, Object requestBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_PRODUCT.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody != null && requestBody instanceof String) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							ProductDto productDto = mapper.readValue(requestBody.toString(), ProductDto.class);
							if (productDto.getId() == null) {
								placeholderMap.put("productName", productDto.getProductName());
								placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
								String eventId = NotificationEventIdEnum.ADD_PRODUCT.getEventId();
								sendNotfication(eventId, placeholderMap, true, false, false, true);
							}

						} catch (Exception ex) {
							logger.error("Following error occured", ex);
						}
					}
				});
	}

	@AfterReturning(pointcut = "saveRegimen()&& args(.., @RequestBody requestBody)")
	@Async
	public void sendNotificationForsaveRegimen(JoinPoint joinPoint, Object requestBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.ADD_REGIMEN.getEventId()), true)
				.ifPresent(x -> {
					if (requestBody != null && requestBody instanceof RegimenDto) {

						try {
							RegimenDto regimenDto = (RegimenDto) requestBody;
							placeholderMap.put("regimenName", regimenDto.getRegimenName());
							placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
							String eventId = NotificationEventIdEnum.ADD_REGIMEN.getEventId();
							if (regimenDto.getId() == null) {
								sendNotfication(eventId, placeholderMap, false, false, false, true);
							}
						} catch (Exception ex) {
							logger.error("Following error occured", ex);
						}
					}
				});
	}

	@AfterReturning(pointcut = "resetPassword()&& args(.., @PathVariable userName)")
	@Async
	public void sendNotificationForResetPassword(JoinPoint joinPoint, String userName) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		notificationEventRepository
				.findByEventIdAndIsEnabled(Long.parseLong(NotificationEventIdEnum.RESET_PASSWORD.getEventId()), true)
				.ifPresent(x -> {
					UserAuth user = userAuthRepository.findByUserName(userName);
					String eventId = "";
					if (user != null) {
						List<String> toEmailsList = new ArrayList<>();
						toEmailsList.add(user.getEmail());
						String userPassword = UserMapperUtil.generateUserPassword(userName);
						placeholderMap.put("username", userName);
						placeholderMap.put("password", userPassword);
						List<String> toMobileNumbers = new ArrayList<String>();
						toMobileNumbers.add(user.getUserMaster().getMobileNumber());
						placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER, userName);
						placeholderMap.put(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER, toEmailsList);
						placeholderMap.put(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER,
								toMobileNumbers);
						placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
						eventId = NotificationEventIdEnum.RESET_PASSWORD.getEventId();
					} else
						System.out.println("userAuth is null");
					sendNotfication(eventId, placeholderMap, true, true, false, false);
				});
	}

	@AfterReturning(pointcut = "addProcurementAgent()&& args(.., @RequestBody requestBody)", returning = "responseBody")
	@Async
	public void sendNotificationForAddProcurementAgent(JoinPoint joinPoint, Object requestBody, Object responseBody) {
		HashMap<String, Object> placeholderMap = new HashMap<>();
		String eventId = NotificationEventIdEnum.ADD_PROCUREMENT_AGENT.getEventId();
		if (requestBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) requestBody;
			placeholderMap.put("procurementAgentName", sacsFacilityDtoReq.getName());
		}
		if (responseBody instanceof SacsFacilityDto) {
			SacsFacilityDto sacsFacilityDtoReq = (SacsFacilityDto) responseBody;
			placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, sacsFacilityDtoReq.getId());
		}
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);
		notificationEventRepository.findByEventIdAndIsEnabled(Long.parseLong(eventId), true).ifPresent(x -> {
			sendFacilityCreationNotification(requestBody, eventId, responseBody, placeholderMap);
		});
	}

	private void sendNotfication(String eventId, HashMap<String, Object> placeholderMap, boolean emailToBeSent,
			boolean smsToBeSent, boolean whatsAppToBeSent, boolean webNotification) {

		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(currentUser.getToken());
		headers.setContentType(MediaType.APPLICATION_JSON);
		placeholderMap.put(CommonConstants.PROPERTY_ACCESS_KEY, accessKey);

		HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(placeholderMap, headers);
		// New System web user notification.
		try {
			if (webNotification) {
				logger.info("Going to send web user notification for the eventId-->{}:", eventId);
				restTemplate.exchange(WEB_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, HashMap.class);
				logger.info("Sent  web user notification for the eventId-->{}:", eventId);
			}
		} catch (Exception e) {
			logger.error("EXCEPTION in web user notification", e);
		}
		try {
			if (emailToBeSent) {
				logger.info("Going to send email for the eventId-->{}:", eventId);
				restTemplate.exchange(EMAIL_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, HashMap.class);
				logger.info("Sent the email for the eventId-->{}:restTemplateResponse-->{}:", eventId);
			}
		} catch (Exception e) {
			logger.error("EXCEPTION in sendEmail", e);
		}
		try {
			if (smsToBeSent) {
				logger.info("Going to send sms for the eventId-->{}:", eventId);
				restTemplate.exchange(SMS_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, HashMap.class);
				logger.info("Sent sms for the eventId-->{}:", eventId);
			}
		} catch (Exception e) {
			logger.error("EXCEPTION in sendSMS", e);
		}
		try {
			if (whatsAppToBeSent) {
				logger.info("Going to send whatsapp for the eventId-->{}:", eventId);
				restTemplate.exchange(WHATSAPP_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, HashMap.class);
				logger.info("Sent whatsapp for the eventId-->{}:", eventId);
			}
		} catch (Exception e) {
			logger.error("EXCEPTION in sendWhatsApp", e);
		}

	}

}
