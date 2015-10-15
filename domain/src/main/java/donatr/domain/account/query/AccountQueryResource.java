package donatr.domain.account.query;

import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.aggregate.UserAccount;
import donatr.domain.account.repository.ProductAccountRepository;
import donatr.domain.account.repository.UserAccountRepository;
import donatr.domain.account.response.GetProductAccountResponse;
import donatr.domain.account.response.GetUserAccountResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static donatr.domain.account.response.ResponseHelper.createGetAccountResponse;
import static donatr.domain.account.response.ResponseHelper.createGetProductAccountResponse;

@ApplicationScoped
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AccountQueryResource {
	@Inject
	UserAccountRepository userAccountRepository;

	@Inject
	ProductAccountRepository productAccountRepository;

	@GET
	@Path("/user/{id: [a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+}")
	public GetUserAccountResponse getUserAccount(@PathParam("id") String id) {
		UserAccount account = userAccountRepository.load(id);
		return createGetAccountResponse(account);
	}

	@GET
	@Path("/user")
	public List<GetUserAccountResponse> getUserAccounts() {
		List<UserAccount> accounts = userAccountRepository.getAllAccounts();
		return createGetAccountResponse(accounts);
	}

	@GET
	@Path("/product/{id: [a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+-[a-f0-9]+}")
	public GetProductAccountResponse getProductAccount(@PathParam("id") String id) {
		ProductAccount account = productAccountRepository.load(id);
		return createGetProductAccountResponse(account);
	}

	@GET
	@Path("/product")
	public List<GetProductAccountResponse> getProductAccounts() {
		List<ProductAccount> account = productAccountRepository.getAllAccounts();
		return createGetProductAccountResponse(account);
	}
}