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

import gov.naco.soch.admin.dto.PageReportModuleDto;
import gov.naco.soch.admin.dto.PageReportsSubModule;
import gov.naco.soch.admin.dto.ReportModuleDto;
import gov.naco.soch.admin.dto.ReportsSubModuleDto;
import gov.naco.soch.entity.ReportsModule;
import gov.naco.soch.entity.ReportsSubModule;
import gov.naco.soch.repository.ReportsModuleRepository;
import gov.naco.soch.repository.ReportsSubModuleRepository;

@Service
@Transactional
public class ReportsModuleAndSubModuleService {

	@Autowired
	ReportsModuleRepository reportsModuleRepository;
	@Autowired
	ReportsSubModuleRepository reportsSubModuleRepository;

	public PageReportModuleDto getAllReportsModuleData(int pageIndex, int pageSize, String sortBy, String sortType) {
		PageReportModuleDto pageReportModuleDto = new PageReportModuleDto();
		List<ReportModuleDto> reportModuleDtos = new ArrayList<ReportModuleDto>();
		List<ReportsModule> reportsModules = new ArrayList<ReportsModule>();
		try {
			Pageable paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).descending());
			if (sortType.equalsIgnoreCase("asc")) {
				paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).ascending());
			}
			paginateObject = parenthesisEncapsulation(paginateObject);
			// Page<ReportsModule> modulePagedlist =
			// reportsModuleRepository.findAll(paginateObject);
			Page<ReportsModule> modulePagedlist = reportsModuleRepository.findList(paginateObject);
			if (modulePagedlist != null && (!modulePagedlist.isEmpty())) {
				reportsModules = modulePagedlist.getContent();
				if (pageIndex == 0) {
					Long totalResultCount = modulePagedlist.getTotalElements();
					pageReportModuleDto.setTotalRecordCount(totalResultCount);
				}
			}
			if (reportsModules != null && reportsModules.size() > 0) {
				for (ReportsModule reportsModule : reportsModules) {
					reportModuleDtos.add(convertToModuleDto(reportsModule));
				}
			}
			pageReportModuleDto.setReportModuleDtos(reportModuleDtos);

		} catch (Exception e) {
		}
		return pageReportModuleDto;
	}

	public List<ReportModuleDto> getAllReportsModuleData() {
		List<ReportModuleDto> reportModuleDtos = new ArrayList<ReportModuleDto>();
		List<ReportsModule> reportsModules = new ArrayList<ReportsModule>();
		try {
			reportsModules = reportsModuleRepository.findAll();
			if (reportsModules != null && reportsModules.size() > 0) {
				for (ReportsModule reportsModule : reportsModules) {
					reportModuleDtos.add(convertToModuleDto(reportsModule));
				}
			}
		} catch (Exception e) {
		}
		return reportModuleDtos;
	}

	public ReportModuleDto getReportsModuleDataById(Long moduleId) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		Optional<ReportsModule> reportsModule = reportsModuleRepository.findById(moduleId);
		if (reportsModule != null && reportsModule.isPresent()) {
			reportModuleDto = convertToModuleDto(reportsModule.get());
		}
		return reportModuleDto;
	}

	public ReportModuleDto saveReportsModuleDataById(ReportsModule reportsModule) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		ReportsModule module = new ReportsModule();
		try {
			module = reportsModuleRepository.save(reportsModule);
			if (module != null) {
				reportModuleDto = convertToModuleDto(module);
			}
		} catch (Exception e) {
		}
		return reportModuleDto;
	}

	public ReportsSubModuleDto saveReportsSubModuleDataById(ReportsSubModule reportsSubModule) {
		ReportsSubModuleDto reportsSubModuleDto = new ReportsSubModuleDto();
		ReportsSubModule subModule = new ReportsSubModule();
		try {
			subModule = reportsSubModuleRepository.save(reportsSubModule);
			if (subModule != null) {
				reportsSubModuleDto = convertToDto(reportsSubModule);
			}
		} catch (Exception e) {
		}
		return reportsSubModuleDto;
	}

	public PageReportsSubModule getReportsSubModuleData(int pageIndex, int pageSize, String sortBy, String sortType) {
		PageReportsSubModule pageReportsSubModule = new PageReportsSubModule();
		List<ReportsSubModule> reportsSubModules = new ArrayList<ReportsSubModule>();
		List<ReportsSubModuleDto> reportsSubModuleDtos = new ArrayList<ReportsSubModuleDto>();
		try {
			Pageable paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).descending());
			if (sortType.equalsIgnoreCase("asc")) {
				paginateObject = PageRequest.of(pageIndex, pageSize, Sort.by(sortBy).ascending());
			}
			paginateObject = parenthesisEncapsulation(paginateObject);
			Page<ReportsSubModule> subModulePagedList = reportsSubModuleRepository.findList(paginateObject);
