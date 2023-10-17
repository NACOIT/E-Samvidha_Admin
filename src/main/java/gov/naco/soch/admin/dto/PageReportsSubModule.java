package gov.naco.soch.admin.dto;

import java.util.List;

public class PageReportsSubModule {
	List<ReportsSubModuleDto> reportsSubModuleDtos;
	private Long totalRecordCount;

	public List<ReportsSubModuleDto> getReportsSubModuleDtos() {
		return reportsSubModuleDtos;
	}

	public void setReportsSubModuleDtos(List<ReportsSubModuleDto> reportsSubModuleDtos) {
		this.reportsSubModuleDtos = reportsSubModuleDtos;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

}
