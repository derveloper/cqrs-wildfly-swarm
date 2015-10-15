package donatr.websocket.message;

import donatr.domain.account.aggregate.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class AccountCreatedEventWebsocketMessage extends WebsocketMessage {
	private String id;
	private String name;
	private AccountType accountType;
	private BigDecimal balance;
}
