package donatr.domain.account.command;

import donatr.common.command.ChangeAccountEmailCommand;
import donatr.common.command.CreateAccountCommand;
import donatr.domain.account.request.ChangeAccountEmailRequest;
import donatr.domain.account.request.CreateAccountRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@ApplicationScoped
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountCommandResource {
	@Inject
	CommandGateway commandGateway;

	@POST
	@Transactional
	public Response createAccount(CreateAccountRequest createAccountRequest) throws Exception {
		String itemId = UUID.randomUUID().toString();
		commandGateway.send(CreateAccountCommand.builder()
				.id(itemId)
				.name(createAccountRequest.getName())
				.email(createAccountRequest.getEmail())
				.build());
		return Response.ok(itemId).build();
	}

	@POST
	@Path("/{id}/email")
	@Transactional
	public Response changeAccountEmail(@PathParam("id") String id, ChangeAccountEmailRequest changeAccountEmailRequest) {
		commandGateway.send(ChangeAccountEmailCommand.builder()
				.id(id)
				.email(changeAccountEmailRequest.getEmail())
				.build());
		return Response.ok().build();
	}
}