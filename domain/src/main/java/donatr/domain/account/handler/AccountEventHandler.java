package donatr.domain.account.handler;

import donatr.common.command.CreditAccountCommand;
import donatr.common.command.DebitAccountCommand;
import donatr.common.event.TransactionCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.annotation.EventHandler;

public class AccountEventHandler {
	private CommandGateway commandGateway;

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
