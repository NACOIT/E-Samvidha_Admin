package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.BaseDto;
import gov.naco.soch.dto.MasterDto;

public class ArtMiniMasterReferToDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	/* /admin/master/masterAdherenceRemarks */
	private List<MasterDto> masterAdherenceRemarks;

	/* /admin/master/masterSACEPReferralReason */
	private List<MasterDto> masterSACEPReferralReason;

	/* /admin/master/masterReferralType */
	private List<MasterDto> masterReferralType;

	public List<MasterDto> getMasterAdherenceRemarks() {
		return masterAdherenceRemarks;
	}

	public List<MasterDto> getMasterSACEPReferralReason() {
		return masterSACEPReferralReason;
	}

	public List<MasterDto> getMasterReferralType() {
		return masterReferralType;
	}

	public void setMasterAdherenceRemarks(List<MasterDto> masterAdherenceRemarks) {
		this.masterAdherenceRemarks = masterAdherenceRemarks;
	}

	public void setMasterSACEPReferralReason(List<MasterDto> masterSACEPReferralReason) {
		this.masterSACEPReferralReason = masterSACEPReferralReason;
	}

	public void setMasterReferralType(List<MasterDto> masterReferralType) {
		this.masterReferralType = masterReferralType;
	}

}
