package gov.naco.soch.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.admin.service.NgoSoeService;
import gov.naco.soch.admin.service.NgoDocumentService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.constant.FileUploadConstants;
import gov.naco.soch.dto.DistrictFacilityDto;
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.FacilityDto;
import gov.naco.soch.dto.FacilityListByDistrictAndFacilityTypeDTO;
import gov.naco.soch.dto.FacilityRequestDto;
import gov.naco.soch.dto.NacoBudgetAllocationDto;
import gov.naco.soch.dto.NgoDocumentsDto;
import gov.naco.soch.dto.NgoMemberDto;
import gov.naco.soch.dto.NgoProjectsDto;
import gov.naco.soch.dto.NgoReleasedAmountDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.dto.SecondaryTypologyDto;
import gov.naco.soch.dto.TypologyDto;
import gov.naco.soch.projection.FacilityDetailedProjection;
import gov.naco.soch.projection.FacilityDetailsProjectionForMobile;
import gov.naco.soch.util.CommonConstants;

@RestController
@RequestMapping("/facility")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacilityController {

	private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);

	@Autowired
	private FacilityService facilityService;
	
	@Autowired
	private NgoDocumentService ngoDocumentService;
	
	@Autowired
	private NgoSoeService ngoSoeService;

	@Autowired
	private Environment env;

	public FacilityController() {
	}

	// API to add details to facility table
	@PostMapping("/add")
	public @ResponseBody FacilityDto addFacility(@Valid @RequestBody FacilityDto facilityDto) {
		logger.debug("addFacility method called with parameters->{}", facilityDto);
		facilityService.addFacility(facilityDto);
		logger.debug("addFacility method returns with parameters->{}", facilityDto);
		return facilityDto;
	}

	// API to get all details from facility table
	@GetMapping("/list")
	public @ResponseBody List<FacilityDto> getAllFacilities(@RequestParam(required = false) List<Long> divisionIds,
			@RequestParam(required = false) Long stateId) {		
		logger.debug("getAllFacilities method is invoked");
		return facilityService.getAllFacilities(divisionIds, stateId);
	}

	/**
	 * Optimized Facility list based on
	 * 
	 * @param divisionIds
	 * @param stateId
	 * @param facilityTypeId
	 * @return FacilityBasicListDto
	 */
	@GetMapping("/listby")
	public @ResponseBody List<FacilityBasicListDto> getFacilities(
			@RequestParam(required = false) List<Long> divisionIds, @RequestParam(required = false) Long stateId,
			@RequestParam(required = false) Long facilityTypeId,
			@RequestParam(required = false) List<Long> facilityTypeIds,
			@RequestParam(required = false) String facilityName, @RequestParam(required = false) Long sacsId,
			@RequestParam(required = false) Integer limit, @RequestParam(defaultValue = "false") String isExternal,
			@RequestParam(required = false) Long parentId) {
		logger.debug("getAllFacilities method is invoked");
		return facilityService.getFacilities(divisionIds, stateId, facilityTypeId, facilityTypeIds, facilityName,
				sacsId, limit, isExternal, parentId);
	}

	// API to retrieve facilities based on division and facility type

	/**
	 * Optimized
	 * 
	 * @param divisionId
	 * @param facilityTypeId
	 * @return
	 */
	@GetMapping("/get/{divisionId}/{facilityTypeId}")
	public @ResponseBody List<FacilityRequestDto> getFacility(@PathVariable("divisionId") Long divisionId,
			@PathVariable("facilityTypeId") Long facilityTypeId) {
		logger.debug("getFacility method called with division and facility type id as ", divisionId, facilityTypeId);
		List<FacilityRequestDto> facilities = facilityService.getFacilities(divisionId, facilityTypeId);
		logger.debug("getFacility method returns with parameters->{}", facilities);
		return facilities;
	}

	// Bugfix 14-02-2020

	//
	/**
	 * Optimized API to get facilities mapped to a lab
	 * 
	 * @param labId
	 * @return
	 */
	@GetMapping("/mappedtolab/{labId}")
	public @ResponseBody List<FacilityDto> getFacilitiesMappedToLab(@PathVariable("labId") Long labId) {
		logger.info("getAllFacilities method is invoked");
		return facilityService.getFacilitiesMappedToLab(labId);
	}

	// API to get all details from facility table
	@GetMapping("/labs")
	public @ResponseBody List<FacilityDto> getLabs() {
		logger.debug("getAllFacilities method is invoked");
		return facilityService.getLabs();
	}

	// API to map facilities to a lab
	@PostMapping("/labs/{labId}/mapToFacility")
	public @ResponseBody List<FacilityDto> mapLabToFacility(@PathVariable("labId") Long labId,
			@RequestBody List<FacilityDto> facilities) {
		logger.debug("getAllFacilities method is invoked");
		return facilityService.mapLabToFacility(labId, facilities);
	}

	// API to get all details from facility table
	@GetMapping("/localfacilities/{facilityId}")
	public @ResponseBody DistrictFacilityDto getLocalFacilites(@PathVariable("facilityId") Long facilityId,
			@RequestParam(required = true) List<Long> divisionIds) {
		logger.debug("getLocalFacilites method is invoked");
		return facilityService.getLocalFacilites(facilityId, divisionIds);
	}

	// API to list all active facilities created by particular SACS
	// @parameter sacsId
	// only IsActive = TRUE and Is_Delete = FALSE

	@GetMapping("/{sacsId}/list")
	public @ResponseBody List<FacilityDto> getFacilitySacs(@PathVariable("sacsId") Long sacsId) {
		List<FacilityDto> facilityDtoList = new ArrayList<FacilityDto>();
		facilityDtoList = facilityService.getFacilitySacs(sacsId);
		return facilityDtoList;
	}

	// API to list all facilities(SACS)
	@GetMapping("/list/sacs")
	public @ResponseBody List<FacilityDto> getSacsList() {
		List<FacilityDto> facilityDtoList = new ArrayList<FacilityDto>();
		facilityDtoList = facilityService.getSacsList();
		return facilityDtoList;

	}	

	/**
	 * From here to down latest API for all facility functionalities
	 */

	/**
	 * Create and Edit Facility Asjad
	 * 
	 * @param sacsFacilityDto
	 * @return 
	 */
	@PostMapping("/create/facilities")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_FACILITY + "') or hasAuthority('"
			+ AdminAccessCodes.NACO_PROJECTDIRECTOR_FACILITIES + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_FACILITY + "') or hasAuthority('" + AdminAccessCodes.SACS_FACILITY
			+ "') or hasAuthority('" + AdminAccessCodes.ICTC_FACILITIES + "')or hasAuthority('"
			+ AdminAccessCodes.TI_NGO_PROFILE + "')")
	public @ResponseBody SacsFacilityDto addAnyFacility(@Valid @RequestBody SacsFacilityDto sacsFacilityDto) {
		sacsFacilityDto.setFacilityNo(sacsFacilityDto.getFacilityNo() != null ? sacsFacilityDto.getFacilityNo().trim() : null);
		sacsFacilityDto.setName(sacsFacilityDto.getName() != null ? sacsFacilityDto.getName().trim() : null);
		if (sacsFacilityDto.getId() == null) {
			logger.debug("addFacilityFromSacs method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.addAnyFacilities(sacsFacilityDto);
			logger.debug("addFacilityFromSacs method returns with parameters->{}", sacsFacilityDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.editAnyFacilities(sacsFacilityDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", sacsFacilityDto);
		}
		return sacsFacilityDto;

	}

	/**
	 * API to list all active facilities created by particular SACS only Is_Delete =
	 * FALSE
	 * 
	 * @param sacsId
	 * @return
	 */

	@GetMapping("list/{sacsId}")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_FACILITY + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_FACILITY + "') or hasAuthority('" + AdminAccessCodes.SACS_FACILITY
			+ "')")
	public @ResponseBody List<SacsFacilityDto> getAllFacilityBySacs(@PathVariable("sacsId") Long sacsId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", sacsId);
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getAllFacilityBySacs(sacsId,searchText,false, pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}
	
	/**
	 * API to list all new facilities created by particular SACS only Is_Delete =
	 * FALSE
	 * 
	 * @param sacsId
	 * @return
	 */
	@GetMapping("newList/{sacsId}")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_FACILITY + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_FACILITY + "') or hasAuthority('" + AdminAccessCodes.SACS_FACILITY
			+ "')")
	public @ResponseBody List<SacsFacilityDto> getAllNewFacilityBySacs(@PathVariable("sacsId") Long sacsId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllNewFacilityBySacs method called with parameters->{}", sacsId);
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getAllFacilityBySacs(sacsId,searchText,true, pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;

	}

	/**
	 * API to list all active facilities created by particular parent facility only
	 * Is_Delete = FALSE
	 * 
	 * @param parentFacilityId
	 * @return
	 */
	@GetMapping("list/parent/{parentFacilityId}")
	public @ResponseBody List<SacsFacilityDto> getAllFacilityByParent(
			@PathVariable("parentFacilityId") Long parentFacilityId) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getAllFacilityByParentId(parentFacilityId);
		return sacsFacilityDtoList;

	}

	/**
	 * Delete Facility
	 * 
	 * @param facilityId
	 * @return
	 */
	@DeleteMapping("/delete/{facilityId}")
	public SacsFacilityDto deleteFacility(@PathVariable("facilityId") Long facilityId) {
		logger.info("deleteFacility method called with facilityId" + facilityId);
		return facilityService.deleteFacility(facilityId);
	}

	/**
	 * Find facility by facility ID
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/findby/{facilityId}")
	public @ResponseBody SacsFacilityDto getFacilityByFacilityId(@PathVariable("facilityId") Long facilityId) {
		System.out.println("/findby/{facilityId} function Invoked");
		SacsFacilityDto sacsFacilityDto = new SacsFacilityDto();
		sacsFacilityDto = facilityService.getFacilityByFacilityId(facilityId);
		System.out.println("Return after process /findby/{facilityId}");
		System.out.println(sacsFacilityDto);
		return sacsFacilityDto;
	}

	/**
	 * Find facility list based on facilityType
	 * 
	 * @param facilityTypeId
	 * @return
	 */
	@GetMapping("/findby/facilityTypeId/{facilityTypeId}/andsacs")
	public @ResponseBody List<SacsFacilityDto> getFacilityByFacilityTypeIdAndSacs(
			@PathVariable("facilityTypeId") Long facilityTypeId) {
		List<SacsFacilityDto> sacsFacilityDtos = facilityService.getFacilityByFacilityTypeIdAndSacs(facilityTypeId);
		return sacsFacilityDtos;
	}

	@GetMapping("/ticenter/list/underIdu")
	public @ResponseBody List<SacsFacilityDto> getTiCenterByIduAndSacs() {
		List<SacsFacilityDto> sacsFacilityDtos = facilityService.getTiCenterByIduAndSacs();
		return sacsFacilityDtos;
	}

	/**
	 * Find typology list by facility id
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/typology/list/byfacility/{facilityId}")
	public @ResponseBody List<TypologyDto> getTypologyListByFacility(@PathVariable("facilityId") Long facilityId) {
		List<TypologyDto> typologyDtoList = new ArrayList<TypologyDto>();
		typologyDtoList = facilityService.getTypologyListByFacilityId(facilityId);
		return typologyDtoList;
	}

	/**
	 * Find all typology list
	 * 
	 * @return
	 */
	@GetMapping("/typology/list")
	public @ResponseBody List<TypologyDto> getTypologyList() {
		List<TypologyDto> typologyDtoList = new ArrayList<TypologyDto>();
		typologyDtoList = facilityService.getAllTypology();
		return typologyDtoList;
	}

	/**
	 * Find list of Parent ost center
	 * 
	 * @return
	 */
	@GetMapping("/parentostcenter/list")
	public @ResponseBody List<SacsFacilityDto> getParentOstCenterList() {
		List<SacsFacilityDto> sacsFacilityDtos = facilityService.getParentOstCenterList();
		return sacsFacilityDtos;
	}

	/**
	 * Dangerous API: http://localhost:8080/admin/facility/listbyfacilitytype/15 API
	 * to list all India ART based on facility type id Instead of this use :
	 * http://localhost:8080/admin/facility/listby?facilityTypeId=15
	 * 
	 * @param facilityType
	 * @return
	 */
	// @GetMapping("/listbyfacilitytype/{facilityTypeId}")
	// public @ResponseBody List<SacsFacilityDto> getAllArt(@PathVariable Long
	// facilityTypeId) {
	// List<Long> facilityType = new ArrayList<Long>();
	// facilityType.add((facilityTypeId));
	// List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
	// Integer pageNumber = null;
	// Integer pageSize = null;
	// sacsFacilityDtoList = facilityService.getFacilityByFacilityType(facilityType,
	// pageNumber, pageSize);
	// return sacsFacilityDtoList;
	// }

	/**
	 * Fetch facility list based on district id (Mandatory) and facility type id
	 * (Not Mandatory) api as query parameter
	 * 
	 * @param district
	 * @param facilityType
	 * @return
	 */
	@GetMapping("/getFacilityListByDistrict")
	public @ResponseBody List<FacilityListByDistrictAndFacilityTypeDTO> getFacilityByDistrictAndFacilityType(
			@RequestParam Long district, @RequestParam(required = false) Long facilityType) {
		List<FacilityListByDistrictAndFacilityTypeDTO> FacilityList = facilityService
				.getFacilityByDistrictAndFacilityType(district, facilityType);
		return FacilityList;
	}

	/**
	 * Advance search for the facilities created by sacs login Search criteria:
	 * facilityname,code,username,mobilenumber,email
	 * 
	 * @param searchValues
	 * @return
	 */
	@GetMapping("/createdbysacs/advancesearch")
	public @ResponseBody List<SacsFacilityDto> advanceSearchForFacilities(
			@RequestParam Map<String, String> searchValues, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		List<Long> facilityTypeIds = null;
		sacsFacilityDtoList = facilityService.advanceSearchForFacilities(searchValues, facilityTypeIds, pageNumber,
				pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}

	/**
	 * Normal search Query parameters searchvalue: contain value to search.
	 * facilitytype: for determine which facility screen. division: for determine
	 * api call from laboratory screen or not
	 * 
	 * @param searchDetails
	 * @return
	 */
	@GetMapping("/normalsearch")
	public @ResponseBody List<SacsFacilityDto> normalSearchForFacilities(
			@RequestParam Map<String, String> searchDetails, @RequestParam(required = false) Integer facilityId, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.normalSearchForFacilities(searchDetails, pageNumber, pageSize, sortBy,
				sortType);
		return sacsFacilityDtoList;
	}

	@GetMapping("/scheduler/reminderForContractExpiryDate")
	public @ResponseBody List<FacilityDto> reminderForContractExpiryDate(String accessKey) {
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		} else {
			logger.warn("JOB--> reminderforcontractexpirydate job started by API");
			List<FacilityDto> facilities = facilityService.reminderForContractExpiryDate();
			logger.warn("JOB--> reminderforcontractexpirydate job started by API ended");
			return facilities;
		}
	}

	/**
	 * for fetching the ictc name
	 * 
	 */
	@GetMapping("mobile/listby")
	public @ResponseBody List<FacilityDetailsProjectionForMobile> getFacilitiesForMobile(
			@RequestParam(required = true)Long facilityTypeId,
			@RequestParam(required = false)String searchParam,
			@RequestParam(required = true)int pageNumber,
			@RequestParam(required = true)int pageSize) {
		logger.debug("getFacilitiesForMobile method is invoked");
		return facilityService.getFacilitiesForMobile(facilityTypeId, searchParam, pageNumber, pageSize);
	}

	/**
	 * for fetching parent facility by facilityId
	 * 
	 */
	@GetMapping("mobile/{facilityId}/parent-facility")
	public @ResponseBody FacilityDetailsProjectionForMobile getParentFacilityDetailForMobile(
			@PathVariable Long facilityId) {
		logger.debug("getParentFacilityDetailForMobile method is invoked");
		return facilityService.getParentFacilityDetailForMobile(facilityId);
	}

	@GetMapping("mobile/typology/list/secondary")
	public @ResponseBody List<SecondaryTypologyDto> getSecondaryTypologyList() {
		List<SecondaryTypologyDto> secondaryTypologyDtoList = new ArrayList<SecondaryTypologyDto>();
		secondaryTypologyDtoList = facilityService.getSecondaryTypologyList();
		return secondaryTypologyDtoList;
	}

	@GetMapping("/typology/mobile/list/byUserId/{userId}")
	public @ResponseBody List<TypologyDto> getTypologyListByUserIdForMobile(@PathVariable("userId") Long userId) {
		List<TypologyDto> typologyDtoList = new ArrayList<TypologyDto>();
		typologyDtoList = facilityService.getTypologyListByUserIdForMobile(userId);
		return typologyDtoList;
	}	
	
	/**
	 * for fetching parent facility(Sacs) by stateId and districtId
	 * 
	 */
	@GetMapping("/sacsId")
	public @ResponseBody FacilityDetailedProjection getSacsIdByStateDistrict(@RequestParam Integer stateId,@RequestParam Integer districtId) {
		logger.debug("getSacsIdByStateDistrict method is invoked");
		return facilityService.getSacsIdByStateDistrict(stateId,districtId);
	}
	
	/**
	 * API to list all NGO Members created by particular Facility 
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/ngoMember/list/{facilityId}")
	public @ResponseBody List<NgoMemberDto> getAllMemberByFacility(@PathVariable("facilityId") Long facilityId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", facilityId);
		List<NgoMemberDto> ngoMemberDtoList = new ArrayList<NgoMemberDto>();
		ngoMemberDtoList = facilityService.getAllMemberByFacility(facilityId,searchText, pageNumber, pageSize, sortBy, sortType);
		return ngoMemberDtoList;

	}
	
	/**
	 * Change Verification Status
	 * 
	 * @param updateVerificationStatus
	 * @return
	 */
	@PostMapping("/recordVerificationStatus/{recordId}")
	public boolean updateVerificationStatus(@PathVariable("recordId") Long recordId,@RequestParam Integer recordStatus,@RequestParam String recordType) {
		System.out.println("updateVerificationStatus==FacilityController");
		logger.info("VerificationStatus method called with memberId" + recordId);
		ngoDocumentService.updateVerificationStatus(recordId,recordStatus,recordType);
		return true;
	}
	
	/**
	 * Change Members Status
	 * 
	 * @param memberStatus
	 * @return
	 */
	@PostMapping("/memberStatus/{memberId}")
	public boolean  memberStatus(@PathVariable("memberId") Long memberId,@RequestParam Boolean memberStatus) {
		logger.info("memberStatus method called with memberId" + memberId);
		facilityService.changeMemberStatus(memberId,memberStatus);
		return true;
	}
	/**
	 * Change Verification Status
	 * 
	 * @param updateVerificationStatus
	 * @return
	 */
	@DeleteMapping("/deleteDocuments/{recordId}")
	public boolean deleteDocuments(@PathVariable("recordId") Long recordId,@RequestParam String recordType) {
		System.out.println("deleteDocuments==FacilityController");
		logger.info("deleteDocuments method called with recordId" + recordId);
		ngoDocumentService.deleteDocuments(recordId,recordType);
		return true;
	}
	/**
	 * Change Members Status
	 * 
	 * @param memberStatus
	 * @return
	 */
	@PostMapping("/ngoCboStatus/{ngoId}")
	public boolean  ngoCboStatus(@PathVariable("ngoId") Long ngoId,@RequestParam Boolean ngoStatus) {
		logger.info("ngoCboStatus method called with ngoId" + ngoId);
		facilityService.changeNgoCboStatus(ngoId,ngoStatus);
		return true;
	}
	/**
	 * API to Add Documents
	 *
	 * @param facilityId
	 * @return
	 */
	@PostMapping("/uploadDocuments")
	public @ResponseBody ResponseEntity<NgoDocumentsDto> saveNgoDocuments(
			@Valid @RequestParam(name = "fileKey", required = true) MultipartFile fileKey,
			@RequestParam(name = "additionalData", required = true) String additionalData) throws IOException {
		NgoDocumentsDto ngoDocumentsDto = new NgoDocumentsDto();
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<NgoDocumentsDto> returnValue = null;
		
		try {
			ngoDocumentsDto = mapper.readValue(additionalData, NgoDocumentsDto.class);
			logger.debug("Entering into method saveNgoDocuments with NgoDocumentsDto->{}:", ngoDocumentsDto);
			
			System.out.println("***********************uploadDocuments*********************"+ngoDocumentsDto.getFolderName());
			
			if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.registration_Certificate)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadRegCert(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.return_File_Certificate)){				
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadReturnFile(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.activity_Report)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadActivityReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.fcra_Registration)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadFcra(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.audit_Report)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadAuditReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.tax_Registration)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadTaxReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.contract_Letter)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadContractLetter(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.darpan_Certificate)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadDarpanCertificate(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.other_Document)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadOtherDocuments(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.annual_Report)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadAnnualReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.soe_Document)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadSoeDocuments(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.utilization_Certificate)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadUcDocuments(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.sacs_Audit_Report)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadSacsAuditReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			else if(ngoDocumentsDto.getFolderName().contains(FileUploadConstants.sacs_Annual_Report)){
				returnValue = new ResponseEntity<NgoDocumentsDto>(ngoDocumentService.uploadSacsAnnualReport(ngoDocumentsDto, fileKey), HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		System.out.println("***********************uploadDocuments-- End*********************");
		return returnValue;
	}
	/**
	 * API to list all Uploaded Certificate
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/uploadedDocuments/list/{facilityId}")
	public @ResponseBody List<NgoDocumentsDto> getAllRegistrationCertByFacility(@PathVariable("facilityId") Long facilityId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType,@RequestParam(required = true) String docType) {
		System.out.println("==docType==============/uploadedDocuments/list/");
		logger.debug("getAllRegistrationCertByFacility method called with parameters->{}", facilityId);
		List<NgoDocumentsDto> ngoDocumentsDtoList = new ArrayList<NgoDocumentsDto>();		
		ngoDocumentsDtoList = ngoDocumentService.getAllUploasedDocumentsByFacility(facilityId,searchText, pageNumber, pageSize, sortBy, sortType,docType);
				
		return ngoDocumentsDtoList;

	}
	/**
	 * API to Add Documents
	 *
	 * @param facilityId
	 * @return
	 */
	@PostMapping("/addNgoProject")
	public @ResponseBody ResponseEntity<NgoProjectsDto> saveNgoProject(
			@Valid @RequestParam(name = "fileKey", required = true) MultipartFile fileKey,
			@RequestParam(name = "additionalData", required = true) String additionalData) throws IOException {
		NgoProjectsDto ngoProjectsDto = new NgoProjectsDto();
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<NgoProjectsDto> returnValue = null;	

		try {
			ngoProjectsDto = mapper.readValue(additionalData, NgoProjectsDto.class);
			logger.debug("Entering into method saveNgoDocuments with NgoDocumentsDto->{}:", ngoProjectsDto);
			
			System.out.println("***********************uploadDocuments*********************"+ngoProjectsDto.getFolderName()+"=="+FileUploadConstants.sanction_Letter);
			if(ngoProjectsDto.getFolderName().contains(FileUploadConstants.sanction_Letter)){
				System.out.println("==Folder--Name===>"+ngoProjectsDto.getFolderName());
				returnValue = new ResponseEntity<NgoProjectsDto>(ngoDocumentService.uploadSanctionLetter(ngoProjectsDto, fileKey), HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		System.out.println("***********************uploadDocuments-- End*********************");
		return returnValue;
	}
	
	/**
	 * Create and Edit Governance Body Details
	 * 
	 * @param sacsFacilityDto
	 * @return 
	 */
	@PostMapping("/save/gbDetails")
	public @ResponseBody NgoMemberDto addGBDetails(@Valid @RequestBody NgoMemberDto ngoMemberDto) {
		ngoMemberDto.setFirstname(ngoMemberDto.getFirstname() != null ? ngoMemberDto.getFirstname().trim() : null);
		ngoMemberDto.setMobileNumber(ngoMemberDto.getMobileNumber() != null ? ngoMemberDto.getMobileNumber().trim() : null);
		ngoMemberDto.setEmail(ngoMemberDto.getEmail() != null ? ngoMemberDto.getEmail().trim() : null);
		ngoMemberDto.setEducation(ngoMemberDto.getEducation() != null ? ngoMemberDto.getEducation().trim() : null);
		
		if (ngoMemberDto.getId() == null) {
			logger.debug("addFacilityFromSacs method called with parameters->{}", ngoMemberDto);
			ngoMemberDto = facilityService.addGBDetails(ngoMemberDto);
			logger.debug("addFacilityFromSacs method returns with parameters->{}", ngoMemberDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", ngoMemberDto);
			//ngoMemberDto = facilityService.editGBDetails(ngoMemberDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", ngoMemberDto);
		}
		return ngoMemberDto;

	}
	
	/**
	 * API to list all NGO Members created by particular Facility 
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/gbMember/list/{facilityId}")
	public @ResponseBody List<NgoMemberDto> getAllGBMemberByFacility(@PathVariable("facilityId") Long facilityId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", facilityId);
		List<NgoMemberDto> ngoMemberDtoList = new ArrayList<NgoMemberDto>();
		ngoMemberDtoList = facilityService.getAllGBMemberByFacility(facilityId,searchText, pageNumber, pageSize, sortBy, sortType);
		return ngoMemberDtoList;

	}
	/**
	 * Change GB Status
	 * 
	 * @param gbStatus
	 * @return
	 */
	@PostMapping("/gbStatus/{gbId}")
	public boolean  gbStatus(@PathVariable("gbId") Long gbId,@RequestParam Boolean gbStatus) {
		logger.info("gbStatus method called with gbId" + gbId);
		facilityService.changeGbStatus(gbId,gbStatus);
		return true;
	}
	
	/**
	 * Delete GB Details
	 * 
	 * @param facilityId
	 * @return
	 */
	@DeleteMapping("gbDetails/delete/{gbId}")
	public boolean deleteGbDetails(@PathVariable("gbId") Long gbId) {
		logger.info("deleteGbDetails method called with facilityId" + gbId);
		facilityService.deleteGbDetails(gbId);
		return true;
	}
	/**
	 * API to list all active facilities created by particular SACS only Is_Delete =
	 * FALSE
	 * 
	 * @param sacsId
	 * @return
	 */
	@GetMapping("blacklist/{sacsId}")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_FACILITY + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_FACILITY + "') or hasAuthority('" + AdminAccessCodes.SACS_FACILITY
			+ "')")
	public @ResponseBody List<SacsFacilityDto> getBlackListNGOPaginate(@PathVariable("sacsId") Long sacsId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", sacsId);
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getBlackListNGOPaginate(sacsId,searchText, pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;

	}
	
	/**
	 * Create and Edit SACS Officials Details
	 * 
	 * @param sacsFacilityDto
	 * @return 
	 */
	@PostMapping("/add/sacsOfficials")
	public @ResponseBody NgoMemberDto saveSacsOfficials(@Valid @RequestBody NgoMemberDto ngoMemberDto) {
		ngoMemberDto.setFirstname(ngoMemberDto.getFirstname() != null ? ngoMemberDto.getFirstname().trim() : null);
		ngoMemberDto.setMobileNumber(ngoMemberDto.getMobileNumber() != null ? ngoMemberDto.getMobileNumber().trim() : null);
		ngoMemberDto.setEmail(ngoMemberDto.getEmail() != null ? ngoMemberDto.getEmail().trim() : null);
		ngoMemberDto.setEducation(ngoMemberDto.getEducation() != null ? ngoMemberDto.getEducation().trim() : null);
		
		if (ngoMemberDto.getId() == null) {
			logger.debug("addFacilityFromSacs method called with parameters->{}", ngoMemberDto);
			ngoMemberDto = facilityService.addGBDetails(ngoMemberDto);
			logger.debug("addFacilityFromSacs method returns with parameters->{}", ngoMemberDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", ngoMemberDto);
			//ngoMemberDto = facilityService.editGBDetails(ngoMemberDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", ngoMemberDto);
		}
		return ngoMemberDto;

	}
	
	/**
	 * API to list all Projects created by particular NGO/CBO 
	 * 
	 * @param facilityId
	 * @return
	 */
	@GetMapping("/ngoProject/list/{facilityId}")
	public @ResponseBody List<NgoProjectsDto> getAllNgoProjectByFacility(@PathVariable("facilityId") Long facilityId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", facilityId);
		List<NgoProjectsDto> ngoProjectsDtoList = new ArrayList<NgoProjectsDto>();
		ngoProjectsDtoList = ngoSoeService.getAllNgoProjectByFacility(facilityId,searchText, pageNumber, pageSize, sortBy, sortType);
		return ngoProjectsDtoList;

	}
	
	/**
	 * Update Values for NGO/CBO
	 * 
	 * @param updateNgoCbo
	 * @return
	 */
	@PostMapping("/updateNgo/{facilityId}")
	public boolean updateNgoCbo(@PathVariable("facilityId") Long facilityId,@RequestParam String workingsince,@RequestParam String facilityLandLineNumber,@RequestParam String facilityEmailId,@RequestParam String addressLineOne,@RequestParam String addressLineTwo,@RequestParam Integer districtId,@RequestParam Integer subDistrictId,@RequestParam Integer townId,@RequestParam String pincode) {
		System.out.println("updateNgoCbo >>> FacilityController");
		logger.info("updateNgoCbo method called with facilityId" + facilityId);
		facilityService.updateNgoCbo(facilityId,workingsince,facilityLandLineNumber,facilityEmailId,addressLineOne,addressLineTwo,districtId,subDistrictId,townId,pincode);
		return true;
	}
	
	/**
	 * Create and Edit Budget Details
	 *  
	 * @param NacoBudgetAllocationDto
	 * @return 
	 */
	@PostMapping("/save/budgetAllocationByNACO")
	public @ResponseBody NacoBudgetAllocationDto addBudgetAllocationByNACO(@Valid @RequestBody NacoBudgetAllocationDto nacoBudgetAllocationDto) {
		nacoBudgetAllocationDto.setApprovedBudget(nacoBudgetAllocationDto.getApprovedBudget() != null ? nacoBudgetAllocationDto.getApprovedBudget().trim() : null);
		nacoBudgetAllocationDto.setComments(nacoBudgetAllocationDto.getComments() != null ? nacoBudgetAllocationDto.getComments().trim() : null);
		
		
		if (nacoBudgetAllocationDto.getId() == null) {
			logger.debug("addBudgetAllocationByNACO method called with parameters->{}", nacoBudgetAllocationDto);
			nacoBudgetAllocationDto = facilityService.addBudgetAllocationByNACO(nacoBudgetAllocationDto);
			logger.debug("addBudgetAllocationByNACO method returns with parameters->{}", nacoBudgetAllocationDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", nacoBudgetAllocationDto);
			//nacoBudgetAllocationDto = facilityService.editGBDetails(nacoBudgetAllocationDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", nacoBudgetAllocationDto);
		}
		return nacoBudgetAllocationDto;

	}
	
	/**
	 * API to list all Allocated Budgets created by NACO 
	 * 
	 * @param 
	 * @return
	 */
	@GetMapping("/allocatedBugetByNaco/list")
	public @ResponseBody List<NacoBudgetAllocationDto> getAllAllocatedBugetByNaco(@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllAllocatedBugetByNaco method called with parameters->{}", searchText);
		List<NacoBudgetAllocationDto> nacoBudgetAllocationList = new ArrayList<NacoBudgetAllocationDto>();
		nacoBudgetAllocationList = facilityService.getAllAllocatedBugetByNaco(searchText, pageNumber, pageSize, sortBy, sortType);
		return nacoBudgetAllocationList;
	}
	
	/**
	 * Change Project Status
	 * 
	 * @param projectStatus
	 * @return
	 */
	@PostMapping("/projectStatus/{projectId}")
	public boolean  projectStatus(@PathVariable("projectId") Long projectId,@RequestParam Boolean projectStatus) {
		logger.info("projectStatus method called with projectId" + projectId);
		facilityService.changeProjectStatus(projectId,projectStatus);
		return true;
	}
	
	/**
	 * API to list all Released Fund of particular Project 
	 * 
	 * @param projectId
	 * @return
	 */
	@GetMapping("/getReleaseFund/list/{projectId}")
	public @ResponseBody List<NgoReleasedAmountDto> getReleaseFund(@PathVariable("projectId") Long projectId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getReleaseFund method called with parameters->{}", projectId);
		List<NgoReleasedAmountDto> ngoReleasedAmountDtoList = new ArrayList<NgoReleasedAmountDto>();
		ngoReleasedAmountDtoList = ngoSoeService.getAllReleaseFundofProject(projectId,searchText, pageNumber, pageSize, sortBy, sortType);
		return ngoReleasedAmountDtoList;

	}
	
}
