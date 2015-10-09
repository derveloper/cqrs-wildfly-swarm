package donatr.common;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.File;

@Singleton
public class AxonConfiguration {
	@Produces
	public CommandBus getCommandBus() {
		return new SimpleCommandBus();
	}

	@Produces
	public CommandGateway getCommandGateway() {
		return new DefaultCommandGateway(getCommandBus());
	}

	@Produces
	public EventStore getEventStore() {
		return new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
	}

	@Produces
	public EventBus getEventBus() {
		return new SimpleEventBus();
	}
}
