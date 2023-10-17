package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.PageReportsRoleMappingDto;
import gov.naco.soch.admin.dto.ReportsRoleMappingDto;
import gov.naco.soch.admin.service.ReportsRoleMappingService;
import gov.naco.soch.dto.ReportRoleDivisionDto;
import gov.naco.soch.entity.ReportsRoleMapping;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.repository.GlobalReportsDivisionMappingRepository;
import gov.naco.soch.repository.ReportsRoleMappingRepository;

@RestController
@RequestMapping("/api")
@Transactional
public class ReportsRoleMappingController {

	@Autowired
	ReportsRoleMappingService reportsRoleMappingService;
	@Autowired
	ReportsRoleMappingRepository reportsRoleMappingRepository;
	@Autowired
	GlobalReportsDivisionMappingRepository globalReportsDivisionMappingRepository;

	@GetMapping("/report/role/all")
	public ResponseEntity<PageReportsRoleMappingDto> getAllReportsRoleMappingData(
			@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize,
			@RequestParam(defaultValue = "mapping_id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		PageReportsRoleMappingDto pageReportsRoleMappingDto = new PageReportsRoleMappingDto();
		try {
			pageReportsRoleMappingDto = reportsRoleMappingService.getAllReportsRoleMappingData(pageIndex, pageSize,
					sortBy, sortType);
		} catch (Exception e) {
			new ResponseEntity<List<ReportsRoleMappingDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PageReportsRoleMappingDto>(pageReportsRoleMappingDto, HttpStatus.OK);

	}

	@GetMapping("/report/role/mapping/{reportId}")
	public ResponseEntity<List<ReportsRoleMappingDto>> getAllReportsRoleMappingByReportId(
			@PathVariable("reportId") Long reportId) {

		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		try {
			reportsRoleMappingDtos = reportsRoleMappingService.getAllReportsRoleMappingByReportId(reportId);
		} catch (Exception e) {
			new ResponseEntity<List<ReportsRoleMappingDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ReportsRoleMappingDto>>(reportsRoleMappingDtos, HttpStatus.OK);

	}

	@GetMapping("/report/role/{mappingId}")
	public ResponseEntity<ReportsRoleMappingDto> getAllReportsRoleMappingDataById(
			@PathVariable("mappingId") Long mappingId) {

		ReportsRoleMappingDto reportsRoleMappingDto = new ReportsRoleMappingDto();
		try {
			reportsRoleMappingDto = reportsRoleMappingService.getAllReportsRoleMappingDataById(mappingId);
		} catch (Exception e) {
			new ResponseEntity<List<ReportsRoleMappingDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportsRoleMappingDto>(reportsRoleMappingDto, HttpStatus.OK);

	}

	@PostMapping("/save/reports/mapping/{reportId}")
	public ResponseEntity<List<ReportsRoleMappingDto>> saveReportsAndmapping(
			@RequestBody List<ReportsRoleMapping> reportsRoleMapping, @PathVariable("reportId") Long reportId,
			@RequestParam(required = false) List<Long> divisionId) {

		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		try {
			reportsRoleMappingDtos = reportsRoleMappingService.bulkSaveReportsAndRoleMapping(reportsRoleMapping,
					reportId, divisionId);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportsRoleMappingDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ReportsRoleMappingDto>>(reportsRoleMappingDtos, HttpStatus.OK);

	}

	@PutMapping("/update/reports/mapping/{reportId}")
	public ResponseEntity<List<ReportsRoleMappingDto>> updateReportsAndmapping(
			@RequestBody List<ReportsRoleMapping> reportsRoleMapping, @PathVariable("reportId") Long reportId,
			@RequestParam(required = false) List<Long> divisionId) {

		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		try {
			reportsRoleMappingDtos = reportsRoleMappingService.bulkUpdateReportsAndRoleMapping(reportsRoleMapping,
					reportId, divisionId);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportsRoleMappingDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ReportsRoleMappingDto>>(reportsRoleMappingDtos, HttpStatus.OK);

	}

	@DeleteMapping("mapping/delete/all")
	public ResponseEntity<?> deleteAllReportsAndmapping() {

		try {
			reportsRoleMappingRepository.deleteAll();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("mapping/delete/{mappingId}")
	public ResponseEntity<?> deleteAllReportsAndmappingById(@PathVariable("mappingId") Long mappingId) {

		try {
			Optional<ReportsRoleMapping> reportsRoleMappingOpt = reportsRoleMappingRepository.findById(mappingId);
			ReportsRoleMapping reportsRoleMapping = null;
			if (reportsRoleMappingOpt.isPresent()) {
				reportsRoleMapping = reportsRoleMappingOpt.get();
			}
			if (reportsRoleMapping != null && reportsRoleMapping.getMasterReport() != null
					&& reportsRoleMapping.getRole() != null
					&& reportsRoleMapping.getRole().getId() == RoleEnum.DIVISION_ADMIN.getRole()) {
				long reportId = reportsRoleMapping.getMasterReport().getReportId();
				globalReportsDivisionMappingRepository.deleteByReportId(reportId);
			}
			reportsRoleMappingRepository.deleteById(mappingId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/report/role/division/list/{reportId}")
	public @ResponseBody ReportRoleDivisionDto getRoleDivisionListByReportId(@PathVariable("reportId") Long reportId) {
		ReportRoleDivisionDto reportRoleDivisionDto = reportsRoleMappingService.getRoleDivisionListByReportId(reportId);
		return reportRoleDivisionDto;
	}

}
