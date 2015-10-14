package donatr.domain.account.repository;

import donatr.domain.account.aggregate.Account;
import lombok.Getter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.persistence.EntityManager;
import java.util.List;

@Getter
public class AccountRepository extends DonatrRepository<Account> {
	public AccountRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		super(eventStore, eventBus, entityManager, Account.class);
	}

	public List<Account> getAllAccounts() {
		return entityManager.createQuery("select account from Account as account", Account.class).getResultList();
	}
}
