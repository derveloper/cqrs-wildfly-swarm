package donatr.rest.command;

import donatr.common.CreateAccountCommand;
import donatr.rest.response.CreateAccountResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/accounts")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AccountCommandController {
	@Inject
	Event<CreateAccountCommand> createAccountCommandBus;

	@POST
	public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
		System.out.println("+++ACCOUNTS_QUERY " + createAccountRequest);
		CreateAccountResponse response = new CreateAccountResponse("ok");
		createAccountCommandBus.fire(CreateAccountCommand.builder()
						.name(createAccountRequest.getName())
						.email(createAccountRequest.getEmail())
						.build());
		return response;
	}
}
