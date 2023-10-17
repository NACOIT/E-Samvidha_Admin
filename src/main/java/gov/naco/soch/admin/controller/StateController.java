package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.StateService;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.StateDto;
import gov.naco.soch.dto.SubdistrictDto;
import gov.naco.soch.util.UserUtils;

@RestController
@RequestMapping("/state")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StateController {

	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired
	StateService stateService;

	/**
	 * Dangerous API: https://localhost:8080/admin/state/list . API to get all
	 * states and corresponding districts. Instead of this use:
	 * http://localhost:8080/admin/address/state,
	 * http://localhost:8080/admin/address/districtbystateid?stateId=1
	 * 
	 * @return
	 */
	// @GetMapping("/list")
	// public @ResponseBody List<StateDto> getAllStatesAndDistricts() {
	// logger.debug("getAllStatesAndDistricts method is invoked");
	// return stateService.getAllStatesAndDistricts();
	// }

	@GetMapping("/subdistrictandtown/{districtId}/list")
	public @ResponseBody List<SubdistrictDto> getSubDistrictAndTowns(@PathVariable("districtId") Long districtId) {
		List<SubdistrictDto> subdistrictDtoList = new ArrayList<SubdistrictDto>();
		subdistrictDtoList = stateService.getSubDistrictAndTownList(districtId);
		return subdistrictDtoList;

	}

	/**
	 * Dangerous API: https://localhost:8080//admin/state/subdistrictandtown/list .
	 * API to get all subdistrict and corresponding town. 
	 * Instead of this use:
	 * http://localhost:8080/admin/address/subdistrictbydistrictid?districtId=1 ,
	 * http://localhost:8080/admin/address/townbysubdistrictid?subdistrictId=1
	 * 
	 * @return
	 */
	// @GetMapping("/subdistrictandtown/list")
	// public @ResponseBody List<SubdistrictDto> getSubDistrictAndTowns() {
	// List<SubdistrictDto> subdistrictDtoList = new ArrayList<SubdistrictDto>();
	// subdistrictDtoList = stateService.getSubDistrictAndTownList();
	// return subdistrictDtoList;
	// }

	@GetMapping("/currentfacility/stateanddistrict")
	public @ResponseBody StateDto getCurrentFacilityStateAndDistricts() {
		logger.debug("getCurrentStatesAndDistricts method is invoked");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		return stateService.getCurrentFacilityStateAndDistricts(currentUser.getFacilityId());
	}

	@GetMapping("/byfacility/{facilityId}")
	public @ResponseBody StateDto getStateAndDistrictsByFacility(@PathVariable("facilityId") Long facilityId) {
		logger.debug("getStateAndDistrictsByFacility method is invoked");
		return stateService.getStateAndDistrictsByFacility(facilityId);
	}
}
