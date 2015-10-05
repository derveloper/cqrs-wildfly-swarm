package cc.vileda.query.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/accounts")
@ApplicationScoped
public class AccountController {
	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getAccount(@PathParam("id") String id) {
		System.out.println("+++ACCOUNTS_QUERY " + id);
		return id;
	}
}
