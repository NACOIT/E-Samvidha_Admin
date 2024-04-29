package gov.naco.soch.admin.service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.File;


import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileSystemUtils;
// import org.apache.tomcat.jni.File;
import org.ehcache.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import gov.naco.soch.admin.util.NotificationUtil;
import gov.naco.soch.constant.FileUploadConstants;
import gov.naco.soch.dto.ArtPlusCoeMappingDto;
import gov.naco.soch.dto.DistrictDto;
import gov.naco.soch.dto.DistrictFacilityDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.FacilityDto;
import gov.naco.soch.dto.FacilityListByDistrictAndFacilityTypeDTO;
import gov.naco.soch.dto.FacilityRequestDto;
import gov.naco.soch.dto.FacilityTypeDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.NacoBudgetAllocationDto;
import gov.naco.soch.dto.NgoAcceptRejectDto;
import gov.naco.soch.dto.NgoDocumentsDto;
import gov.naco.soch.dto.ProductDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.dto.SecondaryTypologyDto;
import gov.naco.soch.dto.TypologyDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.dto.NgoMemberDto;
import gov.naco.soch.dto.PrisonQuestionResultDto;
import gov.naco.soch.entity.Address;
import gov.naco.soch.entity.Contract;
import gov.naco.soch.entity.ContractProduct;
import gov.naco.soch.entity.District;
import gov.naco.soch.entity.Division;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.FacilityLinkFacilityMapping;
import gov.naco.soch.entity.FacilityType;
import gov.naco.soch.entity.GbDetailsEntity;
import gov.naco.soch.entity.Machine;
import gov.naco.soch.entity.MappingLabFacility;
import gov.naco.soch.entity.NacoBudgetAllocationEntity;
import gov.naco.soch.entity.NgoAcceptRejectEntity;
import gov.naco.soch.entity.NgoBlackListEntity;
import gov.naco.soch.entity.NgoDocumentsEntity;
import gov.naco.soch.entity.NgoMember;
import gov.naco.soch.entity.Pincode;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.SatelliteOstParentOstMapping;
import gov.naco.soch.entity.State;
import gov.naco.soch.entity.Subdistrict;
import gov.naco.soch.entity.Town;
import gov.naco.soch.entity.TransporterSacsMapping;
import gov.naco.soch.entity.TypologyFacilityMapping;
import gov.naco.soch.entity.TypologyMaster;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.entity.UserRoleMapping;
import gov.naco.soch.enums.AcceptRejectEnum;
import gov.naco.soch.enums.AccessCodeEnum;
import gov.naco.soch.enums.DivisionEnum;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.enums.NotificationEventIdEnum;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.AddressMapperUtil;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.FacilityMapperUtil;
import gov.naco.soch.mapper.FacilityTypeMapperUtil;
import gov.naco.soch.mapper.NGOMapperUtil;
import gov.naco.soch.mapper.RoleMapperUtil;
import gov.naco.soch.mapper.TypologyMapperUtil;
import gov.naco.soch.projection.FacilityDetailedProjection;
import gov.naco.soch.projection.FacilityDetailsProjectionForMobile;
import gov.naco.soch.projection.FacilityListColumnProjection;
import gov.naco.soch.projection.FacilityListProjection;
import gov.naco.soch.projection.FacilityProjection;
import gov.naco.soch.projection.FacilityTypeProjection;
import gov.naco.soch.projection.NgoMemberListProjection;
import gov.naco.soch.repository.AddressRepository;
import gov.naco.soch.repository.ContractProductDetailRepository;
import gov.naco.soch.repository.ContractRepository;
import gov.naco.soch.repository.DistrictRepository;
import gov.naco.soch.repository.DivisionFacilitytypeMappingRepository;
import gov.naco.soch.repository.DivisionRepository;
import gov.naco.soch.repository.FacilityLinkFacilityMappingRepository;
import gov.naco.soch.repository.FacilityLinkedFacilityBeneficiaryRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.FacilityTypeRepository;
import gov.naco.soch.repository.MachineRepository;
import gov.naco.soch.repository.MappingLabFacilityRepository;
import gov.naco.soch.repository.MasterHrgSecondaryRepository;
import gov.naco.soch.repository.NacoBudgetAllocationRepository;
import gov.naco.soch.repository.NgoAcceptRejectRepository;
import gov.naco.soch.repository.NgoBlackListRepository;
import gov.naco.soch.repository.NgoDocumentRepository;
import gov.naco.soch.repository.NgoGbRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.PincodeRepository;
import gov.naco.soch.repository.NgoMemberRepository;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.repository.SatelliteOstParentOstMappingRepository;
import gov.naco.soch.repository.StateRepository;
import gov.naco.soch.repository.SubdistrictRepository;
import gov.naco.soch.repository.TownRespository;
import gov.naco.soch.repository.TransporterSacsMappingRepository;
import gov.naco.soch.repository.TypologyFacilityMappingRepository;
import gov.naco.soch.repository.TypologyRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.repository.UserRoleMappingRepository;
import gov.naco.soch.util.CommonConstants;
import gov.naco.soch.util.UserUtils;


//service class interact data with database

@Transactional
@Service
public class FacilityService {

	private static final Logger logger = LoggerFactory.getLogger(FacilityService.class);
	private static final Long IDU = 4l;

	@Value("${exportRecordsLimit}")
	private Integer exportRecordsLimit;

	@Autowired
	FacilityRepository facilityRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	DistrictRepository districtRepository;

	@Autowired
	private MappingLabFacilityRepository mappingLabFacilityRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DivisionFacilitytypeMappingRepository divisionFacilityTypeRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private TypologyRepository typologyRepository;

	@Autowired
	private TypologyFacilityMappingRepository typologyFacilityMappingRepository;

	@Autowired
	private TownRespository townRespository;

	@Autowired
	private SubdistrictRepository subdistrictRepository;

	@Autowired
	private PincodeRepository pincodeRepository;
	
	@Autowired 
	private NgoMemberRepository ngoMemberRepository;
	
	@Autowired
	private NgoDocumentRepository ngoDocumentRepository;
	
	@Autowired
	private NgoAcceptRejectRepository ngoAcceptRejectRepository;
	
	@Autowired
	private NgoBlackListRepository ngoBlackListRepository;
	
	@Autowired
	private NgoGbRepository ngoGbRepository;
	
	@Autowired
	private NacoBudgetAllocationRepository nacoBudgetAllocationRepository;

	@Autowired
	private MachineRepository machineRepository;

	@Autowired
	private UserMasterRepository userRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ContractProductDetailRepository contractProductDetailRepository;

	@Autowired
	private FacilityTypeRepository facilityTypeRepository;

	@Autowired
	private FacilityLinkedFacilityBeneficiaryRepository facilityLinkedFacilityBeneficiaryRepository;

	@Autowired
	private CommonAdminService commonAdminService;

	@Autowired
	private NotificationEventRepository notificationEventRepository;

	@Autowired
	private SatelliteOstParentOstMappingRepository satelliteOstParentOstMappingRepository;

	@Autowired
	private NotificationUtil notificationUtil;

	@Autowired
	private Environment env;

	@Autowired
	private TransporterSacsMappingRepository transporterSacsMappingRepository;

	@Autowired
	private MasterHrgSecondaryRepository masterHrgSecondaryRepository;

	@Autowired
	private FacilityLinkFacilityMappingRepository facilityLinkFacilityMappingRepository;
	
	private static final HashMap<String, Object> placeholderMap = new HashMap<>();

	private static final String REQUIRED_ERROR = "SACS is required to create laboratory";

	private static final String PERMISSION_ERROR = "You are not permitted to create a laboratory";
	
//	private static final String uploadPath = "http://localhost:4200\\assets\\documents";
	private static final String uploadPath = "\\assets\\documents";
	private String fileStorageLocation = null;
	private Path fileLocation = null;
	private String fileName;
	
	
	// method to insert facility details into facility table,address details into
	// address table and user-facility mapping details into user-facility mapping
	// table
	public void addFacility(FacilityDto facilityDto) {

		facilityDto.setIsActive(facilityDto.getIsActive());
		facilityDto.setIsDelete(false);
		int count = 0;

		// To check whether the facility name is already exist in table
		boolean isExistsName = facilityRepository.existsByNameIgnoreCase(facilityDto.getName());
		count = facilityRepository.existsByOtherNameInAdd(facilityDto.getName(), facilityDto.getFacilityTypeId());

		if (count != 0) {
			String errorfield = "name";
			facilityDto.setName(facilityDto.getName());
			throwError(errorfield, facilityDto.getName());
		}
		Pincode pincode = null;
		if (facilityDto.getPincode() != null && facilityDto.getPincode() != "") {
			Optional<Pincode> pincodeOpt = pincodeRepository.findByPincode(facilityDto.getPincode());
			if (!pincodeOpt.isPresent()) {
				pincode = new Pincode();
				pincode.setPincode(facilityDto.getPincode());
				pincode.setIsActive(true);
				pincode.setIsDelete(false);
				pincode = pincodeRepository.save(pincode);
			} else {
				pincode = pincodeOpt.get();
			}
		}
		Optional<State> state = stateRepository.findById(facilityDto.getStateId());
		Optional<District> district = districtRepository.findById(facilityDto.getDistrictId());
		Facility facility = FacilityMapperUtil.mapToFacility(facilityDto);
		Address address = AddressMapperUtil.mapToAddressFacility(facilityDto, state.get(), district.get(), pincode);
		facility.setAddress(address);

		// Set<Facility> facilities = new HashSet<Facility>();
		// address.setFacilities(facilities);
		// address.getFacilities().add(facility);
		//
		// address = addressRepository.save(address);

		// auto generate facility code after saving a facility based on its primary key
		// https://www.java67.com/2014/10/how-to-pad-numbers-with-leading-zeroes-in-Java-example.html
		facilityRepository.save(facility);

		DecimalFormat df = new DecimalFormat("0000");
		String facilityCode = "FAC" + df.format(facility.getId());
		facility.setCode(facilityCode);
		facilityRepository.save(facility);
		facilityDto.setCode(facility.getCode());

		facilityDto.setId(facility.getId());

	}

