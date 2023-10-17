package gov.naco.soch.admin.dto;

public class CaptchaResponse {
	
	private Long captchaId;
	private String captchaString;
	
	public Long getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(Long captchaId) {
		this.captchaId = captchaId;
	}
	public String getCaptchaString() {
		return captchaString;
	}
	public void setCaptchaString(String captchaString) {
		this.captchaString = captchaString;
	}
	
}
