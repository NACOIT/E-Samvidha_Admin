package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;

public class ArtMinimasterIptAttDetailsDto {

	private List<MasterDto> masterGender; // admin/master/gender
	private List<MasterDto> beneficiaryCategory; // admin/master/beneficiarycategory
	private List<LocationDto> masterState; // admin/address/state
	private List<MasterDto> foursScreening; // admin/master/foursscreening
	private List<MasterDto> tbTestType;// admin/master/tbtesttype
	private List<MasterDto> iptStatus;// admin/master/iptstatus
	private List<MasterDto> tbResult;// admin/master/tbresult
	private List<MasterDto> tbTreatmentStatus;// admin/master/tbtreatmentstatus
	private List<MasterDto> tbRegimen;// admin/master/tbregimen
	private List<MasterDto> diagnosedBy;// admin/master/diagnosedby
	private List<MasterDto> treatmentUnder;// admin/master/treatmentunder
	private List<MasterDto> treatmentOutcome;// admin/master/treatmentoutcome

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

	public List<LocationDto> getMasterState() {
		return masterState;
	}

	public void setMasterState(List<LocationDto> masterState) {
		this.masterState = masterState;
	}

	public List<MasterDto> getFoursScreening() {
		return foursScreening;
	}

	public void setFoursScreening(List<MasterDto> foursScreening) {
		this.foursScreening = foursScreening;
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

	public List<MasterDto> getTbResult() {
		return tbResult;
	}

	public void setTbResult(List<MasterDto> tbResult) {
		this.tbResult = tbResult;
	}

	public List<MasterDto> getTbTreatmentStatus() {
		return tbTreatmentStatus;
	}

	public void setTbTreatmentStatus(List<MasterDto> tbTreatmentStatus) {
		this.tbTreatmentStatus = tbTreatmentStatus;
	}

	public List<MasterDto> getTbRegimen() {
		return tbRegimen;
	}

	public void setTbRegimen(List<MasterDto> tbRegimen) {
		this.tbRegimen = tbRegimen;
	}

	public List<MasterDto> getDiagnosedBy() {
		return diagnosedBy;
	}

	public void setDiagnosedBy(List<MasterDto> diagnosedBy) {
		this.diagnosedBy = diagnosedBy;
	}

	public List<MasterDto> getTreatmentUnder() {
		return treatmentUnder;
	}

	public void setTreatmentUnder(List<MasterDto> treatmentUnder) {
		this.treatmentUnder = treatmentUnder;
	}

	public List<MasterDto> getTreatmentOutcome() {
		return treatmentOutcome;
	}

	public void setTreatmentOutcome(List<MasterDto> treatmentOutcome) {
		this.treatmentOutcome = treatmentOutcome;
	}

}
