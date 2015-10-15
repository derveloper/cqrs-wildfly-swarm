package donatr.domain.account.aggregate;

import donatr.domain.account.command.*;
import donatr.domain.account.event.*;
import donatr.domain.account.repository.AccountTypeConverter;
import lombok.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter @Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Account extends AbstractAnnotatedAggregateRoot<String> {
	@AggregateIdentifier
	@Id
	private String id;
	private String name;
	private String email;
	private BigDecimal balance;
	@Convert(converter = AccountTypeConverter.class)
	private AccountType accountType;

	@CommandHandler
	public Account(CreateAccountCommand command) {
		apply(AccountCreatedEvent.builder()
				.id(command.getId())
				.name(command.getName())
				.accountType(command.getAccountType())
				.email(command.getEmail()).build());
	}

	@CommandHandler
	public void on(ChangeAccountEmailCommand command) {
		apply(AccountEmailChangedEvent.builder()
			.id(command.getId())
			.email(command.getEmail()).build());
	}

	@CommandHandler
	public void on(ChangeAccountTypeCommand command) {
		apply(AccountTypeChangedEvent.builder()
				.id(command.getId())
				.accountType(command.getAccountType()).build());
	}

	@CommandHandler
	public void on(CreditAccountCommand command) {
		apply(AccountCreditedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@CommandHandler
	public void on(DebitAccountCommand command) {
		apply(AccountDebitedEvent.builder()
				.id(command.getId())
				.amount(command.getAmount()).build());
	}

	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
		this.balance = BigDecimal.ZERO;
		this.accountType = event.getAccountType() == null ? AccountType.USER : event.getAccountType();
	}

	@EventSourcingHandler
	public void on(AccountEmailChangedEvent event) {
		this.email = event.getEmail();
	}

	@EventSourcingHandler
	public void on(AccountTypeChangedEvent event) {
		this.accountType = event.getAccountType();
	}

	@EventSourcingHandler
	public void on(AccountCreditedEvent event) {
		this.balance = balance.add(event.getAmount());
	}

	@EventSourcingHandler
	public void on(AccountDebitedEvent event) {
		this.balance = balance.subtract(event.getAmount());
	}
}
