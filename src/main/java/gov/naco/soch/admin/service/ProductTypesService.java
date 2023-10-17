package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.ProductTypesDto;
import gov.naco.soch.entity.ProductTypesMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.ProductTypesMapperUtil;
import gov.naco.soch.repository.ProductTypesMasterRepository;

@Transactional
@Service
public class ProductTypesService {

	@Autowired
	private ProductTypesMasterRepository productTypesRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProductTypesService.class);

	// function to select all product_type details
	public List<ProductTypesDto> getAllProductTypes() {
		List<ProductTypesMaster> productTypesList = productTypesRepository.findAll();
		logger.debug("mapProductTypesDtoList method called with parameters->{}", productTypesList);
		return ProductTypesMapperUtil.mapProductTypesDtoList(productTypesList);
	}

	// function to add/update product_types details
	public ProductTypesDto addProductTypes(ProductTypesDto productTypesDto) {

		int count = 0;
		boolean isEdit = false;
		String productTypeName = productTypesDto.getProductTypeName().trim().toString();
		ProductTypesMaster productTypes = new ProductTypesMaster();

		if (productTypesDto.getId() != null && productTypesDto.getId() != 0) {
			// To check whether the productTypeName is already exist in table in edit
			count = productTypesRepository.existsByproductTypeNameInEdit(productTypeName, productTypesDto.getId());
			isEdit=true;
		} else {
			
			// To check whether the productTypeName is already exist in table in add
			count = productTypesRepository.existsByproductTypeNameIgnoreCase(productTypeName);
		}

		// invoking throwError method if duplicate found in productTypeName
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "productTypeName";
			throwError(errorfield, productTypesDto.getProductTypeName());
		}

		if (isEdit) {
			//fetch all productTypes details using product_type_id
			productTypes = productTypesRepository.findById(productTypesDto.getId()).get();
		}

		logger.debug("mapToProductTypes method called with parameters->{}", productTypesDto);
		productTypes = ProductTypesMapperUtil.mapToProductTypes(productTypesDto, productTypes);
		productTypesRepository.save(productTypes);
		productTypesDto.setId(productTypes.getId());

		return productTypesDto;

	}

	// Method to throw error
	private void throwError(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(Constants.DUPLICATE_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}

}
