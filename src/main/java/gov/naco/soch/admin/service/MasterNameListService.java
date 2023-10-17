
package gov.naco.soch.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.controller.MasterNameListController;
import gov.naco.soch.dto.MasterNameListDto;
import gov.naco.soch.entity.MasterNameList;
import gov.naco.soch.mapper.MasterNameListMapperUtil;
import gov.naco.soch.repository.MasterBeneficiaryCategoryRepository;
import gov.naco.soch.repository.MasterEducationLevelRepository;
//import gov.naco.soch.repository.MasterEmploymentStatusRepository;
import gov.naco.soch.repository.MasterMaritalStatusRepository;
import gov.naco.soch.repository.MasterNameListRepository;
import gov.naco.soch.repository.MasterOccupationRepository;

/**
 * @date 2020-Jan-03 1:54:11 PM
 */

// service class interact data with database
@Transactional
@Service
public class MasterNameListService {

	@Autowired
	private MasterNameListRepository masterNameListRepository;



	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(MasterNameListController.class);

	// function to select all master_name,master_description details from the table
	// master_name_list
	public List<MasterNameListDto> getAllMasterNames() {

		logger.debug("findAll method called to fetch all values from MasterNameList Table");
		List<MasterNameList> masterNameList = masterNameListRepository.findAll();

		logger.debug("mapToMasterNameList method called with parameter{}", masterNameList);
		return MasterNameListMapperUtil.mapToMasterNameList(masterNameList);
	}

	/*
	 * 
	 * 
	 * //Method to throw error private void throwError(String errorfield, String
	 * errorFieldValue) { List<ErrorDto> errorDtoList = new ArrayList<>();
	 * List<String> detailsSimplified = new ArrayList<String>(); ErrorDto errorDto =
	 * new ErrorDto(); errorDto.setField(errorfield);
	 * errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue +
	 * "'"); errorDtoList.add(errorDto);
	 * detailsSimplified.add(errorDto.getDescription()); ErrorResponse errorResponse
	 * = new ErrorResponse(errorDtoList.toString(), errorDtoList,
	 * detailsSimplified); throw new ServiceException(Constants.DUPLICATE_FOUND +
	 * " '" + errorFieldValue + "' ", errorResponse, HttpStatus.BAD_REQUEST); }
	 * 
	 */

}
