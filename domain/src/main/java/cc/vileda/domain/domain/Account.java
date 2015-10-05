package cc.vileda.domain.domain;

import cc.vileda.cqrs.common.AccountCreatedEvent;
import cc.vileda.cqrs.common.CreateAccountCommand;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@RequestScoped
public class Account {
	@Inject
	AccountCreatedEvent accountCreatedEvent;
	@Inject
	Event<AccountCreatedEvent> accountCreatedEventBus;

	private String id;

	public Account() {
	}

	public void on(@Observes CreateAccountCommand command) {
		System.out.println("+++COMMAND " + command);
		id = command.getId();
		accountCreatedEvent.setId(id);
		accountCreatedEventBus.fire(accountCreatedEvent);
	}
}
