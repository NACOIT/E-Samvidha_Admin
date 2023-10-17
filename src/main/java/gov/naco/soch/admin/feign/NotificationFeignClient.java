//package gov.naco.soch.admin.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@FeignClient(name = "notificationFeign", url="${notification-base-url}", configuration = FeignConfiguration.class)
//public interface NotificationFeignClient {
//
//    @RequestMapping(method = RequestMethod.POST, value = "/notification/mobile/general/send-otp")
//    ResponseEntity<String> sendOtpNotification(@RequestBody Map<String, Object> otpInputMap);
//
//}
