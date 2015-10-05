package cc.vileda.query.rest;

import cc.vileda.cqrs.common.AccountCreatedEvent;
import cc.vileda.cqrs.common.CreateAccountCommand;
import com.zanox.rabbiteasy.cdi.ConnectionConfiguration;
import com.zanox.rabbiteasy.cdi.EventBinder;

@ConnectionConfiguration(
		host = "localhost", port=5672,
		username = "admin", password = "KSzQsW6B9Ea2"
)
public class QueryRestEventBinder extends EventBinder {
	@Override
	protected void bindEvents() {
		bind(AccountCreatedEvent.class)
				.toQueue("event");
	}
}
