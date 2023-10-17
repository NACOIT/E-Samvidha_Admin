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
import gov.naco.soch.dto.NgoAcceptRejectDto;
import gov.naco.soch.dto.NgoDocumentsDto;
import gov.naco.soch.dto.ProductDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.dto.SecondaryTypologyDto;
import gov.naco.soch.dto.TypologyDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.dto.NgoMemberDto;
import gov.naco.soch.dto.NgoProjectsDto;
import gov.naco.soch.dto.NgoReleasedAmountDto;
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
import gov.naco.soch.entity.NgoAcceptRejectEntity;
import gov.naco.soch.entity.NgoBlackListEntity;
import gov.naco.soch.entity.NgoDocumentsEntity;
import gov.naco.soch.entity.NgoMember;
import gov.naco.soch.entity.NgoReleasedAmtEntity;
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
import gov.naco.soch.projection.NgoProjectListProjection;
import gov.naco.soch.projection.NgoSoeReleaseFundProjection;
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
import gov.naco.soch.repository.NgoAcceptRejectRepository;
import gov.naco.soch.repository.NgoBlackListRepository;
import gov.naco.soch.repository.NgoDocumentRepository;
import gov.naco.soch.repository.NgoGbRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.PincodeRepository;
import gov.naco.soch.repository.NgoMemberRepository;
import gov.naco.soch.repository.NgoProjectsRepository;
import gov.naco.soch.repository.NgoReleasedAmtRepository;
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
public class NgoSoeService {

	private static final Logger logger = LoggerFactory.getLogger(NgoSoeService.class);
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
	private NgoReleasedAmtRepository ngoReleasedAmtRepository;

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
	
	@Autowired
	private NgoProjectsRepository ngoProjectsRepository;
	
	private static final HashMap<String, Object> placeholderMap = new HashMap<>();

	private static final String REQUIRED_ERROR = "SACS is required to create laboratory";

	private static final String PERMISSION_ERROR = "You are not permitted to create a laboratory";
	
//	private static final String uploadPath = "http://localhost:4200\\assets\\documents";
	private static final String uploadPath = "\\assets\\documents";
	private String fileStorageLocation = null;
	private Path fileLocation = null;
	private String fileName;
		
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
	
	public List<NgoProjectsDto> getAllNgoProjectByFacility(Long facilityId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
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
		List<NgoProjectListProjection> projectList = new ArrayList<NgoProjectListProjection>();
		List<NgoProjectsDto> ngoProjectsDtoList = new ArrayList<NgoProjectsDto>();
		Page<NgoProjectListProjection> memberPage = null;
		int actualCount = 0;
		Optional<List> projectListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if(searchText == null || searchText == "") {
			System.out.println("Not-Inside ------searchText=========");
			memberPage = ngoProjectsRepository.findAllProjectsByFacilityId(facilityId, pageable);
			actualCount = ngoProjectsRepository.findCountByFacilityId(facilityId);
		}else {
			System.out.println("Inside ------searchText=========");
			memberPage = ngoProjectsRepository.findAllProjectsByFacilityIdSearch(facilityId,searchText, pageable);
			actualCount = ngoProjectsRepository.findCountByFacilityIdSearch(facilityId,searchText);
		}
		projectListOptional = Optional.ofNullable(memberPage.getContent());

		if (projectListOptional != null && projectListOptional.isPresent()) {
			projectList = projectListOptional.get();
		}
		
		ngoProjectsDtoList = FacilityMapperUtil.mapProjectListProjectionToFacilityDto(projectList);
		if (!ngoProjectsDtoList.isEmpty()) {
			ngoProjectsDtoList.get(0).setActualRecordCount(actualCount);
		}
		return ngoProjectsDtoList;
	}
	
	/**
	 * @param NgoReleasedAmountDto
	 * @return
	 */
	public NgoReleasedAmountDto addReleasedAmount(NgoReleasedAmountDto ngoReleasedAmountDto) {
		System.out.println("/addReleasedAmount");
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		int count = 0;
		// To check whether the facility name is already exist in table
//		count = facilityRepository.existsByOtherNameInAdd(ngoMemberDto.getStatus(),
//				ngoMemberDto.getFacilityId());
		System.out.println("/createngo-ngoMemberDto-1 ->"+ngoReleasedAmountDto.getFinancialYear());
		if (count != 0) {
			String errorfield = "Released Amount";
			// throwError(errorfield, ngoReleasedAmountDto.getStatus());
		} else {			
			NgoReleasedAmtEntity ngoReleasedAmtEntity = new NgoReleasedAmtEntity();
			ngoReleasedAmtEntity.setFacilityId(ngoReleasedAmountDto.getFacilityId());
			ngoReleasedAmtEntity.setProjectId(ngoReleasedAmountDto.getProjectId());
			ngoReleasedAmtEntity.setFinancialYear(ngoReleasedAmountDto.getFinancialYear());
			ngoReleasedAmtEntity.setReleasedAmount(ngoReleasedAmountDto.getReleasedAmount());
			ngoReleasedAmtEntity.setReleaseDate(ngoReleasedAmountDto.getReleaseDate());
			ngoReleasedAmtEntity.setRemarks(ngoReleasedAmountDto.getRemarks());
			ngoReleasedAmtEntity.setIsActive(true);
			ngoReleasedAmtEntity.setIsDelete(false);
			ngoReleasedAmtRepository.save(ngoReleasedAmtEntity);
			System.out.println("/NgoReleasedAmountDto addReleasedAmount==>"+ngoReleasedAmtEntity);

		}

		return ngoReleasedAmountDto;
	}
	
	public List<NgoReleasedAmountDto> getAllReleaseFundofProject(Long projectId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
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
		List<NgoSoeReleaseFundProjection> releaseFundList = new ArrayList<NgoSoeReleaseFundProjection>();
		List<NgoReleasedAmountDto> ngoReleasedAmountDtoList = new ArrayList<NgoReleasedAmountDto>();
		Page<NgoSoeReleaseFundProjection> memberPage = null;
		int actualCount = 0;
		Optional<List> memberListOptional = null;
	//	LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if(searchText == null || searchText == "") {
			memberPage = ngoReleasedAmtRepository.findAllReleaseFundByProjectId(projectId, pageable);
			actualCount = ngoReleasedAmtRepository.findCountByProjectId(projectId);
		}else {
			memberPage = ngoReleasedAmtRepository.findAllReleaseFundByProjectIdSearch(projectId,searchText, pageable);
			actualCount = ngoReleasedAmtRepository.findCountByProjectIdSearch(projectId,searchText);
		}
			memberListOptional = Optional.ofNullable(memberPage.getContent());

		if (memberListOptional != null && memberListOptional.isPresent()) {
			releaseFundList = memberListOptional.get();
		}
		
		ngoReleasedAmountDtoList = FacilityMapperUtil.mapReleaseFundListProjectionToProject(releaseFundList);
		if (!ngoReleasedAmountDtoList.isEmpty()) {
			ngoReleasedAmountDtoList.get(0).setActualRecordCount(actualCount);
		}
		return ngoReleasedAmountDtoList;
	}

}
