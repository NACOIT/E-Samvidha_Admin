package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.enums.FacilityTypeEnum;

@RestController
@RequestMapping("/labs")
public class LabsController {
	private static final Logger logger = LoggerFactory.getLogger(LabsController.class);

	@Autowired
	private FacilityService facilityService;

	public LabsController() {

	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_LABORATORIES + "')")
	public @ResponseBody List<SacsFacilityDto> getAllLabs(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<Long> facilityTypeIds = new ArrayList<Long>();
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_EID.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_APEX.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_NRL.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_SRL.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_CD4.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.VL_PUBLIC.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.VL_PRIVATE.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_ICTC_PPTCT.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_HIV2_LABS.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.NARI.getFacilityType());
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getFacilityByFacilityTypeOptimized(facilityTypeIds, pageNumber, pageSize,
				sortBy, sortType);
		return sacsFacilityDtoList;
	}

	// API to add details to facility table
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_LABORATORIES + "')")
	public @ResponseBody SacsFacilityDto addLabs(@Valid @RequestBody SacsFacilityDto sacsFacilityDto) {
		sacsFacilityDto.setName(sacsFacilityDto.getName() != null ? sacsFacilityDto.getName().trim() : null);
		if (sacsFacilityDto.getId() == null) {
			logger.debug("addFacilityFromSacs method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.addAnyFacilities(sacsFacilityDto);
			logger.debug("addFacilityFromSacs method returns with parameters->{}", sacsFacilityDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.editAnyFacilities(sacsFacilityDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", sacsFacilityDto);
		}
		return sacsFacilityDto;

	}

	@DeleteMapping("/delete/{facilityId}")
	public SacsFacilityDto deleteLabs(@PathVariable("facilityId") Long facilityId) {
		logger.info("deleteFacility method called with facilityId" + facilityId);
		return facilityService.deleteFacility(facilityId);
	}

	/**
	 * Advance search for the Laboratory. Search criteria:
	 * facilityname,code,username,mobilenumber,email
	 * 
	 * @param searchValues
	 * @return
	 */
	@GetMapping("/advancesearch")
	public @ResponseBody List<SacsFacilityDto> advanceSearchForFacilities(
			@RequestParam Map<String, String> searchValues, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		List<Long> facilityTypeIds = new ArrayList<>();
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_EID.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_APEX.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_NRL.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_SRL.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_CD4.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.VL_PUBLIC.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.VL_PRIVATE.getFacilityType());
		facilityTypeIds.add(FacilityTypeEnum.LABORATORY_ICTC_PPTCT.getFacilityType());
		sacsFacilityDtoList = facilityService.advanceSearchForFacilities(searchValues, facilityTypeIds, pageNumber,
				pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}

	/**
	 * Get ictc facility list mapped with current login facility as EID lab
	 * 
	 * @return
	 */
	@GetMapping("/mappedictc")
	public @ResponseBody List<SacsFacilityDto> getMappedIctcForEidLab() {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getMappedIctcForEidLab();
		return sacsFacilityDtoList;
	}

	/**
	 * Fetching vl labs mapped with current facility id
	 * 
	 * @return
	 */
	@GetMapping("/mapped/undercurrentFacility")
	public @ResponseBody List<SacsFacilityDto> getMappedVlLabsUnderCurrentFacility() {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getMappedVlLabsUnderCurrentFacility();
		return sacsFacilityDtoList;
	}

}
