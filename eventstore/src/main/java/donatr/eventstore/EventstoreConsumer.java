package donatr.eventstore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventstoreConsumer {
	@Inject
	EventRepository repository;

}
