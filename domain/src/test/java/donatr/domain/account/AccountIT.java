package donatr.domain.account;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import donatr.domain.account.request.ChangeAccountEmailRequest;
import donatr.domain.account.request.CreateAccountRequest;
import donatr.domain.account.request.CreateTransactionRequest;
import donatr.domain.account.response.GetAccountResponse;
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

	@Before
	public void setup() {
		client = ClientBuilder.newBuilder()
				.register(JacksonJsonProvider.class).build();
		target = client.target("http://localhost:8080").path("api").path("accounts");
	}

	private String createAccount(String name, String email) {
		CreateAccountRequest foo1 = CreateAccountRequest.builder().name(name).email(email).build();
		Response response = target.request().post(Entity.entity(foo1, MediaType.APPLICATION_JSON_TYPE));
		return response.readEntity(String.class);
	}

	@Test
	public void shouldCreateAndFetchAccountViaRest() {
		String responseEntity = createAccount("foo1", "foo@bar.tld");
		assertThat(responseEntity, containsString("-"));

		GetAccountResponse getAccountResponse = target.path(responseEntity).request().get(GetAccountResponse.class);
		assertThat(getAccountResponse.getId(), is(responseEntity));
		assertThat(getAccountResponse.getName(), is("foo1"));
		assertThat(getAccountResponse.getEmail(), is("foo@bar.tld"));
		assertThat(getAccountResponse.getBalance().equals(BigDecimal.ZERO.movePointLeft(2)), is(true));
		//assertThat(getAccountResponse.getAccountType(), is(AccountType.USER));
	}

	@Test
	public void shouldTransferMoneyViaRest() {
		String responseEntity1 = createAccount("foo1", "foo@bar.tld");
		String responseEntity2 = createAccount("foo2", "foo2@bar.tld");

		CreateTransactionRequest tx = CreateTransactionRequest.builder()
				.fromAccount("foo1")
				.toAccount("foo2")
				.amount(BigDecimal.TEN).build();
		Response response = target.path("transaction").request().post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatus(), is(HttpURLConnection.HTTP_OK));

		GetAccountResponse getAccountResponse = target.path(responseEntity1)
				.request().get(GetAccountResponse.class);
		assertThat(getAccountResponse.getBalance().equals(BigDecimal.ZERO.movePointLeft(2)), is(true));

		GetAccountResponse getAccountResponse2 = target.path(responseEntity2)
				.request().get(GetAccountResponse.class);
		assertThat(getAccountResponse2.getBalance().equals(BigDecimal.ZERO.movePointLeft(2)), is(true));
	}

	@Test
	public void shouldChangeEmailViaRest() {
		String responseEntity1 = createAccount("foo1", "foo@bar.tld");

		ChangeAccountEmailRequest emailRequest = ChangeAccountEmailRequest.builder()
				.email("foo@change.tld")
				.build();
		Response response = target.path(responseEntity1).path("email").request()
				.post(Entity.entity(emailRequest, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatus(), is(HttpURLConnection.HTTP_OK));

		GetAccountResponse getAccountResponse = target.path(responseEntity1)
				.request().get(GetAccountResponse.class);
		assertThat(getAccountResponse.getEmail(), is("foo@change.tld"));
	}
}
