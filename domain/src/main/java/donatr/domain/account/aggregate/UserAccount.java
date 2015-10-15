package donatr.domain.account.aggregate;

import donatr.domain.account.command.ChangeAccountEmailCommand;
import donatr.domain.account.command.CreateUserAccountCommand;
import donatr.domain.account.command.CreditUserAccountCommand;
import donatr.domain.account.command.DebitUserAccountCommand;
import donatr.domain.account.event.AccountEmailChangedEvent;
import donatr.domain.account.event.UserAccountCreatedEvent;
import donatr.domain.account.event.UserAccountCreditedEvent;
import donatr.domain.account.event.UserAccountDebitedEvent;
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
public class UserAccount extends AbstractAnnotatedAggregateRoot<String> {
	@AggregateIdentifier
	@Id
	private String id;
	private String name;
	private String email;
	private BigDecimal balance;

	@CommandHandler
	public UserAccount(CreateUserAccountCommand command) {
		apply(UserAccountCreatedEvent.builder()
				.id(command.getId())
				.name(command.getName())
				.email(command.getEmail()).build());
	}

	@CommandHandler
	public void on(ChangeAccountEmailCommand command) {
		apply(AccountEmailChangedEvent.builder()
			.id(command.getId())
			.email(command.getEmail()).build());
	}

	@CommandHandler
	public void on(CreditUserAccountCommand command) {
		apply(UserAccountCreditedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@CommandHandler
	public void on(DebitUserAccountCommand command) {
		apply(UserAccountDebitedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@EventSourcingHandler
	public void on(UserAccountCreatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
		this.balance = BigDecimal.ZERO;
	}

	@EventSourcingHandler
	public void on(AccountEmailChangedEvent event) {
		this.email = event.getEmail();
	}

	@EventSourcingHandler
	public void on(UserAccountCreditedEvent event) {
		this.balance = balance.add(event.getAmount());
	}

	@EventSourcingHandler
	public void on(UserAccountDebitedEvent event) {
		this.balance = balance.subtract(event.getAmount());
	}
}
