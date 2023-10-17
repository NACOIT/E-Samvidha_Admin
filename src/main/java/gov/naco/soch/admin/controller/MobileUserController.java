package gov.naco.soch.admin.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.UserSearchResponseDto;
import gov.naco.soch.admin.service.UserService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.criteria.SearchCriteria;
import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.UpdatePasswordDto;
import gov.naco.soch.dto.UserAuthDto;
import gov.naco.soch.dto.UserDto;
import gov.naco.soch.dto.UserMasterDto;

@RestController
@RequestMapping("/user/mobile")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MobileUserController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(MobileUserController.class);

	public MobileUserController() {
	}

	// API to get all details from User_master table
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.SACS_ADMIN_USERS + "') or hasAuthority('" + AdminAccessCodes.TI_NGO_USERS
			+ "') or hasAuthority('" + AdminAccessCodes.TI_OST_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.ART_USERS + "') or hasAuthority('" + AdminAccessCodes.ICTC_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.VL_USERS + "') or hasAuthority('" + AdminAccessCodes.EIDLAB_USERS + "') or"
			+ " hasAuthority('" + AdminAccessCodes.NACO_PROJECTDIRECTOR_USERS + "') ")
	public @ResponseBody UserSearchResponseDto getAllUsers(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllUsers method called");
		return userService.getAllUsers(pageNumber, pageSize, sortBy, sortType);
	}

	@GetMapping("/findby/{userId}")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.SACS_ADMIN_USERS + "') or hasAuthority('" + AdminAccessCodes.TI_NGO_USERS
			+ "') or hasAuthority('" + AdminAccessCodes.TI_OST_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.ART_USERS + "') or hasAuthority('" + AdminAccessCodes.ICTC_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.VL_USERS + "') or hasAuthority('" + AdminAccessCodes.EIDLAB_USERS + "') or"
			+ " hasAuthority('" + AdminAccessCodes.NACO_PROJECTDIRECTOR_USERS + "') ")
	public @ResponseBody UserMasterDto getUserByUserId(@PathVariable("userId") Long userId) {
		logger.debug("getUserByUserId method called");
		return userService.getUserByUserId(userId);
	}

	// API to add details to User_master table
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.SACS_ADMIN_USERS + "') or hasAuthority('" + AdminAccessCodes.TI_NGO_USERS
			+ "') or hasAuthority('" + AdminAccessCodes.TI_OST_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.ART_USERS + "') or hasAuthority('" + AdminAccessCodes.ICTC_USERS + "') or hasAuthority('"
			+ AdminAccessCodes.VL_USERS + "') or hasAuthority('" + AdminAccessCodes.EIDLAB_USERS + "') or"
			+ " hasAuthority('" + AdminAccessCodes.NACO_PROJECTDIRECTOR_USERS + "') ")
	public @ResponseBody UserMasterDto saveUser(@Valid @RequestBody UserMasterDto userDto) {
		logger.info("saveUser method called with parameters->{}", userDto);
		UserMasterDto userMasterDto = userService.saveUser(null,userDto);
		// prepareRegistrationDetailsForSavedUser(userMasterDto);
		return userMasterDto;

	}

	// API to delete details from User_master table
	@DeleteMapping("/delete/{userId}")
	public boolean deleteUser(@PathVariable Long userId) {
		logger.info("deleteUser method called with parameters->{}", userId);
		userService.deleteUser(userId);
		return true;
	}

	// API to get user list of a facility.
	@GetMapping("/list/{facilityId}")
	public @ResponseBody List<UserDto> getAllFacilityUsers(@PathVariable("facilityId") Long facilityId) {
		logger.debug("getAllFacilityUsers method called");
		return userService.getAllFacilityUsers(facilityId);
	}

	//
	// // Test API to try sample codes.
	// @PostMapping("/addtest")
	// public @ResponseBody UserMasterDto addUserTest(@Valid @RequestBody
	// UserMasterDto userDto) {
	// logger.info("addUserTest method called with parameters->{}", userDto);
	// if ("soch".equalsIgnoreCase(userDto.getFirstname())) {
	// List<ErrorDto> errorDtoList = new ArrayList<>();
	// List<String> detailsSimplified = new ArrayList<String>();
	//
	// ErrorDto errorDto = new ErrorDto();
	// errorDto.setField("firstname");
	// errorDto.setDescription("Our business does not allow a user named '" +
	// userDto.getFirstname() + "'");
	// errorDtoList.add(errorDto);
	// detailsSimplified.add(errorDto.getDescription());
	// ErrorResponse errorResponse = new ErrorResponse("Validation Failed",
	// errorDtoList, detailsSimplified);
	// throw new ServiceException("Our business does not allow a user named '" +
	// userDto.getFirstname() + "'",
	// errorResponse, HttpStatus.BAD_REQUEST);
	// }
	// userDto = new UserMasterDto();
	// userDto.setFirstname("created user");
	// return userDto;
	// // return userService.addUser(userDto);
	// }
	//
	// // API to get all the access baced on the user roles
	// @GetMapping("/{userId}/roles/access")
	// public List<String> getUserRolesAccess(@PathVariable("userId") Long userId) {
	// logger.debug("getUserRolesAccess method called");
	// return userService.getUserRolesAccess(userId);
	// }
	@PostMapping("/search")
	public @ResponseBody Set<UserMasterDto> searchAllUsers(@RequestBody List<SearchCriteria> searchCriteria) {
		logger.debug("searchAllUsers method called");
		return userService.searchAllUsers(searchCriteria);
	}

	@GetMapping("/getuser/{firstname}")
	public @ResponseBody UserMasterDto getUserByName(@PathVariable("firstname") String firstname) {
		UserMasterDto userDto = userService.getUserIdByName(firstname);
		return userDto;
	}

	@GetMapping("/getuserinfo/{id}")
	public @ResponseBody UserMasterDto getUserProfileInfoById(@PathVariable("id") Long id) {
		UserMasterDto userDto = userService.getUserProfileInfoById(id);
		return userDto;
	}

	@PostMapping("/updateprofile")
	public @ResponseBody UserMasterDto updateUserProfile(@Valid @RequestBody UserMasterDto userDto) {
		logger.info("saveUser method called with parameters->{}", userDto);
		UserMasterDto user = userService.updateUserProfile(userDto);
		return user;

	}

	@PostMapping("/updatepassword")
	public @ResponseBody String updateUserPassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
		logger.info("update password API called");
		String message = userService.updateUserPasswordForMobile(updatePasswordDto);
		return message;

	}

	/**
	 * User List Normal Search
	 */
	@GetMapping("/normal/searchby/{searchValue}")
	public @ResponseBody UserSearchResponseDto getUserListByNormalSearch(
			@PathVariable("searchValue") String searchValue, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		UserSearchResponseDto userSearchResponse = userService.getUserListByNormalSearch(searchValue, pageNumber,
				pageSize, sortBy, sortType);
		return userSearchResponse;
	}

	/**
	 * User List Advanced Search Search
	 */
	@GetMapping("/advanced/searchby")
	public @ResponseBody UserSearchResponseDto getUserListByAdvancedSearch(
			@RequestParam Map<String, String> searchValue, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		UserSearchResponseDto userSearchResponse = userService.getUserListByAdvancedSearch(searchValue, pageNumber,
				pageSize, sortBy, sortType);
		return userSearchResponse;
	}

	/**
	 * Fetch all Users list for current log in facility
	 * 
	 * @return
	 */
	@GetMapping("/facilityuserlist")
	public @ResponseBody List<FacilityUserDto> getUserListForCurrentFacility() {
		List<FacilityUserDto> facilityUserDtos = userService.getUserListForCurrentFacility();
		return facilityUserDtos;
	}

	@PostMapping("/resetpassword/{userName}")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.RESET_PASSWORD + "')")
	public boolean resetPassword(@PathVariable(value = "userName") String userName) {
		boolean result = userService.resetPassword(userName);
		return result;
	}
}
