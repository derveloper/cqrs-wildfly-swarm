package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.AccountEmailChangedEvent;
import donatr.common.ChangeAccountEmailCommand;
import donatr.common.CreateAccountCommand;
import lombok.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

@Getter @Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractAnnotatedAggregateRoot<String> {
	@AggregateIdentifier
	private String id;
	private String name;
	private String email;

	@CommandHandler
	public Account(CreateAccountCommand command) {
		System.out.println(command);
		apply(AccountCreatedEvent.builder()
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

	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		System.out.println("Account " + event);
		this.id = event.getId();
		this.name = event.getName();
		this.email = event.getEmail();
	}

	@EventSourcingHandler
	public void on(AccountEmailChangedEvent event) {
		System.out.println("Account " + event);
		this.email = event.getEmail();
	}

	@Override
	public String getIdentifier() {
		return id;
	}
}
