package deliveryapp.auth.otp.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.twilio.Twilio;

import deliveryapp.auth.otp.app.models.OtpData;
import deliveryapp.auth.otp.app.models.VerifyOtpPayload;
import io.swagger.annotations.Api;
import okhttp3.OkHttpClient;

@RestController
@Api
public class OtpController {
	
	
	private Map<String,OtpData> otp_data = new HashMap<>();

	

	
	 @PostMapping(value = "/mobileNumber/getotp")
	  public ResponseEntity<Object> sendOTP(@RequestBody String mobileNumber) throws IOException, UnirestException {
		 
		 if(mobileNumber != null)
		 if(mobileNumber.length() != 10) {
			 return new ResponseEntity<Object>("Invalid Number..Please try again", HttpStatus.BAD_REQUEST);
		 }
		 else {
			 return new ResponseEntity<Object>("Empty Number sent..Please try again", HttpStatus.BAD_REQUEST);
		 }
		 
		 OtpData otpData = new OtpData();
		 otpData.setMobile(mobileNumber);
		 
		 Random r = new Random();
		 int low = 100000;
		 int high = 999999;
		 int result = r.nextInt(high-low) + low;
		 
		 otpData.setOtp(String.valueOf(result));
		 
		 otpData.setExpiryTime(System.currentTimeMillis()+40000);
		 
		 otp_data.put(mobileNumber, otpData);
		 
		 String payload = "sender_id=FSTSMS&message=Your OTP is"+otpData.getOtp()+"&language=english&route=p&numbers="+mobileNumber;
		 
		 String z=payload.replace("\"", "");
	
		 com.mashape.unirest.http.HttpResponse<String> response = Unirest.post("https://www.fast2sms.com/dev/bulk")
				  .header("authorization", "TyhpMrZHvAVMdtCp3X4JdhTWXpN2BcivKLfrYB6RYEvjpzDzfPQq9sV2ysOA")
				  .header("Content-Type", "application/x-www-form-urlencoded")
				  .body(z)
				  .asString();
		 System.out.println("jell"+ response.getBody()+"-----------------"+z);
		 
		 JSONObject jsonObject = new JSONObject(response.getBody());
		 
		 System.out.println("jell"+ jsonObject.get("return"));     


		 if(  jsonObject.get("return").toString().equalsIgnoreCase("true"))
		  return new ResponseEntity<Object>("OTP is sent successfully", HttpStatus.OK);
		 
	 else 
			  return new ResponseEntity<Object>("Invalid Number..Please try again", HttpStatus.BAD_REQUEST);
		  
		  
	  }
	 

	 @PostMapping(value = "/otp/verify")
	  public ResponseEntity<Object> verifyOtp(@RequestBody VerifyOtpPayload verifyOtpPayload) {
		 
		 
		 if(otp_data.containsKey(verifyOtpPayload.getMobile())) {
			 OtpData otpData = otp_data.get(verifyOtpPayload.getMobile());
			 if(otpData.getExpiryTime()>=System.currentTimeMillis()) {
				 if(otpData.getOtp().equalsIgnoreCase(verifyOtpPayload.getOtp())) {
					 otp_data.remove(verifyOtpPayload.getMobile());
					 return new ResponseEntity<Object>("Verified and Authenticated user", HttpStatus.OK);
				 }
				 else {
					 return new ResponseEntity<Object>("Wrong OTP entered please try again", HttpStatus.OK);
				 }
			 }
			 else 
				 return new ResponseEntity<Object>("OTP expired, Please try again", HttpStatus.REQUEST_TIMEOUT);
		 }
		 
		  return new ResponseEntity<Object>("Some unkown error occured..Please try again", HttpStatus.BAD_GATEWAY);
	  }
	 

}
