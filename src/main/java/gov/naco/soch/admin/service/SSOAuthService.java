package gov.naco.soch.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import gov.naco.soch.dto.SSOResponseDto;

@Service
public class SSOAuthService {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Autowired
	private RestTemplate restTemplate;
	@Value("${sso.manager.url}")
	private String ssoManagerUrl;
	
	private static final Logger logger = LoggerFactory.getLogger(SSOAuthService.class);

	public ResponseEntity<SSOResponseDto> registeruserForSSO(String applicationName, String userName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("applicationName ", applicationName);
		map.add("username  ", userName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		logger.info("Calling sso service with url->{}",ssoManagerUrl);
		long startTime = System.currentTimeMillis();
		ResponseEntity<SSOResponseDto> entity = restTemplate.postForEntity(ssoManagerUrl, httpRequest,
				SSOResponseDto.class);
		logger.info("SSO SSOCALLDURATION for registering user userName-->{}:duration -->{} ms:", userName,
				(System.currentTimeMillis() - startTime));
		logger.info("Called sso service with url->{}",ssoManagerUrl);
		return entity;

	}

	public boolean deleteRegisteredSession(String ssoSessionId) {
		boolean isDeleted = true;
		RestTemplate restTemplate = new RestTemplate();
		try {
			// restTemplate.ex ("http://34.212.135.8:1978/ssosession/" + ssoSessionId);
			ResponseEntity<Boolean> responseEntity = restTemplate.exchange(ssoManagerUrl + "/" + ssoSessionId,
					HttpMethod.DELETE, null, Boolean.class);
		} catch (RestClientException e) {
			isDeleted = false;
		}
		return isDeleted;

	}
}
