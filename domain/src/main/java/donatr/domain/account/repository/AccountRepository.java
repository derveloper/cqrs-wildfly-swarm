package donatr.domain.account.repository;

import donatr.domain.account.Account;
import lombok.Getter;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.HybridJpaRepository;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventstore.EventStore;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;

import javax.persistence.EntityManager;

@Getter
public class AccountRepository {
	private HybridJpaRepository<Account> eventSourcingRepository;

	public AccountRepository(EventStore eventStore, EventBus eventBus, SnapshotterTrigger snapshotterTrigger, EntityManager entityManager) {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		eventSourcingRepository = new HybridJpaRepository<>(entityManagerProvider, Account.class);
		eventSourcingRepository.setEventBus(eventBus);
		eventSourcingRepository.setEventStore(eventStore);
		//eventSourcingRepository.setSnapshotterTrigger(snapshotterTrigger);
	}

	public Account load(Object aggregateIdentifier) {
		UnitOfWork uow;
		if(!CurrentUnitOfWork.isStarted()) {
			uow = DefaultUnitOfWork.startAndGet();
		}
		else {
			uow = CurrentUnitOfWork.get();
		}
		Account account = eventSourcingRepository.load(aggregateIdentifier, null);
		uow.commit();
		return account;
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
