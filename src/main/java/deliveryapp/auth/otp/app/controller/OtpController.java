package deliveryapp.auth.otp.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import deliveryapp.auth.otp.app.config.DataMapper;
import deliveryapp.auth.otp.app.models.OtpData;
import deliveryapp.auth.otp.app.models.ResultStatus;
import deliveryapp.auth.otp.app.models.Status;
import deliveryapp.auth.otp.app.models.VerifyOtpPayload;
import deliveryapp.auth.otp.app.service.ServiceAuth;
import io.swagger.annotations.Api;

@RestController
@Api
public class OtpController {
	
	@Autowired
	ServiceAuth serviceAuth;
	
	@Autowired
	DataMapper dataMapper;
	
	private Map<Long,OtpData> otp_data = new HashMap<>();

	

	
	 @PostMapping(value = "/mobileNumber/getotp")
	  public ResponseEntity<Status> sendOTP(@RequestBody Long mobileNumber) throws IOException, UnirestException {
		 
		 Status status = new Status(); 
		 
		 if(mobileNumber == null ) {
			 status.setErrorCode("FC:122");
			 status.setStatus("FAILURE");
			 status.setErrorMessage("Invalid Mobile Number");
			 return new ResponseEntity<Status>(status, HttpStatus.BAD_REQUEST);
		 }
			  
	
		 
		 OtpData otpData = new OtpData();
		 otpData.setMobile(mobileNumber);
		 
		 Random r = new Random();
		 int low = 100000;
		 int high = 999999;
		 int result = r.nextInt(high-low) + low;
		 
		 otpData.setOtp(String.valueOf(result));
		 
		 otpData.setExpiryTime(System.currentTimeMillis()+200000);
		 
		 otp_data.put(mobileNumber, otpData);
		 String payload = "sender_id=FSTSMS&message=42237&variables={#BB#}&variables_values="+otpData.getOtp()+"&language=english&route=qt&numbers="+mobileNumber;
		 
		 String z=payload.replace("\"", "");
		 	try {
		 		 com.mashape.unirest.http.HttpResponse<String> response = Unirest.post("https://www.fast2sms.com/dev/bulk")
						  .header("authorization", "B3TRggmtkRZf4camtx2ZmN3vNgB8ibj2XJJFgI1jY51DquT3BfmeTHpJHf59")
						  .header("Content-Type", "application/x-www-form-urlencoded")
						  .body(z)
						  .asString();	
		 		 
		 		 JSONObject jsonObject = new JSONObject(response.getBody());
				   
		 		
				 

				 if(  jsonObject.get("return").toString().equalsIgnoreCase("true")) {
					 
					 status.setMessage("OTP is sent successfully");
					 status.setStatus("SUCCESS");
					 return new ResponseEntity<Status>(status, HttpStatus.OK); 
				 }
		 	}
		 	catch(Exception e) {
		 		 e.printStackTrace();
		 	}
		 	
		 	
	 		 status.setErrorCode("FC:125");
			 status.setStatus("FAILURE");
			 status.setErrorMessage("Invalid Mobile Number");
			 return new ResponseEntity<Status>(status, HttpStatus.BAD_REQUEST);	
		
		 
	
			 
		  
	  }
	 

	 @PostMapping(value = "/otp/verify")
	  public ResponseEntity<ResultStatus> verifyOtp(@RequestBody VerifyOtpPayload verifyOtpPayload) {
	
		 if(otp_data.containsKey(verifyOtpPayload.getMobile())) {
			 OtpData otpData = otp_data.get(verifyOtpPayload.getMobile());
			 if(otpData.getExpiryTime()>=System.currentTimeMillis()) {
				 if(otpData.getOtp().equalsIgnoreCase(verifyOtpPayload.getOtp())) {
					 otp_data.remove(verifyOtpPayload.getMobile());
					
					 return new ResponseEntity<ResultStatus>( serviceAuth.authService(verifyOtpPayload.getMobile().toString()), HttpStatus.OK);
				 }
				 else {
					 return new ResponseEntity<ResultStatus>(dataMapper.createResponse("incorrect otp"), HttpStatus.UNAUTHORIZED);
				 }
			 }
			 else 
				 return new ResponseEntity<ResultStatus>(dataMapper.createResponse("time out"), HttpStatus.GATEWAY_TIMEOUT);
				
		 }
		 
		 return new ResponseEntity<ResultStatus>(dataMapper.createResponse("Some unknown error occured please try again"), HttpStatus.UNAUTHORIZED);
			
	  }


}
