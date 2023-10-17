
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
import gov.naco.soch.dto.StatusGlobalDto;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.StatusGlobalMapperUtil;

@Transactional
@Service
public class StatusGlobalService {

	//@Autowired
	//private StatusGlobalMasterRepository statusGlobalRepository;

	private static final Logger logger = LoggerFactory.getLogger(StatusGlobalService.class);

	// method to get all statusGlobal details
	/*public List<StatusGlobalDto> getAllStatusGlobals() {
		List<StatusGlobalMaster> statusGlobals = statusGlobalRepository.findAll();
		logger.debug("mapToStatusGlobalDto method called with parameters->{}", statusGlobals);
		return StatusGlobalMapperUtil.mapToStatusGlobalDto(statusGlobals);
	}*/

	// method to add/update statusGlobal details
	/*public StatusGlobalDto addStatusGlobal(StatusGlobalDto statusGlobalDto) {

		String statusGlobalName = statusGlobalDto.getGlobalStatusName().trim().toString();
		int count = 0;
		boolean isEdit = false;
		StatusGlobalMaster statusGlobal = new StatusGlobalMaster();

		if (statusGlobalDto.getId() != null && statusGlobalDto.getId() != 0) {
			// To check whether the statusGlobalName is already exist in table in edit
			count = statusGlobalRepository.existsByglobalStatusNameInEdit(statusGlobalName, statusGlobalDto.getId());
			isEdit = true;
		} else {
			// To check whether the statusGlobalName is already exist in table in add
			count = statusGlobalRepository.existsByglobalStatusNameIgnoreCase(statusGlobalName);
		}

		//get all statusGlobal details by statusGlobal_id in edit
		if (isEdit) {
			statusGlobal = statusGlobalRepository.findById(statusGlobalDto.getId()).get();
		}

		// invoking throwError method if duplicate found in statusGlobalName
		if (count != 0) {
			String errorfield = "statusGlobalName";
			throwError(errorfield, statusGlobalDto.getGlobalStatusName());
		}

		logger.debug("mapToStatusGlobal method called with parameters->{}", statusGlobalDto);
		statusGlobal = StatusGlobalMapperUtil.mapToStatusGlobal(statusGlobalDto, statusGlobal);

		statusGlobalRepository.save(statusGlobal);
		statusGlobalDto.setId(statusGlobal.getId());
		return statusGlobalDto;
	}*/

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
