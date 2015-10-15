package donatr.domain.account.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateProductAccountCommand {
	@TargetAggregateIdentifier
	private String id;
	private String name;
	private BigDecimal fixedAmount;
}
