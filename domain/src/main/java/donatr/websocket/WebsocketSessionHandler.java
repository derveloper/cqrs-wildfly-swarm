package donatr.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class WebsocketSessionHandler {
	private final Set<Session> sessions = new HashSet<>();

	public void sendToAll(String message) {
		sessions.stream().forEach(session -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				System.out.println("could not send " + message + " to " + session);
			}
		});
	}

	public void add(Session session) {
		sessions.add(session);
	}

	public void remove(Session session) {
		sessions.remove(session);
	}
}
