package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.BaseDto;
import gov.naco.soch.dto.RoleAccessMappingDto;

/*Class which represents the response for access settings list API.*/
public class ListAccessSettingsResponse extends BaseDto {

	private static final long serialVersionUID = 1L;

	private String module;
	private List<RoleAccessMappingDto> accessSettingsInfo;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<RoleAccessMappingDto> getAccessSettingsInfo() {
		return accessSettingsInfo;
	}

	public void setAccessSettingsInfo(List<RoleAccessMappingDto> accessSettingsInfo) {
		this.accessSettingsInfo = accessSettingsInfo;
	}

	@Override
	public String toString() {
		return "ListAccessSettingsResponse [module=" + module + ", accessSettingsInfo=" + accessSettingsInfo + "]";
	}

}
