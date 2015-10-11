package donatr.common.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateTransactionCommand {
	@TargetAggregateIdentifier
	private String id;
	@TargetAggregateIdentifier
	private String fromAccount;
	@TargetAggregateIdentifier
	private String toAccount;
	private BigDecimal amount;
}
