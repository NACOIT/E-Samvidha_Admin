package gov.naco.soch.admin.service;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.naco.soch.util.CommonConstants;

@Service
public class CommonAdminService {

	private static final Logger logger = LoggerFactory.getLogger(CommonAdminService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${cache.clearEndpointsAdmin}")
	private String CACHE_CLEAR_ENDPOINTS;

	@Autowired
	private Environment env;

	public void clearCache(String cacheName, String cacheKey) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<>("body", headers);
			String[] cacheClearEndpoints = CACHE_CLEAR_ENDPOINTS.split(",");
			if (cacheClearEndpoints != null) {
				for (String cacheClearEndpoint : cacheClearEndpoints) {
					StringBuilder cacheClearEndpointBuilder = new StringBuilder(cacheClearEndpoint);
					if (!StringUtils.isBlank(cacheName)) {
						cacheClearEndpointBuilder.append("?cacheName=");
						cacheClearEndpointBuilder.append(cacheName);
						if (!StringUtils.isBlank(cacheKey)) {
							cacheClearEndpointBuilder.append("&cacheKey=");
							cacheClearEndpointBuilder.append(cacheKey);
						}
						cacheClearEndpointBuilder.append("&accessKey=");
						cacheClearEndpointBuilder.append(env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY));
					}
					restTemplate.exchange(cacheClearEndpointBuilder.toString(), HttpMethod.GET, entity, String.class);
				}
			} else {
				logger.error("Cache clear endpoint is missing !");
			}

		} catch (Exception e) {
			logger.error("Exception in clearing cache !", e);
		}

	}
}
