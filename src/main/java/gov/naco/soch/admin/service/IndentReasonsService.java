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
import gov.naco.soch.dto.IndentReasonsDto;
import gov.naco.soch.entity.IndentReasonsMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.IndentReasonsMapperUtil;
import gov.naco.soch.repository.IndentReasonsMasterRepository;

/**
 * @date 2020-Jan-01 12:27:15 PM
 */
@Transactional
@Service
public class IndentReasonsService {

	@Autowired
	private IndentReasonsMasterRepository indentReasonsRepository;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(IndentReasonsService.class);

	// Function to select all IndentReasons details from database
	public List<IndentReasonsDto> getAllIndentReasons() {
		logger.debug("findAll method called to fetch all values from indent_reasons_master Table");
		List<IndentReasonsMaster> indentReasons = (List<IndentReasonsMaster>) indentReasonsRepository.findAll();
		return IndentReasonsMapperUtil.mapToIndentReasonsDtoList(indentReasons);
	}

	// Function to save IndentReasons details to database
	public IndentReasonsDto saveIndentReasons(IndentReasonsDto indentReasonsDto) {
		int count = 0;
		boolean isEdit = false;

		if (indentReasonsDto.getId() != null && indentReasonsDto.getId() != 0) {

			logger.debug("existsByIndentReasonsNameInEdit method called");
			count = indentReasonsRepository.existsByIndentReasonsNameInEdit(indentReasonsDto.getIndentReasonsName(),
					indentReasonsDto.getId());
			logger.debug("existsByIndentReasonsNameInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherIndentReasonsNameInAdd method called");
			count = indentReasonsRepository
					.existsByOtherIndentReasonsNameInAdd(indentReasonsDto.getIndentReasonsName());
			logger.debug("existsByOtherIndentReasonsNameInAdd method returns with Count ", count);
		}

		// Handling exception for duplication of indentReasonsName field and sending a
		// valid
		// error response JSON to UI layer
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "indentReasonsName";

			logger.debug("call throwError if duplicate found");
			throwError(errorfield, indentReasonsDto.getIndentReasonsName());
		}

		IndentReasonsMaster indentReasons = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular indentReasons");
			indentReasons = indentReasonsRepository.findById(indentReasonsDto.getId()).get();
			logger.debug("findById method returns with entity values ", indentReasons);
		}

		logger.debug("mapToIndentReasonsEntity method called for convert Dto to entity for indentReasonsDto");
		indentReasons = IndentReasonsMapperUtil.mapToIndentReasonsEntity(indentReasonsDto, indentReasons);
		logger.debug("mapToIndentReasonsEntity method return entity indentReasons", indentReasons);

		logger.debug("save method called for save values of indentReasons to DB");
		// Code segment to save indentReasons if duplicates are not found
		indentReasons = indentReasonsRepository.save(indentReasons);
		logger.debug("save method return entity indentReasons", indentReasons);

		logger.debug("mapToIndentReasonsDto method called for convert entity to Dto for indentReasons");
		indentReasonsDto = IndentReasonsMapperUtil.mapToIndentReasonsDto(indentReasons);
		logger.debug("mapToIndentReasonsDto method return Dto indentReasonsDto", indentReasonsDto);

		return indentReasonsDto;
	}

	/**
	 * @param errorfield
	 * @param indentReasonsName
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
