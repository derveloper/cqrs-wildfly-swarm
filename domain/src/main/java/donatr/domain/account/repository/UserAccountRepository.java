package donatr.domain.account.repository;

import donatr.domain.account.aggregate.UserAccount;
import lombok.Getter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.persistence.EntityManager;
import java.util.List;

@Getter
public class UserAccountRepository extends DonatrRepository<UserAccount> {
	public UserAccountRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		super(eventStore, eventBus, entityManager, UserAccount.class);
	}

	public List<UserAccount> getAllAccounts() {
		return entityManager.createQuery("select account from UserAccount as account", UserAccount.class).getResultList();
	}
}
