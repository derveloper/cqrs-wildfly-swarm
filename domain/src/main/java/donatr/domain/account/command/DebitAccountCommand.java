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
public class DebitAccountCommand {
	@TargetAggregateIdentifier
	private String id;
	private BigDecimal amount;
}
