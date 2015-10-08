package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.UUID;

@Stateless
public class Account {
	@Inject
	Event<AccountCreatedEvent> accountCreatedEventBus;
	@Inject
	AccountRepository accountRepository;

	public void on(@Observes CreateAccountCommand command) {
		System.out.println("+++COMMAND " + command);
		AccountEntry accountEntry = AccountEntry.builder()
				.id(UUID.randomUUID().toString())
				.name(command.getName())
				.email(command.getEmail())
				.build();
		accountRepository.save(accountEntry);
		accountCreatedEventBus.fire(AccountCreatedEvent.builder()
				.id(accountEntry.getId())
				.name(accountEntry.getName())
				.build());
	}
}
