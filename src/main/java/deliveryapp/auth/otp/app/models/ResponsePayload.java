package deliveryapp.auth.otp.app.models;

public class ResponsePayload{

	private User user;
	private AddInfo additonalUserInfo;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public AddInfo getAdditonalUserInfo() {
		return additonalUserInfo;
	}
	public void setAdditonalUserInfo(AddInfo additonalUserInfo) {
		this.additonalUserInfo = additonalUserInfo;
	}
}
