package gov.naco.soch.admin.dto;

import java.util.List;

public class PageMasterReportDto {
	List<MasterReportsDto> masterReportsDtos;
	private Long totalRecordCount;

	public List<MasterReportsDto> getMasterReportsDtos() {
		return masterReportsDtos;
	}

	public void setMasterReportsDtos(List<MasterReportsDto> masterReportsDtos) {
		this.masterReportsDtos = masterReportsDtos;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

}
