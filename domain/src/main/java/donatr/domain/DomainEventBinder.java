package donatr.domain;

import donatr.common.DomainConfig;
import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.aggregate.Transaction;
import donatr.domain.account.aggregate.UserAccount;
import donatr.domain.account.handler.TransactionSaga;
import donatr.domain.account.repository.ProductAccountRepository;
import donatr.domain.account.repository.TransactionRepository;
import donatr.domain.account.repository.UserAccountRepository;
import donatr.websocket.AccountWebsocketServer;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.saga.GenericSagaFactory;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.SimpleResourceInjector;
import org.axonframework.saga.annotation.AnnotatedSagaManager;

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
	UserAccountRepository userAccountRepository;

	@Inject
	ProductAccountRepository productAccountRepository;

	@Inject
	TransactionRepository transactionRepository;

	@Inject
	AccountWebsocketServer websocketEventHandler;

	@Inject
	SagaRepository sagaRepository;

	@Override
	public void initialize() {
		System.out.println("init handlers");
		AnnotationEventListenerAdapter.subscribe(websocketEventHandler, eventBus);
		//AnnotationEventListenerAdapter.subscribe(new AccountEventHandler(productAccountRepository, commandGateway), eventBus);
		GenericSagaFactory sagaFactory = new GenericSagaFactory();
		sagaFactory.setResourceInjector(new SimpleResourceInjector(commandGateway, productAccountRepository));
		AnnotatedSagaManager sagaManager = new AnnotatedSagaManager(
				sagaRepository, sagaFactory, eventBus, TransactionSaga.class);
		sagaManager.subscribe();
		//AnnotationEventListenerAdapter.subscribe(sagaManager, eventBus);
		AggregateAnnotationCommandHandler.subscribe(UserAccount.class, userAccountRepository.getEventSourcingRepository(), commandBus);
		AggregateAnnotationCommandHandler.subscribe(ProductAccount.class, productAccountRepository.getEventSourcingRepository(), commandBus);
		AggregateAnnotationCommandHandler.subscribe(Transaction.class, transactionRepository.getEventSourcingRepository(), commandBus);
	}
}
