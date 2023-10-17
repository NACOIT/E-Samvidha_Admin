package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.MasterDto;

public class ArtMinimasterPaedatricBeneficiaryDto {

	private List<MasterDto> artStayingWith; // admin/master/artstayingwith
	private List<MasterDto> masterGender; // admin/master/gender
	private List<MasterDto> educationalLeval; // admin/master/educationlevel
	private List<MasterDto> infantFeeding; // admin/master/infantfeeding
	private List<MasterDto> birthHistory; // admin/master/birthhistory
	private List<MasterDto> dnaPcrTest; // admin/master/dnapcrtest
	private List<MasterDto> guardinCaregiver; // admin/master/guardiancaregiver
	private List<FacilityUserDto> facilityUserlist;// admin/user/facilityuserlist

	public List<MasterDto> getArtStayingWith() {
		return artStayingWith;
	}

	public void setArtStayingWith(List<MasterDto> artStayingWith) {
		this.artStayingWith = artStayingWith;
	}

	public List<MasterDto> getMasterGender() {
		return masterGender;
	}

	public void setMasterGender(List<MasterDto> masterGender) {
		this.masterGender = masterGender;
	}

	public List<MasterDto> getEducationalLeval() {
		return educationalLeval;
	}

	public void setEducationalLeval(List<MasterDto> educationalLeval) {
		this.educationalLeval = educationalLeval;
	}

	public List<MasterDto> getInfantFeeding() {
		return infantFeeding;
	}

	public void setInfantFeeding(List<MasterDto> infantFeeding) {
		this.infantFeeding = infantFeeding;
	}

	public List<MasterDto> getBirthHistory() {
		return birthHistory;
	}

	public void setBirthHistory(List<MasterDto> birthHistory) {
		this.birthHistory = birthHistory;
	}

	public List<MasterDto> getDnaPcrTest() {
		return dnaPcrTest;
	}

	public void setDnaPcrTest(List<MasterDto> dnaPcrTest) {
		this.dnaPcrTest = dnaPcrTest;
	}

	public List<MasterDto> getGuardinCaregiver() {
		return guardinCaregiver;
	}

	public void setGuardinCaregiver(List<MasterDto> guardinCaregiver) {
		this.guardinCaregiver = guardinCaregiver;
	}

	public List<FacilityUserDto> getFacilityUserlist() {
		return facilityUserlist;
	}

	public void setFacilityUserlist(List<FacilityUserDto> facilityUserlist) {
		this.facilityUserlist = facilityUserlist;
	}

}
