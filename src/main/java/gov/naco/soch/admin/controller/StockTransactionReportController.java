package gov.naco.soch.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.ProductListReportService;
import gov.naco.soch.admin.service.StockTransactionReportService;
import gov.naco.soch.dto.StockTransactionDto;
import gov.naco.soch.projection.ProductListProjection;
import gov.naco.soch.projection.StockTransactionProjection;

@RestController
@RequestMapping("/stocktransaction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StockTransactionReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(StockTransactionReportController.class);
	
	@Autowired
	private StockTransactionReportService stockTransactionReportService;
	
	@Autowired
	private ProductListReportService productListReportService;
	
	@GetMapping("/report/list/{facilityId}")
	public ResponseEntity <List<StockTransactionDto>> getStockReport(
			@PathVariable(name="facilityId") int facilityId,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value="fromDate",required=true) Date fromDate,
			@RequestParam(value="toDate",required=true) Date toDate) {
		List<StockTransactionDto> stockTransactionDto;
		try {
			stockTransactionDto = stockTransactionReportService.getStockReport(facilityId,productId,fromDate,toDate);
			logger.info("fetched the stock transaction list");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new ResponseEntity <List<StockTransactionDto>>(stockTransactionDto,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/report/export/{facilityId}")
	public ResponseEntity<InputStreamResource> getReportExcelExport(@PathVariable("facilityId") int facilityId,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value="fromDate",required=true) Date fromDate,
			@RequestParam(value="toDate",required=true) Date toDate) throws IOException{
		
		ByteArrayInputStream inputstream = stockTransactionReportService.exportToExcel(facilityId,productId,fromDate,toDate);	  
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
	    headers.add("Content-Disposition", "attachment; filename=StockTransctionReport.xlsx");
	    return ResponseEntity
	               .ok()
	               .headers(headers)
	               .body(new InputStreamResource(inputstream));
	}

}
