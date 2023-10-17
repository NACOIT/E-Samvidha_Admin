package gov.naco.soch.admin.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.DivisionDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.entity.Division;
import gov.naco.soch.entity.DivisionAdminDivisionMapping;
import gov.naco.soch.entity.FacilityType;
import gov.naco.soch.entity.FacilityTypeDivisionMapping;
import gov.naco.soch.entity.Role;
import gov.naco.soch.enums.DivisionEnum;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.DivisionMapperUtil;
import gov.naco.soch.projection.DivisionProjection;
import gov.naco.soch.repository.DivisionAdminDivisionMappingRepository;
import gov.naco.soch.repository.DivisionFacilitytypeMappingRepository;
import gov.naco.soch.repository.DivisionRepository;
import gov.naco.soch.repository.FacilityTypeRepository;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.util.UserUtils;

/**
 * Service class that handle division related methods
 *
 */
@Transactional
@Service
public class DivisionService {

	@Autowired
	private UserService userService;

	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private DivisionAdminDivisionMappingRepository divisionAdminDivisionMappingRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DivisionFacilitytypeMappingRepository divisionFacilityTypeRepository;

	@Autowired
	private FacilityTypeRepository facilityTypeRepo;

	private static final Logger logger = LoggerFactory.getLogger(DivisionService.class);

	/**
	 * Method to fetch all divisions
	 */
	public List<DivisionDto> getAllDivision() {
		// listing all values from division table
		logger.debug("In getAllDivision() of DivisionService");
		List<DivisionProjection> divisionList = divisionRepository.findDivisionList();
		List<DivisionDto> divisionDtoList = DivisionMapperUtil.mapProjectionToDivisionDtoList(divisionList);
		return divisionDtoList;
	}

	public DivisionDto getDivisionById(Long divisionId) {
		Division division = divisionRepository.findByIdAndIsDelete(divisionId, Boolean.FALSE);
		DivisionDto divisionDto = new DivisionDto();
		if (division != null) {
			divisionDto = DivisionMapperUtil.mapDivisionToDivisionDto(division);
		}
		return divisionDto;
	}
	
	public Integer getDivisionByFacTypeId(Long facilityTypeId) {
		Integer division = divisionRepository.getDivisionByFacTypeId(facilityTypeId);
//		DivisionDto divisionDto = new DivisionDto();
//		if (division != null) {
//			divisionDto = DivisionMapperUtil.mapDivisionToDivisionDto(division);
//		}
		return division;
	}

	/**
	 * Optimized division List return only id and name condition is_delete=false and
	 * is_active=true
	 * 
	 * @return
	 */
	public List<MasterDto> getAllBasicDivisionList() {
		List<Object[]> divisionList = divisionRepository.findBasicDivisionList();
		List<MasterDto> divisionDtoList = DivisionMapperUtil.mapToBasicDivisionDtoList(divisionList);
		return divisionDtoList;
	}

