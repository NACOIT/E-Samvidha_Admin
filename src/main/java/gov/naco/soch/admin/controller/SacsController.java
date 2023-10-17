package gov.naco.soch.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.admin.service.NgoSoeService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.NgoAcceptRejectDto;
import gov.naco.soch.dto.NgoMemberDto;
import gov.naco.soch.dto.NgoReleasedAmountDto;
import gov.naco.soch.dto.PrisonQuestionResultDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.dto.TypologyDto;
import gov.naco.soch.enums.FacilityTypeEnum;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/sacs")
public class SacsController {
	private static final Logger logger = LoggerFactory.getLogger(SacsController.class);
	private static final String uploadPath = "D:\\ngo";
	// private static final String uploadPath = "http://localhost:4200/assets/documents/";

	@Autowired
	private FacilityService facilityService;
	
	@Autowired
	private NgoSoeService ngoSoeService;

	public SacsController() {
	}

	// API to listing SACS
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_SACS + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_SACS + "') or hasAuthority('" + AdminAccessCodes.NACO_ME_SACS + "')")
	public @ResponseBody List<SacsFacilityDto> getAllSacsList(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<Long> facilityTypeId = new ArrayList<Long>();
		facilityTypeId.add((long) 2); // SACS facility Type Id
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getFacilityByFacilityTypeOptimized(facilityTypeId, pageNumber, pageSize,
				sortBy, sortType);
		return sacsFacilityDtoList;
	}

	// API to add/edit details to facility table
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_ADMIN_SACS + "') or hasAuthority('"
			+ AdminAccessCodes.DIVISION_ADMIN_SACS + "') or hasAuthority('" + AdminAccessCodes.NACO_ME_SACS + "')")
	public @ResponseBody SacsFacilityDto addSacs(@Valid @RequestBody SacsFacilityDto sacsFacilityDto) {
		sacsFacilityDto.setName(sacsFacilityDto.getName() != null ? sacsFacilityDto.getName().trim() : null);
		if (sacsFacilityDto.getId() == null) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> inside>>>>>>>>>>>>>>>>>>");
			logger.debug("addFacilityFromSacs method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.addAnyFacilities(sacsFacilityDto);
			logger.debug("addFacilityFromSacs method returns with parameters->{}", sacsFacilityDto);
		} else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> else-side>>>>>>>>>>>>>>>>>>");
			logger.debug("editAnyFacilities method called with parameters->{}", sacsFacilityDto);
			sacsFacilityDto = facilityService.editAnyFacilities(sacsFacilityDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", sacsFacilityDto);
		}
		return sacsFacilityDto;

	}

	@DeleteMapping("/delete/{facilityId}")
	public SacsFacilityDto deleteSacs(@PathVariable("facilityId") Long facilityId) {
		logger.info("deleteFacility method called with facilityId" + facilityId);
		return facilityService.deleteFacility(facilityId);
	}

	/**
	 * Advance search for the SACS. Search criteria:
	 * facilityname,code,username,mobilenumber,email
	 * 
	 * @param searchValues
	 * @return
	 */
	@GetMapping("/advancesearch")
	public @ResponseBody List<SacsFacilityDto> advanceSearchForFacilities(
			@RequestParam Map<String, String> searchValues, @RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortType) {
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		List<Long> facilityTypeIds = new ArrayList<>();
		facilityTypeIds.add(FacilityTypeEnum.SACS.getFacilityType());
		sacsFacilityDtoList = facilityService.advanceSearchForFacilities(searchValues, facilityTypeIds, pageNumber,
				pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}
	
	// API to add/edit details to facility table
	@PostMapping("/rejectNgo")
	public @ResponseBody NgoAcceptRejectDto rejectNgo(@Valid @RequestBody NgoAcceptRejectDto ngoAcceptRejectDto) {
		System.out.println("==============================NgoAcceptRejectDto======================================");
		ngoAcceptRejectDto.setRemarks(ngoAcceptRejectDto.getRemarks() != null ? ngoAcceptRejectDto.getRemarks().trim() : null);
//		if (ngoAcceptRejectDto.getId() == null) {
			logger.debug("rejectNgo method called with parameters->{}", ngoAcceptRejectDto);
			ngoAcceptRejectDto = facilityService.addAcceptRejectDetails(ngoAcceptRejectDto);
			logger.debug("rejectNgo method returns with parameters->{}", ngoAcceptRejectDto);
//		} else {
//			logger.debug("rejectNgo method called with parameters->{}", ngoAcceptRejectDto);
//			ngoAcceptRejectDto = facilityService.editAcceptRejectDetails(ngoAcceptRejectDto);
//			logger.debug("rejectNgo method returns with parameters->{}", ngoAcceptRejectDto);
//		}
		return ngoAcceptRejectDto;

	}
	
	// API to add/edit details to facility table
	@PostMapping("/approveNgo")
	public @ResponseBody ResponseEntity<NgoAcceptRejectDto> approveNgo(@Valid @RequestParam(name = "approvalDetails", required = true) String approvalDetails) {
		
		System.out.println("approveNGO=======================");
	//	@RequestParam(name = "fileKey", required = false) MultipartFile file,
		
		NgoAcceptRejectDto ngoAcceptRejectDto = new NgoAcceptRejectDto();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // Added to remove LocalDate format mapping issue
		try {
			ngoAcceptRejectDto = mapper.readValue(approvalDetails, NgoAcceptRejectDto.class);
			mapper.readValue(approvalDetails,NgoAcceptRejectDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Entering into method saveProduct with ngoAcceptRejectDto->{}:", ngoAcceptRejectDto);
		ngoAcceptRejectDto.setRemarks(ngoAcceptRejectDto.getRemarks() != null ? ngoAcceptRejectDto.getRemarks().trim() : null);
		System.out.println("Before addAcceptDocument Function=================================================="+ngoAcceptRejectDto);
	//	ngoAcceptRejectDto = facilityService.addAcceptDocument(ngoAcceptRejectDto, file);
		
		System.out.println("==============================NgoAcceptRejectDto======================================");
	//	return new ResponseEntity<NgoAcceptRejectDto>(facilityService.addAcceptDocument(ngoAcceptRejectDto, file), HttpStatus.OK);
		return new ResponseEntity<NgoAcceptRejectDto>(facilityService.addAcceptDocument(ngoAcceptRejectDto), HttpStatus.OK);
		
	//	return ngoAcceptRejectDto;

	}
	
	@GetMapping("/approveRejectList/{facilityId}")
	public @ResponseBody List<NgoAcceptRejectDto> approveRejectList(@PathVariable("facilityId") Long facilityId) {
		logger.debug("approveRejectList method called");
		return facilityService.approveRejectList(facilityId, null);
		
	}
	
	// API to add/edit details to facility table
	@PostMapping("/blackListNgo")
	public @ResponseBody NgoAcceptRejectDto blackListNgo(@Valid @RequestBody NgoAcceptRejectDto ngoAcceptRejectDto) {
		System.out.println("==============================blackListNgo======================================");
		ngoAcceptRejectDto.setRemarks(ngoAcceptRejectDto.getRemarks() != null ? ngoAcceptRejectDto.getRemarks().trim() : null);
		logger.debug("blackListNgo method called with parameters->{}", ngoAcceptRejectDto);
		ngoAcceptRejectDto = facilityService.addBlackListDetails(ngoAcceptRejectDto);
		logger.debug("blackListNgo method returns with parameters->{}", ngoAcceptRejectDto);

		return ngoAcceptRejectDto;

	}
	
	/**
	 * Create and Edit Released Amount to NGO/CBO Body Details
	 * 
	 * @param sacsFacilityDto
	 * @return 
	 */
	@PostMapping("/save/releasedAmount")
	public @ResponseBody NgoReleasedAmountDto addReleasedAmount(@Valid @RequestBody NgoReleasedAmountDto ngoReleasedAmountDto) {
		ngoReleasedAmountDto.setReleasedAmount(ngoReleasedAmountDto.getReleasedAmount() != null ? ngoReleasedAmountDto.getReleasedAmount().trim() : "0");
		ngoReleasedAmountDto.setRemarks(ngoReleasedAmountDto.getRemarks() != null ? ngoReleasedAmountDto.getRemarks().trim() : "--");
		
		if (ngoReleasedAmountDto.getId() == null) {
			logger.debug("addReleasedAmount method called with parameters->{}", ngoReleasedAmountDto);
			ngoReleasedAmountDto = ngoSoeService.addReleasedAmount(ngoReleasedAmountDto);
			logger.debug("addReleasedAmount method returns with parameters->{}", ngoReleasedAmountDto);
		} else {
			logger.debug("editAnyFacilities method called with parameters->{}", ngoReleasedAmountDto);
			//ngoMemberDto = facilityService.editGBDetails(ngoMemberDto);
			logger.debug("editAnyFacilities method returns with parameters->{}", ngoReleasedAmountDto);
		}
		return ngoReleasedAmountDto;

	}

}
