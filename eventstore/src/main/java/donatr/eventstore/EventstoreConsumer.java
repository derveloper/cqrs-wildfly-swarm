package donatr.eventstore;

import donatr.common.DonatrEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;

@ApplicationScoped
public class EventstoreConsumer {
	public void accountCreatedEvent(@Observes DonatrEvent event) throws IOException {
		System.out.println("persisting event " + event);
	}
}
