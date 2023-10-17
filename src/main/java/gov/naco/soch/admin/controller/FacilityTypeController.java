
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

import gov.naco.soch.admin.service.FacilityTypeService;
import gov.naco.soch.dto.FacilityTypeDto;
import gov.naco.soch.dto.FacilityTypeListByDivisionDTO;

@RestController
@RequestMapping("/facilityType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacilityTypeController {

	@Autowired
	private FacilityTypeService facilityTypeService;

	public FacilityTypeController() {
	}

	private static final Logger logger = LoggerFactory.getLogger(FacilityTypeController.class);

	/**
	 * @param facilityTypeDto
	 * @return facilityTypeDto
	 */
	@PostMapping("/add")
	public @ResponseBody FacilityTypeDto addFacilityType(@RequestBody FacilityTypeDto facilityTypeDto) {
		logger.debug("addFacilityType method called with parameters->{}", facilityTypeDto);
		return facilityTypeService.addFacilityType(facilityTypeDto);
	}

	// API to get all details from facilityType table
	/**
	 * @return facilityTypeList
	 */
	@GetMapping("/list")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypes() {
		logger.debug("getFacilityTypes method called");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getAllFacilityType();
		return facilityTypeList;
	}

	// Get facility type with its division and primary user details
	/**
	 * @return facilityTypeList
	 */
	@GetMapping("/divisionlist")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypesWithDivision() {
		logger.debug("getFacilityTypesWithDivision method called");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getFacilityTypesWithDivision();
		return facilityTypeList;
	}

	// API to get all details from facilityType table
	/**
	 * @return facilityTypeList
	 */
	@GetMapping("/divisionNotMappedFacTypes")
	public @ResponseBody List<FacilityTypeDto> getDivisionNotMappedFacilityTypes() {
		logger.debug("getDivisionNotMappedFacilityTypes method called");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getDivisionNotMappedFacilityTypes();
		return facilityTypeList;
	}

	// API to get facilityType for labs
	/**
	 * @return facilityTypeList
	 */
	@GetMapping("/labs")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypesForLabs() {
		logger.debug("getFacilityTypes method called");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getFacilityTypesForLabs();
		return facilityTypeList;
	}

	/**
	 * Optimzed facility type listing Restricted for facility creation screen
	 * 
	 * @return
	 */
	@GetMapping("/list/forfacilitycreation")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypesForFacilityCreation() {
		logger.debug("getFacilityTypesForFacilityCreation method called");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getFacilityTypesForFacilityCreation();
		return facilityTypeList;
	}

	/**
	 * Optimized
	 * List Facility type based on division id
	 * 
	 * @param divisionid
	 * @return
	 */
	@GetMapping("/listbyDivisionId/{divisionid}")
	public @ResponseBody List<FacilityTypeListByDivisionDTO> getFacilityTypesListByDivision(
			@PathVariable("divisionid") Long divisionid) {
		logger.debug("getFacilityTypesListByDivision method called");
		List<FacilityTypeListByDivisionDTO> facilityTypeList = facilityTypeService
				.getFacilityTypesListByDivision(divisionid);
		return facilityTypeList;
	}
	
	@GetMapping("/mobile/list")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypesForMobile() {
		logger.debug("getFacilityTypesForMobile method called(Nearest Naco Centre)");
		List<FacilityTypeDto> facilityTypeList = facilityTypeService.getFacilityTypesForMobile();
		return facilityTypeList;
	}

}
