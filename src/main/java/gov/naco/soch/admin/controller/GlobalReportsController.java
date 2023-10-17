package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.GlobalReportsService;
import gov.naco.soch.dto.ReportsModuleDto;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GlobalReportsController {
	@Autowired
	GlobalReportsService globalReportsService;

	@GetMapping("/get/{roleID}")
	public ResponseEntity<List<ReportsModuleDto>> getReportsDetails(@PathVariable("roleID") Long roleId) {
		List<ReportsModuleDto> reportsModuleDtos = new ArrayList<ReportsModuleDto>();
		try {
			reportsModuleDtos = globalReportsService.getReportsDetails(roleId);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportsModuleDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ReportsModuleDto>>(reportsModuleDtos, HttpStatus.OK);

	}

}
