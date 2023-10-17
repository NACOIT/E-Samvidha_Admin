package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gov.naco.soch.constants.IndicatorConstant;
import gov.naco.soch.constructordto.FacilityPerformanceDto;
import gov.naco.soch.dto.FacilityPerformanceData;
import gov.naco.soch.dto.FacilityPerformanceResultDto;
import gov.naco.soch.dto.StateDto;
import gov.naco.soch.entity.District;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.mapper.FacilityPerformanceDataMapper;
import gov.naco.soch.projection.DailyDosageRegistrationProjection;
import gov.naco.soch.projection.FacilityPerformanceDetailProjection;
import gov.naco.soch.projection.UserDetailsProjection;
import gov.naco.soch.repository.DailyDosageReportRepository;
import gov.naco.soch.repository.DistrictRepository;
import gov.naco.soch.repository.FacilityPerformanceRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.UserMasterRepository;

@Service
public class FacilityPerformanceService {

	@Autowired
	FacilityPerformanceRepository facilityPerformanceRepository;
	@Autowired
	private DailyDosageReportRepository dailyDosageReportRepository;
	@Autowired
	UserMasterRepository userMasterRepository;
	@Autowired
	FacilityPerformanceExcelReport excelReport;
	@Autowired
	FacilityRepository facilityRepository;
	@Autowired
	StateService stateService;
	@Autowired
	DistrictRepository districtRepository;

	public List<FacilityPerformanceResultDto> getPerformanceData(String requestType, String sectionId) {
		List<FacilityPerformanceResultDto> facilityPerformanceFinalList = new ArrayList<FacilityPerformanceResultDto>();
		FacilityPerformanceResultDto facilityPerformanceResultDtos = new FacilityPerformanceResultDto();
		List<FacilityPerformanceDto> facilityPerformaneDtoList = facilityPerformanceRepository
				.findConsolidatedPerfromanceData(requestType, sectionId);
		/* Convert the query fetched data to Map with section ID as the key -[START] */
		if (facilityPerformaneDtoList != null && facilityPerformaneDtoList.size() > 0) {
			sectionId = facilityPerformaneDtoList.get(0).getSectionId();
			facilityPerformanceResultDtos.setSectionId(sectionId);
			facilityPerformanceResultDtos.setSectionDesc(FacilityPerformanceService.getSectionDataAsJson(sectionId));
			List<FacilityPerformanceDto> tempfacPerformList = new ArrayList<FacilityPerformanceDto>();

			for (FacilityPerformanceDto iterateFacilityPerformanceDto : facilityPerformaneDtoList) {
				FacilityPerformanceDto tempFacilityPerformObj = new FacilityPerformanceDto();
				tempFacilityPerformObj.setIndicatorId(iterateFacilityPerformanceDto.getIndicatorId());
				tempFacilityPerformObj.setIdicatorDesc(FacilityPerformanceService
						.getIndicatorDataAsJson(iterateFacilityPerformanceDto.getIndicatorId(), "description"));
				tempFacilityPerformObj.setMaleCount(iterateFacilityPerformanceDto.getMaleCount());
				tempFacilityPerformObj.setFemaleCount(iterateFacilityPerformanceDto.getFemaleCount());
				tempFacilityPerformObj.setTotalCount(iterateFacilityPerformanceDto.getTotalCount());
				tempFacilityPerformObj.setSlNo(FacilityPerformanceService
						.getIndicatorDataAsJson(iterateFacilityPerformanceDto.getIndicatorId(), "sNo"));
				tempFacilityPerformObj.setSourceDesc(FacilityPerformanceService
						.getIndicatorDataAsJson(iterateFacilityPerformanceDto.getIndicatorId(), "source"));

				tempfacPerformList.add(tempFacilityPerformObj);
			}
			facilityPerformanceResultDtos.setIndicatorList(tempfacPerformList);
			facilityPerformanceFinalList.add(facilityPerformanceResultDtos);
			if (requestType.equalsIgnoreCase(IndicatorConstant.REQUEST_TYPE_INIT)) {

				for (Map.Entry<String, String> entry : IndicatorConstant.INDICATOR_SECTION_DESC_MAP.entrySet()) {
					if (!entry.getKey().equalsIgnoreCase(sectionId)) {
						FacilityPerformanceResultDto performanceResultDto = new FacilityPerformanceResultDto();
						performanceResultDto.setSectionId(entry.getKey());
						performanceResultDto.setSectionDesc(entry.getValue());
						facilityPerformanceFinalList.add(performanceResultDto);
					}
				}
			}

		}
		/* Convert the query fetched data to Map with section ID as the key -[END] */

		return facilityPerformanceFinalList;

	}

