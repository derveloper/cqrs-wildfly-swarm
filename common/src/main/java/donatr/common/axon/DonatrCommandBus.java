package donatr.common.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.naming.NamingException;

@Singleton
public class DonatrCommandBus {
	@Produces @Singleton
	public CommandBus getCommandBus() throws NamingException {
		SimpleCommandBus simpleCommandBus = new SimpleCommandBus();
		return simpleCommandBus;
	}
}
