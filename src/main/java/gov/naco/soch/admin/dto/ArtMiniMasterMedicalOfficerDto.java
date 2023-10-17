package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;

public class ArtMiniMasterMedicalOfficerDto {

	private List<MasterDto> masterGender; // admin/master/gender
	private List<RegimenDto> regimen; // admin/regimen/list
	private List<MasterDto> artSideEffects; // admin/master/sideeffects
	private List<MasterDto> opportunisticInfections; // admin/master/opportunisticinfections
	private List<MasterDto> functionalStatus; // admin/master/functionalstatus
	private List<MasterDto> whoClinicalStage; // admin/master/clinicalstage
	private List<MasterDto> tbRegimen; // admin/master/tbregimen
	private List<FacilityUserDto> userList; // admin/user/facilityuserlist
	private List<MasterDto> concurrentConditions; // admin/master/otherailments
	private List<MasterDto> socialWelfareScheme;// admin/master/socialwelfare
	private List<MasterDto> tbType;// admin/master/tbtype
	private List<MasterDto> changeRegimen;// admin/master/artregimenaction
	private List<MasterDto> facilityType;// admin/master/facilitytype

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

	public List<MasterDto> getArtSideEffects() {
		return artSideEffects;
	}

	public void setArtSideEffects(List<MasterDto> artSideEffects) {
		this.artSideEffects = artSideEffects;
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

	public List<MasterDto> getConcurrentConditions() {
		return concurrentConditions;
	}

	public void setConcurrentConditions(List<MasterDto> concurrentConditions) {
		this.concurrentConditions = concurrentConditions;
	}

	public List<MasterDto> getSocialWelfareScheme() {
		return socialWelfareScheme;
	}

	public void setSocialWelfareScheme(List<MasterDto> socialWelfareScheme) {
		this.socialWelfareScheme = socialWelfareScheme;
	}

	public List<MasterDto> getTbType() {
		return tbType;
	}

	public void setTbType(List<MasterDto> tbType) {
		this.tbType = tbType;
	}

	public List<MasterDto> getChangeRegimen() {
		return changeRegimen;
	}

	public void setChangeRegimen(List<MasterDto> changeRegimen) {
		this.changeRegimen = changeRegimen;
	}

	public List<MasterDto> getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(List<MasterDto> facilityType) {
		this.facilityType = facilityType;
	}

}
