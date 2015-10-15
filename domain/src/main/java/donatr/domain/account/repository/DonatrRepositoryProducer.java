package donatr.domain.account.repository;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@ApplicationScoped
public class DonatrRepositoryProducer {
	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	EventStore eventStore;

	@Inject
	EventBus eventBus;


	@Produces @Singleton
	public UserAccountRepository getEventRepository() {
		System.out.println("init event repository");
		return new UserAccountRepository(eventStore, eventBus, entityManager);
	}

	@Produces @Singleton
	public ProductAccountRepository getProductEventRepository() {
		System.out.println("init event repository");
		return new ProductAccountRepository(eventStore, eventBus, entityManager);
	}

	@Produces @Singleton
	public TransactionRepository getTransactionRepository() {
		System.out.println("init tx repository");
		return new TransactionRepository(eventStore, eventBus, entityManager);
	}
}
