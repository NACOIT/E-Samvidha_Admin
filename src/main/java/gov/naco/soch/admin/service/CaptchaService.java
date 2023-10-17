package gov.naco.soch.admin.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.entity.Captcha;
import gov.naco.soch.repository.CaptchaRepository;

@Service
@Transactional
public class CaptchaService {

	@Autowired
	CaptchaRepository captchaRepository;

	private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);

	public Long saveCaptcha(String answer) {
		Captcha captcha = new Captcha();
		captcha.setAnswer(answer);
		captcha.setUsed(false);
		captcha = captchaRepository.save(captcha);
		return captcha.getId();
	}

	public boolean updateCaptchaAsUsed(Long captchaId) {
		captchaRepository.updateCaptchaAsUsed(captchaId);
		return true;
	}

	/* If 0 = valid, 1 = expired/used, 2 = invalid answer */
	public int validateCaptcha(Long captchaId, String answer) {
		Captcha captcha = null;
		Optional<Captcha> captchaOpt = captchaRepository.findById(captchaId);
		if (captchaOpt.isPresent()) {
			captcha = captchaOpt.get();
			if (captcha.isUsed()) {
				return 1;
			} else if (!answer.contentEquals(captcha.getAnswer())) {
				return 2;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	/**
	 * Method Name : cleanCaptcha Description : Method to clear captcha table with 2
	 * days before to the current date
	 * 
	 * @return
	 */
	public boolean cleanCaptcha() {
		try {
			captchaRepository.deleteAllCreatedBefore2Days();
			logger.warn("cleancaptchadata job ended");
			logger.debug("Clear captcha is working...");
			return true;
		} catch (Exception e) {
			logger.debug("Exception :", e.getMessage());
			return false;
		}
	}

}
