package donatr.common.axon;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.*;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.json.JacksonSerializer;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class DonatrEventStore {
	@PersistenceContext
	EntityManager entityManager;


	@Produces @Singleton
	public EventStore getEventStore() {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		EventEntryFactory eventEntryFactory = new DefaultEventEntryFactory(true);
		EventEntryStore eventEntryStore = new DefaultEventEntryStore(eventEntryFactory);
		Serializer serializer = new JacksonSerializer();
		JpaEventStore jpaEventStore = new JpaEventStore(entityManagerProvider, serializer, eventEntryStore);
		jpaEventStore.setMaxSnapshotsArchived(5);
		return jpaEventStore;
	}
}
