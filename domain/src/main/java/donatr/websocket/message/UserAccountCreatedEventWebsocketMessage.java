package donatr.websocket.message;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class UserAccountCreatedEventWebsocketMessage extends WebsocketMessage {
	private String id;
	private String name;
	private BigDecimal balance;
}
