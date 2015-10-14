package donatr.common;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class DonatrCommandGateway {
	@Inject
	CommandBus commandBus;

	@Produces @ApplicationScoped
	public CommandGateway getCommandGateway() {
		return new DefaultCommandGateway(commandBus);
	}
}
