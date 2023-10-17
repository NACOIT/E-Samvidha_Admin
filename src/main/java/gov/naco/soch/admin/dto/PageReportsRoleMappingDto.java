package gov.naco.soch.admin.dto;

import java.util.List;

public class PageReportsRoleMappingDto {

	private List<ReportsRoleMappingDto> reportsRoleMappingDtos;
	private Long totalRecordCount;

	public List<ReportsRoleMappingDto> getReportsRoleMappingDtos() {
		return reportsRoleMappingDtos;
	}

	public void setReportsRoleMappingDtos(List<ReportsRoleMappingDto> reportsRoleMappingDtos) {
		this.reportsRoleMappingDtos = reportsRoleMappingDtos;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

}
