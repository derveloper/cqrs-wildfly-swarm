package donatr.domain.account.repository;

import lombok.Getter;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.domain.AggregateRoot;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.HybridJpaRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;

import javax.persistence.EntityManager;

@Getter
public class DonatrRepository<T extends AggregateRoot> {
	private final HybridJpaRepository<T> eventSourcingRepository;

	public DonatrRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager, Class<T> clazz) {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		eventSourcingRepository = new HybridJpaRepository<>(entityManagerProvider, clazz);
		eventSourcingRepository.setEventBus(eventBus);
		eventSourcingRepository.setEventStore(eventStore);
	}

	public T load(Object aggregateIdentifier) {
		return load(aggregateIdentifier, null);
	}

	public T load(Object aggregateIdentifier, Long version) {
		UnitOfWork uow;
		if(!CurrentUnitOfWork.isStarted()) {
			uow = DefaultUnitOfWork.startAndGet();
		}
		else {
			uow = CurrentUnitOfWork.get();
		}
		T account = eventSourcingRepository.load(aggregateIdentifier, version);
		uow.commit();
		return account;
	}
}
