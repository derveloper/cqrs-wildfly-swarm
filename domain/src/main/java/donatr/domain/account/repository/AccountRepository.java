package donatr.domain.account.repository;

import donatr.domain.account.Account;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventstore.EventStore;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;

public class AccountRepository extends EventSourcingRepository<Account> {
	public AccountRepository(EventStore eventStore, EventBus eventBus, SnapshotterTrigger snapshotterTrigger) {
		super(Account.class, eventStore);
		setEventBus(eventBus);
		setSnapshotterTrigger(snapshotterTrigger);
	}

	@Override
	public Account load(Object aggregateIdentifier) {
		UnitOfWork uow = DefaultUnitOfWork.startAndGet();
		Account load = super.load(aggregateIdentifier);
		uow.commit();
		return load;
	}
}
