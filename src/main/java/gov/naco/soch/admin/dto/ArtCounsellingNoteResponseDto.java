package gov.naco.soch.admin.dto;

import java.util.List;


public class ArtCounsellingNoteResponseDto {

	private Long totalcount;
	private Integer currentCount;
	private Integer pageSize;
	private Integer pageNumber;
	private List<ArtCounsellingNoteDto> artCounsellingNoteDto;
	
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}
	public Integer getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(Integer currentCount) {
		this.currentCount = currentCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public List<ArtCounsellingNoteDto> getArtCounsellingNoteDto() {
		return artCounsellingNoteDto;
	}
	public void setArtCounsellingNoteDto(List<ArtCounsellingNoteDto> artCounsellingNoteDto) {
		this.artCounsellingNoteDto = artCounsellingNoteDto;
	}
	
	@Override
	public String toString() {
		return "ArtCounsellingNoteResponseDto [totalcount=" + totalcount + ", currentCount=" + currentCount
				+ ", pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", artCounsellingNoteDto="
				+ artCounsellingNoteDto + "]";
	}
	
	
}
