package cc.vileda.frontend;

import cc.vileda.cqrs.common.AccountCreatedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AccountEventListener {

	public void on(@Observes AccountCreatedEvent event) {
		System.out.println("+++EVENT REMOTE" + event.toString());
	}
}
