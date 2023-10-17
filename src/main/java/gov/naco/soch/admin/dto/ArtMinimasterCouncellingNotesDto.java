package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.MasterDto;

public class ArtMinimasterCouncellingNotesDto {

	private List<MasterDto> alcoholUseStatus; // admin/master/habitsalcoholuse
	private List<MasterDto> smokingStatus; // admin/master/habitssmoking
	private List<MasterDto> tobaccoUseStatus; // admin/master/tobaccouse
	private List<MasterDto> hbvStatus; // admin/master/hbvstatus
	private List<MasterDto> hcvStatus; // admin/master/hcvstatus
	private List<MasterDto> contraception; // admin/master/contraception
	private List<MasterDto> otherAilments; // admin/master/otherailments
	private List<MasterDto> deliveryOutcome; // admin/master/deliveryoutcome

	public List<MasterDto> getAlcoholUseStatus() {
		return alcoholUseStatus;
	}

	public void setAlcoholUseStatus(List<MasterDto> alcoholUseStatus) {
		this.alcoholUseStatus = alcoholUseStatus;
	}

	public List<MasterDto> getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(List<MasterDto> smokingStatus) {
		this.smokingStatus = smokingStatus;
	}

	public List<MasterDto> getTobaccoUseStatus() {
		return tobaccoUseStatus;
	}

	public void setTobaccoUseStatus(List<MasterDto> tobaccoUseStatus) {
		this.tobaccoUseStatus = tobaccoUseStatus;
	}

	public List<MasterDto> getHbvStatus() {
		return hbvStatus;
	}

	public void setHbvStatus(List<MasterDto> hbvStatus) {
		this.hbvStatus = hbvStatus;
	}

	public List<MasterDto> getHcvStatus() {
		return hcvStatus;
	}

	public void setHcvStatus(List<MasterDto> hcvStatus) {
		this.hcvStatus = hcvStatus;
	}

	public List<MasterDto> getContraception() {
		return contraception;
	}

	public void setContraception(List<MasterDto> contraception) {
		this.contraception = contraception;
	}

	public List<MasterDto> getOtherAilments() {
		return otherAilments;
	}

	public void setOtherAilments(List<MasterDto> otherAilments) {
		this.otherAilments = otherAilments;
	}

	public List<MasterDto> getDeliveryOutcome() {
		return deliveryOutcome;
	}

	public void setDeliveryOutcome(List<MasterDto> deliveryOutcome) {
		this.deliveryOutcome = deliveryOutcome;
	}

}
