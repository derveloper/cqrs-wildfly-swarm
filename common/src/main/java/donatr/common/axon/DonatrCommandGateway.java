package donatr.common.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DonatrCommandGateway {
	@Inject
	CommandBus commandBus;

	@Produces @Singleton
	public CommandGateway getCommandGateway() {
		return new DefaultCommandGateway(commandBus);
	}
}
