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
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;

/**
 * @date 2020-Jan-02 12:25:04 PM
 */

@Transactional
@Service
public class PatientStatusService {
	@Autowired
	//private PatientStatusMasterRepository patientStatusRepository;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(PatientStatusService.class);

	// Function to select all PatientStatus details from database
	/*public List<PatientStatusDto> getAllPatientStatus() {
		logger.debug("findAll method called to fetch all values from PatientStatusMaster Table");
		List<PatientStatusMaster> patientStatus = (List<PatientStatusMaster>) patientStatusRepository.findAll();
		return PatientStatusMapperUtil.mapToPatientStatusDtoList(patientStatus);
	}*/

	// Function to save PatientStatus details to database
	/*public PatientStatusDto savePatientStatus(PatientStatusDto patientStatusDto) {

		int count = 0;
		boolean isEdit = false;

		if (patientStatusDto.getId() != null && patientStatusDto.getId() != 0) {

			logger.debug("existsByPatientStatusNameInEdit method called");
			count = patientStatusRepository.existsByPatientStatusNameInEdit(patientStatusDto.getPatientStatusName(),
					patientStatusDto.getId());
			logger.debug("existsByPatientStatusNameInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherPatientStatusNameInAdd method called");
			count = patientStatusRepository
					.existsByOtherPatientStatusNameInAdd(patientStatusDto.getPatientStatusName());
			logger.debug("existsByOtherPatientStatusNameInAdd method returns with Count ", count);
		}

		// Handling exception for duplication of patientStatusName field and sending a
		// valid
		// error response JSON to UI layer
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "patientStatusName";

			logger.debug("call throwError if duplicate found");
			throwError(errorfield, patientStatusDto.getPatientStatusName());
		}

		PatientStatusMaster patientStatus = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular patientStatus");
			patientStatus = patientStatusRepository.findById(patientStatusDto.getId()).get();
			logger.debug("findById method returns with entity values ", patientStatus);
		}

		logger.debug("mapToPatientStatusEntity method called for convert Dto to entity for patientStatusDto");
		patientStatus = PatientStatusMapperUtil.mapToPatientStatusEntity(patientStatusDto, patientStatus);
		logger.debug("mapToPatientStatusEntity method return entity patientStatus", patientStatus);

		logger.debug("save method called for save values of patientStatus to DB");
		// Code segment to save patientStatus if duplicates are not found
		patientStatus = patientStatusRepository.save(patientStatus);
		logger.debug("save method return entity patientStatus", patientStatus);

		logger.debug("mapToPatientStatusDto method called for convert entity to Dto for patientStatus");
		patientStatusDto = PatientStatusMapperUtil.mapToPatientStatusDto(patientStatus);
		logger.debug("mapToPatientStatusDto method return Dto patientStatusDto", patientStatusDto);

		return patientStatusDto;
	}
	*/

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
