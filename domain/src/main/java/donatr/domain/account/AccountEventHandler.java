package donatr.domain.account;

import donatr.common.AccountCreatedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;

public class AccountEventHandler {
	@EventHandler
	public void on(AccountCreatedEvent event) {
		System.out.println("AccountHandler " + event);
	}
}
