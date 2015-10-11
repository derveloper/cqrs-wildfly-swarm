package donatr.domain.account.repository;

import donatr.domain.account.aggregate.Account;
import lombok.Getter;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.HybridJpaRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.saga.repository.jpa.JpaSagaRepository;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;

import javax.persistence.EntityManager;

@Getter
public class AccountSagaRepository {
	private HybridJpaRepository<Account> eventSourcingRepository;

	public AccountSagaRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		JpaSagaRepository sagaRepository = new JpaSagaRepository(entityManagerProvider);

		eventSourcingRepository = new HybridJpaRepository<>(entityManagerProvider, Account.class);
		eventSourcingRepository.setEventBus(eventBus);
		eventSourcingRepository.setEventStore(eventStore);
	}

	public Account load(Object aggregateIdentifier) {
		return load(aggregateIdentifier, null);
	}

	public Account load(Object aggregateIdentifier, Long version) {
		UnitOfWork uow;
		if(!CurrentUnitOfWork.isStarted()) {
			uow = DefaultUnitOfWork.startAndGet();
		}
		else {
			uow = CurrentUnitOfWork.get();
		}
		Account account = eventSourcingRepository.load(aggregateIdentifier, version);
		uow.commit();
		return account;
	}
}
