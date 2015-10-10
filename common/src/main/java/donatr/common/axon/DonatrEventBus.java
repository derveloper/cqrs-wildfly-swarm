package donatr.common.axon;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class DonatrEventBus {
	@Produces @Singleton
	public EventBus getEventBus() {
		return new SimpleEventBus();
	}
}
