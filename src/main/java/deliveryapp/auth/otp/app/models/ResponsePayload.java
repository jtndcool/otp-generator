package deliveryapp.auth.otp.app.models;

public class ResponsePayload{

	private User user;
	private AddInfo additionalUserInfo;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public AddInfo getAdditionalUserInfo() {
		return additionalUserInfo;
	}
	public void setAdditionalUserInfo(AddInfo additionalUserInfo) {
		this.additionalUserInfo = additionalUserInfo;
	}

}
