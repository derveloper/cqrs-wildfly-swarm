package donatr.websocket;

import donatr.domain.account.event.ProductAccountCreatedEvent;
import donatr.domain.account.event.UserAccountCreatedEvent;
import donatr.domain.account.event.UserAccountCreditedEvent;
import donatr.domain.account.event.UserAccountDebitedEvent;
import donatr.websocket.message.AccountCreditedEventWebsocketMessage;
import donatr.websocket.message.AccountDebitedEventWebsocketMessage;
import donatr.websocket.message.ProductAccountCreatedEventWebsocketMessage;
import donatr.websocket.message.UserAccountCreatedEventWebsocketMessage;
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
	public void userAccountCreatedEvent(UserAccountCreatedEvent event) throws IOException {
		sessionHandler.sendToAll(UserAccountCreatedEventWebsocketMessage.builder()
				.id(event.getId())
				.name(event.getName())
				.email(event.getEmail())
				.balance(BigDecimal.ZERO)
				.build());
	}

	@EventHandler
	public void productAccountCreatedEvent(ProductAccountCreatedEvent event) throws IOException {
		sessionHandler.sendToAll(ProductAccountCreatedEventWebsocketMessage.builder()
				.id(event.getId())
				.name(event.getName())
				.balance(BigDecimal.ZERO)
				.fixedAmount(event.getFixedAmount())
				.build());
	}

	@EventHandler
	public void accountDebitedEvent(UserAccountDebitedEvent event) throws IOException {
		sessionHandler.sendToAll(AccountDebitedEventWebsocketMessage.builder()
				.id(event.getId())
				.amount(event.getAmount())
				.build());
	}

	@EventHandler
	public void accountCreditedEvent(UserAccountCreditedEvent event) throws IOException {
		sessionHandler.sendToAll(AccountCreditedEventWebsocketMessage.builder()
				.id(event.getId())
				.amount(event.getAmount())
				.build());
	}
}
