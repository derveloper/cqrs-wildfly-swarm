package donatr.common.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AccountCreditedEvent {
	private String id;
	private String accountId;
	private BigDecimal amount;
}
