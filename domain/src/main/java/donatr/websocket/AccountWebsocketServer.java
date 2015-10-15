package donatr.websocket;

import donatr.domain.account.event.AccountCreatedEvent;
import donatr.domain.account.event.AccountCreditedEvent;
import donatr.domain.account.event.AccountDebitedEvent;
import donatr.websocket.message.AccountCreatedEventWebsocketMessage;
import donatr.websocket.message.AccountCreditedEventWebsocketMessage;
import donatr.websocket.message.AccountDebitedEventWebsocketMessage;
import org.axonframework.eventhandling.annotation.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigDecimal;

@ApplicationScoped
@ServerEndpoint("/socket")
public class AccountWebsocketServer {
	@Inject
	WebsocketSessionHandler sessionHandler;

	@OnOpen
	public void setSession(Session session) {
		sessionHandler.add(session);
	}

	@OnClose
	public void removeSession(Session session) {
		sessionHandler.remove(session);
	}

	@EventHandler
	public void accountCreatedEvent(AccountCreatedEvent event) throws IOException {
		sessionHandler.sendToAll(AccountCreatedEventWebsocketMessage.builder()
				.id(event.getId())
				.name(event.getName())
				.balance(BigDecimal.ZERO)
				.accountType(event.getAccountType())
				.build());
	}

	@EventHandler
	public void accountDebitedEvent(AccountDebitedEvent event) throws IOException {
		sessionHandler.sendToAll(AccountDebitedEventWebsocketMessage.builder()
				.id(event.getId())
				.amount(event.getAmount())
				.build());
	}

	@EventHandler
	public void accountCreditedEvent(AccountCreditedEvent event) throws IOException {
		sessionHandler.sendToAll(AccountCreditedEventWebsocketMessage.builder()
				.id(event.getId())
				.amount(event.getAmount())
				.build());
	}
}
