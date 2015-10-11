package donatr.domain.account.aggregate;

import donatr.common.command.CreateTransactionCommand;
import donatr.common.event.TransactionCreatedEvent;
import lombok.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter @Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Transaction extends AbstractAnnotatedAggregateRoot<String> {
	@AggregateIdentifier
	@Id
	private String id;
	@AggregateIdentifier
	private String fromAccount;
	@AggregateIdentifier
	private String toAccount;
	private BigDecimal amount;

	@CommandHandler
	public Transaction(CreateTransactionCommand command) {
		apply(TransactionCreatedEvent.builder()
				.id(command.getId())
				.fromAccount(command.getFromAccount())
				.toAccount(command.getToAccount())
				.amount(command.getAmount()).build());
	}

	@EventSourcingHandler
	public void on(TransactionCreatedEvent event) {
		this.id = event.getId();
		this.fromAccount = event.getFromAccount();
		this.toAccount = event.getToAccount();
		this.amount = event.getAmount();
	}

	@Override
	public String getIdentifier() {
		return id;
	}
}
