package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.mapper.FacilityMapperUtil;
import gov.naco.soch.projection.FacilityListProjection;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.util.UserUtils;

@Transactional
@Service
public class SupplierService {

	private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

	@Value("${exportRecordsLimit}")
	private Integer exportRecordsLimit;

	@Autowired
	FacilityRepository facilityRepository;

	public List<SacsFacilityDto> getSupplierList(List<Long> facilityTypeId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		LoginResponseDto currentLogin = UserUtils.getLoggedInUserDetails();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		// For using alias name if sorting field name like (noa) then only use alias
		// name
		pageable = parenthesisEncapsulation(pageable);
		Page<FacilityListProjection> facilityListPage = null;
		Optional<List> facilityListOptional = null;
		List<FacilityListProjection> facilityProjectList = new ArrayList<>();
		int actualRecordCount = 0;
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		if (currentLogin != null
				&& currentLogin.getFacilityTypeId() == FacilityTypeEnum.PROCUREMENT_AGENT.getFacilityType()) {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdInAndProcurementAgentId(facilityTypeId,
					currentLogin.getFacilityId());
			facilityListPage = facilityRepository.findSupplierListForProcurementAgent(
					FacilityTypeEnum.SUPPLIER.getFacilityType(), currentLogin.getFacilityId(), pageable);
			facilityListOptional = Optional.ofNullable(facilityListPage.getContent());
		} else {
			actualRecordCount = facilityRepository.CountByFacilityTypeIdIn(facilityTypeId);
			facilityListPage = facilityRepository.findSupplierList(FacilityTypeEnum.SUPPLIER.getFacilityType(),
					pageable);
			facilityListOptional = Optional.ofNullable(facilityListPage.getContent());
		}
		if (facilityListOptional.isPresent()) {
			facilityProjectList = facilityListOptional.get();
		}

		sacsFacilityDtoList = FacilityMapperUtil.mapFacilityListProjectionToSacsfacilityDto(facilityProjectList);
		if (!sacsFacilityDtoList.isEmpty()) {
			sacsFacilityDtoList.get(0).setActualRecordCount(actualRecordCount);
		}
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

}
