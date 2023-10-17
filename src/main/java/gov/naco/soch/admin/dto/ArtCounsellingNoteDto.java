package gov.naco.soch.admin.dto;

public class ArtCounsellingNoteDto {

	private Long artCounsellingNoteId;
	private String counsellingNoteName;
	private String counsellingTypeName;
	private String counsellingSection;
	private Boolean firstVisitOnly;
	private Boolean counsellingNoteIsActive;
	private Long counsellingTypeId;
	private String questionKey;
	
	public Long getArtCounsellingNoteId() {
		return artCounsellingNoteId;
	}
	public void setArtCounsellingNoteId(Long artCounsellingNoteId) {
		this.artCounsellingNoteId = artCounsellingNoteId;
	}
	public String getCounsellingNoteName() {
		return counsellingNoteName;
	}
	public void setCounsellingNoteName(String counsellingNoteName) {
		this.counsellingNoteName = counsellingNoteName;
	}
	public String getCounsellingTypeName() {
		return counsellingTypeName;
	}
	public void setCounsellingTypeName(String counsellingTypeName) {
		this.counsellingTypeName = counsellingTypeName;
	}
	public String getCounsellingSection() {
		return counsellingSection;
	}
	public void setCounsellingSection(String counsellingSection) {
		this.counsellingSection = counsellingSection;
	}
	public Boolean getFirstVisitOnly() {
		return firstVisitOnly;
	}
	public void setFirstVisitOnly(Boolean firstVisitOnly) {
		this.firstVisitOnly = firstVisitOnly;
	}
	public Boolean getCounsellingNoteIsActive() {
		return counsellingNoteIsActive;
	}
	public void setCounsellingNoteIsActive(Boolean counsellingNoteIsActive) {
		this.counsellingNoteIsActive = counsellingNoteIsActive;
	}
	public Long getCounsellingTypeId() {
		return counsellingTypeId;
	}
	public void setCounsellingTypeId(Long counsellingTypeId) {
		this.counsellingTypeId = counsellingTypeId;
	}
	public String getQuestionKey() {
		return questionKey;
	}
	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}
	@Override
	public String toString() {
		return "ArtCounsellingNoteDto [artCounsellingNoteId=" + artCounsellingNoteId + ", counsellingNoteName="
				+ counsellingNoteName + ", counsellingTypeName=" + counsellingTypeName + ", counsellingSection="
				+ counsellingSection + ", firstVisitOnly=" + firstVisitOnly + ", counsellingNoteIsActive="
				+ counsellingNoteIsActive + ", counsellingTypeId=" + counsellingTypeId + ", questionKey=" + questionKey
				+ "]";
	}
	

	
	
}
