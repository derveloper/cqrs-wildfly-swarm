package donatr.common;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class DonatrCommandBus {
	@Produces @Singleton
	public CommandBus getCommandBus()
	{
		return new SimpleCommandBus();
	}
}
