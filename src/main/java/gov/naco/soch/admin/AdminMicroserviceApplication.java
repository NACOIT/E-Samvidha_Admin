package gov.naco.soch.admin;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import gov.naco.soch.admin.captcha.CaptchaGenerator;
import gov.naco.soch.util.CommonConstants;

@SpringBootApplication
@EnableJpaRepositories("gov.naco.soch.repository")
@EnableAsync
@EntityScan(basePackages = {"gov.naco.soch.entity"})
@ComponentScan(basePackages = { "gov.naco.soch" })
//@PropertySource("classpath:application-${spring.profiles.active}.yml")
@EnableScheduling
@EnableFeignClients
public class AdminMicroserviceApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminMicroserviceApplication.class);
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args
	 *            the command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AdminMicroserviceApplication.class);
	}

	/*
	 * created_time, modified_time time values should be saved in UTC timezone
	 * values, using '@CreatedDate' and '@LastModifiedDate' in Auditable class.
	 */
	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(CommonConstants.TIMEZONE_IST));
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public CaptchaGenerator getCaptchaGenerator() {
		return new CaptchaGenerator();
	}

}
