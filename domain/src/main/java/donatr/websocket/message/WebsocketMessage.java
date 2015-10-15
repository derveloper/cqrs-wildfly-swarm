package donatr.websocket.message;

public class WebsocketMessage {
	private final String event;
	public WebsocketMessage() {
		this.event = this.getClass().getSimpleName();
	}

	public String getEvent() {
		return event;
	}
}
