package donatr.websocket.message;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class AccountDebitedEventWebsocketMessage extends WebsocketMessage {
	private String id;
	private BigDecimal amount;
}
