package donatr.domain.account.handler;

import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.command.CreditProductAccountCommand;
import donatr.domain.account.command.DebitUserAccountCommand;
import donatr.domain.account.event.TransactionCreatedEvent;
import donatr.domain.account.repository.ProductAccountRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.annotation.EventHandler;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public class AccountEventHandler {
	private ProductAccountRepository productAccountRepository;
	private CommandGateway commandGateway;

	public AccountEventHandler(ProductAccountRepository productAccountRepository, CommandGateway commandGateway) {
		this.productAccountRepository = productAccountRepository;
		this.commandGateway = commandGateway;
	}

	@EventHandler
	@Transactional
	public void handle(TransactionCreatedEvent event) {
		System.out.println("SAGA " + event);
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
}
