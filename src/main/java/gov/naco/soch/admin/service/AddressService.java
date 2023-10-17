package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.entity.District;
import gov.naco.soch.entity.DistrictReadOnly;
import gov.naco.soch.entity.State;
import gov.naco.soch.entity.StateReadOnly;
import gov.naco.soch.entity.SubDistrictReadOnly;
import gov.naco.soch.entity.Subdistrict;
import gov.naco.soch.entity.Town;
import gov.naco.soch.entity.TownReadOnly;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.repository.DistrictReadOnlyRepository;
import gov.naco.soch.repository.DistrictRepository;
import gov.naco.soch.repository.StateReadOnlyRepository;
import gov.naco.soch.repository.StateRepository;
import gov.naco.soch.repository.SubdistrictReadOnlyRepository;
import gov.naco.soch.repository.SubdistrictRepository;
import gov.naco.soch.repository.TownReadOnlyRepository;
import gov.naco.soch.repository.TownRespository;

//service for address controller
@Service
@Transactional
public class AddressService {

	@Autowired
	private StateReadOnlyRepository stateReadOnlyRepository;

	@Autowired
	private DistrictReadOnlyRepository districtReadOnlyRepository;

	@Autowired
	private SubdistrictReadOnlyRepository subdistrictReadOnlyRepository;

	@Autowired
	private TownReadOnlyRepository townReadOnlyRepository;

	@Cacheable(value = "AllStateListCache")
	public List<LocationDto> getStateList() {

		List<StateReadOnly> stateList = new ArrayList<StateReadOnly>();
		stateList = stateReadOnlyRepository.findByIsDelete();
		List<LocationDto> locationDtolist = new ArrayList<LocationDto>();
		if (!stateList.isEmpty()) {
			stateList.forEach(row -> {
				LocationDto locationDto = new LocationDto();
				locationDto.setId(row.getId());
				locationDto.setName(row.getName());
				locationDto.setCode(row.getAlternateName());
				locationDtolist.add(locationDto);
			});
		}
		return locationDtolist;
	}
	
	@Cacheable(value = "AllDistrictListByStateCache")
	public List<LocationDto> getDistrictList(Long stateId) {
		List<LocationDto> locationDtolist = new ArrayList<LocationDto>();
		if (stateId == null) {
			throwError("District selection needs State");
		} else {
			List<DistrictReadOnly> districtList = new ArrayList<DistrictReadOnly>();
			districtList = districtReadOnlyRepository.findByStateIdAndIsDeleteAndIsActive(stateId);
			if (!districtList.isEmpty()) {
				districtList.forEach(row -> {
					LocationDto locationDto = new LocationDto();
					locationDto.setId(row.getId());
					locationDto.setName(row.getName());
					locationDto.setCode(row.getAlternateName());
					locationDtolist.add(locationDto);
				});
			}

		}
		return locationDtolist;
	}
	@Cacheable(value = "AllSubDistrictListByDistrictCache")
	public List<LocationDto> getSubDistrictList(Long districtId) {
		List<SubDistrictReadOnly> subdistrictList = new ArrayList<SubDistrictReadOnly>();
		List<LocationDto> locationDtolist = new ArrayList<LocationDto>();
		if (districtId == null) {
			throwError("Subdistrict selection needs District");
		} else {
		subdistrictList = subdistrictReadOnlyRepository.findByDistrictIdAndIsDeleteAndIsActive(districtId);
		if (!subdistrictList.isEmpty()) {
			subdistrictList.forEach(row -> {
				LocationDto locationDto = new LocationDto();
				locationDto.setId(row.getSubdistrictId());
				locationDto.setName(row.getSubdistrictName());
				//locationDto.setCode(row.getAlternateName());
				locationDtolist.add(locationDto);
			});
		}
		}
		return locationDtolist;
	}
	@Cacheable(value = "AllTownListBySubDistrictCache")
	public List<LocationDto> getTownList(Long subdistrictId) {
		List<TownReadOnly> townList = new ArrayList<TownReadOnly>();
		List<LocationDto> locationDtolist = new ArrayList<LocationDto>();
		if (subdistrictId == null) {
			throwError("Town selection needs Subdistrict");
		} else {
		townList = townReadOnlyRepository.findBySubDistrictIdAndIsDeleteAndIsActive(subdistrictId);
		
		if (!townList.isEmpty()) {
			townList.forEach(row -> {
				LocationDto locationDto = new LocationDto();
				locationDto.setId(row.getTownId());
				locationDto.setName(row.getTownName());
				locationDto.setCode(row.getTownCode());
				locationDtolist.add(locationDto);
			});
		}
		}
		return locationDtolist;
	}

	/**
	 * Method to throw error in case of validation errors
	 * 
	 * @param errorfield
	 * @param errorFieldValue
	 */
	private void throwError(String errorMessage) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();

		if (errorMessage != null) {
			errorDto.setDescription(errorMessage);
			errorDtoList.add(errorDto);
			detailsSimplified.add(errorDto.getDescription());
			ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
			throw new ServiceException(errorMessage, errorResponse, HttpStatus.BAD_REQUEST);
		}

	}

}
