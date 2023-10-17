package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.MasterReportsDto;
import gov.naco.soch.admin.dto.PageMasterReportDto;
import gov.naco.soch.admin.service.MasterAdminReportsService;
import gov.naco.soch.entity.MasterReport;
import gov.naco.soch.repository.MasterReportsRepository;

@RestController
@RequestMapping("/api")
public class MasterReportsController {
	@Autowired
	MasterReportsRepository masterReportsRepository;
	@Autowired
	MasterAdminReportsService masterAdminReportsService;

	@GetMapping("/reports/get")
	public ResponseEntity<PageMasterReportDto> getAllMasterReportsData(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize, @RequestParam(defaultValue = "report_id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		PageMasterReportDto pageMasterReportDto = new PageMasterReportDto();
		try {
			pageMasterReportDto = masterAdminReportsService.getAllMasterReportsData(pageIndex, pageSize, sortBy,
					sortType);
		} catch (Exception e) {
			return new ResponseEntity<PageMasterReportDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PageMasterReportDto>(pageMasterReportDto, HttpStatus.OK);
	}

	@GetMapping("/reports/filter/{subModuleId}")
	public ResponseEntity<List<MasterReportsDto>> getAllMasterReportsData(
			@PathVariable("subModuleId") Long subModuleId) {
		List<MasterReportsDto> masterReportsDtos = new ArrayList<MasterReportsDto>();
		try {
			masterReportsDtos = masterAdminReportsService.getAllMasterReportsBySubModuleId(subModuleId);
		} catch (Exception e) {
			return new ResponseEntity<List<MasterReportsDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MasterReportsDto>>(masterReportsDtos, HttpStatus.OK);
	}

	@GetMapping("/reports/get/{reportId}")
	public ResponseEntity<MasterReportsDto> getAllMasterReportsDataById(@PathVariable("reportId") Long reportId) {
		MasterReportsDto masterReportsDto = new MasterReportsDto();
		try {
			masterReportsDto = masterAdminReportsService.getAllMasterReportsDataById(reportId);
		} catch (Exception e) {
			return new ResponseEntity<MasterReportsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<MasterReportsDto>(masterReportsDto, HttpStatus.OK);
	}

	@PostMapping("/reports/save")
	public ResponseEntity<MasterReportsDto> saveMasterReports(@RequestBody MasterReport masterReport) {
		MasterReportsDto masterReportsDto = new MasterReportsDto();
		try {
			masterReportsDto = masterAdminReportsService.saveMasterReports(masterReport);
		} catch (Exception e) {
			return new ResponseEntity<MasterReportsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<MasterReportsDto>(masterReportsDto, HttpStatus.OK);
	}

	@PutMapping("/reports/update/{reportId}")
	public ResponseEntity<MasterReportsDto> updateMasterReports(@RequestBody MasterReport masterReport,
			@PathVariable("reportId") Long reportId) {
		MasterReportsDto masterReportsDto = new MasterReportsDto();
		try {
			masterReportsDto = masterAdminReportsService.saveMasterReports(masterReport);
		} catch (Exception e) {
			return new ResponseEntity<MasterReportsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<MasterReportsDto>(masterReportsDto, HttpStatus.OK);
	}

	@DeleteMapping("/delete/all/reports")
	public ResponseEntity<?> deleteAllMaterReports() {
		try {
			masterReportsRepository.deleteAll();
		} catch (Exception e) {
			return new ResponseEntity<MasterReport>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/delete/reports/{reportId}")
	public ResponseEntity<?> deleteAllMaterReports(@PathVariable("reportId") Long reportId) {
		try {
			masterReportsRepository.deleteById(reportId);
		} catch (Exception e) {
			return new ResponseEntity<MasterReport>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
