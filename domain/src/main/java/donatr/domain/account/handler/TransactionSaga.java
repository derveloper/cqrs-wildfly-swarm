package donatr.domain.account.handler;

import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.command.CreditProductAccountCommand;
import donatr.domain.account.command.DebitUserAccountCommand;
import donatr.domain.account.event.TransactionCreatedEvent;
import donatr.domain.account.repository.ProductAccountRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;

import java.math.BigDecimal;

public class TransactionSaga extends AbstractAnnotatedSaga {
	private transient CommandGateway commandGateway;
	private transient ProductAccountRepository productAccountRepository;

	public TransactionSaga() {
		System.out.println("init tx saga");
	}

	@StartSaga
	@SagaEventHandler(associationProperty = "id")
	public void handle(TransactionCreatedEvent event) {
		System.out.println("SAGA " + event);
		// associate the Saga with these values, before sending the commands (2)
		//associateWith("toAccount", event.getToAccount());
		//associateWith("fromAccount", event.getFromAccount());
		ProductAccount productAccount = productAccountRepository.load(event.getToAccount());
		BigDecimal amount = event.getAmount();
		if(productAccount != null) {
			amount = productAccount.getFixedAmount();
			commandGateway.send(CreditProductAccountCommand.builder()
					.id(event.getToAccount())
					.amount(amount)
					.build());
		}
		commandGateway.send(DebitUserAccountCommand.builder()
				.id(event.getFromAccount())
				.amount(amount)
				.build());
	}

	public TransactionSaga setCommandGateway(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
		return this;
	}

	public TransactionSaga setProductAccountRepository(ProductAccountRepository productAccountRepository) {
		this.productAccountRepository = productAccountRepository;
		return this;
	}
}
