package donatr.rest.websocket;

import donatr.common.AccountCreatedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ApplicationScoped
@ServerEndpoint("/socket/accounts")
public class AccountWebsocketServer {
	@Inject
	AccountWebsocketSessionHandler sessionHandler;

	@OnOpen
	public void setSession(Session session) {
		sessionHandler.add(session);
	}

	@OnClose
	public void removeSession(Session session) {
		sessionHandler.remove(session);
	}

	public void accountCreatedEvent(@Observes AccountCreatedEvent event) throws IOException {
		System.out.println("pushing websocket event " + event);
		sessionHandler.sendToAll(event.toString());
	}
}
