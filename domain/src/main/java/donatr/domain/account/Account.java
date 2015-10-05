package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class Account {
	@Inject
	Event<AccountCreatedEvent> accountCreatedEventBus;
	@PersistenceContext
	EntityManager entityManager;

	private String id;

	public void on(@Observes CreateAccountCommand command) {
		System.out.println("+++COMMAND " + command);
		id = command.getId();
		entityManager.persist(new AccountEntry(id));
		entityManager.flush();
		accountCreatedEventBus.fire(new AccountCreatedEvent(id));
	}
}
