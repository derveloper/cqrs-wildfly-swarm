package donatr.domain.account;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import donatr.domain.account.request.ChangeAccountEmailRequest;
import donatr.domain.account.request.CreateProductAccountRequest;
import donatr.domain.account.request.CreateTransactionRequest;
import donatr.domain.account.request.CreateUserAccountRequest;
import donatr.domain.account.response.GetAccountResponse;
import donatr.domain.account.response.GetProductAccountResponse;
import donatr.domain.account.response.GetUserAccountResponse;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class AccountIT {
	Client client;
	WebTarget target;
	WebTarget userTarget;
	WebTarget productTarget;

	@Before
	public void setup() {
		client = ClientBuilder.newBuilder()
				.register(JacksonJsonProvider.class).build();
		target = client.target("http://localhost:8080").path("domain").path("accounts");
		userTarget = client.target("http://localhost:8080").path("domain").path("accounts").path("user");
		productTarget = client.target("http://localhost:8080").path("domain").path("accounts").path("product");
	}

	private String createUserAccount(String name, String email) {
		CreateUserAccountRequest foo1 = CreateUserAccountRequest.builder().name(name).email(email).build();
		Response response = userTarget.request().post(Entity.entity(foo1, MediaType.APPLICATION_JSON_TYPE));
		return response.readEntity(String.class);
	}

	private String createProductAccount(String name, BigDecimal fixedAmount) {
		CreateProductAccountRequest foo1 = CreateProductAccountRequest.builder().name(name).fixedAmount(fixedAmount).build();
		Response response = productTarget.request().post(Entity.entity(foo1, MediaType.APPLICATION_JSON_TYPE));
		return response.readEntity(String.class);
	}

	@Test
	public void shouldCreateAndFetchUserAccountViaRest() {
		String responseEntity = createUserAccount("foo1", "foo@bar.tld");
		assertThat(responseEntity, containsString("-"));

		GetUserAccountResponse getAccountResponse = userTarget.path(responseEntity).request().get(GetUserAccountResponse.class);
		assertThat(getAccountResponse.getId(), is(responseEntity));
		assertThat(getAccountResponse.getName(), is("foo1"));
		assertThat(getAccountResponse.getEmail(), is("foo@bar.tld"));
		assertThat(getAccountResponse.getBalance().equals(BigDecimal.ZERO.movePointLeft(2)), is(true));
	}

	@Test
	public void shouldCreateAndFetchProductAccountViaRest() {
		String responseEntity = createProductAccount("foo1", BigDecimal.TEN);
		assertThat(responseEntity, containsString("-"));

		GetProductAccountResponse getAccountResponse = productTarget.path(responseEntity).request().get(GetProductAccountResponse.class);
		assertThat(getAccountResponse.getId(), is(responseEntity));
		assertThat(getAccountResponse.getName(), is("foo1"));
		assertThat(getAccountResponse.getFixedAmount(), is(BigDecimal.TEN.setScale(2, 2)));
		assertThat(getAccountResponse.getBalance().equals(BigDecimal.ZERO.movePointLeft(2)), is(true));
	}

	@Test
	public void shouldTransferMoneyViaRest() {
		String responseEntity1 = createUserAccount("foo1", "foo@bar.tld");
		String responseEntity2 = createProductAccount("foo2", BigDecimal.TEN);

		CreateTransactionRequest tx = CreateTransactionRequest.builder()
				.fromAccount(responseEntity1)
				.toAccount(responseEntity2)
				.amount(BigDecimal.TEN).build();
		Response response = target.path("transaction").request().post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatus(), is(HttpURLConnection.HTTP_OK));

		GetUserAccountResponse getAccountResponse = userTarget.path(responseEntity1)
				.request().get(GetUserAccountResponse.class);
		assertThat(getAccountResponse.getBalance(), is(BigDecimal.TEN.negate().setScale(2, 2)));

		GetProductAccountResponse getAccountResponse2 = productTarget.path(responseEntity2)
				.request().get(GetProductAccountResponse.class);
		assertThat(getAccountResponse2.getBalance(), is(BigDecimal.TEN.setScale(2, 2)));
	}

	@Test
	public void shouldChangeEmailViaRest() {
		String responseEntity1 = createUserAccount("foo1", "foo@bar.tld");

		ChangeAccountEmailRequest emailRequest = ChangeAccountEmailRequest.builder()
				.email("foo@change.tld")
				.build();
		Response response = userTarget.path(responseEntity1).path("email").request()
				.post(Entity.entity(emailRequest, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatus(), is(HttpURLConnection.HTTP_OK));

		GetAccountResponse getAccountResponse = userTarget.path(responseEntity1)
				.request().get(GetAccountResponse.class);
		assertThat(getAccountResponse.getEmail(), is("foo@change.tld"));
	}
}
