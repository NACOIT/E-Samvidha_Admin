package gov.naco.soch.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import gov.naco.soch.admin.service.AddressService;
import gov.naco.soch.admin.service.DivisionService;
import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.admin.service.FacilityTypeService;
import gov.naco.soch.admin.service.RoleService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.DivisionDto;
import gov.naco.soch.dto.FacilityTypeDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.ProductDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.SacsFacilityDto;

//Address controller for State , District , Thaluk

@RestController
@RequestMapping("/address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController {

	@Autowired
	AddressService addressService;
	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	public AddressController() {
	}
	
	//API to list all states
	@GetMapping("/state")
	public @ResponseBody List<LocationDto> getStateList() {
		logger.debug("Entering into method getStateList()");
		List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
		locationDtoList = addressService.getStateList();
		return locationDtoList;
	}
	
	//API to list all districts by state_id
	@GetMapping("/districtbystateid")
	public @ResponseBody List<LocationDto> getDistrictList(@RequestParam Long stateId) {
		logger.debug("Entering into method getDistrictList()");
		List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
		locationDtoList = addressService.getDistrictList(stateId);
		return locationDtoList;
	}
	
	
	//API to list all sub districts by district_id
	@GetMapping("/subdistrictbydistrictid")
	public @ResponseBody List<LocationDto> getSubDistrictList(@RequestParam Long districtId) {
		logger.debug("Entering into method getSubDistrictList()");
		List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
		locationDtoList = addressService.getSubDistrictList(districtId);
		return locationDtoList;
	}
	
	//API to list all town by sub district id
	@GetMapping("/townbysubdistrictid")
	public @ResponseBody List<LocationDto> getTownList(@RequestParam Long subdistrictId) {
		logger.debug("Entering into method getTownList()");
		List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
		locationDtoList = addressService.getTownList(subdistrictId);
		return locationDtoList;
	}
	
	@Autowired
	private FacilityTypeService facilityTypeService;
	// API to get all details from ngoType table
	@GetMapping("/ngoTypeList")
	public @ResponseBody List<FacilityTypeDto> getFacilityTypes(){
		System.out.println("ngo address Controller");
		logger.debug("getNgoType method called");
		System.out.println("getNgoType method called===");
		List<FacilityTypeDto> ngoTypeList = facilityTypeService.getNGOType();
		return ngoTypeList;
	}
	
	// API to get all bank list
	@GetMapping("/bankList")
	public @ResponseBody List<FacilityTypeDto> getBankList(){
		logger.debug("getBankList method called");
		List<FacilityTypeDto> bankList = facilityTypeService.getBankList();
		return bankList;
	}
	
	@Autowired RoleService roleService;
	//API to get all roles of specified facility Type
	@GetMapping("/role/facilitytype/{facilityTypeId}")
	public @ResponseBody List<RoleDto> getRoleListByFacilityType(
			@PathVariable("facilityTypeId") Long facilityTypeId) {
		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
		roleDtoList = roleService.getRoleListByFacilityType(facilityTypeId);
		return roleDtoList;

	}
	
	@Autowired DivisionService divisionService;
	//API to get division of specified facility Type	
	@GetMapping("/division/facilitytype/{facilityTypeId}")
	public @ResponseBody Integer getDivisionByFacTypeId(@PathVariable("facilityTypeId") Long facilityTypeId) {
		logger.info("getDivisionByFacTypeId method called");
		return divisionService.getDivisionByFacTypeId(facilityTypeId);
	}
	
	@Autowired FacilityService facilityService;
	@PostMapping("/create/ngo")
	public @ResponseBody SacsFacilityDto addAnyNGO(@Valid  @RequestBody SacsFacilityDto sacsFacilityDto) {		
		System.out.println("/createngo");
		logger.debug("addAnyFacility method called with parameters");
	//	sacsFacilityDto.setFacilityNo(sacsFacilityDto.getFacilityNo() != null ? sacsFacilityDto.getFacilityNo().trim() : null);
		sacsFacilityDto.setName(sacsFacilityDto.getName() != null ? sacsFacilityDto.getName().trim() : null);
		System.out.println("/createngo-2");
//		if (sacsFacilityDto.getId() == null) {
			logger.debug("addFacilityFromSacs method called with parameters->{}", sacsFacilityDto);
			System.out.println("/createngo-3");
			sacsFacilityDto = facilityService.addAnyNGO(sacsFacilityDto);
			System.out.println("/createngo-4");
			logger.debug("addFacilityFromSacs method returns with parameters->{}", sacsFacilityDto);
//		} else {
//			logger.debug("editAnyFacilities method called with parameters->{}", sacsFacilityDto);
//			sacsFacilityDto = facilityService.editAnyFacilities(sacsFacilityDto);
//			logger.debug("editAnyFacilities method returns with parameters->{}", sacsFacilityDto);
//		}
		return sacsFacilityDto;
	}
	
	@PostMapping("/create/ngoMembers")
	public @ResponseBody ResponseEntity<SacsFacilityDto> ngoMembers(
			@Valid @RequestParam(name = "fileKeyImgCf", required = true) MultipartFile fileKeyImgCf,
			@RequestParam(name = "fileKeyIdCf", required = true) MultipartFile fileKeyIdCf,
			@RequestParam(name = "fileKeyImgpd", required = false) MultipartFile fileKeyImgpd,
			@RequestParam(name = "fileKeyIdpd", required = false) MultipartFile fileKeyIdpd,
			@RequestParam(name = "fileKeyImgpm", required = false) MultipartFile fileKeyImgpm,
			@RequestParam(name = "fileKeyIdpm", required = false) MultipartFile fileKeyIdpm,
			@RequestParam(name = "membersData", required = true) String membersData) throws IOException {
		SacsFacilityDto sacsFacilityDto = new SacsFacilityDto();
		ObjectMapper mapper = new ObjectMapper();
		try {
			sacsFacilityDto = mapper.readValue(membersData, SacsFacilityDto.class);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Entering into method saveNgoMembers with productDto->{}:", sacsFacilityDto);
		return new ResponseEntity<SacsFacilityDto>(facilityService.saveNgoMembers(sacsFacilityDto, fileKeyImgCf,fileKeyIdCf,fileKeyImgpd,fileKeyIdpd,fileKeyImgpm,fileKeyIdpm), HttpStatus.OK);

	}
	
}
