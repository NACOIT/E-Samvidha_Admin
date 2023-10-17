package gov.naco.soch.admin.dto;

import java.util.Date;
import java.util.List;

import gov.naco.soch.dto.MasterDto;

public class ReportsRoleMappingDto {
	private Long mappingId;
	private Long reportId;
	private String reportName;
	private Long subModuleId;
	private String subModuleName;
	private Long moduleId;
	private String moduleName;
	private Long roleId;
	private String roleName;
	private String url;
	private String createdUserName;
	private Long createdUserId;
	private Date createdTime;
	private Date modifiedTime;
	private List<MasterDto> reportDivisions;

	public Long getMappingId() {
		return mappingId;
	}

	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Long getSubModuleId() {
		return subModuleId;
	}

	public void setSubModuleId(Long subModuleId) {
		this.subModuleId = subModuleId;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date date) {
		this.createdTime = date;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date date) {
		this.modifiedTime = date;
	}

	public List<MasterDto> getReportDivisions() {
		return reportDivisions;
	}

	public void setReportDivisions(List<MasterDto> reportDivisions) {
		this.reportDivisions = reportDivisions;
	}

	@Override
	public String toString() {
		return "ReportsRoleMappingDto [mappingId=" + mappingId + ", reportId=" + reportId + ", reportName=" + reportName
				+ ", subModuleId=" + subModuleId + ", subModuleName=" + subModuleName + ", moduleId=" + moduleId
				+ ", moduleName=" + moduleName + ", roleId=" + roleId + ", roleName=" + roleName + ", url=" + url
				+ ", createdUserName=" + createdUserName + ", createdUserId=" + createdUserId + ", createdTime="
				+ createdTime + ", modifiedTime=" + modifiedTime + ", reportDivisions=" + reportDivisions + "]";
	}

}
