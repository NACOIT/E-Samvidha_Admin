package gov.naco.soch.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.ListAccessSettingsResponse;
import gov.naco.soch.admin.service.AccessSettingsService;
import gov.naco.soch.dto.RoleAccessMappingDto;

//Controller class for API call

@RestController
@RequestMapping("/access-settings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccessSettingsController {

	@Autowired
	AccessSettingsService accessSettingsService;

	private static final Logger logger = LoggerFactory.getLogger(AccessSettingsController.class);

	public AccessSettingsController() {
	}

	@GetMapping("/{roleId}/list")
	public @ResponseBody List<RoleAccessMappingDto> getRoleAccessSettings(@PathVariable("roleId") Long roleId) {
		logger.debug("Entering into method getRoleAccessSettings with roleId->{}:", roleId);
		return accessSettingsService.getRoleAccessSettings(roleId);
	}
	
	@PostMapping("/{roleId}/save")
	public @ResponseBody boolean saveRoleAccessSettings(@RequestBody List<RoleAccessMappingDto> roleAccessMappingDtoList,
			@PathVariable("roleId") Long roleId) {
		logger.debug("Entering into method saveRoleAccessSettings with roleAccessMappingDtoList->{}:roleId->{}:", roleId);
		return accessSettingsService.saveAccessSettings(roleAccessMappingDtoList, roleId);
	}
	
	@GetMapping("/list")
	public @ResponseBody List<RoleAccessMappingDto> getAllAccessSettings() {
		logger.debug("Entering into method getAllAccessSettings");
		return accessSettingsService.getAllAccessSettings();
	}

}
