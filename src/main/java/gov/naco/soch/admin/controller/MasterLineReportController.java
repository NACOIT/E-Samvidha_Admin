package gov.naco.soch.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.MasterLineReportService;
import gov.naco.soch.projection.MasterLineProjection;

@RestController
@RequestMapping("/masterline")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterLineReportController {
	@Autowired
	MasterLineReportService masterLineReportService;

	@GetMapping("/reports/list/{facilityId}")
	public ResponseEntity<List<MasterLineProjection>> getBeneficiaryFacilityMappingData(
			@PathVariable("facilityId") Long facilityId) throws Exception {
		List<MasterLineProjection> masterLineObjList = new ArrayList<MasterLineProjection>();
		try {
			masterLineObjList = masterLineReportService.findByFacility(facilityId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return new ResponseEntity<List<MasterLineProjection>>(masterLineObjList, HttpStatus.CREATED);

	}

	@GetMapping("/reports/export/{facilityId}")
	public ResponseEntity<InputStreamResource> getReportExcelExport(@PathVariable("facilityId") Long facilityId)
			throws IOException {

		ByteArrayInputStream reportStream;
		try {
			reportStream = masterLineReportService.exportExcelData(facilityId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=Master_Line_Report.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(reportStream));
	}

}
