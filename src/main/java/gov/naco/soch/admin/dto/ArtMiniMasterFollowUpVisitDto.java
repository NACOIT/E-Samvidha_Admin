package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;
import gov.naco.soch.dto.SacsFacilityDto;

public class ArtMiniMasterFollowUpVisitDto {

	private List<MasterDto> functionalStatus; // admin/master/functionalstatus
	private List<MasterDto> whoClinicalStage; // admin/master/clinicalstage
	private List<MasterDto> opportunisticInfections; // admin/master/opportunisticinfections
	private List<MasterDto> artSideEffects; // admin/master/sideeffects
	private List<MasterDto> concurrentConditions; // admin/master/otherailments
	private List<MasterDto> organisationalType; // admin/master/organisationtype
	private List<MasterDto> purposes; // admin/master/purposes
	private List<MasterDto> masterGender; // admin/master/gender
	private List<MasterDto> beneficiaryCategory; // admin/master/beneficiarycategory
	private List<MasterDto> facilityType;//admin/master/facilitytype 
	private List<RegimenDto> regimen; // admin/regimen/list
	private List<FacilityUserDto> userList; // admin/user/facilityuserlist
	private List<LocationDto>masterState;     //admin/address/state
	private List<SacsFacilityDto> lacList; //admin/lac/list


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

	public List<MasterDto> getOpportunisticInfections() {
		return opportunisticInfections;
	}

	public void setOpportunisticInfections(List<MasterDto> opportunisticInfections) {
		this.opportunisticInfections = opportunisticInfections;
	}

	public List<MasterDto> getArtSideEffects() {
		return artSideEffects;
	}

	public void setArtSideEffects(List<MasterDto> artSideEffects) {
		this.artSideEffects = artSideEffects;
	}

	public List<MasterDto> getConcurrentConditions() {
		return concurrentConditions;
	}

	public void setConcurrentConditions(List<MasterDto> concurrentConditions) {
		this.concurrentConditions = concurrentConditions;
	}

	public List<MasterDto> getOrganisationalType() {
		return organisationalType;
	}

	public void setOrganisationalType(List<MasterDto> organisationalType) {
		this.organisationalType = organisationalType;
	}

	public List<MasterDto> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<MasterDto> purposes) {
		this.purposes = purposes;
	}

	public List<MasterDto> getMasterGender() {
		return masterGender;
	}

	public void setMasterGender(List<MasterDto> masterGender) {
		this.masterGender = masterGender;
	}

	public List<MasterDto> getBeneficiaryCategory() {
		return beneficiaryCategory;
	}

	public void setBeneficiaryCategory(List<MasterDto> beneficiaryCategory) {
		this.beneficiaryCategory = beneficiaryCategory;
	}

	public List<MasterDto> getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(List<MasterDto> facilityType) {
		this.facilityType = facilityType;
	}

	public List<RegimenDto> getRegimen() {
		return regimen;
	}

	public void setRegimen(List<RegimenDto> regimen) {
		this.regimen = regimen;
	}

	public List<FacilityUserDto> getUserList() {
		return userList;
	}

	public void setUserList(List<FacilityUserDto> userList) {
		this.userList = userList;
	}

	public List<LocationDto> getMasterState() {
		return masterState;
	}

	public void setMasterState(List<LocationDto> masterState) {
		this.masterState = masterState;
	}

	public List<SacsFacilityDto> getLacList() {
		return lacList;
	}

	public void setLacList(List<SacsFacilityDto> lacList) {
		this.lacList = lacList;
	}
	
	

}
