package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.dto.MasterReportsDto;
import gov.naco.soch.admin.dto.PageMasterReportDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.entity.MasterReport;
import gov.naco.soch.repository.MasterReportsRepository;

@Service
@Transactional
public class MasterAdminReportsService {

	@Autowired
	MasterReportsRepository masterReportsRepository;

	public PageMasterReportDto getAllMasterReportsData(int pageIndex, int pageSize, String sortBy, String sortType) {
		PageMasterReportDto pageReportModuleDto = new PageMasterReportDto();
		List<MasterReportsDto> masterReportsDtos = new ArrayList<MasterReportsDto>();
		List<MasterReport> masterReportList = new ArrayList<MasterReport>();
		Pageable paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).ascending());
		}
		paginateObject = parenthesisEncapsulation(paginateObject);
		Page<MasterReport> reportsPagedList = masterReportsRepository.findList(paginateObject);
//		Page<MasterReport> reportsPagedList = masterReportsRepository.findAll(paginateObject);
		if (reportsPagedList != null && (!reportsPagedList.isEmpty())) {
			masterReportList = reportsPagedList.getContent();
			if (pageIndex == 0) {
				Long totalResultCount = reportsPagedList.getTotalElements();
				pageReportModuleDto.setTotalRecordCount(totalResultCount);
			}
		}

		for (MasterReport masterReport : masterReportList) {
			masterReportsDtos.add(convertMasterReports(masterReport));
		}
		pageReportModuleDto.setMasterReportsDtos(masterReportsDtos);

		return pageReportModuleDto;

	}

	public List<MasterReportsDto> getAllMasterReportsBySubModuleId(Long subModuleId) {
		List<MasterReportsDto> masterReportsDtos = new ArrayList<MasterReportsDto>();
		List<MasterReport> masterReportList = new ArrayList<MasterReport>();
		masterReportList = masterReportsRepository.findBySubModule_SubModuleId(subModuleId);
		for (MasterReport masterReport : masterReportList) {
			masterReportsDtos.add(convertMasterReports(masterReport));
		}

		return masterReportsDtos;

	}

	public MasterReportsDto getAllMasterReportsDataById(Long reportId) {
		MasterReportsDto masterReportsDto = new MasterReportsDto();
		Optional<MasterReport> masterReportOptional = masterReportsRepository.findById(reportId);
		if (masterReportOptional != null && masterReportOptional.isPresent()) {
			masterReportsDto = convertMasterReports(masterReportOptional.get());
		}
		return masterReportsDto;

	}

	public MasterReportsDto saveMasterReports(MasterReport masterReport) {
		MasterReportsDto reportsDto = new MasterReportsDto();
		MasterReport report = new MasterReport();
		report = masterReportsRepository.save(masterReport);
		if (report != null) {
			reportsDto = convertMasterReports(report);
		}
		return reportsDto;
	}

	public List<MasterReportsDto> bulkUpdateReportsAndRoleMapping(List<MasterReport> masterReports, Long reportId) {
		masterReportsRepository.deleteById(reportId);
		List<MasterReportsDto> masterReportsDtos = new ArrayList<MasterReportsDto>();
		List<MasterReport> masterReportList = new ArrayList<MasterReport>();
		masterReportList = masterReportsRepository.saveAll(masterReports);
		if (masterReportList != null && masterReportList.size() > 0) {
			for (MasterReport masterReport : masterReportList) {
				masterReportsDtos.add(convertMasterReports(masterReport));
			}
		}
		return masterReportsDtos;
	}

	static MasterReportsDto convertMasterReports(MasterReport masterReport) {
		MasterReportsDto masterReportsDto = new MasterReportsDto();
		masterReportsDto.setReportId(masterReport.getReportId());
		masterReportsDto.setReportName(masterReport.getReportname());
		masterReportsDto.setSubModuleId(masterReport.getSubModule().getSubModuleId());
		masterReportsDto.setSubModuleName(masterReport.getSubModule().getSubModuleName());
		masterReportsDto.setModuleId(masterReport.getReportsModule().getModuleId());
		masterReportsDto.setModuleName(masterReport.getReportsModule().getModuleName());
		masterReportsDto.setUrl(masterReport.getUrl());
		masterReportsDto.setSuperSetReportExists(masterReport.isSuperSetReportExists());
		masterReportsDto.setDisplayOrder(masterReport.getDisplayOrder());
		if (masterReport.getCreatedUser() != null) {
			masterReportsDto.setCreatedUserId(masterReport.getCreatedUser().getId());
			masterReportsDto.setCreatedUserName(masterReport.getCreatedUser().getFirstname());
		}
		masterReportsDto.setCreatedTime(masterReport.getCreatedDate());
		masterReportsDto.setModifiedTime(masterReport.getModifiedDate());
		if (masterReport.getGlobalReportsDivisionMappings() != null
				&& !masterReport.getGlobalReportsDivisionMappings().isEmpty()) {
			List<MasterDto> reportDivisions = new ArrayList<>();
			masterReport.getGlobalReportsDivisionMappings().forEach(data -> {
				MasterDto reportDivision = new MasterDto();
				if (data != null && data.getDivision() != null) {
					reportDivision.setId(data.getDivision().getId());
					reportDivision.setName(data.getDivision().getName());
					reportDivisions.add(reportDivision);
				}
			});
			masterReportsDto.setReportDivisions(reportDivisions);
		}

		return masterReportsDto;

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
