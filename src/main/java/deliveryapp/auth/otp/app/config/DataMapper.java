package deliveryapp.auth.otp.app.config;

import org.springframework.stereotype.Component;

import deliveryapp.auth.otp.app.models.AddInfo;
import deliveryapp.auth.otp.app.models.ResponsePayload;
import deliveryapp.auth.otp.app.models.ResultStatus;
import deliveryapp.auth.otp.app.models.Status;
import deliveryapp.auth.otp.app.models.User;

@Component
public class DataMapper {

	public ResultStatus createResponse(String message) {
		
		ResultStatus resultStatus = new ResultStatus();
		
		if(message.equalsIgnoreCase("incorrect otp")) {
			Status status = new Status();
			status.setStatus("FAILURE");
			status.setErrorMessage("Incorrect Otp");
			status.setErrorCode("FC:101");
			resultStatus.setStatus(status);
			return resultStatus;
		}
		
		else if(message.equalsIgnoreCase("Some unknown error occured please try again")) {
			Status status = new Status();
			status.setStatus("FAILURE");
			status.setErrorMessage("Some unknown error occured please try again");
			status.setErrorCode("FC:102");
			resultStatus.setStatus(status);
			return resultStatus;
		}
		else if(message.equalsIgnoreCase("time out")) {
			Status status = new Status();
			status.setStatus("FAILURE");
			status.setErrorMessage("Time out for OTP. Please try again");
			status.setErrorCode("FC:102");
			resultStatus.setStatus(status);
			return resultStatus;
		}
		return null;
		
	}
}
