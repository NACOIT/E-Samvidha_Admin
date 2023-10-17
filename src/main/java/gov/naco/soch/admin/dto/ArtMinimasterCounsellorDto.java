package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;

public class ArtMinimasterCounsellorDto {

	private List<MasterDto> masterGender; // admin/master/gender
	private List<RegimenDto> regimen; // admin/regimen/list
	private List<MasterDto> beneficiaryActivityStatus;// admin/master/beneficiaryactivitystatus
	private List<MasterDto> socialWelfare;// admin/master/socialwelfare
	private List<MasterDto> otherAilments; // admin/master/otherailments
	private List<MasterDto> sideEffects; // admin/master/sideeffects
	private List<MasterDto> functionalStatus; // admin/master/functionalstatus
	private List<MasterDto> clinicalStage; // admin/master/clinicalstage
	private List<MasterDto> opportunisticInfections; // admin/master/opportunisticinfections

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

	public List<MasterDto> getBeneficiaryActivityStatus() {
		return beneficiaryActivityStatus;
	}

	public void setBeneficiaryActivityStatus(List<MasterDto> beneficiaryActivityStatus) {
		this.beneficiaryActivityStatus = beneficiaryActivityStatus;
	}

	public List<MasterDto> getSocialWelfare() {
		return socialWelfare;
	}

	public void setSocialWelfare(List<MasterDto> socialWelfare) {
		this.socialWelfare = socialWelfare;
	}

	public List<MasterDto> getOtherAilments() {
		return otherAilments;
	}

	public void setOtherAilments(List<MasterDto> otherAilments) {
		this.otherAilments = otherAilments;
	}

	public List<MasterDto> getSideEffects() {
		return sideEffects;
	}

	public void setSideEffects(List<MasterDto> sideEffects) {
		this.sideEffects = sideEffects;
	}

	public List<MasterDto> getFunctionalStatus() {
		return functionalStatus;
	}

	public void setFunctionalStatus(List<MasterDto> functionalStatus) {
		this.functionalStatus = functionalStatus;
	}

	public List<MasterDto> getClinicalStage() {
		return clinicalStage;
	}

	public void setClinicalStage(List<MasterDto> clinicalStage) {
		this.clinicalStage = clinicalStage;
	}

	public List<MasterDto> getOpportunisticInfections() {
		return opportunisticInfections;
	}

	public void setOpportunisticInfections(List<MasterDto> opportunisticInfections) {
		this.opportunisticInfections = opportunisticInfections;
	}
	
	

}
