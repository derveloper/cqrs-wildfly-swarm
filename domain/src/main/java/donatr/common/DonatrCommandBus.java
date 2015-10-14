package donatr.common;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class DonatrCommandBus {
	@Produces @ApplicationScoped
	public CommandBus getCommandBus()
	{
		return new SimpleCommandBus();
	}
}
