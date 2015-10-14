package donatr.common;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class DonatrEventBus {
	@Produces @ApplicationScoped
	public EventBus getEventBus() {
		return new SimpleEventBus();
	}
}
