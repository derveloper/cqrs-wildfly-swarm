package donatr.domain;

import donatr.common.CreateAccountCommand;
import donatr.common.DomainConfig;
import donatr.domain.account.Account;
import donatr.domain.account.AccountRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class DomainEventBinder implements DomainConfig {
	@Inject
	CommandBus commandBus;

	@Inject
	EventStore eventStore;

	@Inject
	EventBus eventBus;

	@Inject
	CommandGateway commandGateway;

	@Override
	public void initialize() {
		System.out.println("init handlers");
		AggregateAnnotationCommandHandler.subscribe(Account.class, getAccountRepository(), commandBus);
		final String itemId = UUID.randomUUID().toString();
		commandGateway.send(new CreateAccountCommand(itemId, "foo", "bar@foo.tld"));
	}

	/*@Schedule(hour = "*", minute = "*", persistent = false)
	protected void init(Timer timer)
	{
		final String itemId = UUID.randomUUID().toString();
		commandGateway.send(new CreateAccountCommand(itemId, "foo", "bar@foo.tld"));
		timer.cancel();
	}*/

	@Produces
	public AccountRepository getAccountRepository() {
		AccountRepository repository = new AccountRepository(eventStore);
		repository.setEventBus(eventBus);

		return repository;
	}
}
