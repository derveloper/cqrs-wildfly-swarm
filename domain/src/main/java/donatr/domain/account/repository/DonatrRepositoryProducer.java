package donatr.domain.account.repository;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.repository.jpa.JpaSagaRepository;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
public class DonatrRepositoryProducer {
	@PersistenceContext
	EntityManager entityManager;

	@Inject
	EventStore eventStore;

	@Inject
	EventBus eventBus;


	@Produces @Singleton
	public AccountRepository getEventRepository() {
		System.out.println("init repository");
		return new AccountRepository(eventStore, eventBus, entityManager);
	}

	@Produces @Singleton
	public TransactionRepository getTransactionRepository() {
		System.out.println("init repository");
		return new TransactionRepository(eventStore, eventBus, entityManager);
	}

	@Produces @Singleton
	public SagaRepository getSagaRepository() {
		System.out.println("init saga repository");
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		return new JpaSagaRepository(entityManagerProvider);
	}
}
