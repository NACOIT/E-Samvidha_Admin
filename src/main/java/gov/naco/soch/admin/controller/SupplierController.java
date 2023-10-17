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
import gov.naco.soch.admin.service.SupplierService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.enums.FacilityTypeEnum;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

	@Autowired
	private FacilityService facilityService;

	@Autowired
	private SupplierService SupplierService;

	public SupplierController() {

	}

	/**
	 * Optimized
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_SUPPLIERS + "') or hasAuthority('"
			+ AdminAccessCodes.PROCUREMENT_STAFF_SUPPLIERS + "') or hasAuthority('"
			+ AdminAccessCodes.PROCUREMENT_AGENT_SUPPLIERS + "')")
	public @ResponseBody List<SacsFacilityDto> getAllSupplier(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<Long> facilityTypeId = new ArrayList<Long>();
		facilityTypeId.add((long) 3); // Supplier facility Type Id
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = SupplierService.getSupplierList(facilityTypeId, pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;

	}

	// API to add details to facility table
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_SUPPLIERS + "') or hasAuthority('"
			+ AdminAccessCodes.PROCUREMENT_STAFF_SUPPLIERS + "') or hasAuthority('"
			+ AdminAccessCodes.PROCUREMENT_AGENT_SUPPLIERS + "')")
	public @ResponseBody SacsFacilityDto addSupplier(@Valid @RequestBody SacsFacilityDto sacsFacilityDto) {
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
	public SacsFacilityDto deleteSupplier(@PathVariable("facilityId") Long facilityId) {
		logger.info("deleteFacility method called with facilityId" + facilityId);
		return facilityService.deleteFacility(facilityId);
	}

	/**
	 * Advance search for the Supplier. Search criteria:
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
		facilityTypeIds.add(FacilityTypeEnum.SUPPLIER.getFacilityType());
		sacsFacilityDtoList = facilityService.advanceSearchForFacilities(searchValues, facilityTypeIds, pageNumber,
				pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}

	/**
	 * Optimized supplier list by procurement Agent
	 * 
	 * @param procurementAgentId
	 * @return
	 */
	@GetMapping("/listby/procurementAgent/{procurementAgentId}")
	public @ResponseBody List<FacilityBasicListDto> getSupplierByProcurementAgent(
			@PathVariable("procurementAgentId") Long procurementAgentId) {
		logger.debug("getSupplierByProcurementAgent method is invoked");
		return facilityService.getSupplierByProcurementAgent(procurementAgentId);
	}

}
