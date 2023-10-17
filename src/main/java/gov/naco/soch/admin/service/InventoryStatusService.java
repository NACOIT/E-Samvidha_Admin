
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
import gov.naco.soch.dto.InventoryStatusDto;
import gov.naco.soch.entity.InventoryStatusMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.InventoryStatusMapperUtil;
import gov.naco.soch.repository.InventoryStatusMasterRepository;

@Transactional
@Service
public class InventoryStatusService {

	@Autowired
	private InventoryStatusMasterRepository inventoryStatusRepository;

	private static final Logger logger = LoggerFactory.getLogger(InventoryStatusService.class);

	// function to select all inventory_status details
	public List<InventoryStatusDto> getAllInventoryStatus() {
		List<InventoryStatusMaster> inventoryStatuses = inventoryStatusRepository.findAll();
		logger.debug("mapToInventoryStatusDto method called with parameters->{}", inventoryStatuses);
		return InventoryStatusMapperUtil.mapToInventoryStatusDto(inventoryStatuses);
	}

	// function to add/update inventory_status details
	public InventoryStatusDto addInventoryStatus(InventoryStatusDto inventoryStatusDto) {

		String inventoryStatusName = inventoryStatusDto.getInventoryStatusName().trim().toString();
		int count = 0;
		boolean isEdit = false;
		InventoryStatusMaster inventoryStatus = new InventoryStatusMaster();

		if (inventoryStatusDto.getId() != null && inventoryStatusDto.getId() != 0) {
			count = inventoryStatusRepository.existsByInventoryStatusNameInEdit(inventoryStatusName,
					inventoryStatusDto.getId());
			isEdit = true;
		} else {
			count = inventoryStatusRepository.existsByInventoryStatusNameInAdd(inventoryStatusName);
		}

		// invoking throw error method if duplicate found in inventoryStatusName
		if (count != 0) {
			String errorfield = "inventoryStatusName";
			throwError(errorfield, inventoryStatusDto.getInventoryStatusName());
		}

		//to get all inventory status details using id in edit
		if (isEdit) {
			inventoryStatus = inventoryStatusRepository.findById(inventoryStatusDto.getId()).get();
		}

		logger.debug("mapToInventoryStatus method called with parameters->{}", inventoryStatusDto);
		inventoryStatus = InventoryStatusMapperUtil.mapToInventoryStatus(inventoryStatusDto, inventoryStatus);

		// save inventoryStatus details into table
		inventoryStatusRepository.save(inventoryStatus);
		inventoryStatusDto.setId(inventoryStatus.getId());
		return inventoryStatusDto;

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
