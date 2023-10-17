
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
import gov.naco.soch.dto.ProductUomDto;
import gov.naco.soch.entity.ProductUomMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.ProductUomMapperUtil;
import gov.naco.soch.repository.ProductUomRepository;

@Service
@Transactional
public class ProductUomService {

	@Autowired
	private ProductUomRepository productUomRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProductUomService.class);

	// function to select all Product_uom details
	public List<ProductUomDto> getAllProductUoms() {
		List<ProductUomMaster> productUoms = productUomRepository.findAll();
		return ProductUomMapperUtil.mapToProductUomDto(productUoms);
	}

	// function to add/update Product_uom details
	public ProductUomDto addProductUom(ProductUomDto productUomDto) {

		String productUomName = productUomDto.getUomName().trim().toString();
		int count = 0;
		boolean isEdit = false;
		ProductUomMaster productUom = new ProductUomMaster();

		if (productUomDto.getId() != null && productUomDto.getId() != 0) {
			// To check whether the productUomName is already exist in table in edit
			count = productUomRepository.existsByuomNameInEdit(productUomName, productUomDto.getId());
			isEdit=true;
		} else {
			// To check whether the productUomName is already exist in table in add
			count = productUomRepository.existsByuomNameIgnoreCase(productUomName);
		}

		// invoking throwError method if duplicate found in productUomName
		if (count != 0) {
			String errorfield = "uomName";
			throwError(errorfield, productUomDto.getUomName());
		}

		//get productUom details by productUom_id in edit
		if (isEdit) {
			productUom = productUomRepository.findById(productUomDto.getId()).get();
		}
		logger.debug("mapToProductUom method called with parameters->{}", productUomDto);
		productUom = ProductUomMapperUtil.mapToProductUom(productUomDto, productUom);
		
		//save productUom details into productUom_master table
		productUomRepository.save(productUom);
		productUomDto.setId(productUom.getId());

		return productUomDto;
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
