/**
 * 
 */
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
import gov.naco.soch.dto.IndentRequestStatusDto;
import gov.naco.soch.entity.IndentRequestStatusMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.IndentRequestStatusMapperUtil;
import gov.naco.soch.repository.IndentRequestStatusMasterRepository;

/**
 * @date 2019-Dec-31 10:15:55 AM
 */
@Transactional
@Service
public class IndentRequestStatusService {

	@Autowired
	private IndentRequestStatusMasterRepository indentRequestStatusRepository;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(IndentRequestStatusService.class);

	// Function to select all IndentRequestStatus details from database
	public List<IndentRequestStatusDto> getAllIndentRequestStatuses() {
		logger.debug("findAll method called to fetch all values from indentRequestStatusMaster Table");
		List<IndentRequestStatusMaster> indentRequestStatuses = (List<IndentRequestStatusMaster>) indentRequestStatusRepository
				.findAll();
		return IndentRequestStatusMapperUtil.mapToIndentRequestStatusDtoList(indentRequestStatuses);
	}

	// Function to save IndentRequestStatus details to database
	public IndentRequestStatusDto saveIndentRequestStatus(IndentRequestStatusDto indentRequestStatusDto) {

		int count = 0;
		boolean isEdit = false;

		if (indentRequestStatusDto.getId() != null && indentRequestStatusDto.getId() != 0) {

			logger.debug("existsByIndentRequestStatusNameInEdit method called");
			count = indentRequestStatusRepository.existsByIndentRequestStatusNameInEdit(
					indentRequestStatusDto.getIndentRequestStatusName(), indentRequestStatusDto.getId());
			logger.debug("existsByIndentRequestStatusNameInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherIndentRequestStatusNameInAdd method called");
			count = indentRequestStatusRepository
					.existsByOtherIndentRequestStatusNameInAdd(indentRequestStatusDto.getIndentRequestStatusName());
			logger.debug("existsByOtherIndentRequestStatusNameInAdd method returns with Count ", count);
		}

		// Handling exception for duplication of indentRequestStatusName field and
		// sending a
		// valid
		// error response JSON to UI layer
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "indentRequestStatusName";
			
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, indentRequestStatusDto.getIndentRequestStatusName());
		}

		IndentRequestStatusMaster indentRequestStatus = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular indentRequestStatus");
			indentRequestStatus = indentRequestStatusRepository.findById(indentRequestStatusDto.getId()).get();
			logger.debug("findById method returns with entity values ", indentRequestStatus);
		}

		logger.debug(
				"mapToIndentRequestStatusEntity method called for convert Dto to entity for indentRequestStatusDto");
		indentRequestStatus = IndentRequestStatusMapperUtil.mapToIndentRequestStatusEntity(indentRequestStatusDto,
				indentRequestStatus);
		logger.debug("mapToIndentRequestStatusEntity method return entity indentRequestStatus", indentRequestStatus);

		logger.debug("save method called for save values of indentRequestStatus to DB");
		// Code segment to save indentRequestStatus if duplicates are not found
		indentRequestStatus = indentRequestStatusRepository.save(indentRequestStatus);
		logger.debug("save method return entity indentRequestStatus", indentRequestStatus);

		logger.debug("mapToIndentRequestStatusDto method called for convert entity to Dto for indentRequestStatus");
		indentRequestStatusDto = IndentRequestStatusMapperUtil.mapToIndentRequestStatusDto(indentRequestStatus);
		logger.debug("mapToIndentRequestStatusDto method return Dto gitStatusDto", indentRequestStatusDto);

		return indentRequestStatusDto;
	}

	/**
	 * @param errorfield
	 * @param indentRequestStatusName
	 */
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