	public ByteArrayInputStream exportExcelData(Long facilityId, String sectionId, Long userId) {
		FacilityPerformanceDetailProjection facility = facilityRepository.getFacilityById(facilityId);
		DailyDosageRegistrationProjection registrationDetails = dailyDosageReportRepository
				.getRegistrationCounts(facilityId);
		UserDetailsProjection userData = userMasterRepository.getUserDetailById(userId);
		FacilityPerformanceData facilityPerformanceData = FacilityPerformanceDataMapper
				.mapReportStaticHeaderData(facility, registrationDetails, userData);
		List<FacilityPerformanceResultDto> performanceResultDtos = getPerformanceData("CLICK", sectionId);
		ByteArrayInputStream byteArrayInputStream = excelReport.exportExcelData(performanceResultDtos,
				facilityPerformanceData);

		return byteArrayInputStream;

	}

	public static String getIndicatorDataAsJson(String key, String property) {
		String finalString = "";
		Map<String, String> indicatorMap = IndicatorConstant.INDICATOR_DESC_MAP;
		String indicatorObj = "";
		if (indicatorMap.containsKey(key)) {
			indicatorObj = indicatorMap.get(key);
		}
		if (indicatorObj != null && !indicatorObj.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(indicatorObj).getAsJsonObject();
			JsonElement jsonElement = jsonObject.get(property);
			finalString = jsonElement != null ? jsonElement.getAsString() : finalString;
		}
		return finalString;

	}

	public static String getSectionDataAsJson(String key) {
		String finalString = "";
		Map<String, String> sectionMap = IndicatorConstant.INDICATOR_SECTION_DESC_MAP;
		if (sectionMap.containsKey(key)) {
			finalString = sectionMap.get(key);
		}

		return finalString;

	}

	public List<StateDto> getAllStatesAndDistricts() {
		List<StateDto> stateDtos = new ArrayList<StateDto>();
		stateDtos = stateService.getAllStatesAndDistricts();

		return stateDtos;

	}

	public List<Facility> findFacilitiesByDistrictId(Long districtId) {

		List<Facility> facilities = facilityRepository.findFacilitiesByDistrictId(districtId);

		return facilities;

	}

	public FacilityPerformanceData getFacilityPerformanceData(Long facilityId, Long userId) {
		FacilityPerformanceData facilityPerformanceData = new FacilityPerformanceData();
		try {
			FacilityPerformanceDetailProjection facility = facilityRepository.getFacilityById(facilityId);
			DailyDosageRegistrationProjection registrationDetails = dailyDosageReportRepository
					.getRegistrationCounts(facilityId);
			UserDetailsProjection userData = userMasterRepository.getUserDetailById(userId);
			facilityPerformanceData = FacilityPerformanceDataMapper.mapReportStaticHeaderData(facility,
					registrationDetails, userData);
		} catch (Exception e) {
			return facilityPerformanceData;
		}

		return facilityPerformanceData;

	}

	public List<District> findByStateId(Long stateId) {
		List<District> districtsList = new ArrayList<District>();
		districtsList = districtRepository.findByStateId(stateId);
		return districtsList;

	}

}
