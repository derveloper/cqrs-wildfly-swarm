package donatr.common.axon;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class DonatrSnapshotEventStore {
	@PersistenceContext
	EntityManager entityManager;

	/*@Produces @Singleton @Named("snapshotEventStore")
	public SnapshotEventStore getSnapshotEventStore() throws UnknownHostException {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		EventEntryFactory eventEntryFactory = new DefaultEventEntryFactory(true);
		EventEntryStore eventEntryStore = new DefaultEventEntryStore(eventEntryFactory);
		SnapshotEventStore snapshotEventStore = new JpaEventStore(entityManagerProvider, eventEntryStore);
		return snapshotEventStore;
	}*/
}