//			Page<ReportsSubModule> subModulePagedList = reportsSubModuleRepository.findAll(paginateObject);
			if (subModulePagedList != null && (!subModulePagedList.isEmpty())) {
				reportsSubModules = subModulePagedList.getContent();
				if (pageIndex == 0) {
					Long totalResultCount = subModulePagedList.getTotalElements();
					pageReportsSubModule.setTotalRecordCount(totalResultCount);
				}
			}
			if (reportsSubModules != null && reportsSubModules.size() > 0) {
				for (ReportsSubModule reportsSubModule : reportsSubModules) {
					ReportsSubModuleDto subModuleDto = convertToDto(reportsSubModule);
					reportsSubModuleDtos.add(subModuleDto);
				}
			}
			pageReportsSubModule.setReportsSubModuleDtos(reportsSubModuleDtos);
		} catch (Exception e) {
			throw e;
		}
		return pageReportsSubModule;
	}

	public List<ReportsSubModuleDto> getSubModuleByModuleId(Long moduleId) {
		List<ReportsSubModule> reportsSubModules = new ArrayList<ReportsSubModule>();
		List<ReportsSubModuleDto> reportsSubModuleDtos = new ArrayList<ReportsSubModuleDto>();
		try {
			reportsSubModules = reportsSubModuleRepository.findByReportsModule_ModuleId(moduleId);
			if (reportsSubModules != null && reportsSubModules.size() > 0) {
				for (ReportsSubModule reportsSubModule : reportsSubModules) {
					ReportsSubModuleDto subModuleDto = convertToDto(reportsSubModule);
					reportsSubModuleDtos.add(subModuleDto);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return reportsSubModuleDtos;
	}

	public ReportsSubModuleDto getReportsSubModuleDataById(Long subModuleId) {
		Optional<ReportsSubModule> reportsSubModules = null;
		ReportsSubModuleDto reportsSubModuleDto = new ReportsSubModuleDto();
		ReportsSubModule reportsSubModule = null;
		try {
			reportsSubModules = reportsSubModuleRepository.findById(subModuleId);
			if (reportsSubModules.isPresent()) {
				reportsSubModule = reportsSubModules.get();
				reportsSubModuleDto = convertToDto(reportsSubModule);
			}
		} catch (Exception e) {
			throw e;
		}
		return reportsSubModuleDto;
	}

	static ReportsSubModuleDto convertToDto(ReportsSubModule reportsSubModule) {
		ReportsSubModuleDto subModule = new ReportsSubModuleDto();
		if (reportsSubModule != null) {
			subModule.setSubModuleId(reportsSubModule.getSubModuleId());
			subModule.setSubModuleName(reportsSubModule.getSubModuleName());
			subModule.setModuleId(reportsSubModule.getReportsModule().getModuleId());
			subModule.setModuleName(reportsSubModule.getReportsModule().getModuleName());
			subModule.setDisplayOrder(reportsSubModule.getDisplayOrder());
			if (reportsSubModule.getCreatedUser() != null) {
				subModule.setCreatedUserName(reportsSubModule.getCreatedUser().getFirstname());
				subModule.setCreatedUserId(reportsSubModule.getCreatedUser().getId());
			}
			subModule.setCreatedTime(reportsSubModule.getCreatedDate());
			subModule.setModifiedTime(reportsSubModule.getModifiedDate());
		}

		return subModule;
	}

	static ReportModuleDto convertToModuleDto(ReportsModule reportsModule) {
		ReportModuleDto reportModuleDto = new ReportModuleDto();
		reportModuleDto.setModuleId(reportsModule.getModuleId());
		reportModuleDto.setModuleName(reportsModule.getModuleName());
		reportModuleDto.setDisplayOrder(reportsModule.getDisplayOrder());
		if (reportsModule.getCreatedUser() != null) {
			reportModuleDto.setCreatedUserId(reportsModule.getCreatedUser().getId());
			reportModuleDto.setCreatedUserName(reportsModule.getCreatedUser().getFirstname());
		}

		reportModuleDto.setCreatedTime(reportsModule.getCreatedDate());
		reportModuleDto.setModifiedTime(reportsModule.getModifiedDate());
		return reportModuleDto;

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
