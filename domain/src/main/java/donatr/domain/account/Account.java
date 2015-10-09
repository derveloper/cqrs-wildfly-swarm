package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

@Getter @Setter
@NoArgsConstructor
public class Account extends AbstractAnnotatedAggregateRoot {
	@AggregateIdentifier
	private String id;

	@CommandHandler
	public Account(CreateAccountCommand command) {
		System.out.println(command);
		apply(AccountCreatedEvent.builder()
				.id(command.getId())
				.name(command.getName())
				.email(command.getEmail()));
	}

	@EventHandler
	public void on(AccountCreatedEvent event) {
		System.out.println(event);
		this.id = event.getId();
	}
}
