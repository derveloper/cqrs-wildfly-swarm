package donatr.websocket;

import donatr.domain.account.event.AccountCreatedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

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
		System.out.println("pushing websocket event " + event);
		sessionHandler.sendToAll(event.toString());
	}
}
