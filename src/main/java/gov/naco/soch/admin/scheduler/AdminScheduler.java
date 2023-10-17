package gov.naco.soch.admin.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.naco.soch.admin.service.CaptchaService;
import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.dto.FacilityDto;

@Component
public class AdminScheduler {

	private static final Logger logger = LoggerFactory.getLogger(AdminScheduler.class);

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private FacilityService facilityService;

	@Value("${scheduler.job.admin.cleancaptchadata.enabled}")
	private boolean cleanCaptchaJobEnabled;

	@Value("${scheduler.job.admin.reminderforcontractexpirydate.enabled}")
	private boolean reminderForContractExpiryDateJobEnabled;

	/* Job should run in every day midnight 12 AM */
	@Scheduled(cron = "0 0 0 * * ?")
	// @Scheduled(cron = "0 0/01 * * * ?")
	public void cleanCaptchaData() {
		if (cleanCaptchaJobEnabled) {
			logger.warn("JOB--> cleancaptchadata job started");
			boolean result = captchaService.cleanCaptcha();
			logger.debug("Result :", result);
			logger.warn("JOB--> cleancaptchadata job ended");
		} else {
			logger.info("JOB-->cleancaptchadata is disabled, so not running");
		}
	}

	@Scheduled(cron = "0 0 0 * * ?")
	 //@Scheduled(cron = "0 0/01 * * * ?")
	public List<FacilityDto> reminderForContractExpiryDate() {
		if (reminderForContractExpiryDateJobEnabled) {
			logger.warn("JOB--> reminderforcontractexpirydate job started");
			List<FacilityDto> facilities = facilityService.reminderForContractExpiryDate();
			logger.debug("reminderforcontractexpirydate successfully trigerred");
			logger.warn("JOB--> reminderforcontractexpirydate job ended");
			return facilities;
		} else {
			logger.info("JOB-->reminderforcontractexpirydate is disabled, so not running");
		}

		return null;
	}
}
