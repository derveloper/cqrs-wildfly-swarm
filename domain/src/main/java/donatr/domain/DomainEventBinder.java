package donatr.domain;

import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;
import com.zanox.rabbiteasy.cdi.ConnectionConfiguration;
import com.zanox.rabbiteasy.cdi.EventBinder;

@ConnectionConfiguration(
		host = "localhost", port=5672,
		username = "admin", password = "KSzQsW6B9Ea2"
)
public class DomainEventBinder extends EventBinder {
	@Override
	protected void bindEvents() {
		bind(CreateAccountCommand.class)
				.toExchange("donatr").withRoutingKey("command");
		bind(AccountCreatedEvent.class)
				.toExchange("donatr").withRoutingKey("event");
	}
}