	/**
	 * Method to delete a specific division based on its ID. Note: This is only a
	 * logical deletion
	 */
	public Boolean deleteDivision(Long divisionId) {
		logger.debug("In deleteDivision() of DivisionService");
		int count = divisionRepository.findDeleteUser(divisionId);
		if (count == 0 && divisionId > 10) {
			Division division = divisionRepository.findById(divisionId).get();
			division.setIsDelete(true);
			division.getFacilityTypeDivisionMappings().forEach(x -> {
				x.setIsDelete(true);
			});

			divisionRepository.save(division);
			if (division.getDivisionAdminDivisionMappings() != null) {
				Set<DivisionAdminDivisionMapping> divisionAdminDivisionMappings = division
						.getDivisionAdminDivisionMappings();
				divisionAdminDivisionMappings.forEach(element -> {
					if (element != null) {
						element.setIsDelete(Boolean.TRUE);
					}
				});
				divisionAdminDivisionMappingRepository.saveAll(divisionAdminDivisionMappings);
			}
			return Boolean.TRUE;
		} else {

			throw new ServiceException("Division already in use!", null, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method to Add/Edit a division
	 */
	public DivisionDto saveDivision(DivisionDto divisionDto) {
		logger.debug("In saveDivision() of DivisionService");
		int count = 0;
		boolean isEdit = false;
		Division division = new Division();

		if (divisionDto.getId() != null && divisionDto.getId() != 0) {
			count = divisionRepository.existsByNameInEdit(divisionDto.getName(), divisionDto.getId());
			isEdit = true;
		}

		else {
			count = divisionRepository.existsByOtherNameInEdit(divisionDto.getName());

		}

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "name";
			throwError(errorfield, divisionDto.getName());
		}

		if (isEdit) {
			division = divisionRepository.findById(divisionDto.getId()).get();
		}
		divisionDto.setIsDelete(false);
		division = DivisionMapperUtil.mapToDivision(divisionDto, division);

		Set<FacilityTypeDivisionMapping> facilityTypeDivisionMappingList = new HashSet<>();
		for (Long facilityTypeDivisionMappingId : divisionDto.getFacilityTypeIds()) {
			FacilityTypeDivisionMapping facilityTypeDivisionMapping = new FacilityTypeDivisionMapping();
			FacilityType facilityType = facilityTypeRepo.findById(facilityTypeDivisionMappingId).get();
			facilityTypeDivisionMapping.setFacilityType(facilityType);
			facilityTypeDivisionMapping.setIsDelete(false);
			facilityTypeDivisionMapping.setIsActive(true);
			facilityTypeDivisionMapping.setDivision(division);
			facilityTypeDivisionMappingList.add(facilityTypeDivisionMapping);

		}
		if (isEdit) {
			divisionFacilityTypeRepository.deleteInBatch(division.getFacilityTypeDivisionMappings());
		}
		division.setFacilityTypeDivisionMappings(facilityTypeDivisionMappingList);
		divisionRepository.save(division);
		// auto generate DIVISION code after saving a division based on its primary key
		// https://www.java67.com/2014/10/how-to-pad-numbers-with-leading-zeroes-in-Java-example.html
		DecimalFormat df = new DecimalFormat("0000");
		String divisionCode = "DIV" + df.format(division.getId());
		division.setCode(divisionCode);
		divisionRepository.save(division);
		divisionDto.setCode(division.getCode());
		divisionRepository.save(division);

		divisionDto.setId(division.getId());
		UserMasterDto userDto = new UserMasterDto();
		if (divisionDto.getHeadUsername() != null && divisionDto.getHeadUsername() != "") {
			userDto = saveDivisionAdmin(divisionDto);
			divisionDto.setHeadId(userDto.getId());
		}

		if (divisionDto.getId() != null && userDto.getId() != null) {
			Optional<DivisionAdminDivisionMapping> DivisionAdminDivisionMapping = divisionAdminDivisionMappingRepository
					.findExistingDivisionAdminDivisionMapping(divisionDto.getId(), userDto.getId());
			DivisionAdminDivisionMapping divisionAdminDivision = null;
			if (DivisionAdminDivisionMapping.isPresent()) {
				divisionAdminDivision = DivisionAdminDivisionMapping.get();
				divisionAdminDivision.setIsActive(divisionDto.getIsActive());
			} else {
				divisionAdminDivision = DivisionMapperUtil.mapToDivisionAdminDivisionMapping(divisionDto, userDto);
			}
			divisionAdminDivisionMappingRepository.save(divisionAdminDivision);
		}
		return divisionDto;

	}

	private UserMasterDto saveDivisionAdmin(DivisionDto divisionDto) {
		UserMasterDto userDto = new UserMasterDto();

		userDto.setId(divisionDto.getHeadId());
		userDto.setFirstname(divisionDto.getHeadDdgName());
		userDto.setMobileNumber(divisionDto.getHeadDdgMobileNo());
		userDto.setEmail(divisionDto.getHeadDdgEmail());
		userDto.setUserName(divisionDto.getHeadUsername());
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()
				&& currentUser.getDivisionId() == DivisionEnum.NACO.getDivision()) {
			userDto.setFacilityId(currentUser.getFacilityId()); // NACO Facility
		} else {
			throw new ServiceException("Permission only granted for NACO level users ", null, HttpStatus.FORBIDDEN);
		}
		userDto.setFacilityTypeId(FacilityTypeEnum.NACO.getFacilityType());
		userDto.setDivisionId(DivisionEnum.NACO.getDivision());
		userDto.setDesignationId(5l); // Division_Admin Designation

		List<RoleDto> roleList = new ArrayList<>();
		RoleDto roleDto = new RoleDto();
		Optional<Role> roleOpt = roleRepository.findById(RoleEnum.DIVISION_ADMIN.getRole());
		if (roleOpt.isPresent()) {
			roleDto.setId(roleOpt.get().getId());
			roleDto.setName(roleOpt.get().getName());
			roleList.add(roleDto);
		}
		userDto.setRoleDto(roleList);
		if (divisionDto.getIsActive()) {
			userDto.setIsActive(true);
			userDto.setStatus(1l);
		} else {
			userDto.setIsActive(false);
			userDto.setStatus(2l);
		}
		userDto.setIsTrained(2l);
		userDto = userService.saveUser(FacilityTypeEnum.NACO.getFacilityType(),userDto);

		return userDto;
	}

	/**
	 * Division advance search with criteria: facilityType, name, code
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<DivisionDto> divisonAdvanceSearch(Map<String, String> searchValue) {
		// listing all values from division table
		logger.debug("In divisonAdvanceSearch() of DivisionService");
		String searchQuery = DivisionMapperUtil.divisionAdvanceSearchQueryCreator(searchValue);
		List<Object[]> divisionList = divisionRepository.divisionAdvanceSearch(searchQuery, searchValue);
		List<DivisionDto> divisionDtoList = DivisionMapperUtil.mapDivisionObjectToDivisionDtoList(divisionList);
		return divisionDtoList;
	}

	/**
	 * Method to throw error in case of validation errors
	 * 
	 * @param errorfield
	 * @param errorFieldValue
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
