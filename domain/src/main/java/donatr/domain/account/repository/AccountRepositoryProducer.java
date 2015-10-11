package donatr.domain.account.repository;

import donatr.domain.account.Account;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.SnapshotEventStore;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class AccountRepositoryProducer {
	@PersistenceContext
	EntityManager entityManager;

	@Inject
	EventStore eventStore;

	@Inject
	EventBus eventBus;

	private UserTransaction getUserTransaction() throws NamingException {
		return ((UserTransaction) new InitialContext().lookup("java:jboss/UserTransaction"));
	}

	@Produces @Singleton
	public AccountRepository getEventRepository() {
		System.out.println("init repository");
		return new AccountRepository(eventStore, eventBus, getSnapshotterTrigger(), entityManager);
	}

	public SnapshotterTrigger getSnapshotterTrigger() {
		EventCountSnapshotterTrigger snapshotterTrigger = new EventCountSnapshotterTrigger();
		snapshotterTrigger.setTrigger(1);
		AggregateSnapshotter snapshotter = new AggregateSnapshotter();
		List<AggregateFactory<?>> aggregateFactories = new ArrayList<>();
		aggregateFactories.add(new GenericAggregateFactory<>(Account.class));
		snapshotter.setAggregateFactories(aggregateFactories);
		snapshotter.setEventStore((SnapshotEventStore) eventStore);
		snapshotterTrigger.setSnapshotter(snapshotter);
		return snapshotterTrigger;
	}
}
