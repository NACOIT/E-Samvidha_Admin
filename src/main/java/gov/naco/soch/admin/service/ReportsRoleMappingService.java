package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.dto.PageReportsRoleMappingDto;
import gov.naco.soch.admin.dto.ReportsRoleMappingDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.ReportRoleDivisionDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.entity.Division;
import gov.naco.soch.entity.GlobalReportsDivisionMapping;
import gov.naco.soch.entity.MasterReport;
import gov.naco.soch.entity.ReportsRoleMapping;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.repository.GlobalReportsDivisionMappingRepository;
import gov.naco.soch.repository.ReportsRoleMappingRepository;

@Service
@Transactional
public class ReportsRoleMappingService {

	@Autowired
	ReportsRoleMappingRepository reportsRoleMappingRepository;

	@Autowired
	GlobalReportsDivisionMappingRepository globalReportsDivisionMappingRepository;

	public PageReportsRoleMappingDto getAllReportsRoleMappingData(int pageIndex, int pageSize, String sortBy,
			String sortType) {
		PageReportsRoleMappingDto pageReportsRoleMappingDto = new PageReportsRoleMappingDto();
		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		List<ReportsRoleMapping> reportsRoleMappingList = new ArrayList<ReportsRoleMapping>();
		Pageable paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).ascending());
		}
		paginateObject = parenthesisEncapsulation(paginateObject);
		Page<ReportsRoleMapping> pageReportRoleList = reportsRoleMappingRepository.findList(paginateObject);
		// Page<ReportsRoleMapping> pageReportRoleList =
		// reportsRoleMappingRepository.findAll(paginateObject);
		if (pageReportRoleList != null && (!pageReportRoleList.isEmpty())) {
			reportsRoleMappingList = pageReportRoleList.getContent();
			if (pageIndex == 0) {
				Long totalResultCount = pageReportRoleList.getTotalElements();
				pageReportsRoleMappingDto.setTotalRecordCount(totalResultCount);
			}
		}
		for (ReportsRoleMapping reportsRoleMapping : reportsRoleMappingList) {
			reportsRoleMappingDtos.add(convertReportsRoleMappingData(reportsRoleMapping));
		}
		pageReportsRoleMappingDto.setReportsRoleMappingDtos(reportsRoleMappingDtos);
		return pageReportsRoleMappingDto;
	}

	public List<ReportsRoleMappingDto> getAllReportsRoleMappingByReportId(Long reportId) {
		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();

		List<ReportsRoleMapping> reportsRoleMappingList = new ArrayList<ReportsRoleMapping>();
		reportsRoleMappingList = reportsRoleMappingRepository.findByMasterReport_ReportId(reportId);
		for (ReportsRoleMapping reportsRoleMapping : reportsRoleMappingList) {
			reportsRoleMappingDtos.add(convertReportsRoleMappingData(reportsRoleMapping));
		}
		return reportsRoleMappingDtos;
	}

	public ReportsRoleMappingDto getAllReportsRoleMappingDataById(Long mappingId) {
		ReportsRoleMappingDto reportsRoleMappingDto = new ReportsRoleMappingDto();
		Optional<ReportsRoleMapping> reportsRoleMapping = reportsRoleMappingRepository.findById(mappingId);
		if (reportsRoleMapping != null && reportsRoleMapping.isPresent()) {
			reportsRoleMappingDto = convertReportsRoleMappingData(reportsRoleMapping.get());
		}
		return reportsRoleMappingDto;
	}

	public List<ReportsRoleMappingDto> bulkSaveReportsAndRoleMapping(List<ReportsRoleMapping> reportsRoleMappings,
			Long reportId, List<Long> divisions) {
		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		List<ReportsRoleMapping> reportsRoleMappingList = new ArrayList<ReportsRoleMapping>();
		reportsRoleMappingList = reportsRoleMappingRepository.saveAll(reportsRoleMappings);
		if (reportsRoleMappingList != null && reportsRoleMappingList.size() > 0) {
			for (ReportsRoleMapping reportsRoleMapping : reportsRoleMappingList) {
				reportsRoleMappingDtos.add(convertReportsRoleMappingData(reportsRoleMapping));
			}
		}
		Boolean checker = reportsRoleMappingDtos.stream()
				.filter(data -> data.getRoleId() == RoleEnum.DIVISION_ADMIN.getRole()).findFirst().isPresent();
		if (divisions != null && !divisions.isEmpty() && reportId != null && reportId != 0
				&& reportsRoleMappingDtos != null && checker) {
			List<GlobalReportsDivisionMapping> reportsDivisionList = new ArrayList<GlobalReportsDivisionMapping>();
			for (Long division : divisions) {
				reportsDivisionList.add(convertGlobalReportsDivisionMappingData(division, reportId));
			}
			globalReportsDivisionMappingRepository.saveAll(reportsDivisionList);
		}
		return reportsRoleMappingDtos;
	}

	public List<ReportsRoleMappingDto> bulkUpdateReportsAndRoleMapping(List<ReportsRoleMapping> reportsRoleMappings,
			Long reportId, List<Long> divisions) {
		reportsRoleMappingRepository.deleteByMasterReport_ReportId(reportId);
		globalReportsDivisionMappingRepository.deleteByReportId(reportId);
		List<ReportsRoleMappingDto> reportsRoleMappingDtos = new ArrayList<ReportsRoleMappingDto>();
		List<ReportsRoleMapping> reportsRoleMappingList = new ArrayList<ReportsRoleMapping>();
		reportsRoleMappingList = reportsRoleMappingRepository.saveAll(reportsRoleMappings);
		if (reportsRoleMappingList != null && reportsRoleMappingList.size() > 0) {
			for (ReportsRoleMapping reportsRoleMapping : reportsRoleMappingList) {
				reportsRoleMappingDtos.add(convertReportsRoleMappingData(reportsRoleMapping));
			}
		}

		Boolean checker = reportsRoleMappingDtos.stream()
				.filter(data -> data.getRoleId() == RoleEnum.DIVISION_ADMIN.getRole()).findFirst().isPresent();
		if (divisions != null && !divisions.isEmpty() && reportId != null && reportId != 0
				&& reportsRoleMappingDtos != null && checker) {
			List<GlobalReportsDivisionMapping> reportsDivisionList = new ArrayList<GlobalReportsDivisionMapping>();
			for (Long division : divisions) {
				reportsDivisionList.add(convertGlobalReportsDivisionMappingData(division, reportId));
			}
			globalReportsDivisionMappingRepository.saveAll(reportsDivisionList);
		}
		return reportsRoleMappingDtos;
	}

	static ReportsRoleMappingDto convertReportsRoleMappingData(ReportsRoleMapping reportsRoleMapping) {
		ReportsRoleMappingDto reportsRoleMappingDto = new ReportsRoleMappingDto();
		reportsRoleMappingDto.setMappingId(reportsRoleMapping.getMappingId());
		reportsRoleMappingDto.setReportId(reportsRoleMapping.getMasterReport().getReportId());
		reportsRoleMappingDto.setReportName(reportsRoleMapping.getMasterReport().getReportname());
		reportsRoleMappingDto.setSubModuleId(reportsRoleMapping.getSubModule().getSubModuleId());
		reportsRoleMappingDto.setSubModuleName(reportsRoleMapping.getSubModule().getSubModuleName());
		reportsRoleMappingDto.setModuleId(reportsRoleMapping.getReportsModule().getModuleId());
		reportsRoleMappingDto.setModuleName(reportsRoleMapping.getReportsModule().getModuleName());
		reportsRoleMappingDto.setRoleId(reportsRoleMapping.getRole().getId());
		reportsRoleMappingDto.setRoleName(reportsRoleMapping.getRole().getName());
		reportsRoleMappingDto.setUrl(reportsRoleMapping.getMasterReport().getUrl());
		if (reportsRoleMapping.getCreatedUser() != null) {
			reportsRoleMappingDto.setCreatedUserId(reportsRoleMapping.getCreatedUser().getId());
			reportsRoleMappingDto.setCreatedUserName(reportsRoleMapping.getCreatedUser().getFirstname());
		}
		reportsRoleMappingDto.setCreatedTime(reportsRoleMapping.getCreatedDate());
		reportsRoleMappingDto.setModifiedTime(reportsRoleMapping.getModifiedDate());
		if (reportsRoleMapping.getMasterReport() != null
				&& reportsRoleMapping.getMasterReport().getGlobalReportsDivisionMappings() != null
				&& !reportsRoleMapping.getMasterReport().getGlobalReportsDivisionMappings().isEmpty()) {
			List<MasterDto> reportDivisions = new ArrayList<>();
			reportsRoleMapping.getMasterReport().getGlobalReportsDivisionMappings().forEach(data -> {
				MasterDto reportDivision = new MasterDto();
				if (data != null && data.getDivision() != null) {
					reportDivision.setId(data.getDivision().getId());
					reportDivision.setName(data.getDivision().getName());
					reportDivisions.add(reportDivision);
				}
			});
			reportsRoleMappingDto.setReportDivisions(reportDivisions);
		}
		return reportsRoleMappingDto;
	}

	private GlobalReportsDivisionMapping convertGlobalReportsDivisionMappingData(Long divisionId, Long reportId) {
		GlobalReportsDivisionMapping mappingData = new GlobalReportsDivisionMapping();
		Division divisionEntity = new Division();
		MasterReport masterReport = new MasterReport();
		divisionEntity.setId(divisionId);
		masterReport.setReportId(reportId);
		mappingData.setDivision(divisionEntity);
		mappingData.setMasterReport(masterReport);
		mappingData.setIsActive(true);
		mappingData.setIsDelete(false);
		return mappingData;
	}

	public ReportRoleDivisionDto getRoleDivisionListByReportId(Long reportId) {
		List<Object[]> roleList = reportsRoleMappingRepository.findRoleListByReportId(reportId);
		List<Object[]> DivisionList = globalReportsDivisionMappingRepository.findDivisionListByReportId(reportId);
		ReportRoleDivisionDto reportRoleDivisionDto = new ReportRoleDivisionDto();
		reportRoleDivisionDto.setRoleList(convertReportsRoleToDtoList(roleList));
		reportRoleDivisionDto.setDivisionList(convertReportsDivisionToDtoList(DivisionList));
		return reportRoleDivisionDto;
	}

	static List<RoleDto> convertReportsRoleToDtoList(List<Object[]> roleList) {
		List<RoleDto> roleDtoList = new ArrayList<>();
		if (roleList != null && !roleList.isEmpty()) {
			roleDtoList = roleList.stream().map(object -> {
				RoleDto role = new RoleDto();
				role.setId(object[0] != null ? Long.valueOf(object[0].toString()) : null);
				role.setName(object[1] != null ? object[1].toString() : null);
				return role;
			}).collect(Collectors.toList());
		}
		return roleDtoList;
	}

	static List<MasterDto> convertReportsDivisionToDtoList(List<Object[]> divisionList) {
		List<MasterDto> divisionDtoList = new ArrayList<>();
		if (divisionList != null && !divisionList.isEmpty()) {
			divisionDtoList = divisionList.stream().map(object -> {
				MasterDto division = new MasterDto();
				division.setId(object[0] != null ? Long.valueOf(object[0].toString()) : null);
				division.setName(object[1] != null ? object[1].toString() : null);
				return division;
			}).collect(Collectors.toList());
		}
		return divisionDtoList;
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
