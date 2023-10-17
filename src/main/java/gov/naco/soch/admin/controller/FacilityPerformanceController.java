package gov.naco.soch.admin.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.FacilityPerformanceService;
import gov.naco.soch.dto.FacilityPerformanceData;
import gov.naco.soch.dto.FacilityPerformanceResultDto;
import gov.naco.soch.dto.StateDto;
import gov.naco.soch.entity.District;
import gov.naco.soch.entity.Facility;

@RestController
@RequestMapping("/performance")
public class FacilityPerformanceController {
	@Autowired
	FacilityPerformanceService facilityPerformanceService;

	@GetMapping("/report/facility/{facilityId}")
	public ResponseEntity<List<FacilityPerformanceResultDto>> getPerformanceData(
			@PathVariable("facilityId") Long facilityId, @RequestParam("requestType") String requestType,
			@RequestParam("sectionId") String sectionId) {

		List<FacilityPerformanceResultDto> performanceResultDtoList;
		try {
			performanceResultDtoList = facilityPerformanceService.getPerformanceData(requestType, sectionId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return new ResponseEntity<List<FacilityPerformanceResultDto>>(performanceResultDtoList, HttpStatus.OK);

	}

	@GetMapping("/export/report/facility/{facilityId}")
	public ResponseEntity<InputStreamResource> getExcelReportForPerformanceData(
			@PathVariable("facilityId") Long facilityId, @RequestParam("requestType") String requestType,
			@RequestParam("sectionId") String sectionId, @RequestParam("userId") Long userId) {

		ByteArrayInputStream reportStream;
		try {
			reportStream = facilityPerformanceService.exportExcelData(facilityId, sectionId, userId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=Facility_Performance.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(reportStream));

	}

	@RequestMapping("list/states")
	public ResponseEntity<List<StateDto>> getAllStatesAndDistricts() {
		List<StateDto> stateDtos = new ArrayList<StateDto>();
		try {
			stateDtos = facilityPerformanceService.getAllStatesAndDistricts();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return new ResponseEntity<List<StateDto>>(stateDtos, HttpStatus.OK);

	}

	@RequestMapping("list/districts/{stateId}")
	public ResponseEntity<List<District>> getAlldDistrictsByState(@PathVariable("stateId") Long stateId) {
		List<District> districtsList = new ArrayList<District>();
		try {
			districtsList = facilityPerformanceService.findByStateId(stateId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return new ResponseEntity<List<District>>(districtsList, HttpStatus.OK);

	}

	@RequestMapping("list/facilities/{districtId}")
	public ResponseEntity<List<Facility>> findFacilitiesByDistrictId(@PathVariable("districtId")Long districtId) {

		List<Facility> facilities;
		try {
			facilities = facilityPerformanceService.findFacilitiesByDistrictId(districtId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return new ResponseEntity<List<Facility>>(facilities, HttpStatus.OK);

	}

	@RequestMapping("/facility/details/{facilityId}")
	public ResponseEntity<FacilityPerformanceData> getFacilityPerformanceData(
			@PathVariable("facilityId") Long facilityId, @RequestParam("userId") Long userId) {
		FacilityPerformanceData facilityPerformanceData = new FacilityPerformanceData();
		facilityPerformanceData = facilityPerformanceService.getFacilityPerformanceData(facilityId, userId);

		return new ResponseEntity<FacilityPerformanceData>(facilityPerformanceData, HttpStatus.OK);

	}

}
