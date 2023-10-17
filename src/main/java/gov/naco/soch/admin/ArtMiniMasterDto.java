/**
 * 
 */
package gov.naco.soch.admin;

import java.io.Serializable;
import java.util.List;

import gov.naco.soch.dto.BaseDto;
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;
import gov.naco.soch.dto.SacsFacilityDto;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Jul-20 12:29:32 pm 
 * 
 */
public class ArtMiniMasterDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;
	
	///admin/address/state
	private List<LocationDto>masterState;
	//admin/master/relationtype
	private List<MasterDto>masterRelationType;
	///admin/master/purposes
	private List<MasterDto>masterPurposes;
	//admin/master/organisationtype
	private List<MasterDto>masterOrganisationType;
	//admin/facility/listbyfacilitytype/15
	private List<FacilityBasicListDto>allArtFacility;
	//admin/master/socialwelfare
	private List<MasterDto>masterSocialwelfare;
	///admin/master/referralstatus
	private List<MasterDto>masterReferralStatus;
	//admin/facilityType/list
	private List<MasterDto>allFacilityTypeList;
	///admin/lac/list
	private List<FacilityBasicListDto>artLacList;
	///admin/master/iptstatus
	private List<MasterDto>masterIptstatus;
	//admin/master/arttreatmentstatus
	private List<MasterDto>masterArtTreatmentStatus;
	//admin/master/beneficiaryarttransferredfrom
	private List<MasterDto>beneficiaryArtTransferredFrom;
	//admin/user/facilityuserlist
	private List<FacilityUserDto>artFacilityUserList;
	//admin/master/pregnancytypecase
	private List<MasterDto>masterPregnancyTypeCase;
	//admin/facility/listby?divisionIds=6
	private List<FacilityBasicListDto>allICTCcentreList;
	//admin/master/testType
	private List<MasterDto>masterTestType;
	//admin/master/tbregimen
	private List<MasterDto>masterTbRegimen;
	//-----------------------------------------------------------------------
	//admin/master/tbtreatmentstatus
	private List<MasterDto>tbTreatmentStatus;
	//admin/master/foursscreening
	private List<MasterDto>fourSScreening;
	//admin/master/beneficiaryactivitystatus
	private List<MasterDto>beneficiaryActivityStatus;
	///admin/master/beneficiarycategory
	private List<MasterDto>beneficiaryCategory;
	//admin/master/gender
	private List<MasterDto>masterGender;
	//admin/master/maritalstatus
	private List<MasterDto>maritalStatus;
	///admin/master/occupation
	private List<MasterDto>occupation;
	//admin/master/educationlevel
	private List<MasterDto>educationLevel;
	///admin/regimen/list
	private List<RegimenDto>regimenList;
	//admin/master/entrypoint
	private List<MasterDto>entryPoint;
	//admin/master/riskfactor
	private List<MasterDto>riskFactor;
	///admin/master/monthlyincome
	private List<MasterDto>monthlyIncome;
	///admin/master/hivtype
	private List<MasterDto>hivType;
	//admin/master/treatmentline
	private List<MasterDto>treatmentLine;
	//admin/master/deliveryoutcome
	private List<MasterDto>deliveryOutcome;
	///admin/master/treatmentoutcome
	private List<MasterDto>treatmentOutcome;
	///admin/master/multimonthdispensation
	private List<MasterDto>masterMultimonthDispensation;
	
	public List<LocationDto> getMasterState() {
		return masterState;
	}
	public void setMasterState(List<LocationDto> masterState) {
		this.masterState = masterState;
	}
	public List<MasterDto> getMasterRelationType() {
		return masterRelationType;
	}
	public void setMasterRelationType(List<MasterDto> masterRelationType) {
		this.masterRelationType = masterRelationType;
	}
	public List<MasterDto> getMasterPurposes() {
		return masterPurposes;
	}
	public void setMasterPurposes(List<MasterDto> masterPurposes) {
		this.masterPurposes = masterPurposes;
	}
	public List<MasterDto> getMasterOrganisationType() {
		return masterOrganisationType;
	}
	public void setMasterOrganisationType(List<MasterDto> masterOrganisationType) {
		this.masterOrganisationType = masterOrganisationType;
	}
	public List<FacilityBasicListDto> getAllArtFacility() {
		return allArtFacility;
	}
	public void setAllArtFacility(List<FacilityBasicListDto> allArtFacility) {
		this.allArtFacility = allArtFacility;
	}
	public List<MasterDto> getMasterSocialwelfare() {
		return masterSocialwelfare;
	}
	public void setMasterSocialwelfare(List<MasterDto> masterSocialwelfare) {
		this.masterSocialwelfare = masterSocialwelfare;
	}
	public List<MasterDto> getMasterReferralStatus() {
		return masterReferralStatus;
	}
	public void setMasterReferralStatus(List<MasterDto> masterReferralStatus) {
		this.masterReferralStatus = masterReferralStatus;
	}
	public List<MasterDto> getAllFacilityTypeList() {
		return allFacilityTypeList;
	}
	public void setAllFacilityTypeList(List<MasterDto> allFacilityTypeList) {
		this.allFacilityTypeList = allFacilityTypeList;
	}
	public List<FacilityBasicListDto> getArtLacList() {
		return artLacList;
	}
	public void setArtLacList(List<FacilityBasicListDto> artLacList) {
		this.artLacList = artLacList;
	}
	public List<MasterDto> getMasterIptstatus() {
		return masterIptstatus;
	}
	public void setMasterIptstatus(List<MasterDto> masterIptstatus) {
		this.masterIptstatus = masterIptstatus;
	}
	public List<MasterDto> getMasterArtTreatmentStatus() {
		return masterArtTreatmentStatus;
	}
	public void setMasterArtTreatmentStatus(List<MasterDto> masterArtTreatmentStatus) {
		this.masterArtTreatmentStatus = masterArtTreatmentStatus;
	}
	public List<MasterDto> getBeneficiaryArtTransferredFrom() {
		return beneficiaryArtTransferredFrom;
	}
	public void setBeneficiaryArtTransferredFrom(List<MasterDto> beneficiaryArtTransferredFrom) {
		this.beneficiaryArtTransferredFrom = beneficiaryArtTransferredFrom;
	}
	public List<FacilityUserDto> getArtFacilityUserList() {
		return artFacilityUserList;
	}
	public void setArtFacilityUserList(List<FacilityUserDto> artFacilityUserList) {
		this.artFacilityUserList = artFacilityUserList;
	}
	public List<MasterDto> getMasterPregnancyTypeCase() {
		return masterPregnancyTypeCase;
	}
	public void setMasterPregnancyTypeCase(List<MasterDto> masterPregnancyTypeCase) {
		this.masterPregnancyTypeCase = masterPregnancyTypeCase;
	}
	public List<FacilityBasicListDto> getAllICTCcentreList() {
		return allICTCcentreList;
	}
	public void setAllICTCcentreList(List<FacilityBasicListDto> allICTCcentreList) {
		this.allICTCcentreList = allICTCcentreList;
	}
	public List<MasterDto> getMasterTestType() {
		return masterTestType;
	}
	public void setMasterTestType(List<MasterDto> masterTestType) {
		this.masterTestType = masterTestType;
	}
	public List<MasterDto> getMasterTbRegimen() {
		return masterTbRegimen;
	}
	public void setMasterTbRegimen(List<MasterDto> masterTbRegimen) {
		this.masterTbRegimen = masterTbRegimen;
	}
	public List<MasterDto> getTbTreatmentStatus() {
		return tbTreatmentStatus;
	}
	public void setTbTreatmentStatus(List<MasterDto> tbTreatmentStatus) {
		this.tbTreatmentStatus = tbTreatmentStatus;
	}
	public List<MasterDto> getFourSScreening() {
		return fourSScreening;
	}
	public void setFourSScreening(List<MasterDto> fourSScreening) {
		this.fourSScreening = fourSScreening;
	}
	public List<MasterDto> getBeneficiaryActivityStatus() {
		return beneficiaryActivityStatus;
	}
	public void setBeneficiaryActivityStatus(List<MasterDto> beneficiaryActivityStatus) {
		this.beneficiaryActivityStatus = beneficiaryActivityStatus;
	}
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
	public List<MasterDto> getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(List<MasterDto> maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public List<MasterDto> getOccupation() {
		return occupation;
	}
	public void setOccupation(List<MasterDto> occupation) {
		this.occupation = occupation;
	}
	public List<MasterDto> getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(List<MasterDto> educationLevel) {
		this.educationLevel = educationLevel;
	}
	public List<RegimenDto> getRegimenList() {
		return regimenList;
	}
	public void setRegimenList(List<RegimenDto> regimenList) {
		this.regimenList = regimenList;
	}
	public List<MasterDto> getEntryPoint() {
		return entryPoint;
	}
	public void setEntryPoint(List<MasterDto> entryPoint) {
		this.entryPoint = entryPoint;
	}
	public List<MasterDto> getRiskFactor() {
		return riskFactor;
	}
	public void setRiskFactor(List<MasterDto> riskFactor) {
		this.riskFactor = riskFactor;
	}
	public List<MasterDto> getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(List<MasterDto> monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public List<MasterDto> getHivType() {
		return hivType;
	}
	public void setHivType(List<MasterDto> hivType) {
		this.hivType = hivType;
	}
	public List<MasterDto> getTreatmentLine() {
		return treatmentLine;
	}
	public void setTreatmentLine(List<MasterDto> treatmentLine) {
		this.treatmentLine = treatmentLine;
	}
	public List<MasterDto> getDeliveryOutcome() {
		return deliveryOutcome;
	}
	public void setDeliveryOutcome(List<MasterDto> deliveryOutcome) {
		this.deliveryOutcome = deliveryOutcome;
	}
	public List<MasterDto> getTreatmentOutcome() {
		return treatmentOutcome;
	}
	public void setTreatmentOutcome(List<MasterDto> treatmentOutcome) {
		this.treatmentOutcome = treatmentOutcome;
	}
	public List<MasterDto> getMasterMultimonthDispensation() {
		return masterMultimonthDispensation;
	}
	public void setMasterMultimonthDispensation(List<MasterDto> masterMultimonthDispensation) {
		this.masterMultimonthDispensation = masterMultimonthDispensation;
	}
	
	
}
