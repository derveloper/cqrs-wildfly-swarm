package donatr.domain;

import com.zanox.rabbiteasy.cdi.ConnectionConfiguration;
import com.zanox.rabbiteasy.cdi.EventBinder;
import donatr.common.AccountCreatedEvent;
import donatr.common.CreateAccountCommand;

@ConnectionConfiguration(
		host = "localhost", port=5672,
		username = "admin", password = "KSzQsW6B9Ea2"
)
public class DomainEventBinder extends EventBinder {
	@Override
	protected void bindEvents() {
		bind(CreateAccountCommand.class)
				.toQueue("command");

		bind(AccountCreatedEvent.class)
				.toExchange("donatr")
				.withRoutingKey("donatr.event")
				.withImmediateDelivery()
				.withMandatoryDelivery()
				.withPublisherConfirms();
	}
}
