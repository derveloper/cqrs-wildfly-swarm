package donatr.common.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TransactionCreatedEvent {
	private String id;
	private String fromAccount;
	private String toAccount;
	private BigDecimal amount;
}
