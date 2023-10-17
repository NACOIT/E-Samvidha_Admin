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
import gov.naco.soch.dto.GitStatusDto;
import gov.naco.soch.entity.GitStatusMaster;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.GitStatusMapperUtil;
import gov.naco.soch.repository.GitStatusMasterRepository;
import gov.naco.soch.util.CommonConstants;

/**
 * @date 2020-Dec-30 1:37:20 PM
 */
@Transactional
@Service
public class GitStatusService {

	@Autowired
	private GitStatusMasterRepository gitStatusRepository;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(GitStatusService.class);

	// Function to select all GitStatus details from database
	public List<GitStatusDto> getAllGitStatuses() {
		logger.debug("findAll method called to fetch all values from gitStatusMaster Table");
		List<GitStatusMaster> gitStatuses = (List<GitStatusMaster>) gitStatusRepository.findAll();
		return GitStatusMapperUtil.mapToGitStatusDtoList(gitStatuses);
	}

	// Function to save GitStatus details to database
	public GitStatusDto saveGitStatus(GitStatusDto gitStatusDto) {

		int count = 0;
		boolean isEdit = false;

		if (gitStatusDto.getId() != null && gitStatusDto.getId() != 0) {

			logger.debug("existsByGitStatusNameInEdit method called");
			count = gitStatusRepository.existsByGitStatusNameInEdit(gitStatusDto.getGitStatusName(),
					gitStatusDto.getId());
			logger.debug("existsByGitStatusNameInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherGitStatusNameInAdd method called");
			count = gitStatusRepository.existsByOtherGitStatusNameInAdd(gitStatusDto.getGitStatusName());
			logger.debug("existsByOtherGitStatusNameInAdd method returns with Count ", count);
		}

		// Handling exception for duplication of gitStatusName field and sending a
		// valid
		// error response JSON to UI layer
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "gitStatusName";
			
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, gitStatusDto.getGitStatusName());
		}

		GitStatusMaster gitStatus = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular gitStatus");
			gitStatus = gitStatusRepository.findById(gitStatusDto.getId()).get();
			logger.debug("findById method returns with entity values ", gitStatus);
		}

		logger.debug("mapToGitStatusEntity method called for convert Dto to entity for gitStatusDto");
		gitStatus = GitStatusMapperUtil.mapToGitStatusEntity(gitStatusDto, gitStatus);
		logger.debug("mapToGitStatusEntity method return entity gitStatus", gitStatus);

		logger.debug("save method called for save values of gitStatus to DB");
		// Code segment to save gitStatus if duplicates are not found
		gitStatus = gitStatusRepository.save(gitStatus);
		logger.debug("save method return entity gitStatus", gitStatus);

		logger.debug("mapToGitStatusDto method called for convert entity to Dto for gitStatus");
		gitStatusDto = GitStatusMapperUtil.mapToGitStatusDto(gitStatus);
		logger.debug("mapToGitStatusDto method return Dto gitStatusDto", gitStatusDto);

		return gitStatusDto;
	}

	/**
	 * Method to throw error in case of validation errors
	 * 
	 * @param errorfield
	 * @param gitStatusName
	 */
	private void throwError(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(CommonConstants.VALIDATION_FAILED, errorDtoList,
				detailsSimplified);
		throw new ServiceException(Constants.DUPLICATE_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}

}
