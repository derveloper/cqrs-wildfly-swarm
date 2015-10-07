package donatr.eventstore;

import donatr.common.AccountCreatedEvent;
import donatr.common.DonatrEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class EventstoreConsumer {
	@Inject
	EventRepository repository;

	public void accountCreatedEvent(@Observes AccountCreatedEvent event) throws IOException {
		System.out.println("persisting event " + event);
		repository.save(event);
	}
}
