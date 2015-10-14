package donatr.domain;

import donatr.common.DomainConfig;
import donatr.domain.account.aggregate.Account;
import donatr.domain.account.aggregate.Transaction;
import donatr.domain.account.handler.AccountEventHandler;
import donatr.domain.account.repository.AccountRepository;
import donatr.domain.account.repository.TransactionRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DomainEventBinder implements DomainConfig {
	@Inject
	CommandBus commandBus;

	@Inject
	EventBus eventBus;

	@Inject
	CommandGateway commandGateway;

	@Inject
	AccountRepository accountRepository;

	@Inject
	TransactionRepository transactionRepository;


	@Override
	public void initialize() {
		System.out.println("init handlers");
		AnnotationEventListenerAdapter.subscribe(new AccountEventHandler(commandGateway), eventBus);
		AggregateAnnotationCommandHandler.subscribe(Account.class, accountRepository.getEventSourcingRepository(), commandBus);
		AggregateAnnotationCommandHandler.subscribe(Transaction.class, transactionRepository.getEventSourcingRepository(), commandBus);
	}
}
