package donatr.rest;

import donatr.common.AccountCreatedEvent;
import com.zanox.rabbiteasy.cdi.ConnectionConfiguration;
import com.zanox.rabbiteasy.cdi.EventBinder;

@ConnectionConfiguration(
		host = "localhost", port=5672,
		username = "admin", password = "KSzQsW6B9Ea2"
)
public class RestApiEventBinder extends EventBinder {
	@Override
	protected void bindEvents() {
		bind(AccountCreatedEvent.class)
				.toQueue("event");
	}
}
