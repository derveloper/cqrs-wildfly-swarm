package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Stateless
public class Account {
	@Inject
	Event<AccountCreatedEvent> accountCreatedEventBus;
	@Inject
	AccountRepository accountRepository;

	public void on(@Observes CreateAccountCommand command) {
		System.out.println("+++COMMAND " + command);
		accountRepository.save(new AccountEntry(command.getId()));
		accountCreatedEventBus.fire(new AccountCreatedEvent(command.getId()));
	}
}
