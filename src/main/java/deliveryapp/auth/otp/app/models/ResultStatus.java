package deliveryapp.auth.otp.app.models;

public class ResultStatus {
	private Status status;
    private ResponsePayload response;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public ResponsePayload getResponse() {
		return response;
	}
	public void setResponse(ResponsePayload response) {
		this.response = response;
	}
	
}
