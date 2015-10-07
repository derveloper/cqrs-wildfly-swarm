package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;
import donatr.common.domain.model.AccountModel;

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
		AccountModel accountModel = command.getPayload();
		AccountEntry accountEntry = AccountEntry.builder()
				.id(UUID.randomUUID().toString())
				.name(accountModel.getName())
				.build();
		accountRepository.save(accountEntry);
		accountCreatedEventBus.fire(new AccountCreatedEvent(accountEntry.getId()));
	}
}
