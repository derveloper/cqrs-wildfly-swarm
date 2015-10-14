package donatr.common;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.DefaultEventEntryFactory;
import org.axonframework.eventstore.jpa.DefaultEventEntryStore;
import org.axonframework.eventstore.jpa.EventEntryStore;
import org.axonframework.eventstore.jpa.JpaEventStore;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.json.JacksonSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class DonatrEventStore {
	@PersistenceContext
	private EntityManager entityManager;


	@Produces @Singleton
	public EventStore getEventStore() {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		DefaultEventEntryFactory eventEntryFactory = new DefaultEventEntryFactory(true);
		@SuppressWarnings("unchecked")
		EventEntryStore eventEntryStore = new DefaultEventEntryStore(eventEntryFactory);
		Serializer serializer = new JacksonSerializer();
		JpaEventStore jpaEventStore = new JpaEventStore(entityManagerProvider, serializer, eventEntryStore);
		jpaEventStore.setMaxSnapshotsArchived(5);
		return jpaEventStore;
	}
}
