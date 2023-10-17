package gov.naco.soch.admin.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.naco.soch.admin.service.ProductService;
import gov.naco.soch.dto.ProductDto;

//Controller class for API call

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

	@Autowired
	ProductService productService;

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	public ProductController() {
	}

	/*
	 * Save product.
	 * 
	 */
	@PostMapping("/save")
	public @ResponseBody ResponseEntity<ProductDto> saveProduct(
			@Valid @RequestParam(name = "fileKey", required = false) MultipartFile file,
			@RequestParam(name = "product", required = true) String product) {
		ProductDto productDto = new ProductDto();
		ObjectMapper mapper = new ObjectMapper();
		try {
			productDto = mapper.readValue(product, ProductDto.class);
			if (Double.parseDouble(productDto.getMinShelfLife()) < 0
					|| Double.parseDouble(productDto.getMinShelfLife()) > 100)
				return new ResponseEntity<ProductDto>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Entering into method saveProduct with productDto->{}:", productDto);
		return new ResponseEntity<ProductDto>(productService.saveProduct(productDto, file), HttpStatus.OK);
	}

	/**
	 * Currently Not in use
	 * 
	 * @param file
	 * @param Id
	 * @param isEdit
	 * @return
	 */
	@PostMapping("/fileupload")
	public @ResponseBody Boolean productImageUpload(@RequestParam("fileKey") MultipartFile file,
			@RequestParam("productId") Long Id, @RequestParam("isEdit") boolean isEdit) {
		return productService.uploadProductImage(file, Id, isEdit);
	}

	/**
	 * Optimized is_delete=false
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public @ResponseBody List<ProductDto> getProducts() {
		logger.debug("Entering into method getProducts");
		return productService.getProducts();
	}

	/**
	 * find product by product id
	 * 
	 * @param productId
	 * @return
	 */
	@GetMapping("/findby/{productId}")
	public @ResponseBody ProductDto getProductById(@PathVariable("productId") Long productId) {
		logger.debug("Entering into method getProductById");
		return productService.getProductById(productId);
	}

	/**
	 * Optimized product list for Dropdowns is_active=true and is_delete=false
	 * 
	 * @return
	 */
	@GetMapping("/active/list")
	public @ResponseBody List<ProductDto> getActiveProductListForDropDown() {
		logger.debug("Entering into method getProducts");
		return productService.getActiveProductListForDropDown();
	}

	/*
	 * Save product.
	 * 
	 */
	@DeleteMapping("/{productId}/delete")
	public @ResponseBody boolean deleteProduct(@PathVariable("productId") Long productId) {
		logger.debug("Entering into method deleteProduct with productId->{}:", productId);
		return productService.deleteProduct(productId);
	}

	/**
	 * product normal search. search criteria: productname/short code
	 * 
	 * @param searchValue
	 * @return
	 */
	@GetMapping("/normalsearch")
	public @ResponseBody List<ProductDto> productsNormalSearch(@RequestParam String searchValue) {
		logger.debug("Entering into method productsNormalSearch");
		return productService.productsNormalSearch(searchValue);
	}

	/**
	 * product advance search. search criteria:
	 * productname,shortcode,uom,producttype
	 * 
	 * @param searchValue
	 * @return
	 */
	@GetMapping("/advancesearch")
	public @ResponseBody List<ProductDto> productsAdvanceSearch(@RequestParam Map<String, String> searchValue) {
		logger.debug("Entering into method productsAdvanceSearch");
		return productService.productsAdvanceSearch(searchValue);
	}

	@GetMapping("/list/byfacilitytype")
	public @ResponseBody List<ProductDto> getProductsByfacilitytype() {
		logger.debug("Entering into method getProductsByfacilitytype");
		return productService.getProductsByfacilitytype();
	}

	/**
	 * method to get the list of products
	 * 
	 * @return List<ProductDto>
	 */
	@GetMapping("mobile/list")
	public @ResponseBody List<ProductDto> getProductsForMobile() {
		logger.debug("Entering into method getProducts");
		return productService.getProducts();
	}

}
