package donatr.websocket.message;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductAccountCreatedEventWebsocketMessage extends WebsocketMessage {
	private String id;
	private String name;
	private BigDecimal balance;
	private BigDecimal fixedAmount;
}
