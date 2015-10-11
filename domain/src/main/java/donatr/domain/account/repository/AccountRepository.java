package donatr.domain.account.repository;

import donatr.domain.account.aggregate.Account;
import lombok.Getter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.persistence.EntityManager;

@Getter
public class AccountRepository extends DonatrRepository<Account> {
	public AccountRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		super(eventStore, eventBus, entityManager, Account.class);
	}
}
