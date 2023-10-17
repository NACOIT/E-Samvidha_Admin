package gov.naco.soch.admin.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.projection.FacilityDispatchHistoryProjection;
import gov.naco.soch.projection.FacilityProjection;
import gov.naco.soch.repository.FacilityDispatchRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.util.UserUtils;

@Transactional
@Service
public class TransporterService {
	private static final Logger logger = LoggerFactory.getLogger(TransporterService.class);

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private FacilityService facilityService;

	@Autowired
	private FacilityDispatchRepository facilityDispatchRepository;

	public List<FacilityBasicListDto> getAllTransporterListBySacs() {
		logger.info("in getAllTransporterListBySacs()");
		Long sacsId = 0L;
		List<FacilityBasicListDto> transporterDtos = new ArrayList<FacilityBasicListDto>();
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		if (currentUser.getFacilityTypeId().equals(FacilityTypeEnum.SACS.getFacilityType())) {
			sacsId = currentUser.getFacilityId();
		} else {
			FacilityProjection facility = facilityRepository.findFacilityName(currentUser.getFacilityId());
			sacsId = facility.getSacsId();
		}
		if (sacsId != null && !sacsId.equals(0L)) {
			List<FacilityProjection> transporters = facilityRepository.getTransportersBySacs(sacsId);
			transporters.forEach(transporter -> {
				FacilityBasicListDto transporterDto = new FacilityBasicListDto();
				transporterDto.setId(transporter.getId());
				transporterDto.setName(transporter.getName());
				transporterDtos.add(transporterDto);
			});
		}

		return transporterDtos;
	}

	public List<SacsFacilityDto> getAllTransporterListWithstns(Integer pageNumber, Integer pageSize, String sortBy,
			String sortType, LocalDate startDate, LocalDate endDate) {
		List<Long> facilityTypeId = new ArrayList<Long>();
		facilityTypeId.add(FacilityTypeEnum.TRANSPORTER.getFacilityType()); // Transporter facility Type Id
		List<SacsFacilityDto> sacsDtos = facilityService.getFacilityByFacilityTypeOptimized(facilityTypeId, pageNumber,
				pageSize, sortBy, sortType);
		List<Long> facilityIds = sacsDtos.stream().map(f -> f.getId()).collect(Collectors.toList());
		List<FacilityDispatchHistoryProjection> transporterDetails = facilityDispatchRepository
				.findStnNumbersByTransporterIds(facilityIds, startDate, endDate);
		Map<Long, List<FacilityDispatchHistoryProjection>> transporterMap = transporterDetails.stream()
				.collect(Collectors.groupingBy(FacilityDispatchHistoryProjection::getTransporterId));

		for (SacsFacilityDto sacs : sacsDtos) {
			List<FacilityDispatchHistoryProjection> stnNumbersById = transporterMap.get(sacs.getId());
			if (!CollectionUtils.isEmpty(stnNumbersById)) {
				List<String> stns = stnNumbersById.stream().map(f -> f.getStnNumber()).collect(Collectors.toList());
				sacs.setStnNumbers(stns);
			}
		}
		return sacsDtos;
	}

}
