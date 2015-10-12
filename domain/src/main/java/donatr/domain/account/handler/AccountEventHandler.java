package donatr.domain.account.handler;

import donatr.domain.account.command.CreditAccountCommand;
import donatr.domain.account.command.DebitAccountCommand;
import donatr.domain.account.event.TransactionCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.annotation.EventHandler;

public class AccountEventHandler {
	private final CommandGateway commandGateway;

	public AccountEventHandler(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@EventHandler
	public void handle(TransactionCreatedEvent event) {
		System.out.println("SAGA " + event);
		commandGateway.send(DebitAccountCommand.builder()
				.id(event.getFromAccount())
				.amount(event.getAmount())
				.build());
		commandGateway.send(CreditAccountCommand.builder()
				.id(event.getToAccount())
				.amount(event.getAmount())
				.build());
	}
}
