package donatr.domain.account.repository;

import donatr.domain.account.aggregate.ProductAccount;
import lombok.Getter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.persistence.EntityManager;
import java.util.List;

@Getter
public class ProductAccountRepository extends DonatrRepository<ProductAccount> {
	public ProductAccountRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		super(eventStore, eventBus, entityManager, ProductAccount.class);
	}

	public List<ProductAccount> getAllAccounts() {
		return entityManager.createQuery("select account from ProductAccount as account", ProductAccount.class).getResultList();
	}
}
