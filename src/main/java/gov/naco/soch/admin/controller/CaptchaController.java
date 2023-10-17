package gov.naco.soch.admin.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.captcha.CaptchaGenerator;
import gov.naco.soch.admin.captcha.CaptchaUtils;
import gov.naco.soch.admin.dto.CaptchaResponse;
import gov.naco.soch.admin.service.CaptchaService;
import gov.naco.soch.util.CommonConstants;
import nl.captcha.Captcha;

@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CaptchaController {

	@Autowired
	private CaptchaGenerator captchaGenerator;

	@Autowired
	private CaptchaService captchaService;
	
	@Value("${captchaEnabled}")
	private boolean captchaEnabled;
	
	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

	@GetMapping(value = "/load")
	public CaptchaResponse load() {
		CaptchaResponse captchaResponse = new CaptchaResponse();
		try {
			Captcha captcha = captchaGenerator.createCaptcha(200, 50);
			String answer = captcha.getAnswer();
			captchaResponse.setCaptchaString(CaptchaUtils.encodeBase64(captcha));
			if(captchaEnabled) {
				Long captchaId = captchaService.saveCaptcha(answer);
				captchaResponse.setCaptchaId(captchaId);	
			}else {
				captchaResponse.setCaptchaId(-1l);	
			}
		} catch (Exception e) {
			logger.error("Exception in loading captcha", e);
		}
		return captchaResponse;
	}
	/**
	 * API for cleaning all captcha before 2 days to the current date
	 * End point : /clean
	 * Rest Method : GET
	 * @param accessKey
	 * @return
	 */
	@GetMapping(value = "/clean")
	public boolean cleanCaptcha(String accessKey) {
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		}
		else {
		  logger.warn("JOB--> cleancaptchadata from API started");
		  return captchaService.cleanCaptcha();
		}
	}

}
