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

import gov.naco.soch.admin.service.ProductUomService;
import gov.naco.soch.dto.ProductUomDto;

@RestController
@RequestMapping("/productuom")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductUomController {

	@Autowired
	private ProductUomService productUomService;

	private static final Logger logger = LoggerFactory.getLogger(ProductUomController.class);

	public ProductUomController() {
	}

	/**
	 * @return productUomList
	 */
	@GetMapping("/list")
	public @ResponseBody List<ProductUomDto> getAllProductUoms() {
		logger.debug("getAllProductUoms method is invoked");
		List<ProductUomDto> productUomList=productUomService.getAllProductUoms();
		return productUomList;
	}

	/**
	 * @param productUomDto
	 * @return productUomDto
	 */
	@PostMapping("/add")
	public @ResponseBody ProductUomDto addProductUom(@Valid @RequestBody ProductUomDto productUomDto) {
		logger.debug("addProductUom method called with parameters->{}", productUomDto);
		productUomDto = productUomService.addProductUom(productUomDto);
		return productUomDto;
	}

}