	// Method to list all facilities
	public List<FacilityDto> getAllFacilities(List<Long> divisionIds, Long stateId) {

		List<Facility> facilityList = facilityRepository.findByIsDelete(Boolean.FALSE);

		if (!CollectionUtils.isEmpty(divisionIds)) {
			facilityList = facilityList.stream().filter(f -> divisionIds.contains(f.getDivision().getId()))
					.collect(Collectors.toList());
		}

		if (stateId != null) {
			Predicate<Facility> addressFilter = f -> f.getAddress() != null;
			Predicate<Facility> stateFilter = f -> f.getAddress().getState() != null;
			Predicate<Facility> stateIdFilter = f -> f.getAddress().getState().getId() == stateId;
			facilityList = facilityList.stream().filter(addressFilter.and(stateFilter).and(stateIdFilter))
					.collect(Collectors.toList());
		}

		List<FacilityDto> facilityDtoList = FacilityMapperUtil.mapToFacilityDtoList(facilityList);

		return facilityDtoList;
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

	public List<FacilityRequestDto> getFacilities(Long divisionId, Long facilityTypeId) {
		List<Object[]> facilities = facilityRepository.findByDivisionAndFacilityTypeToObject(divisionId,
				facilityTypeId);
		List<FacilityRequestDto> facilityRequests = FacilityMapperUtil.mapObjectToFacilityRequestDto(facilities);
		return facilityRequests;
	}

	// Added for bugfix 14-02-2020
	// Method to delete facility
	public String deleteFacilityById(Long facilityId) {
		int count = facilityRepository.findDeleteUser(facilityId);
		if (count == 0) {
			Boolean isDeleted = false;
			Facility facility = null;
			try {
				facility = facilityRepository.findById(facilityId).get();
				facility.setIsDelete(true);
				facilityRepository.save(facility);
				logger.debug("Facility Deleted for facilityId " + facilityId);
				isDeleted = true;
				return facility.getName();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		else {
			return null;
		}
		return null;
	}

	public List<FacilityDto> getFacilitiesMappedToLab(Long labId) {
		List<FacilityDto> facilityList = new ArrayList<>();
		List<Object[]> mapping = mappingLabFacilityRepository.findByLabId(labId);
		facilityList = FacilityMapperUtil.mapFacilitiesMappedtoLabToFacilityDtoList(mapping);
		return facilityList.stream().sorted(Comparator.comparing(FacilityDto::getName)).collect(Collectors.toList());
	}

	// Method to list all facilities (labs)
	public List<FacilityDto> getLabs() {
		List<Facility> facilityList = facilityRepository.findByIsDelete(false);
		facilityList = facilityList.stream().filter(f -> f.getDivision().getId() == 9L).collect(Collectors.toList());
		List<FacilityDto> facilityDtoList = FacilityMapperUtil.mapToFacilityDtoList(facilityList);
		return facilityDtoList;
	}

	public List<FacilityDto> mapLabToFacility(Long labId, List<FacilityDto> facilities) {

		Optional<Facility> facilityOpt = facilityRepository.findById(labId);
		int count = 0;
		count = mappingLabFacilityRepository.findCountByLabId(labId);
		if (facilityOpt.isPresent()) {
			Facility lab = facilityOpt.get();
			if (count == 0) {
				// List<MappingLabFacility> oldMappingList =
				// mappingLabFacilityRepository.findByLabId(labId);
				//
				// mappingLabFacilityRepository.deleteAll(oldMappingList);
				// mappingLabFacilityRepository.deleteByLabId(labId);

				List<Long> facilityIds = facilities.stream().map(f -> f.getId()).collect(Collectors.toList());

				List<Facility> facilitiesList = facilityRepository.findAllById(facilityIds);

				List<MappingLabFacility> newMappings = new ArrayList<>();

				List<FacilityDto> newFacilityMapping = new ArrayList<>();

				if (!CollectionUtils.isEmpty(facilitiesList)) {

					newMappings = facilitiesList.stream().map(fac -> {
						MappingLabFacility mapping = new MappingLabFacility();
						mapping.setLab(lab);
						mapping.setFacility(fac);
						mapping.setIsDelete(Boolean.FALSE);
						return mapping;
					}).collect(Collectors.toList());

					mappingLabFacilityRepository.saveAll(newMappings);

					newFacilityMapping = FacilityMapperUtil.mapToFacilityDtoList(facilitiesList);
					return newFacilityMapping;
				} else {
					return newFacilityMapping;
				}
			} else {
				List<Long> facilityIds = facilities.stream().map(f -> f.getId()).collect(Collectors.toList());
				List<MappingLabFacility> newMappings = new ArrayList<>();
				mappingLabFacilityRepository.updateIsDeleteByLabId(labId);
				if (facilityIds != null && !facilityIds.isEmpty()) {
					mappingLabFacilityRepository.updateIsDeleteByLabIdAndFacilityIds(labId, facilityIds);
					List<Facility> newFacilitiesList = facilityRepository.findNewFacilitiesForMapWithLab(labId,
							facilityIds);
					if (newFacilitiesList != null && !newFacilitiesList.isEmpty()) {
						newMappings = newFacilitiesList.stream().map(fac -> {
							MappingLabFacility mapping = new MappingLabFacility();
							mapping.setLab(lab);
							mapping.setFacility(fac);
							mapping.setIsDelete(Boolean.FALSE);
							return mapping;
						}).collect(Collectors.toList());

						mappingLabFacilityRepository.saveAll(newMappings);
					}
				}
				return facilities;
			}
		} else {
			throw new ServiceException("Invalid Lab Id", null, HttpStatus.BAD_REQUEST);
		}
	}

	public DistrictFacilityDto getLocalFacilites(Long facilityId, List<Long> divisionIds) {

		if (!CollectionUtils.isEmpty(divisionIds)) {
			DistrictFacilityDto districtFacilityDto = new DistrictFacilityDto();
			List<FacilityTypeDto> facilityTypeDtoList = new ArrayList<FacilityTypeDto>();

			FacilityProjection facilityProjection = facilityRepository.findStateId(facilityId);
			if (facilityProjection != null) {

				Long stateId = facilityProjection.getStateId();

				List<FacilityProjection> districtList = districtRepository.findByState(stateId);

				List<Long> districtIds = districtList.stream().map(d -> d.getId()).collect(Collectors.toList());

				List<DistrictDto> districtDtoList = districtList.stream().map(d -> {

					DistrictDto district = new DistrictDto();
					district.setId(d.getId());
					district.setName(d.getName());
					district.setStateid(d.getStateId());
					return district;
				}).sorted(Comparator.comparing(DistrictDto::getName)).collect(Collectors.toList());

				districtFacilityDto.setDistricts(districtDtoList);

				List<FacilityTypeProjection> facilityTypeDivisionMapping = divisionFacilityTypeRepository
						.findFacilityTypeMappingByDivisionIdList(divisionIds);

				logger.debug("mapToFacilityTypeDtoList method called with parameters->{}", facilityTypeDtoList);
				facilityTypeDtoList = FacilityTypeMapperUtil
						.mapFacilityTypeProjectionToFacilityTypeDtoList(facilityTypeDivisionMapping);
				districtFacilityDto.setFacilityTypes(facilityTypeDtoList);

				if (facilityProjection.getFacilityTypeId() != FacilityTypeEnum.SACS.getFacilityType()) {
					facilityId = facilityProjection.getSacsId();
				}

				if (facilityId != null) {
					List<FacilityDetailedProjection> facilityList = facilityRepository.findByDivisionIds(divisionIds,
							facilityId);
					List<FacilityDto> facilityDtoList = FacilityMapperUtil
							.mapFacilityDetailedProjectionToFacilityDtoList(facilityList);
					facilityDtoList = facilityDtoList.stream().filter(f -> districtIds.contains(f.getDistrictId()))
							.collect(Collectors.toList());
					districtFacilityDto.setFacilities(facilityDtoList);
				}
				return districtFacilityDto;
			} else {
				throw new ServiceException("Invalid Facility Id", null, HttpStatus.BAD_REQUEST);
			}
		} else {
			throw new ServiceException("Required division ids as query parameters", null, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public SacsFacilityDto addAnyFacilities(SacsFacilityDto sacsFacilityDto) {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		errorChecker(sacsFacilityDto);
		int count = 0;
		// To check whether the facility name is already exist in table
		count = facilityRepository.existsByOtherNameInAdd(sacsFacilityDto.getName(),
				sacsFacilityDto.getFacilityTypeId());
		if (count != 0) {
			String errorfield = "Facility name";
			throwError(errorfield, sacsFacilityDto.getName());
		} else {
			if (sacsFacilityDto.getFacilityNo() != null && sacsFacilityDto.getFacilityNo() != "") {
				count = 0;
				count = facilityRepository.existsByFacilityNumberInAdd(sacsFacilityDto.getFacilityNo(),sacsFacilityDto.getDistrictId(),sacsFacilityDto.getFacilityTypeId());
				if (count != 0) {
					String errorfield = "Facility Number";
					throwError(errorfield, sacsFacilityDto.getFacilityNo());
				}
			}
			if (sacsFacilityDto.getTypology() != null && sacsFacilityDto.getTypology().size() != 0
					&& sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_CENTER.getFacilityType()) {
				sacsFacilityDto = FacilityMapperUtil.mapSacsFacilityDtoBasedOnTypology(sacsFacilityDto);
			}

			Machine machine = null;
			System.out.println("sacsFacilityDto.getStateId()>>>>>>>>>>>>>"+sacsFacilityDto.getStateId());
			Optional<State> state = stateRepository.findById(sacsFacilityDto.getStateId());
			Optional<District> district = districtRepository.findById(sacsFacilityDto.getDistrictId());
			Town town = null;
			Subdistrict subdistrict = null;
			Pincode pincode = null;
			if (sacsFacilityDto.getTownId() != null) {
				Optional<Town> townOpt = townRespository.findById(sacsFacilityDto.getTownId());
				if (townOpt.isPresent()) {
					town = townOpt.get();
				}

			}
			if (sacsFacilityDto.getSubDistrictId() != null) {
				Optional<Subdistrict> subdistrictOpt = subdistrictRepository
						.findById(sacsFacilityDto.getSubDistrictId());
				if (subdistrictOpt.isPresent()) {
					subdistrict = subdistrictOpt.get();
				}

			}
			if (sacsFacilityDto.getPincode() != null && sacsFacilityDto.getPincode() != "") {
				Optional<Pincode> pincodeOpt = pincodeRepository.findByPincode(sacsFacilityDto.getPincode());
				if (!pincodeOpt.isPresent()) {
					pincode = new Pincode();
					pincode.setPincode(sacsFacilityDto.getPincode());
					pincode.setIsActive(true);
					pincode.setIsDelete(false);
					pincode = pincodeRepository.save(pincode);
				} else {
					pincode = pincodeOpt.get();
				}
			}
			if (sacsFacilityDto.getMachineId() != null) {
				Optional<Machine> machineOpt = machineRepository.findById(sacsFacilityDto.getMachineId());
				if (machineOpt.isPresent()) {
					machine = machineOpt.get();
				}
			}
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.NARI.getFacilityType()) {
				sacsFacilityDto.setSacsId(null);
			}
			Optional<Division> division = divisionRepository.findById(sacsFacilityDto.getDivisionId());
			Facility facility = FacilityMapperUtil.mapSacsFacilityDtoToFacility(sacsFacilityDto);
			Address address = AddressMapperUtil.mapSacsFacilityDtoToAddressFacility(sacsFacilityDto, state.get(),
					district.get(), town, subdistrict, pincode);
			facility.setAddress(address);
			facility.setDivision(division.get());
			facility.setMachine(machine);

			if (sacsFacilityDto.getParentFacilityId() != null
					|| currentUser.getFacilityTypeId() == FacilityTypeEnum.ICTC_FACILITY.getFacilityType()) {
				if (sacsFacilityDto.getParentFacilityId() == null) {
					sacsFacilityDto.setParentFacilityId(currentUser.getFacilityId());
				}
				Facility parentFacility = facilityRepository.findById(sacsFacilityDto.getParentFacilityId()).get();
				facility.setFacility(parentFacility);
				if ((sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.LAC_FACILITY.getFacilityType()
						|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.F_ICTC.getFacilityType())
						&& parentFacility != null) {
					facility.setSacsId(parentFacility.getSacsId());
				}
			}
			System.out.println("sacsFacilityDto.getFacilityTypeId()>>>>>>>>>>>>>"+sacsFacilityDto.getFacilityTypeId());
			facility = facilityRepository.save(facility);

			if (sacsFacilityDto.getFacilityTypeId().equals(FacilityTypeEnum.TRANSPORTER.getFacilityType())) {
				List<TransporterSacsMapping> transporterSacsMappings = new ArrayList<>();
				for (Long sacsId : sacsFacilityDto.getSacsIdsForTransporterMapping()) {
					TransporterSacsMapping transporterSacsMapping = new TransporterSacsMapping();
					transporterSacsMapping.setIsActive(true);
					transporterSacsMapping.setIsDelete(false);
					transporterSacsMapping.setMappingStatusFlag(true);
					transporterSacsMapping.setMappingDate(LocalDate.now());
					Facility sacs = new Facility();
					sacs.setId(sacsId);
					transporterSacsMapping.setSacs(sacs);
					Facility transporter = new Facility();
					transporter.setId(facility.getId());
					transporterSacsMapping.setTransporter(transporter);
					transporterSacsMappings.add(transporterSacsMapping);
				}
				transporterSacsMappingRepository.saveAll(transporterSacsMappings);
			}

			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.OST_FACILITY.getFacilityType()
					|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType()) {
				TypologyDto dto = new TypologyDto();
				dto.setId(IDU);
				sacsFacilityDto.setTypology(new ArrayList<>());
				sacsFacilityDto.getTypology().add(dto);
			}
			if (sacsFacilityDto.getTypology() != null && sacsFacilityDto.getTypology().size() != 0
					&& (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_CENTER.getFacilityType()
							|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.OST_FACILITY.getFacilityType()
							|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST
									.getFacilityType())) {
				Set<TypologyFacilityMapping> typologyFacilityMappingList = FacilityMapperUtil
						.maptToTypologyFacilityMapping(sacsFacilityDto.getTypology(), facility);
				typologyFacilityMappingRepository.saveAll(typologyFacilityMappingList);
			}

			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType()
					&& sacsFacilityDto.getParentOsts() != null && !sacsFacilityDto.getParentOsts().isEmpty()) {
				List<SatelliteOstParentOstMapping> satelliteOstParentOstMappings = FacilityMapperUtil
						.maptoSatelliteOstParent(sacsFacilityDto.getParentOsts(), facility);
				satelliteOstParentOstMappingRepository.saveAll(satelliteOstParentOstMappings);
			}

			/*
			 * While saving ICTC facility, Updating saved facility's id as
			 * parent_facility_id to selected F_ICTC (Child Facilities).
			 */
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.ICTC_FACILITY.getFacilityType()
					&& sacsFacilityDto.getChildFacilitiesList() != null
					&& !sacsFacilityDto.getChildFacilitiesList().isEmpty()) {
				List<Long> childFacilityIds = sacsFacilityDto.getChildFacilitiesList().stream().map(data -> {
					return data.getId();
				}).collect(Collectors.toList());
				if (childFacilityIds != null && !childFacilityIds.isEmpty()) {
					// Updating saved facility id as parent_facility_id for selected F_ictc (child
					// facilities)
					facilityRepository.updateParentFacilityIdOfChildFacilities(facility.getId(), childFacilityIds);
				}
			}

			/*
			 * Finding Facility Code
			 */
			if ((sacsFacilityDto.getCode() == null || sacsFacilityDto.getCode() == "")
					&& sacsFacilityDto.getFacilityTypeId() == 3l) {
				DecimalFormat df = new DecimalFormat("0000");
				String facilityCode = "SUP" + df.format(facility.getId());
				facility.setCode(facilityCode);
				facilityRepository.save(facility);
				sacsFacilityDto.setCode(facility.getCode());
			} else if ((sacsFacilityDto.getCode() == null || sacsFacilityDto.getCode() == "")
					&& FacilityTypeMapperUtil.facilityTypeLabChecker(sacsFacilityDto.getFacilityTypeId())) {
				DecimalFormat df = new DecimalFormat("0000");
				String facilityCode = "LAB" + df.format(facility.getId());
				facility.setCode(facilityCode);
				facility.setIsLab(Boolean.TRUE);
				facilityRepository.save(facility);
				sacsFacilityDto.setCode(facility.getCode());
			} else if ((sacsFacilityDto.getCode() == null || sacsFacilityDto.getCode() == "")
					&& sacsFacilityDto.getFacilityTypeId() == 6l) {
				DecimalFormat df = new DecimalFormat("0000");
				String facilityCode = "RWH" + df.format(facility.getId());
				facility.setCode(facilityCode);
				facilityRepository.save(facility);
				sacsFacilityDto.setCode(facility.getCode());
			} else if ((sacsFacilityDto.getCode() == null || sacsFacilityDto.getCode() == "")
					&& sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.LAC_FACILITY.getFacilityType()) {
				DecimalFormat df = new DecimalFormat("0000");
				String facilityCode = "LAC" + df.format(facility.getId());
				facility.setCode(facilityCode);
				facilityRepository.save(facility);
				sacsFacilityDto.setCode(facility.getCode());
			} else if (sacsFacilityDto.getCode() == null || sacsFacilityDto.getCode() == "") {
				DecimalFormat df = new DecimalFormat("0000");
				String facilityCode = "FAC" + df.format(facility.getId());
				facility.setCode(facilityCode);
				facilityRepository.save(facility);
				sacsFacilityDto.setCode(facility.getCode());
			}

			sacsFacilityDto.setId(facility.getId());
			if (facility.getId() != null) {
				Long parentFacilityId = facility.getId();
				Long parentFacilityTypeId = sacsFacilityDto.getFacilityTypeId();

				if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.ART_FACILITY.getFacilityType()) {

					List<FacilityLinkFacilityMapping> facilityLinkFacilityMappingsList = new ArrayList<FacilityLinkFacilityMapping>();

					if (!CollectionUtils.isEmpty(sacsFacilityDto.getArtPlusMapping())) {
						sacsFacilityDto.getArtPlusMapping().forEach(row -> {
							if(row!=null)
							{
							FacilityLinkFacilityMapping facilityLinkFacilityMapping = new FacilityLinkFacilityMapping();
							facilityLinkFacilityMapping.setLinkDate(LocalDate.now());
							
							Facility parentFacility = new Facility();
							parentFacility.setId(parentFacilityId);
							facilityLinkFacilityMapping.setParentFacilityId(parentFacility);

							FacilityType parentFacilityType = new FacilityType();
							parentFacilityType.setId(parentFacilityTypeId);
							facilityLinkFacilityMapping.setParentFacilityTypeId(parentFacilityType);

							if (row.getLinkFacilityId() != null) {
								Facility linkFacility = new Facility();
								linkFacility.setId(row.getLinkFacilityId());
								facilityLinkFacilityMapping.setLinkFacilityId(linkFacility);
							}

							if (row.getLinkFacilityTypeId() != null) {
								FacilityType linkFacilityType = new FacilityType();
								linkFacilityType.setId(row.getLinkFacilityTypeId());
								facilityLinkFacilityMapping.setLinkFacilityTypeId(linkFacilityType);
							}

							facilityLinkFacilityMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
							facilityLinkFacilityMapping.setIsActive(Boolean.TRUE);
							facilityLinkFacilityMapping.setIsDelete(Boolean.FALSE);
							facilityLinkFacilityMappingsList.add(facilityLinkFacilityMapping);
							}

						});
						
					}

					if (!CollectionUtils.isEmpty(sacsFacilityDto.getCoeMapping())) {
						sacsFacilityDto.getCoeMapping().forEach(row -> {
							if(row!=null)
							{
							FacilityLinkFacilityMapping facilityLinkFacilityMapping = new FacilityLinkFacilityMapping();
							facilityLinkFacilityMapping.setLinkDate(LocalDate.now());
							
							Facility parentFacility = new Facility();
							parentFacility.setId(parentFacilityId);
							facilityLinkFacilityMapping.setParentFacilityId(parentFacility);

							FacilityType parentFacilityType = new FacilityType();
							parentFacilityType.setId(parentFacilityTypeId);
							facilityLinkFacilityMapping.setParentFacilityTypeId(parentFacilityType);

							if (row.getLinkFacilityId() != null) {
								Facility linkFacility = new Facility();
								linkFacility.setId(row.getLinkFacilityId());
								facilityLinkFacilityMapping.setLinkFacilityId(linkFacility);
							}

							if (row.getLinkFacilityTypeId() != null) {
								FacilityType linkFacilityType = new FacilityType();
								linkFacilityType.setId(row.getLinkFacilityTypeId());
								facilityLinkFacilityMapping.setLinkFacilityTypeId(linkFacilityType);
							}
							facilityLinkFacilityMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
							facilityLinkFacilityMapping.setIsActive(Boolean.TRUE);
							facilityLinkFacilityMapping.setIsDelete(Boolean.FALSE);
							facilityLinkFacilityMappingsList.add(facilityLinkFacilityMapping);
							}
						});
					}

					if (!CollectionUtils.isEmpty(facilityLinkFacilityMappingsList)) {
						facilityLinkFacilityMappingRepository.saveAll(facilityLinkFacilityMappingsList);
					}
				}
			}

			sacsFacilityDto = saveUserForFacilityCreation(sacsFacilityDto, facility);
			sacsFacilityDto.setIsEdit(false);

		}

		return sacsFacilityDto;
	}

	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public SacsFacilityDto editAnyFacilities(SacsFacilityDto sacsFacilityDto) {
		errorChecker(sacsFacilityDto);
		int count = 0;
		// To check whether the facility name is already exist in table
		count = facilityRepository.existsByOtherNameInEdit(sacsFacilityDto.getName(), sacsFacilityDto.getId(),
				sacsFacilityDto.getFacilityTypeId());

		if (count != 0) {
			String errorfield = "Facility name";
			throwError(errorfield, sacsFacilityDto.getName());
		} else {
			if (sacsFacilityDto.getFacilityNo() != null && sacsFacilityDto.getFacilityNo() != "") {
				count = 0;
				count = facilityRepository.existsByFacilityNumberInEdit(sacsFacilityDto.getFacilityNo(),
						sacsFacilityDto.getId(),sacsFacilityDto.getDistrictId(),sacsFacilityDto.getFacilityTypeId());
				if (count != 0) {
					String errorfield = "Facility Number";
					throwError(errorfield, sacsFacilityDto.getFacilityNo());
				}
			}
			Optional<Facility> facilityOpt = facilityRepository.findById(sacsFacilityDto.getId());
			if (facilityOpt.get().getFacilityType().getId() != sacsFacilityDto.getFacilityTypeId()) {
				String errorfield = "Facility Type";
				Optional<FacilityType> facilityType = facilityTypeRepository
						.findById(sacsFacilityDto.getFacilityTypeId());
				String facilityTypeName = facilityType.get().getFacilityTypeName();
				throwErrorCannotChange(errorfield, facilityTypeName);
			}
			if (sacsFacilityDto.getTypology() != null && sacsFacilityDto.getTypology().size() != 0
					&& sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_CENTER.getFacilityType()) {
				List<TypologyFacilityMapping> facilityMappings = typologyFacilityMappingRepository
						.findAllByFacilityId(sacsFacilityDto.getId());
				if (facilityMappings != null && !(facilityMappings.isEmpty())) {
					Set<TypologyFacilityMapping> list = new HashSet<TypologyFacilityMapping>(facilityMappings);
					List<TypologyDto> typologyLists = FacilityMapperUtil.mapToTypology(list);
					sacsFacilityDto.setOldTypology(typologyLists);
				}

				sacsFacilityDto = FacilityMapperUtil.mapSacsFacilityDtoBasedOnTypology(sacsFacilityDto);
			}

			Optional<State> state = stateRepository.findById(sacsFacilityDto.getStateId());
			Optional<District> district = districtRepository.findById(sacsFacilityDto.getDistrictId());
			Machine machine = null;
			Town town = null;
			Subdistrict subdistrict = null;
			Pincode pincode = null;
			if (sacsFacilityDto.getTownId() != null) {
				Optional<Town> townOpt = townRespository.findById(sacsFacilityDto.getTownId());
				if (townOpt.isPresent()) {
					town = townOpt.get();
				}
			}
			if (sacsFacilityDto.getSubDistrictId() != null) {
				Optional<Subdistrict> subdistrictOpt = subdistrictRepository
						.findById(sacsFacilityDto.getSubDistrictId());
				if (subdistrictOpt.isPresent()) {
					subdistrict = subdistrictOpt.get();
				}
			}
			if (sacsFacilityDto.getPincode() != null && sacsFacilityDto.getPincode() != "") {
				Optional<Pincode> pincodeOpt = pincodeRepository.findByPincode(sacsFacilityDto.getPincode());
				if (!pincodeOpt.isPresent()) {
					pincode = new Pincode();
					pincode.setPincode(sacsFacilityDto.getPincode());
					pincode.setIsActive(true);
					pincode.setIsDelete(false);
					pincode = pincodeRepository.save(pincode);
				} else {
					pincode = pincodeOpt.get();
				}
			}
			if (sacsFacilityDto.getMachineId() != null) {
				Optional<Machine> machineOpt = machineRepository.findById(sacsFacilityDto.getMachineId());
				if (machineOpt.isPresent()) {
					machine = machineOpt.get();
				}
			}
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.NARI.getFacilityType()) {
				sacsFacilityDto.setSacsId(null);
			}
			Optional<Division> division = divisionRepository.findById(sacsFacilityDto.getDivisionId());
			Facility facility = FacilityMapperUtil.mapSacsFacilityDtoToFacilityInEdit(sacsFacilityDto,
					facilityOpt.get());
			Address address = new Address();
			if (facility.getAddress() != null) {
				address = facility.getAddress();
			}
			address = AddressMapperUtil.mapSacsFacilityDtoToAddressFacilityInEdit(sacsFacilityDto, state.get(),
					district.get(), town, subdistrict, pincode, address);
			facility.setAddress(address);
			facility.setDivision(division.get());
			facility.setMachine(machine);

			LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
			if (sacsFacilityDto.getParentFacilityId() != null
					|| currentUser.getFacilityTypeId() == FacilityTypeEnum.ICTC_FACILITY.getFacilityType()) {
				if (sacsFacilityDto.getParentFacilityId() == null) {
					sacsFacilityDto.setParentFacilityId(currentUser.getFacilityId());
				}
				Facility parentFacility = facilityRepository.findById(sacsFacilityDto.getParentFacilityId()).get();
				facility.setFacility(parentFacility);
				if ((sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.LAC_FACILITY.getFacilityType()
						|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.F_ICTC.getFacilityType())
						&& parentFacility != null) {
					facility.setSacsId(parentFacility.getSacsId());
				}
			}

			facility = facilityRepository.save(facility);

			if (facility.getFacilityType().getId().equals(FacilityTypeEnum.TRANSPORTER.getFacilityType())) {
				List<TransporterSacsMapping> transporterSacsMappings = transporterSacsMappingRepository
						.findByTransporterId(facility.getId());
				Map<Long, TransporterSacsMapping> transporterSacsMap = new HashMap<>();
				for (TransporterSacsMapping transporterSacsMapping : transporterSacsMappings) {
					transporterSacsMap.put(transporterSacsMapping.getSacs().getId(), transporterSacsMapping);
				}

				List<Long> sacsIds = transporterSacsMappings.stream().map(t -> t.getSacs().getId())
						.collect(Collectors.toList());
				List<TransporterSacsMapping> newTransporterSacsMappings = new ArrayList<>();
				for (Long sacsId : sacsFacilityDto.getSacsIdsForTransporterMapping()) {
					if (CollectionUtils.isEmpty(sacsIds) || !sacsIds.contains(sacsId)) {
						TransporterSacsMapping transporterSacsMapping = new TransporterSacsMapping();
						transporterSacsMapping.setIsActive(true);
						transporterSacsMapping.setIsDelete(false);
						transporterSacsMapping.setMappingDate(LocalDate.now());
						transporterSacsMapping.setMappingStatusFlag(true);
						Facility sacs = new Facility();
						sacs.setId(sacsId);
						transporterSacsMapping.setSacs(sacs);
						Facility transporter = new Facility();
						transporter.setId(facility.getId());
						transporterSacsMapping.setTransporter(transporter);
						newTransporterSacsMappings.add(transporterSacsMapping);
					}
				}
				sacsIds.removeAll(sacsFacilityDto.getSacsIdsForTransporterMapping());
				if (!CollectionUtils.isEmpty(sacsIds)) {
					for (Long sacsId : sacsIds) {
						TransporterSacsMapping transporterSacsMapping = transporterSacsMap.get(sacsId);
						transporterSacsMapping.setMappingStatusFlag(false);
						newTransporterSacsMappings.add(transporterSacsMapping);
					}

				}
				transporterSacsMappingRepository.saveAll(newTransporterSacsMappings);
			}

			int typologyCount = 0;
			typologyCount = typologyFacilityMappingRepository.isExistByFacilityId(facility.getId());
			if (typologyCount != 0) {
				typologyFacilityMappingRepository.deleteByFacilityId(facility.getId());
			}
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.OST_FACILITY.getFacilityType()
					|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType()) {
				TypologyDto dto = new TypologyDto();
				dto.setId(IDU);
				sacsFacilityDto.setTypology(new ArrayList<>());
				sacsFacilityDto.getTypology().add(dto);
			}
			if (sacsFacilityDto.getTypology() != null && sacsFacilityDto.getTypology().size() != 0
					&& (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_CENTER.getFacilityType()
							|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.OST_FACILITY.getFacilityType()
							|| sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST
									.getFacilityType())) {
				Set<TypologyFacilityMapping> typologyFacilityMappingList = FacilityMapperUtil
						.maptToTypologyFacilityMapping(sacsFacilityDto.getTypology(), facility);
				typologyFacilityMappingRepository.saveAll(typologyFacilityMappingList);
			}

			/*
			 * While Editing ICTC facility, Updating edited facility's id as
			 * parent_facility_id to selected F_ICTC (Child Facilities).
			 */
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.ICTC_FACILITY.getFacilityType()) {
				// Updating parent_facility_id as null for all F_ICTC facility having
				// parent_facility_id as editing facility
				facilityRepository.updatingParentIdAsNullByFacilityId(facility.getId(),
						FacilityTypeEnum.F_ICTC.getFacilityType());
				if (sacsFacilityDto.getChildFacilitiesList() != null
						&& !sacsFacilityDto.getChildFacilitiesList().isEmpty()) {
					List<Long> childFacilityIds = sacsFacilityDto.getChildFacilitiesList().stream().map(data -> {
						return data.getId();
					}).collect(Collectors.toList());
					if (childFacilityIds != null && !childFacilityIds.isEmpty()) {
						// Updating edited facility id as parent_facility_id for selected F_ictc (child
						// facilities)
						facilityRepository.updateParentFacilityIdOfChildFacilities(facility.getId(), childFacilityIds);
					}
				}
			}

