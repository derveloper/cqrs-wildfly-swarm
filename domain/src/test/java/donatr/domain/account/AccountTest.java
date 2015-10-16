package donatr.domain.account;

import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.aggregate.Transaction;
import donatr.domain.account.aggregate.UserAccount;
import donatr.domain.account.command.*;
import donatr.domain.account.event.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {
	private FixtureConfiguration fixture;
	private FixtureConfiguration productFixture;
	private FixtureConfiguration<Transaction> transactionFixture;

	@Before
	public void setUp() throws Exception {
		fixture = Fixtures.newGivenWhenThenFixture(UserAccount.class);
		productFixture = Fixtures.newGivenWhenThenFixture(ProductAccount.class);
		transactionFixture = Fixtures.newGivenWhenThenFixture(Transaction.class);
	}

	@Test
	public void shouldCreateUserAccount() throws Exception {
		fixture.given()
				.when(CreateUserAccountCommand.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo@bar.tld").build())
				.expectEvents(UserAccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo@bar.tld").build());
	}

	@Test
	public void shouldCreateProductAccount() throws Exception {
		productFixture.given()
				.when(CreateProductAccountCommand.builder()
						.id("prod1")
						.fixedAmount(BigDecimal.TEN)
						.name("need to implement the aggregate").build())
				.expectEvents(ProductAccountCreatedEvent.builder()
						.id("prod1")
						.name("need to implement the aggregate")
						.fixedAmount(BigDecimal.TEN).build());
	}

	@Test
	public void shouldUpdateEmail() throws Exception {
		fixture.given(UserAccountCreatedEvent.builder()
				.id("todo1")
				.name("need to implement the aggregate")
				.email("foo2@bar.tld").build())
		.when(ChangeAccountEmailCommand.builder().id("todo1").email("foo2@bar.tld").build())
				.expectEvents(AccountEmailChangedEvent.builder().id("todo1").email("foo2@bar.tld").build());
	}

	@Test
	public void shouldCreditUserAccount() throws Exception {
		fixture.given(UserAccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo2@bar.tld").build())
				.when(CreditUserAccountCommand.builder().id("todo1").amount(BigDecimal.TEN).build())
				.expectEvents(UserAccountCreditedEvent.builder().id("todo1").amount(BigDecimal.TEN).build());
	}

	@Test
	public void shouldDebitUserAccount() throws Exception {
		fixture.given(UserAccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo2@bar.tld").build())
				.when(DebitUserAccountCommand.builder().id("todo1").amount(BigDecimal.TEN).build())
				.expectEvents(UserAccountDebitedEvent.builder().id("todo1").amount(BigDecimal.TEN).build());
	}

	@Test
	public void shouldTransferMoney() throws Exception {
		transactionFixture.given()
				.when(CreateTransactionCommand.builder().id("todo1")
						.fromAccount("todo1")
						.toAccount("todo2")
						.amount(BigDecimal.TEN).build())
				.expectEvents(TransactionCreatedEvent.builder()
						.id("todo1")
						.fromAccount("todo1")
						.toAccount("todo2")
						.amount(BigDecimal.TEN).build());
	}
}
