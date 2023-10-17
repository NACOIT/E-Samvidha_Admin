package gov.naco.soch.admin.dto;

import java.util.Date;
import java.util.List;

import gov.naco.soch.dto.MasterDto;

public class MasterReportsDto {
	private Long reportId;
	private String reportName;
	private int displayOrder;
	private Long subModuleId;
	private String subModuleName;
	private Long moduleId;
	private String moduleName;
	private String url;
	private boolean superSetReportExists;
	private String createdUserName;
	private Long createdUserId;
	private Date createdTime;
	private Date modifiedTime;
	private List<MasterDto> reportDivisions;

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

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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

	public boolean isSuperSetReportExists() {
		return superSetReportExists;
	}

	public void setSuperSetReportExists(boolean superSetReportExists) {
		this.superSetReportExists = superSetReportExists;
	}

	public List<MasterDto> getReportDivisions() {
		return reportDivisions;
	}

	public void setReportDivisions(List<MasterDto> reportDivisions) {
		this.reportDivisions = reportDivisions;
	}


}
