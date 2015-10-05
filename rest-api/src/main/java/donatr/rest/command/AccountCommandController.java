package donatr.rest.command;

import donatr.common.CreateAccountCommand;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/accounts")
@ApplicationScoped
public class AccountCommandController {
	@Inject
	CreateAccountCommand createAccountCommand;
	@Inject
	Event<CreateAccountCommand> createAccountCommandBus;

	@POST
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
