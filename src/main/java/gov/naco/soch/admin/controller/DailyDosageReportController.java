package gov.naco.soch.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.DailyDosageReportService;
import gov.naco.soch.dto.DailyDosageDto;

@RestController
@RequestMapping("/dailydosage")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DailyDosageReportController {

	@Autowired
	private DailyDosageReportService dailyDosageReportService;

	private static final Logger logger = LoggerFactory.getLogger(DailyDosageReportController.class);

	public DailyDosageReportController() {
	}

	// API to get daily dosage report
	@GetMapping("reports/list/{facilityId}")
	public @ResponseBody ResponseEntity<List<DailyDosageDto>> getDailyDosageReport(
			@PathVariable("facilityId") Long facilityId, @RequestParam(required = false) String month,
			@RequestParam(required = false) int year) {
		logger.debug("getDailyDosageReportist method called");
		List<DailyDosageDto> dailyDosageDtos = new ArrayList<DailyDosageDto>();
		try {
			dailyDosageDtos = dailyDosageReportService.getDailyDosageReportList(facilityId, month, year);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return new ResponseEntity<List<DailyDosageDto>>(dailyDosageDtos, HttpStatus.OK);
	}

	@GetMapping("/reports/export/{facilityId}")
	public ResponseEntity<InputStreamResource> getReportExcelExport(@PathVariable("facilityId") Long facilityId,
			@RequestParam(required = false) String month, @RequestParam(required = false) int year,
			@RequestParam(required = false) Long userId) throws IOException {

		ByteArrayInputStream reportStream;
		try {
			reportStream = dailyDosageReportService.exportExcelData(facilityId, month, year, userId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=Daily_Dosage_Report.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(reportStream));
	}

}
