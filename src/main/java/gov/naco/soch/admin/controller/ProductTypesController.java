package gov.naco.soch.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.ProductTypesService;
import gov.naco.soch.dto.ProductTypesDto;

@RestController
@RequestMapping("/producttypes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductTypesController {

	@Autowired
	private ProductTypesService productTypesService;

	private static final Logger logger = LoggerFactory.getLogger(ProductTypesController.class);

	/**
	 * @return productTypes
	 */
	@GetMapping("/list")
	public @ResponseBody List<ProductTypesDto> getAllProductTypes() {
		logger.debug("getAllProductTypes method called");
		List<ProductTypesDto> productTypes = productTypesService.getAllProductTypes();
		return productTypes;
	}

	/**
	 * @param productTypesDto
	 * @return productTypesDto
	 */
	@PostMapping("/add")
	public @ResponseBody ProductTypesDto addProductTypes(@Valid @RequestBody ProductTypesDto productTypesDto) {
		logger.debug("addProductTypes method called with parameters->{}", productTypesDto);
		return productTypesService.addProductTypes(productTypesDto);
	}

}
