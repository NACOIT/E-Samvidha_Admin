package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;

public class ArtStaffNurseMinimasterDto {

	private List<MasterDto> masterGender; // admin/master/gender
	private List<RegimenDto> regimen; // admin/regimen/list
	private List<MasterDto> opportunisticInfections; // admin/master/opportunisticinfections
	private List<MasterDto> functionalStatus; // admin/master/functionalstatus
	private List<MasterDto> whoClinicalStage; // admin/master/clinicalstage
	private List<MasterDto> tbRegimen; // admin/master/tbregimen
	private List<FacilityUserDto> userList; // admin/user/facilityuserlist
	private List<MasterDto> foursScreening; // admin/master/foursscreening
	private List<MasterDto> foursSymptom; // admin/master/fourssymptom
	private List<MasterDto> tbType;// admin/master/tbtype
	private List<MasterDto> tbTestType;// admin/master/tbtesttype
	private List<MasterDto> iptStatus;// admin/master/iptstatus
	private List<MasterDto> tbTreatmentStatus;// admin/master/tbtreatmentstatus
	
//	-----------------------------------------------------------------------------------------------
	private List<MasterDto> beneficiaryCategory; // admin/master/beneficiarycategory
	private List<LocationDto>masterState;     //admin/address/state

	public List<MasterDto> getMasterGender() {
		return masterGender;
	}

	public void setMasterGender(List<MasterDto> masterGender) {
		this.masterGender = masterGender;
	}

	public List<RegimenDto> getRegimen() {
		return regimen;
	}

	public void setRegimen(List<RegimenDto> regimen) {
		this.regimen = regimen;
	}

	public List<MasterDto> getOpportunisticInfections() {
		return opportunisticInfections;
	}

	public void setOpportunisticInfections(List<MasterDto> opportunisticInfections) {
		this.opportunisticInfections = opportunisticInfections;
	}

	public List<MasterDto> getFunctionalStatus() {
		return functionalStatus;
	}

	public void setFunctionalStatus(List<MasterDto> functionalStatus) {
		this.functionalStatus = functionalStatus;
	}

	public List<MasterDto> getWhoClinicalStage() {
		return whoClinicalStage;
	}

	public void setWhoClinicalStage(List<MasterDto> whoClinicalStage) {
		this.whoClinicalStage = whoClinicalStage;
	}

	public List<MasterDto> getTbRegimen() {
		return tbRegimen;
	}

	public void setTbRegimen(List<MasterDto> tbRegimen) {
		this.tbRegimen = tbRegimen;
	}

	public List<FacilityUserDto> getUserList() {
		return userList;
	}

	public void setUserList(List<FacilityUserDto> userList) {
		this.userList = userList;
	}

	public List<MasterDto> getFoursScreening() {
		return foursScreening;
	}

	public void setFoursScreening(List<MasterDto> foursScreening) {
		this.foursScreening = foursScreening;
	}

	public List<MasterDto> getFoursSymptom() {
		return foursSymptom;
	}

	public void setFoursSymptom(List<MasterDto> foursSymptom) {
		this.foursSymptom = foursSymptom;
	}

	public List<MasterDto> getTbType() {
		return tbType;
	}

	public void setTbType(List<MasterDto> tbType) {
		this.tbType = tbType;
	}

	public List<MasterDto> getTbTestType() {
		return tbTestType;
	}

	public void setTbTestType(List<MasterDto> tbTestType) {
		this.tbTestType = tbTestType;
	}

	public List<MasterDto> getIptStatus() {
		return iptStatus;
	}

	public void setIptStatus(List<MasterDto> iptStatus) {
		this.iptStatus = iptStatus;
	}

	public List<MasterDto> getTbTreatmentStatus() {
		return tbTreatmentStatus;
	}

	public void setTbTreatmentStatus(List<MasterDto> tbTreatmentStatus) {
		this.tbTreatmentStatus = tbTreatmentStatus;
	}

	public List<MasterDto> getBeneficiaryCategory() {
		return beneficiaryCategory;
	}

	public void setBeneficiaryCategory(List<MasterDto> beneficiaryCategory) {
		this.beneficiaryCategory = beneficiaryCategory;
	}

	public List<LocationDto> getMasterState() {
		return masterState;
	}

	public void setMasterState(List<LocationDto> masterState) {
		this.masterState = masterState;
	}
	

}
