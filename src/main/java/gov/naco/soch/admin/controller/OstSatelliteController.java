package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.SacsFacilityDto;

@RestController
@RequestMapping("/ostsatellite")
public class OstSatelliteController {
	private static final Logger logger = LoggerFactory.getLogger(OstSatelliteController.class);

	@Autowired
	private FacilityService facilityService;

	public OstSatelliteController() {
	}

	/**
	 * Fetching list of ost Satellite based on parent ost center (Current login
	 * facility)
	 * 
	 * @return
	 */
	// @GetMapping("/list/byostcenter")
	// public @ResponseBody List<SacsFacilityDto> getOstSatelliteListByParentOst() {
	// Integer pageNumber = null;
	// Integer pageSize = null;
	// List<SacsFacilityDto> sacsFacilityDtoList =
	// facilityService.getFacilityListByParentAsCurrentFacility(
	// FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType(), pageNumber, pageSize);
	// return sacsFacilityDtoList;
	// }
	@GetMapping("/list/byostcenter")
	public @ResponseBody List<SacsFacilityDto> getOstSatelliteListByParentOst() {
		List<SacsFacilityDto> sacsFacilityDtoList = facilityService.getSatelliteOstListUnderCurrentFacility();
		return sacsFacilityDtoList;
	}

	/**
	 * Optimized List for Ost satellite facility
	 * 
	 * @return
	 */
	@GetMapping("/optimized/list")
	public @ResponseBody List<FacilityBasicListDto> getAllOptimizedFacilityByCurrentFacilityAsParent() {
		List<SacsFacilityDto> sacsFacilityDtoList = facilityService.getSatelliteOstListUnderCurrentFacility();
		List<FacilityBasicListDto> facilityBasicListDtoList = new ArrayList<>();
		for(SacsFacilityDto dto:sacsFacilityDtoList) {
			FacilityBasicListDto basicDto=new FacilityBasicListDto();
			basicDto.setId(dto.getId());
			basicDto.setName(dto.getName());
			basicDto.setCode(dto.getCode());
			facilityBasicListDtoList.add(basicDto);
		}
		// List<FacilityBasicListDto> facilityBasicListDtoList = facilityService
		// .getAllOptimizedFacilityByCurrentFacilityAsParent(FacilityTypeEnum.TI_SATELLITE_OST.getFacilityType());
		return facilityBasicListDtoList;
	}
}