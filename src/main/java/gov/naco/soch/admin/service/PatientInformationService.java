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

import gov.naco.soch.dto.DatavaluesPatientInformationMasterDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;

/**
 * @date 2020-Jan-02 3:45:35 PM
 */

// service class interact data with database
@Transactional
@Service
public class PatientInformationService {

	//@Autowired
	//private DatavaluesPatientInformationMasterRepository patientInformationRepository;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(PatientInformationService.class);

	// Function to select all PatientInformation details from database
	/*public List<DatavaluesPatientInformationMasterDto> getAllPatientInformation() {
		logger.debug("findAll method called to fetch all values from DatavaluesPatientInformationMaster Table");
		List<DatavaluesPatientInformationMaster> patientInformation = (List<DatavaluesPatientInformationMaster>) patientInformationRepository
				.findAll();
		return PatientInformationMapperUtil.mapToPatientInformationDtoList(patientInformation);
	}*/

	// Function to save PatientInformation details to database
	/*public DatavaluesPatientInformationMasterDto savePatientInformation(
			DatavaluesPatientInformationMasterDto patientInformationDto) {

		int count = 0;
		boolean isEdit = false;

		if (patientInformationDto.getId() != null && patientInformationDto.getId() != 0) {

			logger.debug("existsByDataTypeAndDataValueInEdit method called");
			count = patientInformationRepository.existsByDataTypeAndDataValueInEdit(patientInformationDto.getDataType(),
					patientInformationDto.getDataValue(), patientInformationDto.getId());
			logger.debug("existsByDataTypeAndDataValueInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherDataTypeAndDataValueInAdd method called");
			count = patientInformationRepository.existsByOtherDataTypeAndDataValueInAdd(
					patientInformationDto.getDataType(), patientInformationDto.getDataValue());
			logger.debug("existsByOtherDataTypeAndDataValueInAdd method returns with Count ", count);
		}

		/* Error handling for duplicate records */
		// Handling exception for duplication of combination of dataType field and
		// dataType field and sending a valid
		// error response JSON to UI layer
	/*
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String field = "dataType";

			logger.debug("call throwError if duplicate found");
			throwError(field, patientInformationDto.getDataType(), patientInformationDto.getDataValue());
		}

		DatavaluesPatientInformationMaster patientInformation = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular patientInformation");
			patientInformation = patientInformationRepository.findById(patientInformationDto.getId()).get();
			logger.debug("findById method returns with entity values ", patientInformation);
		}
		
		logger.debug("mapToPatientInformationEntity method called for convert Dto to entity for patientInformationDto");
		patientInformation = PatientInformationMapperUtil.mapToPatientInformationEntity(patientInformationDto, patientInformation);
		logger.debug("mapToPatientInformationEntity method return entity patientInformation", patientInformation);

		logger.debug("save method called for save values of patientInformation to DB");
		// Code segment to save patientInformation if duplicates are not found
		patientInformation = patientInformationRepository.save(patientInformation);
		logger.debug("save method return entity patientInformation", patientInformation);

		logger.debug("mapToPatientInformationDto method called for convert entity to Dto for patientInformation");
		patientInformationDto = PatientInformationMapperUtil.mapToPatientInformationDto(patientInformation);
		logger.debug("mapToPatientInformationDto method return Dto patientInformationDto", patientInformationDto);
		
		return patientInformationDto;
	}
*/

	/**
	 * Method to throw error in case of validation errors
	 * 
	 * @param errorfield
	 * @param errorFieldValue
	 */
	private void throwError(String errorfield, String errorFieldValueOne, String errorFieldValueTwo) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(
				Constants.DUPLICATE_FOUND + "'" + errorFieldValueOne + " With " + errorFieldValueTwo + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(
				Constants.DUPLICATE_FOUND + " '" + errorFieldValueOne + " With " + errorFieldValueTwo + "' ",
				errorResponse, HttpStatus.BAD_REQUEST);
	}
}
