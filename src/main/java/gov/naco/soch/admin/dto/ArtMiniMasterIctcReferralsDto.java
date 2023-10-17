package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.FacilityDto;
import gov.naco.soch.dto.MasterDto;

public class ArtMiniMasterIctcReferralsDto {

	private List<MasterDto> beneficiaryCategory; // admin/master/beneficiarycategory
	private List<MasterDto> masterGender; // admin/master/gender
	private List<MasterDto> hivType; // admin/master/hivtype
	private List<MasterDto> artBeneficiaryStatus; // admin/master/artbeneficiarystatus
	private List<FacilityBasicListDto> ictcCenters; // facility/listby?divisionIds=6

	public List<MasterDto> getBeneficiaryCategory() {
		return beneficiaryCategory;
	}

	public void setBeneficiaryCategory(List<MasterDto> beneficiaryCategory) {
		this.beneficiaryCategory = beneficiaryCategory;
	}

	public List<MasterDto> getMasterGender() {
		return masterGender;
	}

	public void setMasterGender(List<MasterDto> masterGender) {
		this.masterGender = masterGender;
	}

	public List<MasterDto> getHivType() {
		return hivType;
	}

	public void setHivType(List<MasterDto> hivType) {
		this.hivType = hivType;
	}

	public List<MasterDto> getArtBeneficiaryStatus() {
		return artBeneficiaryStatus;
	}

	public void setArtBeneficiaryStatus(List<MasterDto> artBeneficiaryStatus) {
		this.artBeneficiaryStatus = artBeneficiaryStatus;
	}

	public List<FacilityBasicListDto> getIctcCenters() {
		return ictcCenters;
	}

	public void setIctcCenters(List<FacilityBasicListDto> ictcCenters) {
		this.ictcCenters = ictcCenters;
	}

}
