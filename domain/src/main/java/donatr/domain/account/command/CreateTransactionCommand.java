package donatr.domain.account.command;

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
	private String fromAccount;
	private String toAccount;
	private BigDecimal amount;
}
