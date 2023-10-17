package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.DesignationDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.entity.Designation;
import gov.naco.soch.entity.DesignationFacilityTypeMapping;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.DesignationMapperUtil;
import gov.naco.soch.repository.DesignationFacilityTypeMappingRepository;
import gov.naco.soch.repository.DesignationRepository;

//service class interact data with database
@Transactional
@Service
public class DesignationService {

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private DesignationFacilityTypeMappingRepository designationFacilityTypeRepository;

	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(DesignationService.class);

	private static final Long SUPER_ADMIN = 1l;

	// function to select all designation details from database
	public List<DesignationDto> getAllDesignations(boolean onlyActive) {

		logger.debug("findByIsDelete method called to fetch all values from Designation Table");

		List<Object[]> designations = null;
		if (onlyActive) {
			designations = designationRepository.findByIsActive(SUPER_ADMIN); // designation list except super admin
		} else {
			designations = designationRepository.findByIsDeleteOrderByIdDesc();
		}

		logger.debug("mapObjectListToDesignationDtoList method called with parameters->{}", designations);
		return DesignationMapperUtil.mapObjectListToDesignationDtoList(designations);
	}

	// function to save designation details to database
	public DesignationDto saveDesignation(DesignationDto designationDto) {

		int count = 0;
		boolean isEdit = false;
		designationDto.setTitle(designationDto.getTitle().trim());
		if (designationDto.getId() != null && designationDto.getId() != 0) {

			logger.debug("existsByTitleInEdit method called");
			count = designationRepository.existsByTitleInEdit(designationDto.getTitle(), designationDto.getId());
			logger.debug("existsByTitleInEdit method returns with Count ", count);
			isEdit = true;

		} else {
			logger.debug("existsByOtherTitleInAdd method called");
			count = designationRepository.existsByOtherTitleInAdd(designationDto.getTitle());
			logger.debug("existsByOtherTitleInAdd method returns with Count ", count);
		}

		// Handling exception for duplication of Title field and sending a
		// valid
		// error response JSON to UI layer
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";

			logger.debug("call throwError if duplicate found");
			throwError(errorfield, designationDto.getTitle());
		}

		Designation designation = null;

		if (isEdit) {
			logger.debug("findById method called for find values of particular designation");
			designation = designationRepository.findById(designationDto.getId()).get();
			logger.debug("findById method returns with entity values ", designation);
			if (!designation.getDesignationFacilityTypeMappings().isEmpty()) {
				designationFacilityTypeRepository.deleteInBatch(designation.getDesignationFacilityTypeMappings());
			}
		}

		logger.debug("mapToDesignation method called for convert Dto to entity for designationDto");
		designation = DesignationMapperUtil.mapToDesignation(designationDto, designation);
		logger.debug("mapToDesignation method return entity designation", designation);

		Set<DesignationFacilityTypeMapping> designationFacilityTypeList = DesignationMapperUtil
				.mapToDesignationFacilityTypeList(designationDto, designation);
		designation.setDesignationFacilityTypeMappings(designationFacilityTypeList);

		logger.debug("save method called for save values of designation to DB");
		// Code segment to save designation if duplicates are not found
		designation = designationRepository.save(designation);
		logger.debug("save method return entity designation", designation);

		designationDto.setId(designation.getId());
		// Code for notification
		if (!isEdit)
			designationDto.setId(null);
		return designationDto;
	}

	// function to delete designation details in database
	public Boolean deleteDesignation(Long id) {

		int count = designationRepository.findDeleteUser(id);
		if (count == 0) {
			Designation designation = new Designation();
			DesignationDto designationDto = new DesignationDto();

			logger.debug("findById method called for find values of particular id");
			designation = designationRepository.findById(id).get();
			logger.debug("findById method returns with entity values ", designation);

			// Setting designation as deleted
			designation.setIsDelete(true);
			if (!designation.getDesignationFacilityTypeMappings().isEmpty()) {
				designation.getDesignationFacilityTypeMappings().forEach(action -> {
					action.setIsDelete(true);
				});
			}

			logger.debug("save method called for save values of designation to DB");
			designation = designationRepository.save(designation);
			logger.debug("save method return entity designation", designation);

			logger.debug("mapToDesignationDto method called for convert entity to Dto for designation");
			designationDto = DesignationMapperUtil.mapToDesignationDto(designation);
			logger.debug("mapToDesignationDto method return Dto designationDto", designationDto);

			return Boolean.TRUE;
		}

		else {
			throw new ServiceException("Designation already in use!", null, HttpStatus.BAD_REQUEST);
		}
	}

	public List<DesignationDto> getDesignationsByFacilityType(Long facilityType) {
		List<Object[]> designationFacilityTypeMapListObj = new ArrayList<>();
		designationFacilityTypeMapListObj = designationFacilityTypeRepository
				.findDesignationByFacilityType(facilityType);
		List<DesignationDto> designationList = new ArrayList<>();
		designationList = DesignationMapperUtil.mapObjToDesignationDtoList(designationFacilityTypeMapListObj);
		designationList = designationList.stream().filter(d -> d.getId() != 1l).collect(Collectors.toList());
		Collections.sort(designationList);
		return designationList;
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
