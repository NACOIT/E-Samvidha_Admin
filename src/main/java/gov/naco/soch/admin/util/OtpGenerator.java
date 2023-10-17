package gov.naco.soch.admin.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Component
public class OtpGenerator {

    public String generateOtp(Integer lengthOfOtp){
        String numbers = "0123456789";
        SecureRandom randomMethod = new SecureRandom();
        char[] otp = new char[lengthOfOtp];
        for(int i = 0; i < lengthOfOtp; ++i) {
            otp[i] = numbers.charAt(randomMethod.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }
}