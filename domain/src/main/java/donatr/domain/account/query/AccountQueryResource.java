package donatr.domain.account.query;

import donatr.domain.account.aggregate.Account;
import donatr.domain.account.aggregate.AccountType;
import donatr.domain.account.repository.AccountRepository;
import donatr.domain.account.response.GetAccountResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static donatr.domain.account.response.ResponseHelper.createGetAccountResponse;

@ApplicationScoped
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AccountQueryResource {
	@Inject
	AccountRepository accountRepository;

	@GET
	@Path("/{id: [a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+}")
	public GetAccountResponse getAccount(@PathParam("id") String id) {
		Account account = accountRepository.load(id);
		return createGetAccountResponse(account);
	}

	@GET
	@Path("/{type: [A-Z]+}")
	public List<GetAccountResponse> getAccountsByType(@PathParam("type") AccountType type) {
		List<Account> account = accountRepository.getAccountsByType(type);
		return createGetAccountResponse(account);
	}
}