			// Ost stellite and parent Ost mapping
			if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType()) {
				// satelliteOstParentOstMappingRepository.deleteBySatelliteOstId(sacsFacilityDto.getId());
				boolean[] exist = { false };
				List<SatelliteOstParentOstMapping> newParentMapping = null;
				List<SatelliteOstParentOstMapping> mappings = satelliteOstParentOstMappingRepository
						.findAllBySatelliteOstId(sacsFacilityDto.getId());
				List<MasterDto> oldParentOstList = new ArrayList<MasterDto>();
				List<SatelliteOstParentOstMapping> oldMappings = satelliteOstParentOstMappingRepository
						.findAllParentOstaBySataliteOstId(sacsFacilityDto.getId());
				if (oldMappings != null && !oldMappings.isEmpty()) {
					oldMappings.forEach(data -> {
						MasterDto master = new MasterDto();
						master.setName(data.getParentOst().getName());
						oldParentOstList.add(master);
					});
					sacsFacilityDto.setOldParentOsts(oldParentOstList);
				}
				if (mappings != null && !mappings.isEmpty()) {
					mappings.forEach(data -> {
						data.setMappingStatusFlag(false);
					});
				}
				if (sacsFacilityDto.getParentOsts() != null && !sacsFacilityDto.getParentOsts().isEmpty()) {
					if (mappings != null && !mappings.isEmpty()) {
						for (MasterDto parentOstDto : sacsFacilityDto.getParentOsts()) {
							exist[0] = false;
							mappings.forEach(data -> {
								if (data.getParentOst() != null
										&& data.getParentOst().getId() == parentOstDto.getId()) {
									data.setMappingStatusFlag(true);
									exist[0] = true;
								}
							});
							if (exist[0] == false) {
								newParentMapping = FacilityMapperUtil.maptoSatelliteOstParentForEdit(parentOstDto,
										facility, newParentMapping);
							}
						}
						if (newParentMapping != null && !newParentMapping.isEmpty()) {
							for (SatelliteOstParentOstMapping temp : newParentMapping) {
								mappings.add(temp);
							}
						}
					} else {
						mappings = FacilityMapperUtil.maptoSatelliteOstParent(sacsFacilityDto.getParentOsts(),
								facility);
					}
				}
				satelliteOstParentOstMappingRepository.saveAll(mappings);
			}

			if (facility.getId() != null) {

				Long parentFacilityId = facility.getId();
				Long parentFacilityTypeId = sacsFacilityDto.getFacilityTypeId();
                
				if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.ART_FACILITY.getFacilityType()) {

					List<FacilityLinkFacilityMapping> facilityLinkFacilityMappingsList = new ArrayList<FacilityLinkFacilityMapping>();

					if (!CollectionUtils.isEmpty(sacsFacilityDto.getArtPlusMapping())) {
						sacsFacilityDto.getArtPlusMapping().forEach(row -> {
							
							if(row!=null)
							{
							FacilityLinkFacilityMapping facilityLinkFacilityMapping = new FacilityLinkFacilityMapping();
							if (row.getId() != null) {
								facilityLinkFacilityMapping.setId(row.getId());
							}
							if (row.getLinkDate() == null) {
								facilityLinkFacilityMapping.setLinkDate(LocalDate.now());
							} else {
								facilityLinkFacilityMapping.setLinkDate(row.getLinkDate());
							}

							Facility parentFacility = new Facility();
							parentFacility.setId(parentFacilityId);
							facilityLinkFacilityMapping.setParentFacilityId(parentFacility);

							FacilityType parentFacilityType = new FacilityType();
							parentFacilityType.setId(parentFacilityTypeId);
							facilityLinkFacilityMapping.setParentFacilityTypeId(parentFacilityType);

							if (row.getLinkFacilityId() != null) {
								Facility linkFacility = new Facility();
								linkFacility.setId(row.getLinkFacilityId());
								facilityLinkFacilityMapping.setLinkFacilityId(linkFacility);
							}

							if (row.getLinkFacilityTypeId() != null) {
								FacilityType linkFacilityType = new FacilityType();
								linkFacilityType.setId(row.getLinkFacilityTypeId());
								facilityLinkFacilityMapping.setLinkFacilityTypeId(linkFacilityType);
							}

							facilityLinkFacilityMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
							if (row.getCurrentLinkStatus() == Boolean.FALSE) {
								facilityLinkFacilityMapping.setIsActive(Boolean.FALSE);
								facilityLinkFacilityMapping.setIsDelete(Boolean.TRUE);
							} else {
								facilityLinkFacilityMapping.setIsActive(Boolean.TRUE);
								facilityLinkFacilityMapping.setIsDelete(Boolean.FALSE);
							}

							facilityLinkFacilityMappingsList.add(facilityLinkFacilityMapping);
							}
						});
					}

					if (!CollectionUtils.isEmpty(sacsFacilityDto.getCoeMapping())) {
						sacsFacilityDto.getCoeMapping().forEach(row -> {
							
							if(row!=null)
							{
							FacilityLinkFacilityMapping facilityLinkFacilityMapping = new FacilityLinkFacilityMapping();
							if (row.getId() != null) {
								facilityLinkFacilityMapping.setId(row.getId());
							}
							if (row.getLinkDate() == null) {
								facilityLinkFacilityMapping.setLinkDate(LocalDate.now());
							} else {
								facilityLinkFacilityMapping.setLinkDate(row.getLinkDate());
							}
							Facility parentFacility = new Facility();
							parentFacility.setId(parentFacilityId);
							facilityLinkFacilityMapping.setParentFacilityId(parentFacility);

							FacilityType parentFacilityType = new FacilityType();
							parentFacilityType.setId(parentFacilityTypeId);
							facilityLinkFacilityMapping.setParentFacilityTypeId(parentFacilityType);

							if (row.getLinkFacilityId() != null) {
								Facility linkFacility = new Facility();
								linkFacility.setId(row.getLinkFacilityId());
								facilityLinkFacilityMapping.setLinkFacilityId(linkFacility);
							}

							if (row.getLinkFacilityTypeId() != null) {
								FacilityType linkFacilityType = new FacilityType();
								linkFacilityType.setId(row.getLinkFacilityTypeId());
								facilityLinkFacilityMapping.setLinkFacilityTypeId(linkFacilityType);
							}
							facilityLinkFacilityMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
							if (row.getCurrentLinkStatus() == Boolean.FALSE) {
								facilityLinkFacilityMapping.setIsActive(Boolean.FALSE);
								facilityLinkFacilityMapping.setIsDelete(Boolean.TRUE);
							} else {
								facilityLinkFacilityMapping.setIsActive(Boolean.TRUE);
								facilityLinkFacilityMapping.setIsDelete(Boolean.FALSE);
							}
							facilityLinkFacilityMappingsList.add(facilityLinkFacilityMapping);
							}
						});
					}

					if (!CollectionUtils.isEmpty(facilityLinkFacilityMappingsList)) {
						facilityLinkFacilityMappingRepository.saveAll(facilityLinkFacilityMappingsList);
					}
				}
			}

			sacsFacilityDto = saveUserForFacilityCreation(sacsFacilityDto, facility);
			sacsFacilityDto.setIsEdit(true);

		}

		return sacsFacilityDto;
	}

	public List<FacilityDto> getFacilitySacs(Long sacsId) {
		List<Facility> facilityList = new ArrayList<Facility>();
		List<FacilityDto> facilityDtoList = new ArrayList<FacilityDto>();
		facilityList = facilityRepository.findBySacsIdAndIsDeleteAndIsActive(sacsId, Boolean.FALSE, Boolean.TRUE);
		facilityDtoList = FacilityMapperUtil.mapToFacilityDtoList(facilityList);
		return facilityDtoList;

	}

	public List<TypologyDto> getAllTypology() {
		List<TypologyMaster> typologyMasterList = typologyRepository.findAll();
		List<TypologyDto> typologyDtoList = FacilityMapperUtil
				.mapTypologyMasterLitToTypologyDtoList(typologyMasterList);
		return typologyDtoList;
	}
	
	public List<FacilityDto> getSacsList() {
		Long facilityTypeId = (long) 2;
		List<Facility> facilityList = new ArrayList<Facility>();
		List<FacilityDto> facilityDtoList = new ArrayList<FacilityDto>();
		facilityList = facilityRepository.findByFacilityTypeIdAndIsDelete(facilityTypeId, Boolean.FALSE);
		facilityDtoList = FacilityMapperUtil.mapToFacilityDtoList(facilityList);
		return facilityDtoList;
	}	

	public List<SacsFacilityDto> getAllFacilityBySacs(Long sacsId,String searchText,Boolean isExternal, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		List<Long> facilityTypeIds = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<FacilityListProjection> facilityList = new ArrayList<FacilityListProjection>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		Page<FacilityListProjection> facilityPage = null;
		int actualCount = 0;
		Optional<List> facilityListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.SACS.getFacilityType()) {
			if(searchText == null || searchText == "") {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInFacilityTypes(sacsId,isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInFacilityTypes(sacsId,isExternal, Boolean.FALSE);
			}else {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInFacilityTypesBySearch(sacsId,searchText,isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInFacilityTypesBySearch(sacsId,searchText,isExternal, Boolean.FALSE);
			}
		}else {
//			if(searchText == null || searchText == "") {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInNACO(isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInNACO(isExternal, Boolean.FALSE);
//			}else {
//				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInNACOBySearch(sacsId,searchText,isExternal, pageable);			
//				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInNACOsBySearch(sacsId,searchText,isExternal, Boolean.FALSE);
//			}
		}
		
				
		facilityListOptional = Optional.ofNullable(facilityPage.getContent());		
		if (facilityListOptional != null && facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}		
		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualCount);
		}
		return sacsFacilityDtoList;
	}	
	
	
	public List<SacsFacilityDto> getAllFacilityBySacsAPI(Long sacsId,String searchText,Boolean isExternal, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		List<Long> facilityTypeIds = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<FacilityListProjection> facilityList = new ArrayList<FacilityListProjection>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		Page<FacilityListProjection> facilityPage = null;
		int actualCount = 0;
		Optional<List> facilityListOptional = null;
//		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		if (1 == FacilityTypeEnum.SACS.getFacilityType()) {
			if(searchText == null || searchText == "") {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInFacilityTypes(sacsId,isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInFacilityTypes(sacsId,isExternal, Boolean.FALSE);
			}else {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInFacilityTypesBySearch(sacsId,searchText,isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInFacilityTypesBySearch(sacsId,searchText,isExternal, Boolean.FALSE);
			}
		}else {
//			if(searchText == null || searchText == "") {
				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInNACO(isExternal, pageable);			
				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInNACO(isExternal, Boolean.FALSE);
//			}else {
//				facilityPage = facilityRepository.findBySacsIdAndIsDeleteAndNotInNACOBySearch(sacsId,searchText,isExternal, pageable);			
//				actualCount = facilityRepository.findCountIdBySacsIdAndIsDeleteNotInNACOsBySearch(sacsId,searchText,isExternal, Boolean.FALSE);
//			}
		}
		
				
		facilityListOptional = Optional.ofNullable(facilityPage.getContent());		
		if (facilityListOptional != null && facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}		
		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualCount);
		}
		return sacsFacilityDtoList;
	}	
	

	public List<SacsFacilityDto> getAllFacilityByParentId(Long parentFacilityId) {
		List<Facility> facilityList = new ArrayList<Facility>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		facilityList = facilityRepository.findLacByArt(parentFacilityId);
		sacsFacilityDtoList = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		return sacsFacilityDtoList;
	}

	public SacsFacilityDto deleteFacility(Long facilityId) {
		Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
		Facility facility = facilityOpt.get();
		facility.setIsDelete(Boolean.TRUE);
		facility.getAddress().setIsDelete(Boolean.TRUE);
		facilityRepository.save(facility);
		SacsFacilityDto sacsFacilityDto = FacilityMapperUtil.mapFacilityToSacsfacilityDto(facility);

		Set<TypologyFacilityMapping> typologyFacilityMappings = facility.getTypologyFacilityMappings();
		typologyFacilityMappings.forEach(element -> {
			element.setIsDelete(Boolean.TRUE);
		});
		typologyFacilityMappingRepository.saveAll(typologyFacilityMappings);

		Set<UserMaster> userMasterList = userRepository.findAllByFacilityId(facilityId);
		userMasterList.forEach(element -> {
			element.setIsDelete(Boolean.TRUE);
			if (element.getUserAuths() != null) {
				element.getUserAuths().setIsDelete(Boolean.TRUE);
			}
			Set<UserRoleMapping> roleMappings = element.getUserRoleMappings();
			if (!roleMappings.isEmpty()) {
				roleMappings.forEach(roleMap -> {
					roleMap.setIsDelete(Boolean.TRUE);
				});
				userRoleMappingRepository.saveAll(roleMappings);
			}
		});
		userRepository.saveAll(userMasterList);

		if (facility.getFacilityType().getId() == FacilityTypeEnum.SUPPLIER.getFacilityType()) {
			if (!facility.getContracts().isEmpty()) {
				Set<Contract> contracts = facility.getContracts();
				contracts.forEach(c -> {
					c.setIsDelete(Boolean.TRUE);
					Set<ContractProduct> contractProducts = c.getContractProducts();
					if (!contractProducts.isEmpty()) {
						contractProducts.forEach(cp -> {
							cp.setIsDelete(Boolean.TRUE);
						});
						contractProductDetailRepository.saveAll(contractProducts);
					}
				});
				contractRepository.saveAll(contracts);
			}
		}

		if (facility.getFacilityType().getId().equals(FacilityTypeEnum.TRANSPORTER.getFacilityType())) {
			facility.getTransporterSacsMappings1().forEach(tsm -> {
				tsm.setIsDelete(Boolean.TRUE);
			});

		}

		return sacsFacilityDto;
	}

	/**
	 * Entity Fetch Low Performance
	 * 
	 * @param facilityTypeId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<SacsFacilityDto> getFacilityByFacilityType(List<Long> facilityTypeId, Integer pageNumber,
			Integer pageSize) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = 1000;
		}
		LoginResponseDto currentLogin = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		Page<Facility> facilityPage = null;
		Optional<List> facilityListOptional = null;
		List<Facility> facilityList = new ArrayList<Facility>();
		int actualRecordCount;
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		if (!facilityTypeId.isEmpty() && facilityTypeId.get(0) == FacilityTypeEnum.WAREHOUSE.getFacilityType()
				&& currentLogin != null
				&& currentLogin.getFacilityTypeId() == FacilityTypeEnum.SACS.getFacilityType()) {
			actualRecordCount = facilityRepository.CountBySacsIdAndFacilityTypeIdIn(facilityTypeId,
					currentLogin.getFacilityId());
			facilityPage = facilityRepository.findBySacsIdAndFacilityTypeIdIn(facilityTypeId,
					currentLogin.getFacilityId(), pageable);
			facilityListOptional = Optional.ofNullable(facilityPage.getContent());
		} else if (!facilityTypeId.isEmpty() && facilityTypeId.get(0) == FacilityTypeEnum.SUPPLIER.getFacilityType()
				&& currentLogin != null
				&& currentLogin.getFacilityTypeId() == FacilityTypeEnum.PROCUREMENT_AGENT.getFacilityType()) {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdInAndProcurementAgentId(facilityTypeId,
					currentLogin.getFacilityId());
			facilityPage = facilityRepository.findByFacilityTypeIdInAndProcurementAgentId(facilityTypeId,
					currentLogin.getFacilityId(), pageable);
			facilityListOptional = Optional.ofNullable(facilityPage.getContent());
		} else {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdIn(facilityTypeId);
			facilityPage = facilityRepository.findByFacilityTypeIdIn(facilityTypeId, pageable);
			facilityListOptional = Optional.ofNullable(facilityPage.getContent());
		}
		if (facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}
		sacsFacilityDtoList = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtoList;
	}

	/**
	 * Optimized listing service using for Sacs, Procurement agent, warehouse,
	 * laboratory
	 * 
	 * @param facilityTypeId
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param sortBy
	 * @return
	 */
	public List<SacsFacilityDto> getFacilityByFacilityTypeOptimized(List<Long> facilityTypeId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		LoginResponseDto currentLogin = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		// For using alias name if sorting field name like (machine) then only use alias
		// name
		pageable = parenthesisEncapsulation(pageable);
		Page<FacilityListProjection> facilityListPage = null;
		Optional<List> facilityListOptional = null;
		List<FacilityListProjection> facilityProjectList = new ArrayList<>();
		int actualRecordCount = 0;
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();

		if (facilityTypeId != null && !facilityTypeId.isEmpty() && facilityTypeId.size() > 1) {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdIn(facilityTypeId);
			facilityListPage = facilityRepository.findLabListByFacilityTypeIdIn(facilityTypeId, pageable);
			facilityListOptional = Optional.ofNullable(facilityListPage.getContent());
		} else {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdIn(facilityTypeId);
			facilityListPage = facilityRepository.findFacilityListByFacilityTypeIdIn(facilityTypeId, pageable);
			facilityListOptional = Optional.ofNullable(facilityListPage.getContent());
		}
		
		if (facilityListOptional != null && facilityListOptional.isPresent()) {
			facilityProjectList = facilityListOptional.get();
		}
		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityProjectList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtoList;
	}

	public List<TypologyDto> getTypologyListByFacilityId(Long facilityId) {
		List<Object[]> typologyList = typologyRepository.findAllByFacilityIdAndIsDelete(facilityId);
		List<TypologyDto> typologyDtoList = TypologyMapperUtil.mapTypologyObjectListToTypologyDtoList(typologyList);
		return typologyDtoList;
	}

