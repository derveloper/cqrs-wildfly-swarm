package donatr.domain;

import donatr.common.DomainConfig;
import donatr.common.command.ChangeAccountEmailCommand;
import donatr.common.command.CreateAccountCommand;
import donatr.domain.account.Account;
import donatr.domain.account.handler.AccountEventHandler;
import donatr.domain.account.repository.AccountRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.Timer;
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

	String itemId;
	private boolean createAccount = true;

	@Override
	public void initialize() {
		System.out.println("init handlers");
		itemId = UUID.randomUUID().toString();
		AggregateAnnotationCommandHandler.subscribe(Account.class, eventRepository, commandBus);
		AnnotationEventListenerAdapter.subscribe(new AccountEventHandler(), eventBus);
	}

	@Asynchronous
	public Future<String> publishCommand() {
		commandGateway.sendAndWait(ChangeAccountEmailCommand.builder().id(itemId).email("bar2@change.tld").build());

		System.out.println("published command");

		return new AsyncResult<>(itemId);
	}

	//@Schedule(hour = "*", minute = "*", second = "*/10", persistent = true)
	protected void init(Timer timer) throws ExecutionException, InterruptedException {
		if(createAccount) {
			commandGateway.sendAndWait(CreateAccountCommand.builder().id(itemId).name("foo2").email("bar2@foo.tld").build());
			createAccount = false;
		}
		//timer.cancel();
		String s = publishCommand().get();
		Account load = eventRepository.load(s);
		System.out.println(load);
	}
}
