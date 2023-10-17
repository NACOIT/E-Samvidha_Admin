package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.SacsFacilityDto;

@RestController
@RequestMapping("/ictcfacility")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IctcFacilityController {

	@Autowired
	private FacilityService facilityService;

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.ICTC_FACILITIES + "')")
	public @ResponseBody List<SacsFacilityDto> getAllFacilityBySacs(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getAllFacilitesUnderIctc(pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;

	}

	@GetMapping("/advancesearch")
	public @ResponseBody List<SacsFacilityDto> advanceSearchForFacilitiesUnderIctc(
			@RequestParam Map<String, String> searchValues, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		List<Long> facilityTypeIds = new ArrayList<>();
		facilityTypeIds.add(13l);
		sacsFacilityDtoList = facilityService.advanceSearchForFacilities(searchValues, facilityTypeIds, pageNumber,
				pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}

}