//	public SacsFacilityDto getFacilityByFacilityId(Long facilityId) {
//		Optional<Facility> facility = facilityRepository.findById(facilityId);
//		SacsFacilityDto sacsFacilityDto = new SacsFacilityDto();
//		if (facility.isPresent()) {
//			sacsFacilityDto = FacilityMapperUtil.mapFacilityToSacsfacilityDto(facility.get());
//
//			// Finding sacs name
//			if (sacsFacilityDto.getSacsId() != null && sacsFacilityDto.getSacsId() != 0) {
//				String sacsName = facilityRepository.findNameById(sacsFacilityDto.getSacsId());
//				if (sacsName != null) {
//					sacsFacilityDto.setSacsName(sacsName);
//				}
//			}
//			// Finding Society Registration Certificate
//			if ((sacsFacilityDto.getId() != null) && (sacsFacilityDto.getId() != 0) && (sacsFacilityDto.getFacilityTypeId() == 3l)) {
//				Optional<Object[]> socityRegList = ngoDocumentRepository.findSocietyCertByFacilityId(sacsFacilityDto.getId()).stream().findFirst();
//				logger.info("abc");
//				logger.info(socityRegList.toString());   
//				
//				if (socityRegList != null && socityRegList.isPresent()) {  //   yaha hai
//					List<NgoDocumentsDto> socityRegListDto = socityRegList.stream().map(object -> {
//						NgoDocumentsDto socityReg = new NgoDocumentsDto();
//						socityReg.setFileName(object[0] != null ? object[0].toString() : null);
//						socityReg.setFilePath(object[1] != null? object[1].toString():null);
//						socityReg.setSocietyValiditydate(object[2] != null? object[2].toString():null);
//						return socityReg;
//					}).collect(Collectors.toList());
//					
//					sacsFacilityDto.setSocityRegList(socityRegListDto);
//				}
//			}
//			System.out.println("sacsFacilityDto.getIsExternal()>>>>>>>>>>>>>>>>>>>>>>>"+sacsFacilityDto.getIsExternal());
//			// Finding Contract Letter
//			if(sacsFacilityDto.getIsExternal() != null) {				
//				if ((sacsFacilityDto.getId() != null) && (sacsFacilityDto.getId() != 0) && (sacsFacilityDto.getIsExternal() == false) && (sacsFacilityDto.getFacilityTypeId() == 3l)) {
//					Optional<Object[]> contractLetterList = ngoDocumentRepository.findContractLetterByFacilityId(sacsFacilityDto.getId()).stream().findFirst();
//					if (contractLetterList != null && !contractLetterList.isEmpty()) {
//						List<NgoDocumentsDto> contractLetterListDto = contractLetterList.stream().map(object -> {
//							NgoDocumentsDto contractLetter = new NgoDocumentsDto();
//							contractLetter.setFileName(object[0] != null ? object[0].toString() : null);
//							contractLetter.setFilePath(object[1] != null? object[1].toString():null);
//							contractLetter.setContractValiditydate(object[2] != null? object[2].toString():null);
//							return contractLetter;
//						}).collect(Collectors.toList());
//						
//						sacsFacilityDto.setContractLetterList(contractLetterListDto);
//					}
//				}
//			}
//			if (facility.get().getFacilityLinkFacilityMapping() != null) {
//				Set<FacilityLinkFacilityMapping> facilityLinkFacilityMappings = facility.get()
//						.getFacilityLinkFacilityMapping();
//				List<ArtPlusCoeMappingDto> artPlusMappingDtos = new ArrayList<ArtPlusCoeMappingDto>();
//				List<ArtPlusCoeMappingDto> artCoeMappingDtos = new ArrayList<ArtPlusCoeMappingDto>();
//				facilityLinkFacilityMappings.forEach(row -> {
//					if (row.getLinkFacilityTypeId().getId()== FacilityTypeEnum.ART_PLUS_FACILITY.getFacilityType()) {
//						if (row.getCurrentLinkStatus() == true) {
//							ArtPlusCoeMappingDto artPlusMapping = new ArtPlusCoeMappingDto();
//							artPlusMapping.setId(row.getId());
//							if(row.getLinkFacilityId().getId()!=null) {
//								artPlusMapping.setLinkFacilityId(row.getLinkFacilityId().getId());
//							}
//							if(row.getLinkFacilityTypeId().getId()!=null) {
//								artPlusMapping.setLinkFacilityTypeId(row.getLinkFacilityTypeId().getId());
//							}
//							
//							if(row.getParentFacilityId().getId()!=null) {
//								artPlusMapping.setParentFacilityId(row.getParentFacilityId().getId());
//							}
//							if(row.getParentFacilityTypeId().getId()!=null) {
//								artPlusMapping.setParentFacilityTypeId(row.getParentFacilityTypeId().getId());
//							}
//							artPlusMapping.setLinkFacilityName(row.getLinkFacilityId().getName());
//							artPlusMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
//							artPlusMapping.setLinkDate(row.getLinkDate());
//							artPlusMapping.setIsActive(row.getIsActive());
//							artPlusMapping.setIsDelete(row.getIsDelete());
//							artPlusMappingDtos.add(artPlusMapping);
//						}
//
//					} else if ((row.getLinkFacilityTypeId().getId() == FacilityTypeEnum.ART_COE_FACILITY.getFacilityType())) {
//						if (row.getCurrentLinkStatus() == true) {
//							ArtPlusCoeMappingDto coeMapping = new ArtPlusCoeMappingDto();
//							coeMapping.setId(row.getId());
//							if(row.getLinkFacilityId().getId()!=null) {
//								coeMapping.setLinkFacilityId(row.getLinkFacilityId().getId());
//							}
//							if(row.getLinkFacilityTypeId().getId()!=null) {
//								coeMapping.setLinkFacilityTypeId(row.getLinkFacilityTypeId().getId());
//							}
//							
//							if(row.getParentFacilityId().getId()!=null) {
//								coeMapping.setParentFacilityId(row.getParentFacilityId().getId());
//							}
//							if(row.getParentFacilityTypeId().getId()!=null) {
//								coeMapping.setParentFacilityTypeId(row.getParentFacilityTypeId().getId());
//							}
//							coeMapping.setLinkFacilityName(row.getLinkFacilityId().getName());
//							coeMapping.setCurrentLinkStatus(row.getCurrentLinkStatus());
//							coeMapping.setLinkDate(row.getLinkDate());
//							coeMapping.setIsActive(row.getIsActive());
//							coeMapping.setIsDelete(row.getIsDelete());
//							artCoeMappingDtos.add(coeMapping);
//						}
//
//					}
//
//				});
//				sacsFacilityDto.setArtPlusMapping(artPlusMappingDtos);
//				sacsFacilityDto.setCoeMapping(artCoeMappingDtos);
//
//			}
//
//		} else {
//			throwErrorManually("Facility Id: " + facilityId + " not present", "Facility Id");
//		}
//		return sacsFacilityDto;
//	}
	
	public SacsFacilityDto getFacilityByFacilityId(Long facilityId) {
	    Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);
	    SacsFacilityDto sacsFacilityDto = new SacsFacilityDto();
	    if (facilityOptional.isPresent()) {
	        Facility facility = facilityOptional.get();
	        sacsFacilityDto = FacilityMapperUtil.mapFacilityToSacsfacilityDto(facility);

	        // Finding sacs name
	        if (sacsFacilityDto.getSacsId() != null && sacsFacilityDto.getSacsId() != 0) {
	            String sacsName = facilityRepository.findNameById(sacsFacilityDto.getSacsId());
	            if (sacsName != null) {
	                sacsFacilityDto.setSacsName(sacsName);
	            }
	        }

	        // Finding Society Registration Certificate
	        if ((sacsFacilityDto.getId() != null) && (sacsFacilityDto.getId() != 0) && (sacsFacilityDto.getFacilityTypeId() == 3L)) {
	            Optional<List<Object[]>> societyRegOptional = Optional.ofNullable(ngoDocumentRepository.findSocietyCertByFacilityId(sacsFacilityDto.getId()));
	            if (societyRegOptional.isPresent() && !societyRegOptional.get().isEmpty()) {
	                List<NgoDocumentsDto> societyRegListDto = new ArrayList<>();
	                for (Object[] objects : societyRegOptional.get()) {
	                    NgoDocumentsDto societyReg = new NgoDocumentsDto();
	                    societyReg.setFileName(objects[0] != null ? objects[0].toString() : null);
	                    societyReg.setFilePath(objects[1] != null ? objects[1].toString() : null);
	                    societyReg.setSocietyValiditydate(objects[2] != null ? objects[2].toString() : null);
	                    societyRegListDto.add(societyReg);
	                }
	                sacsFacilityDto.setSocityRegList(societyRegListDto);
	            }
	        }

	        // Finding Contract Letter
	        if (sacsFacilityDto.getIsExternal() != null) {
	            if ((sacsFacilityDto.getId() != null) && (sacsFacilityDto.getId() != 0) && !sacsFacilityDto.getIsExternal() && (sacsFacilityDto.getFacilityTypeId() == 3L)) {
	                Optional<List<Object[]>> contractLetterOptional = Optional.ofNullable(ngoDocumentRepository.findContractLetterByFacilityId(sacsFacilityDto.getId()));
	                if (contractLetterOptional.isPresent() && !contractLetterOptional.get().isEmpty()) {
	                    List<NgoDocumentsDto> contractLetterListDto = new ArrayList<>();
	                    for (Object[] objects : contractLetterOptional.get()) {
	                        NgoDocumentsDto contractLetter = new NgoDocumentsDto();
	                        contractLetter.setFileName(objects[0] != null ? objects[0].toString() : null);
	                        contractLetter.setFilePath(objects[1] != null ? objects[1].toString() : null);
	                        contractLetter.setContractValiditydate(objects[2] != null ? objects[2].toString() : null);
	                        contractLetterListDto.add(contractLetter);
	                    }
	                    sacsFacilityDto.setContractLetterList(contractLetterListDto);
	                }
	            }
	        }

	        // Facility Link Facility Mapping
	        if (facility.getFacilityLinkFacilityMapping() != null) {
	            List<ArtPlusCoeMappingDto> artPlusMappingDtos = new ArrayList<>();
	            List<ArtPlusCoeMappingDto> artCoeMappingDtos = new ArrayList<>();
	            for (FacilityLinkFacilityMapping mapping : facility.getFacilityLinkFacilityMapping()) {
	                if (mapping.getLinkFacilityTypeId().getId() == FacilityTypeEnum.ART_PLUS_FACILITY.getFacilityType() && mapping.getCurrentLinkStatus()) {
	                    artPlusMappingDtos.add(mapToArtPlusCoeMappingDto(mapping));
	                } else if (mapping.getLinkFacilityTypeId().getId() == FacilityTypeEnum.ART_COE_FACILITY.getFacilityType() && mapping.getCurrentLinkStatus()) {
	                    artCoeMappingDtos.add(mapToArtPlusCoeMappingDto(mapping));
	                }
	            }
	            sacsFacilityDto.setArtPlusMapping(artPlusMappingDtos);
	            sacsFacilityDto.setCoeMapping(artCoeMappingDtos);
	        }
	    } else {
	        throwErrorManually("Facility Id: " + facilityId + " not present", "Facility Id");
	    }
	    return sacsFacilityDto;
	}

	private ArtPlusCoeMappingDto mapToArtPlusCoeMappingDto(FacilityLinkFacilityMapping mapping) {
	    ArtPlusCoeMappingDto mappingDto = new ArtPlusCoeMappingDto();
	    mappingDto.setId(mapping.getId());
	    if (mapping.getLinkFacilityId().getId() != null) {
	        mappingDto.setLinkFacilityId(mapping.getLinkFacilityId().getId());
	    }
	    if (mapping.getLinkFacilityTypeId().getId() != null) {
	        mappingDto.setLinkFacilityTypeId(mapping.getLinkFacilityTypeId().getId());
	    }
	    if (mapping.getParentFacilityId().getId() != null) {
	        mappingDto.setParentFacilityId(mapping.getParentFacilityId().getId());
	    }
	    if (mapping.getParentFacilityTypeId().getId() != null) {
	        mappingDto.setParentFacilityTypeId(mapping.getParentFacilityTypeId().getId());
	    }
	    mappingDto.setLinkFacilityName(mapping.getLinkFacilityId().getName());
	    mappingDto.setCurrentLinkStatus(mapping.getCurrentLinkStatus());
	    mappingDto.setLinkDate(mapping.getLinkDate());
	    mappingDto.setIsActive(mapping.getIsActive());
	    mappingDto.setIsDelete(mapping.getIsDelete());
	    return mappingDto;
	}


	public List<SacsFacilityDto> getFacilityByFacilityTypeIdAndSacs(Long facilityTypeId) {
		List<Facility> facilityList = new ArrayList<Facility>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		facilityList = facilityRepository.findByFacilityTypeIdAndSacsIdAndIsDelete(facilityTypeId,
				loginResponseDto.getFacilityId(), Boolean.FALSE);
		sacsFacilityDtoList = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		return sacsFacilityDtoList;
	}

	public List<SacsFacilityDto> getTiCenterByIduAndSacs() {
		List<Facility> facilityList = new ArrayList<Facility>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		facilityList = facilityRepository.findTiCenterUnderIDU(FacilityTypeEnum.TI_CENTER.getFacilityType(),
				loginResponseDto.getFacilityId());
		sacsFacilityDtoList = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		return sacsFacilityDtoList;
	}

	public List<SacsFacilityDto> getParentOstCenterList() {
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		List<Facility> facilityList = new ArrayList<>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		Facility facility = facilityRepository.findByIdAndIsDelete(loginResponseDto.getFacilityId(), Boolean.FALSE);
		if (facility.getFacilityType().getId() == FacilityTypeEnum.SACS.getFacilityType()) {
			facilityList = facilityRepository.findByFacilityTypeIdAndSacsIdAndIsDelete(
					FacilityTypeEnum.OST_FACILITY.getFacilityType(), facility.getId(), Boolean.FALSE);
		} else {
			facilityList = facilityRepository
					.findByFacilityTypeIdAndIsDelete(FacilityTypeEnum.OST_FACILITY.getFacilityType(), Boolean.FALSE);
		}
		sacsFacilityDtoList = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		return sacsFacilityDtoList;
	}

	public List<SacsFacilityDto> getFacilityListByParentAsCurrentFacility(Long facilityTypeId, Integer pageNumber,
			Integer pageSize) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		Page<Facility> facilityPage = null;
		Optional<List> facilityListOptional = null;
		List<SacsFacilityDto> sacsFacilityDtos = new ArrayList<>();
		List<Facility> facilityList = new ArrayList<>();
		int actualRecordCount = 0;
		actualRecordCount = facilityRepository.countByFacilityIdAndFacilityTypeIdAndIsDelete(
				loginResponseDto.getFacilityId(), facilityTypeId, Boolean.FALSE);
		facilityPage = facilityRepository.findAllByFacilityIdAndFacilityTypeIdAndIsDelete(
				loginResponseDto.getFacilityId(), facilityTypeId, Boolean.FALSE, pageable);
		facilityListOptional = Optional.ofNullable(facilityPage.getContent());
		if (facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}
		sacsFacilityDtos = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtos.isEmpty()) {
			sacsFacilityDtos.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtos;
	}

	/**
	 * Fetch facility list based on district id (Mandatory) and facility type id
	 * (Not Mandatory) api as query parameter
	 * 
	 * @param district
	 * @param facilityType
	 * @return
	 */
	public List<FacilityListByDistrictAndFacilityTypeDTO> getFacilityByDistrictAndFacilityType(Long district,
			Long facilityType) {
		if (district == null || district == 0) {
			throwErrorManually("District Id is Required", "Required_error");
		}
		List<Facility> facilityList = new ArrayList<>();
		List<FacilityListByDistrictAndFacilityTypeDTO> FacilityDtos = new ArrayList<>();
		if (facilityType != null && facilityType != 0) {
			facilityList = facilityRepository.findAllByDistrictIdAndFacilityTypeId(district, facilityType);
		} else {
			facilityList = facilityRepository.findAllByDistrictId(district);
		}
		FacilityDtos = FacilityMapperUtil.mapToFacilityListByDistrictAndFacilityTypeDTOList(facilityList);
		return FacilityDtos;
	}

	public List<SacsFacilityDto> advanceSearchForFacilities(Map<String, String> searchValues,
			List<Long> facilityTypeIds, Integer pageNumber, Integer pageSize, String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Long sacsId = 0l;
		int actualRecordCount = 0;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.SACS.getFacilityType()) {
			sacsId = currentUser.getFacilityId();
		}
		String searchQuery = FacilityMapperUtil.advanceSearchQueryCreator(searchValues, facilityTypeIds, sacsId);
		String countQuery = searchQuery.replace(
				"distinct(f.id) as facilityid,f.code as code,f.name as facilityname, count(distinct(c.noa_number)) as noa, \r\n"
						+ "f2.name as procurementagent, um.firstname as firstname,\r\n"
						+ "um.email as email ,um.mobile_number as mobilenumber,um.id as userid, s.name as state, f.created_time as createdtime,\r\n"
						+ "f.is_active as status,f.national_id as nationalid,ft.facility_type_name as facilitytype,m.machine_name as machine,\r\n"
						+ "count(distinct (lf.id) ) as mappedFacilityCount,f.is_lab as islab,d.name as district,sd.subdistrict_name as subdistrictname  \r\n",
				"count(distinct(f.id)) ");
		actualRecordCount = facilityRepository.actualCount(countQuery);
		searchQuery = FacilityMapperUtil.facilityGroupByAndOrderByClauseAdder(searchQuery, sortBy, sortType);
		// List<SacsFacilityDto> facilityList = facilityRepository.facilityCommonSearch(searchQuery,pageable);
		List<FacilityListColumnProjection> facilityList = facilityRepository.facilityCommonSearch(searchQuery,pageable);
		 List<SacsFacilityDto> sacsFacilityDtos = FacilityMapperUtil.mapFacilityListColumnProjectionToSacsfacilityDto(facilityList);
		//List<SacsFacilityDto> sacsFacilityDtos = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtos.isEmpty()) {
			sacsFacilityDtos.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtos; 
	}

	public List<SacsFacilityDto> normalSearchForFacilities(Map<String, String> searchDetails, Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Long sacsId = 0l;
		int actualRecordCount = 0;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.SACS.getFacilityType()) {
			sacsId = currentUser.getFacilityId();
		}
		String searchQuery = FacilityMapperUtil.normalSearchQueryCreator(searchDetails, sacsId);
		String countQuery = searchQuery.replace(
				"distinct (f.id) as facilityid,f.code as code,f.name as facilityname,\r\n"
						+ "um.firstname as firstname,\r\n"
						+ "um.email as email ,f.darpannumber,f.workingsince ,um.mobile_number as mobilenumber,um.id as userid, s.name as state,\r\n"
						+ "f.is_active as status,ft.facility_type_name as facilitytype,f.approval_status as approvalstatus,\r\n"
						+ "d.name as district,sd.subdistrict_name as subdistrictname,f.created_time as createdtime\r\n",
				"count(distinct(f.id)) ");
		actualRecordCount = facilityRepository.actualCount(countQuery);
		searchQuery = FacilityMapperUtil.facilityGroupByAndOrderByClauseAdder(searchQuery, sortBy, sortType);
		 List<FacilityListColumnProjection> facilityList = facilityRepository.facilityCommonSearch(searchQuery,pageable);
		//List<FacilityListProjection> facilityList = facilityRepository.facilityCommonSearch(searchQuery,pageable);
	
		List<SacsFacilityDto> sacsFacilityDtos = FacilityMapperUtil.mapFacilityListColumnProjectionToSacsfacilityDto(facilityList);
		
	//	List<SacsFacilityDto> sacsFacilityDtos = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtos.isEmpty()) {
			sacsFacilityDtos.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtos;
	}

	/**
	 * Optimized facility list for All facilities is_delete=false and is_active=true
	 * and is_external=false or null
	 * 
	 * @param divisionIds
	 * @param stateId
	 * @param facilityTypeId
	 * @param facilityTypeIds
	 * @param facilityName
	 * @param sacsId
	 * @param isExternal
	 * @return FacilityBasicListDto
	 */
	// @Cacheable(value = "FacilitiesBasicListCache")
	public List<FacilityBasicListDto> getFacilities(List<Long> divisionIds, Long stateId, Long facilityTypeId,
			List<Long> facilityTypeIds, String facilityName, Long sacsId, Integer limit, String isExternal,
			Long parentId) {
		long t1 = System.currentTimeMillis();
		String listQuery = FacilityMapperUtil.FacilityListQueryCreater(divisionIds, stateId, facilityTypeId,
				facilityTypeIds, facilityName, sacsId, limit, isExternal, parentId);
		logger.debug("APIEXECUTIONTIME GETFACILITIES 1 FacilityListQueryCreater --> "
				+ (System.currentTimeMillis() - t1) + " ms");
		long t2 = System.currentTimeMillis();
		List<Object[]> facilityListObj = facilityRepository.getObjectList(listQuery);
		logger.debug("APIEXECUTIONTIME GETFACILITIES 2 getObjectList QUERY --> " + (System.currentTimeMillis() - t2)
				+ " ms:" + "  listQuery-->:" + listQuery);
		long t3 = System.currentTimeMillis();
		List<FacilityBasicListDto> facilityList = FacilityMapperUtil.mapObjToBasicFAcilityList(facilityListObj);
		logger.debug("APIEXECUTIONTIME GETFACILITIES 3 mapObjToBasicFAcilityList --> "
				+ (System.currentTimeMillis() - t3) + " ms");
		return facilityList;
	}

	/**
	 * Optimized Facility list for Link Art and Ost Satellite (Pass facilityTypeId)
	 * Based on current login facility as parent facility is_delete=false and
	 * is_active=true and is_external=false or null
	 * 
	 * @param facilityType
	 * @return
	 */
	public List<FacilityBasicListDto> getAllOptimizedFacilityByCurrentFacilityAsParent(Long facilityType) {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<Object[]> facilityListObj = facilityRepository.findFacilityByParentId(currentUser.getFacilityId(),
				facilityType);
		List<FacilityBasicListDto> facilityList = FacilityMapperUtil.mapObjToBasicFAcilityList(facilityListObj);
		return facilityList;
	}

	/**
	 * Fetching mapped ICTC facility with current login EID lab
	 * 
	 * @return
	 */
	public List<SacsFacilityDto> getMappedIctcForEidLab() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<SacsFacilityDto> sacsFacilityDtos = new ArrayList<>();
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.LABORATORY_EID.getFacilityType()) {
			List<Facility> facilityList = facilityRepository.findMappedFacilityByFacilityIdAndFacilityType(
					currentUser.getFacilityId(), FacilityTypeEnum.ICTC_FACILITY.getFacilityType());
			sacsFacilityDtos = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		}
		return sacsFacilityDtos;
	}

	/**
	 * Fetching vl labs mapped with current facility id
	 * 
	 * @return
	 */
	public List<SacsFacilityDto> getMappedVlLabsUnderCurrentFacility() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<Long> facilityType = new ArrayList<>();
		facilityType.add(FacilityTypeEnum.VL_PRIVATE.getFacilityType());
		facilityType.add(FacilityTypeEnum.VL_PUBLIC.getFacilityType());
		List<Facility> facilityList = facilityRepository.findMappedVlLabsByFacilityId(currentUser.getFacilityId(),
				facilityType);
		List<Long> facilityIds = facilityList.stream().map(f-> f.getId()).collect(Collectors.toList());
		// ART_PLUS_FACILITY Added by ASJAD
		if (currentUser.getFacilityTypeId() == FacilityTypeEnum.ART_FACILITY.getFacilityType() || currentUser.getFacilityTypeId() == FacilityTypeEnum.ART_PLUS_FACILITY.getFacilityType()) {
			List<Facility> mhlFacilityList = findMHLFacilities();
			mhlFacilityList=mhlFacilityList.stream().filter(f -> !facilityIds.contains(f.getId()))
					.collect(Collectors.toList());
			facilityList.addAll(mhlFacilityList);
		}

		List<SacsFacilityDto> sacsFacilityDtos = FacilityMapperUtil.mapToSacsfacilityDto(facilityList);
		return sacsFacilityDtos;
	}

	private List<Facility> findMHLFacilities() {
		List<Facility> mhlFacilityList = facilityRepository
				.findMHLFacilities(FacilityTypeEnum.VL_PRIVATE.getFacilityType());
		return mhlFacilityList;
	}

	private SacsFacilityDto saveUserForFacilityCreation(SacsFacilityDto sacsFacilityDto, Facility facility) {
		if (facility.getId() != null) {

			List<RoleDto> roleList = new ArrayList<>();
			RoleDto roleDto = new RoleDto();
			UserMasterDto userMasterDto = new UserMasterDto();
			Optional<Role> roleOpt;

			// Saving Primary User
			if (sacsFacilityDto.getPrimaryUser() != null) {
				sacsFacilityDto.getPrimaryUser().setFacilityId(facility.getId());
				roleOpt = roleRepository.findById(sacsFacilityDto.getPrimaryUser().getRoleId());
				if (roleOpt.isPresent()) {
					roleDto = RoleMapperUtil.mapToRoleDTO(roleOpt.get());
					roleList.add(roleDto);
				}
				sacsFacilityDto.getPrimaryUser().setRoleDto(roleList);
				if (sacsFacilityDto.getIsActive()) {
					sacsFacilityDto.getPrimaryUser().setIsActive(true);
					sacsFacilityDto.getPrimaryUser().setStatus(1l);
				} else {
					sacsFacilityDto.getPrimaryUser().setIsActive(false);
					sacsFacilityDto.getPrimaryUser().setStatus(2l);
				}

				sacsFacilityDto.getPrimaryUser().setIsTrained(2l);
				try {
					userMasterDto = userService.saveUser(sacsFacilityDto.getFacilityTypeId(),
							sacsFacilityDto.getPrimaryUser());
					sacsFacilityDto.setPrimaryUser(userMasterDto);
				} catch (Exception e) {
					String errorfield = "Primary Username";
					throwError(errorfield, sacsFacilityDto.getPrimaryUser().getUserName());
				}
			}

			if (sacsFacilityDto.getFacilityTypeId().equals(FacilityTypeEnum.TRANSPORTER.getFacilityType())
					&& sacsFacilityDto.getTrasnporterAlternateUsers() != null
					&& !CollectionUtils.isEmpty(sacsFacilityDto.getTrasnporterAlternateUsers())) {
				for (UserMasterDto transporter : sacsFacilityDto.getTrasnporterAlternateUsers()) {
					if (transporter != null && transporter.getUserName() != null && transporter.getUserName() != ""
							&& transporter.getRoleId() != null && transporter.getFirstname() != null
							&& transporter.getFirstname() != "") {
						transporter.setFacilityId(facility.getId());
						roleList = new ArrayList<>();
						roleOpt = roleRepository.findById(transporter.getRoleId());
						if (roleOpt.isPresent()) {
							roleDto = RoleMapperUtil.mapToRoleDTO(roleOpt.get());
							roleList.add(roleDto);
						}
						transporter.setRoleDto(roleList);
						if (sacsFacilityDto.getIsActive()) {
							transporter.setIsActive(true);
							transporter.setStatus(1l);
						} else {
							transporter.setIsActive(false);
							transporter.setStatus(2l);
						}
						transporter.setIsTrained(2l);
						try {
							userMasterDto = userService.saveUser(sacsFacilityDto.getFacilityTypeId(), transporter);
							sacsFacilityDto.setAlternateUser(userMasterDto);
						} catch (Exception e) {
							String errorfield = "Alternate Username";
							throwError(errorfield, sacsFacilityDto.getAlternateUser().getUserName());
						}
					}
				}
			}

			// Saving Alternate User
			if (sacsFacilityDto.getAlternateUser() != null && sacsFacilityDto.getAlternateUser().getUserName() != null
					&& sacsFacilityDto.getAlternateUser().getUserName() != ""
					&& sacsFacilityDto.getAlternateUser().getRoleId() != null
					&& sacsFacilityDto.getAlternateUser().getFirstname() != null
					&& sacsFacilityDto.getAlternateUser().getFirstname() != "") {
				sacsFacilityDto.getAlternateUser().setFacilityId(facility.getId());
				roleList = new ArrayList<>();
				roleOpt = roleRepository.findById(sacsFacilityDto.getAlternateUser().getRoleId());
				if (roleOpt.isPresent()) {
					roleDto = RoleMapperUtil.mapToRoleDTO(roleOpt.get());
					roleList.add(roleDto);
				}
				sacsFacilityDto.getAlternateUser().setRoleDto(roleList);
				if (sacsFacilityDto.getIsActive()) {
					sacsFacilityDto.getAlternateUser().setIsActive(true);
					sacsFacilityDto.getAlternateUser().setStatus(1l);
				} else {
					sacsFacilityDto.getAlternateUser().setIsActive(false);
					sacsFacilityDto.getAlternateUser().setStatus(2l);
				}

				sacsFacilityDto.getAlternateUser().setIsTrained(2l);
				try {
					userMasterDto = userService.saveUser(sacsFacilityDto.getFacilityTypeId(),
							sacsFacilityDto.getAlternateUser());
					sacsFacilityDto.setAlternateUser(userMasterDto);
				} catch (Exception e) {
					String errorfield = "Alternate Username";
					throwError(errorfield, sacsFacilityDto.getAlternateUser().getUserName());
				}
			}

		}
		return sacsFacilityDto;
	}

	/**
	 * Method to throw error in case of validation errors
	 * 
	 * @param errorfield
	 * @param errorFieldValue
	 */
	private void throwErrorCannotChange(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.CANNOT_CHANGE + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(Constants.CANNOT_CHANGE + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * To through By passing String
	 * 
	 * @param errorString
	 * @param errorType
	 */
	private void throwErrorManually(String errorString, String errorType) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorType);
		errorDto.setDescription(errorString);
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(errorString, errorResponse, HttpStatus.BAD_REQUEST);
	}

	private void errorChecker(SacsFacilityDto sacsFacilityDto) {
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		if (FacilityTypeMapperUtil.facilityTypeLabChecker(sacsFacilityDto.getFacilityTypeId())
				&& loginResponseDto.getFacilityTypeId() == FacilityTypeEnum.NACO.getFacilityType()
				&& sacsFacilityDto.getFacilityTypeId() != FacilityTypeEnum.NARI.getFacilityType()
				&& (sacsFacilityDto.getSacsId() == null || sacsFacilityDto.getSacsId() == 0)) {
			throwErrorManually(REQUIRED_ERROR, "Required_error");
		}
		// Duplicate NARI facility check
		if (sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.NARI.getFacilityType()) {
			int count = 0;
			if (sacsFacilityDto.getId() == null) {
				count = facilityRepository.findCountOfNariFacilityForSave(FacilityTypeEnum.NARI.getFacilityType());
			} else if (sacsFacilityDto.getId() != null) {
				count = facilityRepository.findCountOfNariFacilityForEdit(FacilityTypeEnum.NARI.getFacilityType(),
						sacsFacilityDto.getId());
			}
			if (count != 0) {
				throwErrorManually("An active NARI Facility already exist", "Aleady_Exist");
			}
		}
		if (FacilityTypeMapperUtil.facilityTypeLabChecker(sacsFacilityDto.getFacilityTypeId())
				&& loginResponseDto.getFacilityTypeId() != FacilityTypeEnum.NACO.getFacilityType()
				&& loginResponseDto.getFacilityTypeId() != FacilityTypeEnum.SACS.getFacilityType()) {
			throwErrorManually(PERMISSION_ERROR, "Permission_error");
		}
		if (sacsFacilityDto.getIsActive() == false
				&& sacsFacilityDto.getFacilityTypeId() == FacilityTypeEnum.LAC_FACILITY.getFacilityType()) {
			int count = 0;
			count = facilityLinkedFacilityBeneficiaryRepository
					.findFacilityCountLinkedBeneficiary(sacsFacilityDto.getId());
			if (count != 0) {
				throwErrorManually("Facility already linked with beneficiary", "Status cannot change");
			}
		}
	}

	public List<FacilityBasicListDto> getSupplierByProcurementAgent(Long procurementAgentId) {
		Long supplierFacilityType = FacilityTypeEnum.SUPPLIER.getFacilityType();
		List<Object[]> facilityListObj = facilityRepository.findSupplierListByProcurementAgent(procurementAgentId,
				supplierFacilityType);
		List<FacilityBasicListDto> facilityList = FacilityMapperUtil.mapObjToBasicFAcilityList(facilityListObj);
		return facilityList;
	}

	/**
	 * Optimized
	 * 
	 * @param facilityTypeId
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param sortBy
	 * @return
	 */
	public List<SacsFacilityDto> getLacListByParentAsCurrentFacility(Long facilityTypeId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		LoginResponseDto loginResponseDto = UserUtils.getLoggedInUserDetails();
		List<SacsFacilityDto> sacsFacilityDtos = new ArrayList<>();
		List<FacilityListProjection> facilityList = new ArrayList<>();
		int actualRecordCount = 0;
		actualRecordCount = facilityRepository.countByFacilityIdAndFacilityTypeIdAndIsDelete(
				loginResponseDto.getFacilityId(), facilityTypeId, Boolean.FALSE);
		facilityList = facilityRepository.findByParentIdAndFacilityTypeId(loginResponseDto.getFacilityId(),
				facilityTypeId, pageable);
		sacsFacilityDtos = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtos.isEmpty()) {
			sacsFacilityDtos.get(0).setActualRecordCount(actualRecordCount);
		}
		return sacsFacilityDtos;
	}

	// Scheduler service for sending notification to remind contract expiry date
	public List<FacilityDto> reminderForContractExpiryDate() {
		List<Facility> fetchedComingExpiredContracts = null;
		List<FacilityDto> facilities = new ArrayList<FacilityDto>();
		try {
			fetchedComingExpiredContracts = facilityRepository.findComingExpiryContracts();
			logger.info("			fetchedComingExpiredContracts size :" + fetchedComingExpiredContracts.size());
		} catch (Exception e) {
			logger.error("Exception :", e.getMessage());
		}
		if (fetchedComingExpiredContracts != null) {
			logger.info("Inside of if (fetchedComingExpiredContracts != null)");
			for (Facility facility : fetchedComingExpiredContracts) {
				FacilityDto fac = new FacilityDto();
				fac = FacilityMapperUtil.mapToFacilityDto(facility);
				// String receipient = fac.getName();
				String facilityName = fac.getName();
				logger.debug("Facility Name :" + fac.getName());
				if (fac.getValidTill() != null) {
					logger.info("Inside of if (fac.getValidTill() != null)");
					String expirationDate = fac.getValidTill().toString();
					logger.debug("Expiration Date :" + fac.getValidTill());
					notificationEventRepository
							.findByEventIdAndIsEnabled(
									Long.parseLong(NotificationEventIdEnum.REMIND_EXPIRY_DATE.getEventId()), true)
							.ifPresent(x -> {
								placeholderMap.put("facilityName", facilityName);
								placeholderMap.put("expirationDate", expirationDate);
								placeholderMap.put("accessKey", env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY));
								placeholderMap.put(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY, facility.getId());
								String eventId = "";
								eventId = NotificationEventIdEnum.REMIND_EXPIRY_DATE.getEventId();
								try {
									notificationUtil.sendNotfication(eventId, true, false, false, placeholderMap);
									facility.setExpirationReminderDate(LocalDateTime.now());
									facilityRepository.save(facility);
								} catch (Exception e) {
									logger.error("Mail not sent!");
								}
							});
					facilities.add(fac);
				}
			}
		}

		return facilities;
	}

	public List<SacsFacilityDto> getAllFacilitesUnderIctc(Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		LoginResponseDto currentLogin = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<FacilityListProjection> facilityList = new ArrayList<FacilityListProjection>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		Page<FacilityListProjection> facilityPage = null;
		int actualCount = 0;
		Optional<List> facilityListOptional = null;
		facilityPage = facilityRepository.findFacilitiesUnderIctc(currentLogin.getFacilityId(), pageable);
		actualCount = facilityRepository.countOfFacilitiesUnderIctc(currentLogin.getFacilityId());
		facilityListOptional = Optional.ofNullable(facilityPage.getContent());
		if (facilityListOptional != null && facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}
		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualCount);
		}
		return sacsFacilityDtoList;
	}

	public List<SacsFacilityDto> getSatelliteOstListUnderCurrentFacility() {
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<Object[]> satelliteList = facilityRepository.satelliteOstListUnderCurrentFacility(
				currentUser.getFacilityId(), FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType());
		List<SacsFacilityDto> sacsFacilityDtoList = FacilityMapperUtil.mapSatelliteListObjectToDto(satelliteList);
		return sacsFacilityDtoList;
	}

	public static Pageable parenthesisEncapsulation(final Pageable pageable) {

		Sort sort = Sort.by(Collections.emptyList());
		for (final Sort.Order order : pageable.getSort()) {
			if (order.getProperty().matches("^\\(.*\\)$")) {
				sort = sort.and(JpaSort.unsafe(order.getDirection(), order.getProperty()));
			} else {
				sort = sort.and(Sort.by(order.getDirection(), order.getProperty()));
			}
		}
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	public List<FacilityDetailsProjectionForMobile> getFacilitiesForMobile(Long facilityTypeId, String searchParam, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize,
				Sort.by("name"));
		
		return facilityRepository.getFacilitiesForMobileByFacilityTypeId(facilityTypeId, searchParam, pageable);
	}

	public FacilityDetailsProjectionForMobile getParentFacilityDetailForMobile(Long facilityId) {
		return facilityRepository.getParentFacilityDetailForMobile(facilityId);
	}

	public List<SecondaryTypologyDto> getSecondaryTypologyList() {
		List<Object[]> typologyList = masterHrgSecondaryRepository.findAllByIsDeleteForMobile();

		List<TypologyDto> typologyDtoList = TypologyMapperUtil
				.mapTypologyObjectListToTypologyDtoListForMobile(typologyList);
		List<SecondaryTypologyDto> secondaryTyplogyList = TypologyMapperUtil.getSecondaryTypologyList(typologyDtoList);

		return secondaryTyplogyList;
	}

	public List<TypologyDto> getTypologyListByUserIdForMobile(Long userId) {
		List<Object[]> typologyList = typologyRepository.findAllByUserIdAndIsDeleteForMobile(userId);
		List<TypologyDto> typologyDtoList = TypologyMapperUtil
				.mapTypologyObjectListToTypologyDtoListForMobile(typologyList);
		return typologyDtoList;
	}
	
	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public FacilityDetailedProjection getSacsIdByStateDistrict(Integer stateId,Integer districtId){
		return facilityRepository.getSacsIdByStateDistrict(stateId);
	}
	
	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public SacsFacilityDto addAnyNGO(SacsFacilityDto sacsFacilityDto) {
		System.out.println("/createngo-SacsFacilityDto");
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
	//	errorChecker(sacsFacilityDto);
	
		int count = 0;
		// To check whether the facility name is already exist in table
		count = facilityRepository.existsByOtherNameInAdd(sacsFacilityDto.getName(),
				sacsFacilityDto.getFacilityTypeId());
		System.out.println("/createngo-SacsFacilityDto-1 ->"+count);
		if (count != 0) {
			String errorfield = "NGO/CBO name";
			throwError(errorfield, sacsFacilityDto.getName());
		} else {
//			if (sacsFacilityDto.getFacilityNo() != null && sacsFacilityDto.getFacilityNo() != "") {
//				count = 0;
//				count = facilityRepository.existsByFacilityNumberInAdd(sacsFacilityDto.getFacilityNo(),sacsFacilityDto.getDistrictId(),sacsFacilityDto.getFacilityTypeId());
//				if (count != 0) {
//					String errorfield = "Facility Number";
//					throwError(errorfield, sacsFacilityDto.getFacilityNo());
//				}
//			}
			
			Optional<State> state = stateRepository.findById(sacsFacilityDto.getStateId());
			Optional<District> district = districtRepository.findById(sacsFacilityDto.getDistrictId());
			Town town = null;
			Subdistrict subdistrict = null;
			Pincode pincode = null;
			if (sacsFacilityDto.getTownId() != null) {
				Optional<Town> townOpt = townRespository.findById(sacsFacilityDto.getTownId());
				if (townOpt.isPresent()) {
					town = townOpt.get();
				}

			}
			if (sacsFacilityDto.getSubDistrictId() != null) {
				Optional<Subdistrict> subdistrictOpt = subdistrictRepository
						.findById(sacsFacilityDto.getSubDistrictId());
				if (subdistrictOpt.isPresent()) {
					subdistrict = subdistrictOpt.get();
				}

			}
			if (sacsFacilityDto.getPincode() != null && sacsFacilityDto.getPincode() != "") {
				Optional<Pincode> pincodeOpt = pincodeRepository.findByPincode(sacsFacilityDto.getPincode());
				if (!pincodeOpt.isPresent()) {
					pincode = new Pincode();
					pincode.setPincode(sacsFacilityDto.getPincode());
					pincode.setIsActive(true);
					pincode.setIsDelete(false);
					pincode = pincodeRepository.save(pincode);
				} else {
					pincode = pincodeOpt.get();
				}
			}
			System.out.println("/createngo-SacsFacilityDto-2");		
			Optional<Division> division = divisionRepository.findById(sacsFacilityDto.getDivisionId());
			System.out.println("/createngo-SacsFacilityDto-3 --"+division.get());
			Facility facility = FacilityMapperUtil.mapSacsNGODtoToFacility(sacsFacilityDto);
			System.out.println("/createngo-SacsFacilityDto-4");
			Address address = AddressMapperUtil.mapSacsFacilityDtoToAddressFacility(sacsFacilityDto, state.get(),
					district.get(), town, subdistrict, pincode);
			facility.setAddress(address);
			facility.setDivision(division.get());
			System.out.println("/createngo-SacsFacilityDto-5");
			facility = facilityRepository.save(facility);
			System.out.println("/createngo-SacsFacilityDto-6");
			sacsFacilityDto.setId(facility.getId());
			
			sacsFacilityDto = saveUserForFacilityCreation(sacsFacilityDto, facility);
			System.out.println("/createngo-SacsFacilityDto-7");
			sacsFacilityDto.setIsEdit(false);

		}

		return sacsFacilityDto;
	}
	
	/*
	 * Save NGO Member
	 */
	public SacsFacilityDto saveNgoMembers(@RequestBody SacsFacilityDto sacsFacilityDto, MultipartFile fileKeyImgCf, MultipartFile fileKeyIdCf, MultipartFile fileKeyImgpd, MultipartFile fileKeyIdpd, MultipartFile fileKeyImgpm, MultipartFile fileKeyIdpm) throws IOException {
		System.out.println("fileKeyImgCf>>>>"+fileKeyImgCf);
		System.out.println("fileKeyImgpd>>>>"+fileKeyImgpd);
		System.out.println("sacsFacilityDto>>>>"+sacsFacilityDto);
		
		sacsFacilityDto.getNgomembercf().setFirstname(sacsFacilityDto.getNgomembercf().getFirstname() != null ? sacsFacilityDto.getNgomembercf().getFirstname().trim() : null);
		if(fileKeyImgpd != null) {
		sacsFacilityDto.getNgomemberpd().setFirstname(sacsFacilityDto.getNgomemberpd().getFirstname() != null ? sacsFacilityDto.getNgomemberpd().getFirstname().trim() : null);
		}
		if(fileKeyImgpm != null) {
		sacsFacilityDto.getNgomemberpm().setFirstname(sacsFacilityDto.getNgomemberpm().getFirstname() != null ? sacsFacilityDto.getNgomemberpm().getFirstname().trim() : null);
		}
		logger.debug("Entering into method saveProduct with productDto->{}:", sacsFacilityDto);
		int count = 0;
		NgoMember ngoMember = null;
		Facility facility = new Facility();		
		NgoMemberDto ngoMemberDto = new NgoMemberDto();
	// Saving Chief Functionery Details
				if (fileKeyImgCf != null) {
					sacsFacilityDto.getNgomembercf().setPhoto(fileKeyImgCf.getBytes());
					sacsFacilityDto.getNgomembercf().setIdproof(fileKeyIdCf.getBytes());
					
					//if (sacsFacilityDto.getIsActive()) {
						sacsFacilityDto.getNgomembercf().setIsActive(true);
						sacsFacilityDto.getNgomembercf().setStatus(1l);
//					} else {
//						sacsFacilityDto.getNgomembercf().setIsActive(false);
//						sacsFacilityDto.getNgomembercf().setStatus(2l);
//					}
					try {						
						ngoMember = new NgoMember();
						ngoMember.setFirstname(sacsFacilityDto.getNgomembercf().getFirstname());
						ngoMember.setEmail(sacsFacilityDto.getNgomembercf().getEmail());
						ngoMember.setMobileNumber(sacsFacilityDto.getNgomembercf().getMobileNumber());
						ngoMember.setLandlineNumber(sacsFacilityDto.getNgomembercf().getLandlineNumber());
						ngoMember.setRoleId(sacsFacilityDto.getNgomembercf().getRoleId());
						//ngoMember.setFacilityId(sacsFacilityDto.getNgomembercf().getFacilityId());
						facility.setId(sacsFacilityDto.getNgomembercf().getFacilityId());
						ngoMember.setFacility(facility);
					//	ngoMember.setFacilityId(sacsFacilityDto.getNgomembercf().getFacilityId());
						ngoMember.setPhoto(sacsFacilityDto.getNgomembercf().getPhoto());
						ngoMember.setIdproof(sacsFacilityDto.getNgomembercf().getIdproof());
						ngoMember.setIsActive(true);
						ngoMember.setIsDelete(false);
						ngoMemberRepository.save(ngoMember);
						
						sacsFacilityDto.setNgomembercf(ngoMemberDto);
						
					} catch (Exception e) {
						String errorfield = "Ngo Members";
						throwError(errorfield, sacsFacilityDto.getPrimaryUser().getUserName());
					}
				}
				if (fileKeyImgpd != null) {
					sacsFacilityDto.getNgomemberpd().setPhoto(fileKeyImgpd.getBytes());
					sacsFacilityDto.getNgomemberpd().setIdproof(fileKeyIdpd.getBytes());
						sacsFacilityDto.getNgomemberpd().setIsActive(true);
						sacsFacilityDto.getNgomemberpd().setStatus(1l);
					try {						
						ngoMember = new NgoMember();
						ngoMember.setFirstname(sacsFacilityDto.getNgomemberpd().getFirstname());
						ngoMember.setEmail(sacsFacilityDto.getNgomemberpd().getEmail());
						ngoMember.setMobileNumber(sacsFacilityDto.getNgomemberpd().getMobileNumber());
						ngoMember.setLandlineNumber(sacsFacilityDto.getNgomemberpd().getLandlineNumber());
						ngoMember.setRoleId(sacsFacilityDto.getNgomemberpd().getRoleId());
						facility.setId(sacsFacilityDto.getNgomemberpd().getFacilityId());
						ngoMember.setFacility(facility);
					//	ngoMember.setFacilityId(sacsFacilityDto.getNgomemberpd().getFacilityId());
						ngoMember.setPhoto(sacsFacilityDto.getNgomemberpd().getPhoto());
						ngoMember.setIdproof(sacsFacilityDto.getNgomemberpd().getIdproof());
						ngoMember.setIsActive(true);
						ngoMember.setIsDelete(false);
						ngoMemberRepository.save(ngoMember);
						
						sacsFacilityDto.setNgomemberpd(ngoMemberDto);
						
					} catch (Exception e) {
						String errorfield = "Ngo Members";
						throwError(errorfield, sacsFacilityDto.getPrimaryUser().getUserName());
					}
				}
				System.out.println("sacsFacilityDto.getNgomemberpm()=="+sacsFacilityDto.getNgomemberpm());
				if (fileKeyImgpm != null) {
					sacsFacilityDto.getNgomemberpm().setPhoto(fileKeyImgpm.getBytes());
					sacsFacilityDto.getNgomemberpm().setIdproof(fileKeyIdpm.getBytes());
						sacsFacilityDto.getNgomemberpm().setIsActive(true);
						sacsFacilityDto.getNgomemberpm().setStatus(1l);
					try {						
						ngoMember = new NgoMember();
						ngoMember.setFirstname(sacsFacilityDto.getNgomemberpm().getFirstname());
						ngoMember.setEmail(sacsFacilityDto.getNgomemberpm().getEmail());
						ngoMember.setMobileNumber(sacsFacilityDto.getNgomemberpm().getMobileNumber());
						ngoMember.setLandlineNumber(sacsFacilityDto.getNgomemberpm().getLandlineNumber());
						ngoMember.setRoleId(sacsFacilityDto.getNgomemberpm().getRoleId());
						facility.setId(sacsFacilityDto.getNgomemberpm().getFacilityId());
						ngoMember.setFacility(facility);
					//	ngoMember.setFacilityId(sacsFacilityDto.getNgomemberpm().getFacilityId());
						ngoMember.setPhoto(sacsFacilityDto.getNgomemberpm().getPhoto());
						ngoMember.setIdproof(sacsFacilityDto.getNgomemberpm().getIdproof());
						ngoMember.setIsActive(true);
						ngoMember.setIsDelete(false);
						ngoMemberRepository.save(ngoMember);
						
						sacsFacilityDto.setNgomemberpm(ngoMemberDto);
						
					} catch (Exception e) {
						String errorfield = "Ngo Members";
						throwError(errorfield, sacsFacilityDto.getPrimaryUser().getUserName());
					}
				}
				return sacsFacilityDto;
	}
	
	// Method to change Active/Inactive Status
	public void changeMemberStatus(Long memberId, Boolean memberStatus) {			
		logger.debug("Entering into changeMemberStatus  method  - FacilityService");
		facilityRepository.changeMemberStatus(memberId, memberStatus);
	}
	
	// Method to change Active/Inactive Status
	public void changeNgoCboStatus(Long ngoId, Boolean ngoStatus) {			
		logger.debug("Entering into changeNgoCboStatus  method  - FacilityService");
		facilityRepository.changeNgoCboStatus(ngoId, ngoStatus);
	}
	
	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public NgoAcceptRejectDto addAcceptRejectDetails(NgoAcceptRejectDto ngoAcceptRejectDto) {
		System.out.println("/createngo-addAcceptRejectDetails");
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		int count = 0;
		// To check whether the facility name is already exist in table
//		count = facilityRepository.existsByOtherNameInAdd(ngoAcceptRejectDto.getStatus(),
//				ngoAcceptRejectDto.getFacilityId());
		System.out.println("/createngo-addAcceptRejectDetails-1 ->"+ngoAcceptRejectDto.getApproveRejectDate());
		if (count != 0) {
			String errorfield = "NGO/CBO name";
			// throwError(errorfield, ngoAcceptRejectDto.getStatus());
		} else {
			NgoAcceptRejectEntity	ngoAcceptRejectEntity = new NgoAcceptRejectEntity();
			ngoAcceptRejectEntity.setApproveRejectDate(ngoAcceptRejectDto.getApproveRejectDate());
			ngoAcceptRejectEntity.setDisclaimer(ngoAcceptRejectDto.getDisclaimer());
			ngoAcceptRejectEntity.setFacilityId(ngoAcceptRejectDto.getFacilityId());
			ngoAcceptRejectEntity.setRemarks(ngoAcceptRejectDto.getRemarks());
			ngoAcceptRejectEntity.setStatus(AcceptRejectEnum.Rejected.getAcceptRejectStatus());
			ngoAcceptRejectEntity.setIsActive(true);
			ngoAcceptRejectEntity.setIsDelete(false);
			ngoAcceptRejectRepository.save(ngoAcceptRejectEntity);
			System.out.println("/createngo-addAcceptRejectDetails==>"+ngoAcceptRejectEntity);	
			facilityRepository.updateFacilityAcceptRejectStatus(ngoAcceptRejectDto.getFacilityId(),AcceptRejectEnum.Rejected.getAcceptRejectStatus());

		}

		return ngoAcceptRejectDto;
	}
	
	/*
	 * Save Approval and upload document
	 */
	public NgoAcceptRejectDto addAcceptDocument(@RequestBody NgoAcceptRejectDto ngoAcceptRejectDto) {
		// , MultipartFile file
		System.out.println("Facility Service==================================================>"+ngoAcceptRejectDto);
		logger.debug("Entering into method addAcceptDocument with ngoAcceptRejectDto->{}:", ngoAcceptRejectDto);
		int count = 0;
//		if (ngoAcceptRejectDto.getId() != null && ngoAcceptRejectDto.getId() > 0) {
//			count = ngoAcceptRejectRepository.isExistByProductNameInEdit(ngoAcceptRejectDto.getFacilityId(), ngoAcceptRejectDto.getId());
//		} else {
//			count = ngoAcceptRejectRepository.isExistByProductNameInEdit(ngoAcceptRejectDto.getFacilityId(), ngoAcceptRejectDto.getId());
//		}
//		if (count != 0) {
//			logger.error(Constants.DUPLICATE_FOUND);
//			String errorfield = "Product Name";
//			throwError(errorfield, ngoAcceptRejectDto.getProductName());
//		}
		
		NgoAcceptRejectEntity	ngoAcceptRejectEntity = new NgoAcceptRejectEntity();
		ngoAcceptRejectEntity.setApproveRejectDate(ngoAcceptRejectDto.getApproveRejectDate());
		ngoAcceptRejectEntity.setDisclaimer(ngoAcceptRejectDto.getDisclaimer());
		ngoAcceptRejectEntity.setFacilityId(ngoAcceptRejectDto.getFacilityId());
		ngoAcceptRejectEntity.setRemarks(ngoAcceptRejectDto.getRemarks());
		ngoAcceptRejectEntity.setStatus(AcceptRejectEnum.Approved.getAcceptRejectStatus());
		ngoAcceptRejectEntity.setIsActive(true);
		ngoAcceptRejectEntity.setIsDelete(false);
		
//		if (file != null && !file.isEmpty()) {
//			try {
//				if (file.getContentType().equals("application/pdf")) {	
//					System.out.println("Inside pdf block ============ file.getOriginalFilename()===");
//					 // Save file on system
//			        if (!file.getOriginalFilename().isEmpty()) {
//			        	this.setLocation(env);
//			        	this.fileName = this.storeFile(file);
//			        	System.out.println("file.getOriginalFilename()==="+this.fileStorageLocation);
//			        } else {
//			            throw new Exception();
//			        }
//					
//					ngoAcceptRejectEntity.setFileName(this.fileName);
//					ngoAcceptRejectEntity.setFilePath(this.fileStorageLocation); 
//					
//					ngoAcceptRejectDto.setFileName(ngoAcceptRejectEntity.getFileName());
//					ngoAcceptRejectDto.setFilePath(ngoAcceptRejectEntity.getFilePath());
//					
//				} else {
//					throwErrorManually("Darpan Registration document must be pdf format................!", "Format Error");
//				}
//			} catch (Exception e) {
//				System.err.println(e);
//				e.printStackTrace();
//				throwErrorManually("Darpan Registration document cannot upload!", "Upload Error");
//				//e.printStackTrace();
//			}
//		}
		
		ngoAcceptRejectRepository.save(ngoAcceptRejectEntity);
		ngoAcceptRejectDto.setId(ngoAcceptRejectEntity.getId());
		logger.debug("Leaving from method addAcceptDocument with ngoAcceptRejectDto->{}:", ngoAcceptRejectDto);
		facilityRepository.updateFacilityAcceptRejectStatus(ngoAcceptRejectDto.getFacilityId(),AcceptRejectEnum.Approved.getAcceptRejectStatus());
		 return ngoAcceptRejectDto;
	}
	
	
	public void setLocation(Environment env) {
		this.fileLocation = Paths.get(env.getProperty("app.file.upload-dir", "/uploads/files")).toAbsolutePath().normalize();
		//this.fileLocation = Paths.get(env.getProperty("app.file.upload-dir", "http://localhost:4200/assets/documents")).toAbsolutePath().normalize();
				//"http://localhost:4200\\assets\\documents";
				try {
				Files.createDirectories(this.fileLocation);
				this.fileStorageLocation = this.fileLocation.toString();
				} catch (Exception ex) {
				throw new RuntimeException(
				  "Could not create the directory where the uploaded files will be stored.", ex);
				}
		
	}
	public String storeFile(MultipartFile file) {
	    // Normalize file name
	    String fileName =
	        new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

	    try {
	      // Check if the filename contains invalid characters
	      if (fileName.contains("..")) {
	        throw new RuntimeException(
	            "Sorry! Filename contains invalid path sequence " + fileName);
	      }

	      Path targetLocation = this.fileLocation.resolve(fileName);
	      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	      return fileName;
	    } catch (IOException ex) {
	      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
	    }
	  }
    private String getFileExtension(String fileName) {
	    if (fileName == null) {
	      return null;
	    }
	    String[] fileNameParts = fileName.split("\\.");

	    return fileNameParts[fileNameParts.length - 1];
	  }
	  
	public List<NgoAcceptRejectDto> approveRejectList(Long facilityId, List<NgoAcceptRejectDto> ngoAcceptRejectDtoMapper) {
		List<Object[]> ngoAcceptRejectList = new ArrayList<>();
		 ngoAcceptRejectList = ngoAcceptRejectRepository.fetchAcceptRejectListByFacId(facilityId);
		if (ngoAcceptRejectList != null && !ngoAcceptRejectList.isEmpty()) {
			  ngoAcceptRejectDtoMapper = ngoAcceptRejectList.stream().map(object -> {
				  NgoAcceptRejectDto ngoAcceptRejectDtoList = new NgoAcceptRejectDto();
					ngoAcceptRejectDtoList.setId(object[0] != null ? Long.valueOf(object[0].toString()) : null);
					ngoAcceptRejectDtoList.setRemarks(object[1].toString());
					ngoAcceptRejectDtoList.setApproveRejectDate((Date) object[2]);
//					if((Integer)object[7] == 1) {
//						ngoAcceptRejectDtoList.setFileName(object[3].toString());
//						ngoAcceptRejectDtoList.setFilePath((String) object[4]);
//					}
				
				return ngoAcceptRejectDtoList;
			}).collect(Collectors.toList());
		
		}else {
			ngoAcceptRejectList = ngoAcceptRejectRepository.fetchDataByFacId(facilityId);
			ngoAcceptRejectDtoMapper = ngoAcceptRejectList.stream().map(object -> {
				  NgoAcceptRejectDto ngoAcceptRejectDtoList = new NgoAcceptRejectDto();
				  ngoAcceptRejectDtoList.setApproveRejectDate((Date) object[0]);
				return ngoAcceptRejectDtoList;
			}).collect(Collectors.toList());
		}
		return ngoAcceptRejectDtoMapper;
	}
	
	public List<NgoMemberDto> getAllMemberByFacility(Long facilityId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<NgoMemberListProjection> memberList = new ArrayList<NgoMemberListProjection>();
		List<NgoMemberDto> ngoMemberDtoList = new ArrayList<NgoMemberDto>();
		Page<NgoMemberListProjection> memberPage = null;
		int actualCount = 0;
		Optional<List> memberListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if(searchText == null || searchText == "") {
			memberPage = facilityRepository.findAllMembersByFacilityId(facilityId, pageable);
			actualCount = facilityRepository.findCountByFacilityId(facilityId);
		}else {
			memberPage = facilityRepository.findAllMembersByFacilityIdSearch(facilityId,searchText, pageable);
			actualCount = facilityRepository.findCountByFacilityIdSearch(facilityId,searchText);
		}
			memberListOptional = Optional.ofNullable(memberPage.getContent());

		if (memberListOptional != null && memberListOptional.isPresent()) {
			memberList = memberListOptional.get();
		}
		
		ngoMemberDtoList = FacilityMapperUtil.mapMemberListProjectionToFacilityDto(memberList);
		if (!ngoMemberDtoList.isEmpty()) {
			ngoMemberDtoList.get(0).setActualRecordCount(actualCount);
		}
		return ngoMemberDtoList;
	}
	
	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public NgoMemberDto addGBDetails(NgoMemberDto ngoMemberDto) {
		System.out.println("/addGBDetails");
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		int count = 0;
		// To check whether the facility name is already exist in table
//		count = facilityRepository.existsByOtherNameInAdd(ngoMemberDto.getStatus(),
//				ngoMemberDto.getFacilityId());
		System.out.println("/createngo-ngoMemberDto-1 ->"+ngoMemberDto.getEducation());
		if (count != 0) {
			String errorfield = "Member Name";
			// throwError(errorfield, ngoMemberDto.getStatus());
		} else {
			GbDetailsEntity	gbDetailsEntity = new GbDetailsEntity();
			gbDetailsEntity.setFacilityId(ngoMemberDto.getFacilityId());
			gbDetailsEntity.setFirstname(ngoMemberDto.getFirstname());
			gbDetailsEntity.setEmail(ngoMemberDto.getEmail());
			gbDetailsEntity.setMobileNumber(ngoMemberDto.getMobileNumber());
			gbDetailsEntity.setRoleId(ngoMemberDto.getRoleId());
			gbDetailsEntity.setEducation(ngoMemberDto.getEducation());
			gbDetailsEntity.setIsActive(true);
			gbDetailsEntity.setIsDelete(false);
			ngoGbRepository.save(gbDetailsEntity);
			System.out.println("/createngo-ngoGbRepository==>"+gbDetailsEntity);

		}

		return ngoMemberDto;
	}
	
	public List<NgoMemberDto> getAllGBMemberByFacility(Long facilityId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<NgoMemberListProjection> memberList = new ArrayList<NgoMemberListProjection>();
		List<NgoMemberDto> ngoMemberDtoList = new ArrayList<NgoMemberDto>();
		Page<NgoMemberListProjection> memberPage = null;
		int actualCount = 0;
		Optional<List> memberListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();		
		
//			if(currentUser.getFacilityTypeId() == 2l) {
//				if(searchText == null || searchText == "") {
//					memberPage = ngoGbRepository.findAllGBMembersBySacsId(facilityId, pageable);
//					actualCount = ngoGbRepository.findGBCountBySacsId(facilityId);
//				}else {																																																										
//					memberPage = ngoGbRepository.findAllGBMembersBySacsIdSearch(facilityId,searchText, pageable);
//					actualCount = ngoGbRepository.findGBCountBySacsIdSearch(facilityId,searchText);
//				}		
//			}else {
				if(searchText == null || searchText == "") {
					memberPage = ngoGbRepository.findAllGBMembersByFacilityId(facilityId, pageable);
					actualCount = ngoGbRepository.findGBCountByFacilityId(facilityId);
				}else {																																																										
					memberPage = ngoGbRepository.findAllGBMembersByFacilityIdSearch(facilityId,searchText, pageable);
					actualCount = ngoGbRepository.findGBCountByFacilityIdSearch(facilityId,searchText);
				}
//			}
		
		
		
			memberListOptional = Optional.ofNullable(memberPage.getContent());

		if (memberListOptional != null && memberListOptional.isPresent()) {
			memberList = memberListOptional.get();
		}
		
		ngoMemberDtoList = FacilityMapperUtil.mapMemberListProjectionToFacilityDto(memberList);
		if (!ngoMemberDtoList.isEmpty()) {
			ngoMemberDtoList.get(0).setActualRecordCount(actualCount);
		}
		return ngoMemberDtoList;
	}
	
	// Method to change Active/Inactive Status
		public void changeGbStatus(Long gbId, Boolean gbStatus) {			
			logger.debug("Entering into changeGbStatus  method  - FacilityService");
			ngoGbRepository.changeGbStatus(gbId, gbStatus);
		}
		
	public void deleteGbDetails(Long gbId) {
		ngoGbRepository.deleteGbDetail(gbId,Boolean.TRUE);
	}
	/**
	 * @param sacsFacilityDto
	 * @return
	 */
	public NgoAcceptRejectDto addBlackListDetails(NgoAcceptRejectDto ngoAcceptRejectDto) {
		System.out.println("/createngo-addAcceptRejectDetails");
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		int count = 0;
		// To check whether the facility name is already exist in table
//		count = facilityRepository.existsByOtherNameInAdd(ngoAcceptRejectDto.getStatus(),
//				ngoAcceptRejectDto.getFacilityId());
		System.out.println("/createngo-addAcceptRejectDetails-1 ->"+ngoAcceptRejectDto.getApproveRejectDate());
		if (count != 0) {
			String errorfield = "NGO/CBO name";
			// throwError(errorfield, ngoAcceptRejectDto.getStatus());
		} else {
			NgoBlackListEntity	ngoBlackListEntity = new NgoBlackListEntity();
			ngoBlackListEntity.setBlackListDate(ngoAcceptRejectDto.getBlackListDate());
			ngoBlackListEntity.setFacilityId(ngoAcceptRejectDto.getFacilityId());
			ngoBlackListEntity.setRemarks(ngoAcceptRejectDto.getRemarks());
			ngoBlackListEntity.setIsActive(true);
			ngoBlackListEntity.setIsDelete(false);
			ngoBlackListRepository.save(ngoBlackListEntity);
			System.out.println("/createngo-addBlackListDetails==>"+ngoBlackListEntity);	
			facilityRepository.updateFacilityBlackListStatus(ngoBlackListEntity.getFacilityId());
		}

		return ngoAcceptRejectDto;
	}
	
	public List<SacsFacilityDto> getBlackListNGOPaginate(Long sacsId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		List<Long> facilityTypeIds = new ArrayList<>();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<FacilityListProjection> facilityList = new ArrayList<FacilityListProjection>();
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		Page<FacilityListProjection> facilityPage = null;
		int actualCount = 0;
		Optional<List> facilityListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if(searchText == null || searchText == "") {
			facilityPage = ngoBlackListRepository.findBlacklistBySacsId(sacsId, pageable);			
			actualCount = ngoBlackListRepository.findBlacklistCountBySacsId(sacsId, Boolean.FALSE);
		}else {
			facilityPage = ngoBlackListRepository.findBlacklistBySacsIdandSearch(sacsId,searchText, pageable);			
			actualCount = ngoBlackListRepository.findBlacklistCountBySacsIdandSearch(sacsId,searchText, Boolean.FALSE);
		}
				
		facilityListOptional = Optional.ofNullable(facilityPage.getContent());		
		if (facilityListOptional != null && facilityListOptional.isPresent()) {
			facilityList = facilityListOptional.get();
		}		
		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualCount);
		}
		return sacsFacilityDtoList;
	}
	
	// Method to change Active/Inactive Status
		public void updateNgoCbo(Long facilityId,String workingsince,String facilityLandLineNumber,String facilityEmailId,String addressLineOne,String addressLineTwo, Integer districtId,Integer subDistrictId,Integer townId,String pincode){			
			logger.debug("Entering into updateNgoCbo  method  - FacilityService");
			 // Convert String to LocalDateTime using Parse() method
	      //  LocalDateTime localDateTime = LocalDateTime.parse(workingsince);
	        LocalDate localDateTime =  LocalDate.parse(workingsince, DateTimeFormatter.BASIC_ISO_DATE);
	 
	        // Print LocalDateTime object
	        System.out.println("LocalDateTime obj: "+localDateTime);
	        
			facilityRepository.updateNgoCbo(facilityId,localDateTime,facilityLandLineNumber,facilityEmailId,addressLineOne,addressLineTwo,districtId,subDistrictId,townId,pincode);
		}
		
		/**
		 * @param sacsFacilityDto
		 * @return
		 */
		public NacoBudgetAllocationDto addBudgetAllocationByNACO(NacoBudgetAllocationDto nacoBudgetAllocationDto) {
			System.out.println("/addBudgetAllocationByNACO");
		//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
			
			int count = 0;
			// To check whether the facility name is already exist in table
//			count = facilityRepository.existsByOtherNameInAdd(ngoMemberDto.getStatus(),
//					ngoMemberDto.getFacilityId());
			System.out.println("/addBudgetAllocationByNACO- ->"+nacoBudgetAllocationDto.getFacilityId());
			if (count != 0) {
				String errorfield = "Member Name";
				// throwError(errorfield, ngoMemberDto.getStatus());
			} else {
				NacoBudgetAllocationEntity	nacoBudgetAllocationEntity = new NacoBudgetAllocationEntity();
				nacoBudgetAllocationEntity.setFacilityId(nacoBudgetAllocationDto.getFacilityId());
				nacoBudgetAllocationEntity.setApprovedBudget(nacoBudgetAllocationDto.getApprovedBudget());
				nacoBudgetAllocationEntity.setFinancialYear(nacoBudgetAllocationDto.getFinancialYear());
				nacoBudgetAllocationEntity.setComments(nacoBudgetAllocationDto.getComments());
				nacoBudgetAllocationEntity.setIsActive(true);
				nacoBudgetAllocationEntity.setIsDelete(false);
				nacoBudgetAllocationRepository.save(nacoBudgetAllocationEntity);
				System.out.println("/nacoBudgetAllocationRepository==>"+nacoBudgetAllocationEntity);

			}

			return nacoBudgetAllocationDto;
		}
		
		public List<NacoBudgetAllocationDto> getAllAllocatedBugetByNaco(String searchText, Integer pageNumber, Integer pageSize, String sortBy,
				String sortType) {
			if (pageNumber == null || pageSize == null) {
				pageNumber = 0;
				pageSize = exportRecordsLimit;
			}
			Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
			if (sortType.equalsIgnoreCase("asc")) {
				pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
			}
			pageable = parenthesisEncapsulation(pageable);
			List<NgoMemberListProjection> memberList = new ArrayList<NgoMemberListProjection>();
			List<NacoBudgetAllocationDto> ngoMemberDtoList = new ArrayList<NacoBudgetAllocationDto>();
			Page<NgoMemberListProjection> memberPage = null;
			int actualCount = 0;
			Optional<List> memberListOptional = null;
				//	if(searchText == null || searchText == "") {
						memberPage = nacoBudgetAllocationRepository.findAllAllocatedBudgetByNACO(pageable);
						actualCount = nacoBudgetAllocationRepository.findAllAllocatedBudgetCount();
//					}else {																																																										
//						memberPage = nacoBudgetAllocationRepository.findAllGBMembersByFacilityIdSearch(searchText, pageable);
//						actualCount = nacoBudgetAllocationRepository.findGBCountByFacilityIdSearch(searchText);
//					}
			
			
			
				memberListOptional = Optional.ofNullable(memberPage.getContent());

			if (memberListOptional != null && memberListOptional.isPresent()) {
				memberList = memberListOptional.get();
			}
			
			ngoMemberDtoList = FacilityMapperUtil.mapListProjectionToBudgetAllocationDto(memberList);
			if (!ngoMemberDtoList.isEmpty()) {
				ngoMemberDtoList.get(0).setActualRecordCount(actualCount);
			}
			return ngoMemberDtoList;
		}
		
		// Method to change Active/Inactive Status
		public void changeProjectStatus(Long projectId, Boolean projectStatus) {			
			logger.debug("Entering into changeProjectStatus  method  - FacilityService");
			facilityRepository.changeProjectStatus(projectId, projectStatus);
		}
}
