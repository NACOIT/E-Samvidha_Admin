package gov.naco.soch.admin.controller;

import java.io.ByteArrayInputStream;
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
import org.springframework.web.bind.annotation.RestController;
import gov.naco.soch.admin.service.ProductListReportService;
import gov.naco.soch.projection.ProductListProjection;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductListReportController {

	private static final Logger logger = LoggerFactory.getLogger(ProductListReportController.class);

	@Autowired
	private ProductListReportService productListReportService;

	@GetMapping("/reports/list/{facilityId}")
	public ResponseEntity<List<ProductListProjection>> getProductsByFacility(
			@PathVariable("facilityId") Long facilityId) {
		List<ProductListProjection> productListProjection;
		try {
			productListProjection = productListReportService.getProductsByFacility(facilityId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		logger.info("fetched the product list by facilityId");
		return new ResponseEntity<List<ProductListProjection>>(productListProjection, HttpStatus.CREATED);

	}

	@GetMapping(value = "reports/export/{facilityId}")
	public ResponseEntity<InputStreamResource> exportToExcel(@PathVariable("facilityId") Long facilityId) {
		ByteArrayInputStream inputstream;
		try {
			inputstream = productListReportService.exportToExcel(facilityId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=productList.xlsx");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputstream));
	}

}
