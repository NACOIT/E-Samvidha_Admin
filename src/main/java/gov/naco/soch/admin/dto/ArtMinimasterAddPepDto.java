package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;

public class ArtMinimasterAddPepDto {

	private List<MasterDto> masterGender; // admin/master/gender
	private List<MasterDto> exposureSeverity; // admin/master/exposureseverity
	private List<MasterDto> hivExposureCode; // admin/master/hivexposurecode
	private List<MasterDto> hivSourceStatus; // admin/master/hivsourcestatus
	private List<MasterDto> hivStatus; // admin/master/hivstatus
	private List<MasterDto> hcvStatus; // admin/master/hcvstatus
	private List<MasterDto> hbvStatus;// admin/master/hbvstatus
	private List<MasterDto> pepPrescription;// admin/master/pepprescription
	private List<FacilityUserDto> assignTo;// admin/user/facilityuserlist
	private List<LocationDto> masterState; // admin/address/state

	public List<MasterDto> getMasterGender() {
		return masterGender;
	}

	public void setMasterGender(List<MasterDto> masterGender) {
		this.masterGender = masterGender;
	}

	public List<MasterDto> getExposureSeverity() {
		return exposureSeverity;
	}

	public void setExposureSeverity(List<MasterDto> exposureSeverity) {
		this.exposureSeverity = exposureSeverity;
	}

	public List<MasterDto> getHivExposureCode() {
		return hivExposureCode;
	}

	public void setHivExposureCode(List<MasterDto> hivExposureCode) {
		this.hivExposureCode = hivExposureCode;
	}

	public List<MasterDto> getHivSourceStatus() {
		return hivSourceStatus;
	}

	public void setHivSourceStatus(List<MasterDto> hivSourceStatus) {
		this.hivSourceStatus = hivSourceStatus;
	}

	public List<MasterDto> getHivStatus() {
		return hivStatus;
	}

	public void setHivStatus(List<MasterDto> hivStatus) {
		this.hivStatus = hivStatus;
	}

	public List<MasterDto> getHcvStatus() {
		return hcvStatus;
	}

	public void setHcvStatus(List<MasterDto> hcvStatus) {
		this.hcvStatus = hcvStatus;
	}

	public List<MasterDto> getHbvStatus() {
		return hbvStatus;
	}

	public void setHbvStatus(List<MasterDto> hbvStatus) {
		this.hbvStatus = hbvStatus;
	}

	public List<MasterDto> getPepPrescription() {
		return pepPrescription;
	}

	public void setPepPrescription(List<MasterDto> pepPrescription) {
		this.pepPrescription = pepPrescription;
	}

	public List<FacilityUserDto> getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(List<FacilityUserDto> assignTo) {
		this.assignTo = assignTo;
	}

	public List<LocationDto> getMasterState() {
		return masterState;
	}

	public void setMasterState(List<LocationDto> masterState) {
		this.masterState = masterState;
	}

}
