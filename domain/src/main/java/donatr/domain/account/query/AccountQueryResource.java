package donatr.domain.account.query;

import donatr.domain.account.aggregate.Account;
import donatr.domain.account.repository.AccountRepository;
import donatr.domain.account.response.GetAccountResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountQueryResource {
	@Inject
	AccountRepository accountRepository;

	@GET
	@Path("/{id}")
	@Transactional
	public GetAccountResponse getAccount(@PathParam("id") String id) {
		Account account = accountRepository.load(id);
		return GetAccountResponse.builder()
				.id(account.getId())
				.email(account.getEmail())
				.name(account.getName())
				.version(account.getVersion())
				.build();
	}

	@GET
	@Path("/{id}/{version}")
	@Transactional
	public GetAccountResponse getAccount(@PathParam("id") String id, @PathParam("version") Long version) {
		Account account = accountRepository.load(id, version);
		return GetAccountResponse.builder()
				.id(account.getId())
				.email(account.getEmail())
				.name(account.getName())
				.type(account.getType())
				.version(account.getVersion())
				.build();
	}
}