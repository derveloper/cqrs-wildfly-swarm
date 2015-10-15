package donatr.domain.account.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductAccountDebitedEvent {
	private String id;
	private BigDecimal amount;
}
