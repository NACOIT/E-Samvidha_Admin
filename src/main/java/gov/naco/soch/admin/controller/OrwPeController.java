package gov.naco.soch.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import gov.naco.soch.admin.service.OrwPeService;
import gov.naco.soch.dto.OrwPeMappingDto;
import gov.naco.soch.dto.UserBasicDto;
import gov.naco.soch.dto.UserMasterDto;

//Controller class for API call
@RestController
@RequestMapping("/orwandpe")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrwPeController {

	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(OrwPeController.class);

	@Autowired
	private OrwPeService orwPeService;

	/**
	 * Dangerous API: http://localhost:8080/admin/orwandpe/orw/facilityorwusers 
	 * List orw for dropdown
	 * Instead of this use: http://localhost:8080/admin/orwandpe/orw/facilityorwusers/basicdetails 
	 * 
	 * @return
	 */
	// @GetMapping("/orw/facilityorwusers")
	// public @ResponseBody List<UserMasterDto> getOrwUsersForDropdown() {
	// logger.info("getOrwUsersForDropdown method called");
	// return orwPeService.getOrwUsers();
	// }
	
	/**
	 * Optimized API For: List orw for dropdown
	 * @return
	 */
	@GetMapping("/orw/facilityorwusers/basicdetails")
	public @ResponseBody List<UserBasicDto> getOrwUsersForDropdownBasicDetails() {
		logger.info("getOrwUsersForDropdownBasicDetails method called");
		return orwPeService.getOrwUsersForDropdownBasicDetails();
	}

	/**
	 * List pe for dropdown
	 * 
	 * @return
	 */
	@GetMapping("/pe/facilitypeusers")
	public @ResponseBody List<UserMasterDto> getPeUsersForDropdown() {
		logger.info("getPeUsersForDropdown method called");
		return orwPeService.getPeUsersForDropdown();
	}
	
	/**
	 * List pe for dropdow based on typology
	 * 
	 * @return
	 */
	@GetMapping("/pe/facilitypeusers/{typologyId}")
	public @ResponseBody List<UserMasterDto> getPeUsersForTypology(@PathVariable("typologyId") Long typologyId,
			@RequestParam List<Integer> typologyIds) {
		logger.info("getPeUsersForTypology method called");
		return orwPeService.getPeUsersForTypology(typologyId ,typologyIds);
	}

	/**
	 * Save Mapping orw with pe
	 * 
	 * @param orwPeMappingDto
	 * @return
	 */
	@PostMapping("/mapping/save")
	public @ResponseBody OrwPeMappingDto saveOrwPeMapping(@RequestBody OrwPeMappingDto orwPeMappingDto) {
		logger.info("saveOrwPeMapping method called");
		return orwPeService.saveOrwPeMapping(orwPeMappingDto);
	}

	/**
	 * Listing Pe based on orw
	 * 
	 * @param orwId
	 * @return
	 */
	@GetMapping("/pe/listbyorw/{orwId}")
	public @ResponseBody List<UserBasicDto> getPeUsersListBasedOnOrw(@PathVariable("orwId") Long orwId) {
		logger.info("getPeUsersListBasedOnOrw method called");
		return orwPeService.getPeUsersListBasedOnOrw(orwId);
	}

	/**
	 * deleting pe based on pe id
	 * 
	 * @param peId
	 * @return
	 */
	@DeleteMapping("/pe/deletemapping/{peId}")
	public @ResponseBody Boolean deletePeUserMapping(@PathVariable("peId") Long peId) {
		logger.info("deletePeUserMapping method called");
		return orwPeService.deletePeUserMapping(peId);
	}
	
	/**
	 * List orw for dropdown based on typology
	 * 
	 * @return
	 */
	@GetMapping("/orw/facilityorwusers/{typologyId}")
	public @ResponseBody List<UserMasterDto> getOrwUsersForTypology(@PathVariable("typologyId") Long typologyId ,
			@RequestParam List<Integer> typologyIds)  {
		logger.info("getOrwUsersForTypology method called");
		return orwPeService.getOrwUsersForTypology(typologyId , typologyIds);
	}
}
