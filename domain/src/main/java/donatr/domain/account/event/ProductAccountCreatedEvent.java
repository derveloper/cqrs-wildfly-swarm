package donatr.domain.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
public class ProductAccountCreatedEvent {
	private final String id;
	private final String name;
	private final BigDecimal fixedAmount;
}
