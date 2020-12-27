package deliveryapp.auth.otp.app.service;

import deliveryapp.auth.otp.app.models.ResultStatus;

public interface ServiceAuth {

	public ResultStatus authService(String mobile);
}
