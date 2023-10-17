package gov.naco.soch.admin.util;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



@Component
public class NotificationUtil {
	
	@Value("${notification.systemEmailServiceUrl}")
	private String EMAIL_ENDPOINTURL;
	@Value("${notification.systemSmsServiceUrl}")
	private String SMS_ENDPOINTURL;
	@Value("${notification.whatsappServiceUrl}")
	private String WHATSAPP_ENDPOINTURL;
	@Autowired
	private RestTemplate restTemplate;

   public void sendNotfication(String eventId, boolean emailToBeSent, boolean smsToBeSent, boolean whatsAppToBeSent, HashMap<String, Object> placeholdermap) {
		
		//LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		//headers.setBearerAuth(currentUser.getToken()); 
		
		HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(placeholdermap, headers);
		
		if (emailToBeSent) {
			restTemplate.exchange(EMAIL_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, HashMap.class);
		}
		if (smsToBeSent) {
			restTemplate.exchange(SMS_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, Boolean.class);
		}
		if (whatsAppToBeSent) {
			restTemplate.exchange(WHATSAPP_ENDPOINTURL.concat(eventId), HttpMethod.POST, request, Boolean.class);
		}
	}
}
