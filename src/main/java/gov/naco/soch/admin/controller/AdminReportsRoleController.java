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

import gov.naco.soch.admin.dto.PageReportModuleDto;
import gov.naco.soch.admin.dto.PageReportsSubModule;
import gov.naco.soch.admin.dto.ReportModuleDto;
import gov.naco.soch.admin.dto.ReportsSubModuleDto;
import gov.naco.soch.admin.service.ReportsModuleAndSubModuleService;
import gov.naco.soch.entity.ReportsModule;
import gov.naco.soch.entity.ReportsSubModule;
import gov.naco.soch.repository.ReportsModuleRepository;
import gov.naco.soch.repository.ReportsSubModuleRepository;

@RestController
@RequestMapping("/api")
public class AdminReportsRoleController {
	@Autowired
	ReportsModuleAndSubModuleService reportsModuleAndSubModuleService;
	@Autowired
	ReportsModuleRepository reportsModuleRepository;
	@Autowired
	ReportsSubModuleRepository reportsSubModuleRepository;

	/* Reports Module CRUD operations - [START] */
	@GetMapping("/reports/get/module")
	public ResponseEntity<PageReportModuleDto> getAllReportsModuleData(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize, @RequestParam(defaultValue = "module_id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		PageReportModuleDto pageReportModuleDto = new PageReportModuleDto();
		try {
			pageReportModuleDto = reportsModuleAndSubModuleService.getAllReportsModuleData(pageIndex, pageSize, sortBy,
					sortType);
		} catch (Exception e) {
			return new ResponseEntity<PageReportModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PageReportModuleDto>(pageReportModuleDto, HttpStatus.OK);
	}

	@GetMapping("/reports/getAll/module")
	public ResponseEntity<List<ReportModuleDto>> getReportsModuleData() {
		List<ReportModuleDto> reportModuleDtos = new ArrayList<ReportModuleDto>();
		try {
			reportModuleDtos = reportsModuleAndSubModuleService.getAllReportsModuleData();
		} catch (Exception e) {
			return new ResponseEntity<List<ReportModuleDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ReportModuleDto>>(reportModuleDtos, HttpStatus.OK);
	}

	@GetMapping("/reports/get/module/{moduleId}")
	public ResponseEntity<ReportModuleDto> getReportsModuleDataById(@PathVariable("moduleId") Long moduleId) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		try {
			reportModuleDto = reportsModuleAndSubModuleService.getReportsModuleDataById(moduleId);

		} catch (Exception e) {
			return new ResponseEntity<ReportModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportModuleDto>(reportModuleDto, HttpStatus.OK);
	}

	@PostMapping("/reports/module")
	public ResponseEntity<ReportModuleDto> saveReportModuleData(@RequestBody ReportsModule reportsModule) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		try {
			reportModuleDto = reportsModuleAndSubModuleService.saveReportsModuleDataById(reportsModule);
		} catch (Exception e) {
			return new ResponseEntity<ReportModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportModuleDto>(reportModuleDto, HttpStatus.OK);

	}

	@PutMapping("/reports/module")
	public ResponseEntity<ReportModuleDto> updateReportModuleData(@RequestBody ReportsModule reportsModule) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		try {
			reportModuleDto = reportsModuleAndSubModuleService.saveReportsModuleDataById(reportsModule);
		} catch (Exception e) {
			return new ResponseEntity<ReportModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportModuleDto>(reportModuleDto, HttpStatus.OK);

	}

	@DeleteMapping("/reports/delete/module/{moduleId}")
	public ResponseEntity<?> deleteReportsModule(@PathVariable("moduleId") Long moduleId) {

		try {
			reportsModuleRepository.deleteById(moduleId);
		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/reports/deleteAll/module/")
	public ResponseEntity<?> deleteAllReportsModule(@PathVariable("moduleId") Long moduleId) {

		try {
			reportsModuleRepository.deleteAll();
		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/* Reports Module CRUD operations - [END] */

	/* Reports SubModule CRUD operations - [START] */
	@GetMapping("/reports/get/submodule")
	public ResponseEntity<PageReportsSubModule> getAllReportsSubModuleData(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize, @RequestParam(defaultValue = "submodule_id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		PageReportsSubModule pageReportsSubModule = new PageReportsSubModule();
		try {
			pageReportsSubModule = reportsModuleAndSubModuleService.getReportsSubModuleData(pageIndex, pageSize, sortBy,
					sortType);
		} catch (Exception e) {
			return new ResponseEntity<PageReportsSubModule>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PageReportsSubModule>(pageReportsSubModule, HttpStatus.OK);
	}

	@GetMapping("/reports/get/submodule/{subModuleId}")
	public ResponseEntity<ReportsSubModuleDto> getReportsSubModuleDataById(
			@PathVariable("subModuleId") Long subModuleId) {
		ReportsSubModuleDto reportsSubModules = null;
		try {
			reportsSubModules = reportsModuleAndSubModuleService.getReportsSubModuleDataById(subModuleId);
		} catch (Exception e) {
			return new ResponseEntity<ReportsSubModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportsSubModuleDto>(reportsSubModules, HttpStatus.OK);
	}

	@GetMapping("/reports/get/submodules/{moduleId}")
	public ResponseEntity<List<ReportsSubModuleDto>> getSubModuleByModuleId(@PathVariable("moduleId") Long moduleId) {
		List<ReportsSubModuleDto> reportsSubModules = new ArrayList<ReportsSubModuleDto>();
		try {
			reportsSubModules = reportsModuleAndSubModuleService.getSubModuleByModuleId(moduleId);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportsSubModuleDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ReportsSubModuleDto>>(reportsSubModules, HttpStatus.OK);
	}

	@PostMapping("/reports/submodule")
	public ResponseEntity<ReportsSubModuleDto> saveReportSubModuleData(@RequestBody ReportsSubModule reportsSubModule) {
		ReportsSubModuleDto reportsSubModuleDto = new ReportsSubModuleDto();
		try {
			reportsSubModuleDto = reportsModuleAndSubModuleService.saveReportsSubModuleDataById(reportsSubModule);
		} catch (Exception e) {
			return new ResponseEntity<ReportsSubModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportsSubModuleDto>(reportsSubModuleDto, HttpStatus.OK);

	}

	@PutMapping("/reports/submodule")
	public ResponseEntity<ReportsSubModuleDto> updateReportSubModuleData(
			@RequestBody ReportsSubModule reportsSubModule) {
		ReportsSubModuleDto reportsSubModuleDto = new ReportsSubModuleDto();
		try {
			reportsSubModuleDto = reportsModuleAndSubModuleService.saveReportsSubModuleDataById(reportsSubModule);
		} catch (Exception e) {
			return new ResponseEntity<ReportsSubModuleDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportsSubModuleDto>(reportsSubModuleDto, HttpStatus.OK);

	}

	@DeleteMapping("/reports/delete/submodule/{subModuleId}")
	public ResponseEntity<?> deleteSubModulebyId(@PathVariable("subModuleId") Long subModuleId) {

		try {
			reportsSubModuleRepository.deleteById(subModuleId);
		} catch (Exception e) {
			return new ResponseEntity<ReportsSubModule>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/reports/deleteAll/submodule/")
	public ResponseEntity<?> deleteSubModule() {

		try {
			reportsSubModuleRepository.deleteAll();
		} catch (Exception e) {
			return new ResponseEntity<ReportsSubModule>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/* Reports SubModule CRUD operations - [END] */
}
