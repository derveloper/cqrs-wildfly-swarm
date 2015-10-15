package donatr.domain.account.command;

import donatr.domain.account.request.ChangeAccountEmailRequest;
import donatr.domain.account.request.CreateProductAccountRequest;
import donatr.domain.account.request.CreateTransactionRequest;
import donatr.domain.account.request.CreateUserAccountRequest;
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
@Transactional
public class AccountCommandResource {
	@Inject
	CommandGateway commandGateway;

	@POST
	@Path("/user")
	public Response createUserAccount(CreateUserAccountRequest createAccountRequest) throws Exception {
		String itemId = UUID.randomUUID().toString();
		commandGateway.send(CreateUserAccountCommand.builder()
				.id(itemId)
				.name(createAccountRequest.getName())
				.email(createAccountRequest.getEmail())
				.build());
		return Response.ok(itemId).build();
	}

	@POST
	@Path("/product")
	public Response createProductAccount(CreateProductAccountRequest createAccountRequest) throws Exception {
		String itemId = UUID.randomUUID().toString();
		commandGateway.send(CreateProductAccountCommand.builder()
				.id(itemId)
				.name(createAccountRequest.getName())
				.fixedAmount(createAccountRequest.getFixedAmount())
				.build());
		return Response.ok(itemId).build();
	}

	@POST
	@Path("/user/{id}/email")
	public Response changeAccountEmail(@PathParam("id") String id, ChangeAccountEmailRequest changeAccountEmailRequest) {
		commandGateway.send(ChangeAccountEmailCommand.builder()
				.id(id)
				.email(changeAccountEmailRequest.getEmail())
				.build());
		return Response.ok().build();
	}

	@POST
	@Path("/transaction")
	public Response createTransaction(CreateTransactionRequest createTransactionRequest) {
		commandGateway.send(CreateTransactionCommand.builder()
				.id(UUID.randomUUID().toString())
				.fromAccount(createTransactionRequest.getFromAccount())
				.toAccount(createTransactionRequest.getToAccount())
				.amount(createTransactionRequest.getAmount())
				.build());
		return Response.ok().build();
	}
}