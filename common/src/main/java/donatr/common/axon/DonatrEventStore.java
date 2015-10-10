package donatr.common.axon;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.*;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.UnknownHostException;

@Singleton
public class DonatrEventStore {
	@PersistenceContext
	EntityManager entityManager;

//	@Inject
//	@Named("snapshotEventStore")
//	SnapshotEventStore snapshotEventStore;

	@Produces @Singleton
	public EventStore getEventStore() throws UnknownHostException {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		EventEntryFactory eventEntryFactory = new DefaultEventEntryFactory(true);
		EventEntryStore eventEntryStore = new DefaultEventEntryStore(eventEntryFactory);
		JpaEventStore jpaEventStore = new JpaEventStore(entityManagerProvider, eventEntryStore);
		jpaEventStore.setMaxSnapshotsArchived(5);
		return jpaEventStore;
	}
}
