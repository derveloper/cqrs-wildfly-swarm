package donatr.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class WebsocketSessionHandler {
	private final Set<Session> sessions = new HashSet<>();

	public void sendToAll(Object message) {
		sessions.stream().forEach(session -> {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String asString = objectMapper.writeValueAsString(message);
				session.getBasicRemote().sendText(asString);
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
