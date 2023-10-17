package gov.naco.soch.admin.dto;

import java.util.List;

public class PageReportModuleDto {
	private List<ReportModuleDto> reportModuleDtos;
	private Long totalRecordCount;

	public List<ReportModuleDto> getReportModuleDtos() {
		return reportModuleDtos;
	}

	public void setReportModuleDtos(List<ReportModuleDto> reportModuleDtos) {
		this.reportModuleDtos = reportModuleDtos;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

}
