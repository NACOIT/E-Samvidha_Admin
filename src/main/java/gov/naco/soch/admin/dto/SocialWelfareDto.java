/**
 * 
 */
package gov.naco.soch.admin.dto;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Dec-01 8:26:17 pm 
 * 
 */
public class SocialWelfareDto{
	private Long id;
	private String name;
	private String code;
	private String description;
	private Integer stateId;
	private String stateName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	@Override
	public String toString() {
		return "SocialWelfareDto [id=" + id + ", name=" + name + ", code=" + code + ", description=" + description
				+ ", stateId=" + stateId + ", stateName=" + stateName + "]";
	}
	
	
	
	
}
