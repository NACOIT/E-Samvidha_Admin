package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.DistrictDto;
import gov.naco.soch.dto.StateDto;
import gov.naco.soch.dto.SubdistrictDto;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.State;
import gov.naco.soch.entity.Subdistrict;
import gov.naco.soch.mapper.StateDistrictMapperUtil;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.StateRepository;
import gov.naco.soch.repository.SubdistrictRepository;

@Service
public class StateService {

	private static final Logger logger = LoggerFactory.getLogger(StateService.class);

	@Autowired
	StateRepository stateRepository;

	@Autowired
	private SubdistrictRepository subdistrictRepository;

	@Autowired
	FacilityRepository facilityRepository;

	// method to get all state and districts
	@Cacheable(value = "AllStateDistrictsCache")
	public List<StateDto> getAllStatesAndDistricts() {

		logger.debug("getAllStatesAndDistricts method called ");

		List<Object[]> stateObj = new ArrayList<>();
		stateObj = stateRepository.findAllStates();
		List<StateDto> stateDtoList = new ArrayList<>();

		for (StateDto stateDto : StateDistrictMapperUtil.mapStateObjToStateDto(stateObj)) {
			List<Object[]> districtObj = new ArrayList<>();
			districtObj = stateRepository.findAllDistricts(stateDto.getId());
			List<DistrictDto> districtDto = new ArrayList<>();
			districtDto = StateDistrictMapperUtil.mapDistrictObjToDistrictDto(districtObj);
			stateDto.setDistricts(districtDto);
			stateDtoList.add(stateDto);
		}
		logger.debug("getAllStatesAndDistricts method returns with parameters->{}", stateDtoList);
		return stateDtoList;
	}

	@Cacheable(value = "DistrictSubDistrictAndTownListCache")
	public List<SubdistrictDto> getSubDistrictAndTownList(Long districtId) {

		List<SubdistrictDto> subdistrictDtoList = new ArrayList<>();

		List<Subdistrict> subdistrictList = subdistrictRepository.findAllByDistrictId(districtId);
		subdistrictDtoList = StateDistrictMapperUtil.mapSubdistrictListToSubdistrictDtoList(subdistrictList);
		return subdistrictDtoList;
	}

	// @Cacheable(value = "AllSubDistrictAndTownListCache")
	// public List<SubdistrictDto> getSubDistrictAndTownList() {
	// List<SubdistrictDto> subdistrictDtoList = new ArrayList<>();
	// List<Subdistrict> subdistrictList = subdistrictRepository.findAll();
	// subdistrictDtoList =
	// StateDistrictMapperUtil.mapSubdistrictListToSubdistrictDtoList(subdistrictList);
	// return subdistrictDtoList;
	// }
	
	@Cacheable(value = "CurrentFacilityStateAndDistrictsCache")
	public StateDto getCurrentFacilityStateAndDistricts(Long facilityId) {
		StateDto stateDto = null;
		Facility facility = facilityRepository.findByIdAndIsDelete(facilityId, Boolean.FALSE);
		if (facility != null && facility.getAddress() != null && facility.getAddress().getState() != null) {
			State state = facility.getAddress().getState();
			stateDto = StateDistrictMapperUtil.mapStateToStateDto(state);
		}
		return stateDto;
	}

	@Cacheable(value = "StateAndDistrictsByFacilityCache")
	public StateDto getStateAndDistrictsByFacility(Long facilityId) {
		StateDto stateDto = null;
		Facility facility = facilityRepository.findFacilityByIdForState(facilityId);
		if (facility != null && facility.getAddress() != null && facility.getAddress().getState() != null) {
			State state = facility.getAddress().getState();
			stateDto = StateDistrictMapperUtil.mapStateToStateDto(state);
		}
		return stateDto;
	}

}
