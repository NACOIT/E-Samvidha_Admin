package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.BaseDto;
import gov.naco.soch.dto.UserMasterDto;

public class UserSearchResponseDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Integer givenRecordCount;
	private Integer actualRecordCount;
	private List<UserMasterDto> userMasterList;
	public Integer getGivenRecordCount() {
		return givenRecordCount;
	}
	public void setGivenRecordCount(Integer givenRecordCount) {
		this.givenRecordCount = givenRecordCount;
	}
	public Integer getActualRecordCount() {
		return actualRecordCount;
	}
	public void setActualRecordCount(Integer actualRecordCount) {
		this.actualRecordCount = actualRecordCount;
	}
	public List<UserMasterDto> getUserMasterList() {
		return userMasterList;
	}
	public void setUserMasterList(List<UserMasterDto> userMasterList) {
		this.userMasterList = userMasterList;
	}
	@Override
	public String toString() {
		return "UserSearchResponseDto [givenRecordCount=" + givenRecordCount + ", actualRecordCount="
				+ actualRecordCount + ", userMasterList=" + userMasterList + "]";
	}
	
	
}
