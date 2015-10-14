package donatr.common;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class DonatrEventBus {
	@Produces @Singleton
	public EventBus getEventBus() {
		return new SimpleEventBus();
	}
}
