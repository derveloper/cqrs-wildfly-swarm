package donatr.domain;

import donatr.common.ChangeAccountEmailCommand;
import donatr.common.CreateAccountCommand;
import donatr.common.DomainConfig;
import donatr.domain.account.Account;
import donatr.domain.account.AccountEventHandler;
import donatr.domain.account.AccountRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Singleton
public class DomainEventBinder implements DomainConfig {
	@Inject
	CommandBus commandBus;

	@Inject
	EventBus eventBus;

	@Inject
	CommandGateway commandGateway;

	@Inject
	AccountRepository eventRepository;

	@Override
	public void initialize() {
		System.out.println("init handlers");
		AggregateAnnotationCommandHandler.subscribe(Account.class, eventRepository, commandBus);
		AnnotationEventListenerAdapter.subscribe(new AccountEventHandler(), eventBus);
	}

	@Asynchronous
	public Future<String> publishCommand() {
		String itemId = UUID.randomUUID().toString();

		commandGateway.sendAndWait(CreateAccountCommand.builder().id(itemId).name("foo2").email("bar2@foo.tld").build());
		commandGateway.sendAndWait(ChangeAccountEmailCommand.builder().id(itemId).email("bar2@change.tld").build());

		System.out.println("published command");

		return new AsyncResult<>(itemId);
	}

	@Schedule(hour = "*", minute = "*", second = "*/10", persistent = true)
	protected void init(Timer timer) throws ExecutionException, InterruptedException {
		//timer.cancel();
		String s = publishCommand().get();
		Account load = eventRepository.load(s);
		System.out.println(load);
	}
}
