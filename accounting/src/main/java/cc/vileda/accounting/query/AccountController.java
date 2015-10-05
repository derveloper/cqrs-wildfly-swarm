package cc.vileda.accounting.query;

import cc.vileda.cqrs.common.CreateAccountCommand;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/accounts")
@ApplicationScoped
public class AccountController {
	@Inject
	CreateAccountCommand createAccountCommand;
	@Inject
	Event<CreateAccountCommand> createAccountCommandBus;

	@GET
	@Path("{id}")
	@Produces("application/json")
	public AccountEntry getAccount(@PathParam("id") String id) {
		System.out.println("+++ACCOUNTS_QUERY " + id);
		createAccountCommand.setId(id);
		createAccountCommandBus.fire(createAccountCommand);
		AccountEntry accountEntry = new AccountEntry();
		accountEntry.setId(id);
		return accountEntry;
	}
}
