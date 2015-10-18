package donatr.domain.account.repository;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.saga.ResourceInjector;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.SimpleResourceInjector;
import org.axonframework.saga.repository.jpa.JpaSagaRepository;

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

	@Inject
	CommandGateway commandGateway;


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

	@Produces @Singleton
	public SagaRepository getTransactionSagaRepository() {
		System.out.println("init tx repository");
		JpaSagaRepository jpaSagaRepository = new JpaSagaRepository(new SimpleEntityManagerProvider(entityManager));
		ResourceInjector injector = new SimpleResourceInjector(commandGateway, getProductEventRepository());
		jpaSagaRepository.setResourceInjector(injector);
		return jpaSagaRepository;
		//return new TransactionRepository(eventStore, eventBus, entityManager);
	}
}
