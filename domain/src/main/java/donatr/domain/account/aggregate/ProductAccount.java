package donatr.domain.account.aggregate;

import donatr.domain.account.command.CreateProductAccountCommand;
import donatr.domain.account.command.CreditProductAccountCommand;
import donatr.domain.account.command.DebitProductAccountCommand;
import donatr.domain.account.event.ProductAccountCreatedEvent;
import donatr.domain.account.event.ProductAccountCreditedEvent;
import donatr.domain.account.event.ProductAccountDebitedEvent;
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
public class ProductAccount extends AbstractAnnotatedAggregateRoot<String> {
	@AggregateIdentifier
	@Id
	private String id;
	private String name;
	private BigDecimal balance;
	private BigDecimal fixedAmount;

	@CommandHandler
	public ProductAccount(CreateProductAccountCommand command) {
		apply(ProductAccountCreatedEvent.builder()
				.id(command.getId())
				.fixedAmount(command.getFixedAmount())
				.name(command.getName()).build());
	}

	@CommandHandler
	public void on(CreditProductAccountCommand command) {
		apply(ProductAccountCreditedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@CommandHandler
	public void on(DebitProductAccountCommand command) {
		apply(ProductAccountDebitedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@EventSourcingHandler
	public void on(ProductAccountCreatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.fixedAmount = event.getFixedAmount();
		this.balance = BigDecimal.ZERO;
	}

	@EventSourcingHandler
	public void on(ProductAccountCreditedEvent event) {
		this.balance = balance.add(event.getAmount());
	}

	@EventSourcingHandler
	public void on(ProductAccountDebitedEvent event) {
		this.balance = balance.subtract(event.getAmount());
	}
}
