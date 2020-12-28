package deliveryapp.auth.otp.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deliveryapp.auth.otp.app.models.AddInfo;
import deliveryapp.auth.otp.app.models.ResponsePayload;
import deliveryapp.auth.otp.app.models.ResultStatus;
import deliveryapp.auth.otp.app.models.Status;
import deliveryapp.auth.otp.app.models.User;
import deliveryapp.auth.otp.app.repo.AuthRepo;
import deliveryapp.auth.otp.app.service.ServiceAuth;

@Service
public class ServiceAuthImpl implements ServiceAuth {

	@Autowired
	AuthRepo authRepo;
	
	@Override
	public ResultStatus authService(String mobile) {
		
		ResultStatus resultStatus = new ResultStatus();
	
		String isNewUser = authRepo.checkIfNewUser(mobile);
		if(isNewUser.equalsIgnoreCase("new user")) {
			
			Status status = new Status();
			status.setStatus("SUCCESS");
			status.setMessage("User is authenticated");
			resultStatus.setStatus(status);
			
			ResponsePayload response = new ResponsePayload();
			AddInfo additonalUserInfo = new AddInfo();
			additonalUserInfo.setIsNewUser(true);
			response.setAdditionalUserInfo(additonalUserInfo);
			User user = new User();
			user.setPhoneNumber(mobile);
			user.setUid(mobile);
			response.setUser(user);
			resultStatus.setResponse(response);
			
			return resultStatus;
	
			
		}
		else if(isNewUser.equalsIgnoreCase("old user")) {
			Status status = new Status();
			status.setStatus("SUCCESS");
			status.setMessage("User is authenticated");
			resultStatus.setStatus(status);
			
			ResponsePayload response = new ResponsePayload();
			AddInfo additonalUserInfo = new AddInfo();
			additonalUserInfo.setIsNewUser(false);
			response.setAdditionalUserInfo(additonalUserInfo);
			User user = new User();
			user.setPhoneNumber(mobile);
			user.setUid(mobile);
			response.setUser(user);
			resultStatus.setResponse(response);
			return resultStatus;
		}
		
		else {
			Status status = new Status();
			status.setStatus("FAILURE");
			status.setMessage("Some unknown error occured");
			resultStatus.setStatus(status);
			
			ResponsePayload response = new ResponsePayload();
			AddInfo additonalUserInfo = new AddInfo();
			response.setAdditionalUserInfo(additonalUserInfo);
			User user = new User();
			user.setPhoneNumber(mobile);
			user.setUid(mobile);
			response.setUser(user);
			resultStatus.setResponse(response);
			return resultStatus;
		}
		
	}

}
