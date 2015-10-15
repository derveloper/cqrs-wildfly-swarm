package donatr.domain.account.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class UserAccountDebitedEvent {
	private String id;
	private BigDecimal amount;
}
