package donatr.domain.account;

import donatr.domain.account.aggregate.Account;
import donatr.domain.account.aggregate.AccountType;
import donatr.domain.account.aggregate.Transaction;
import donatr.domain.account.command.*;
import donatr.domain.account.event.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {
	private FixtureConfiguration fixture;
	private FixtureConfiguration<Transaction> transactionFixture;

	@Before
	public void setUp() throws Exception {
		fixture = Fixtures.newGivenWhenThenFixture(Account.class);
		transactionFixture = Fixtures.newGivenWhenThenFixture(Transaction.class);
	}

	@Test
	public void shouldCreateAccount() throws Exception {
		fixture.given()
				.when(CreateAccountCommand.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo@bar.tld").build())
				.expectEvents(AccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo@bar.tld").build());
	}

	@Test
	public void shouldUpdateEmail() throws Exception {
		fixture.given(
				AccountCreatedEvent.builder()
				.id("todo1")
				.name("need to implement the aggregate")
				.email("foo2@bar.tld").build())
		.when(ChangeAccountEmailCommand.builder().id("todo1").email("foo2@bar.tld").build())
				.expectEvents(AccountEmailChangedEvent.builder().id("todo1").email("foo2@bar.tld").build());
	}

	@Test
	public void shouldUpdateType() throws Exception {
		fixture.given(
				AccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo2@bar.tld").build())
				.when(ChangeAccountTypeCommand.builder().id("todo1").accountType(AccountType.BANK).build())
				.expectEvents(AccountTypeChangedEvent.builder().id("todo1").accountType(AccountType.BANK).build());
	}

	@Test
	public void shouldCreditAccount() throws Exception {
		fixture.given(
				AccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo2@bar.tld").build())
				.when(CreditAccountCommand.builder().id("todo1").amount(BigDecimal.TEN).build())
				.expectEvents(AccountCreditedEvent.builder().id("todo1").amount(BigDecimal.TEN).build());
	}

	@Test
	public void shouldDebitAccount() throws Exception {
		fixture.given(
				AccountCreatedEvent.builder()
						.id("todo1")
						.name("need to implement the aggregate")
						.email("foo2@bar.tld").build())
				.when(DebitAccountCommand.builder().id("todo1").amount(BigDecimal.TEN).build())
				.expectEvents(AccountDebitedEvent.builder().id("todo1").amount(BigDecimal.TEN).build());
	}

	@Test
	public void shouldTransferMoney() throws Exception {
		transactionFixture.given()
				.when(CreateTransactionCommand.builder().id("todo1")
						.fromAccount("todo1")
						.toAccount("todo2")
						.amount(BigDecimal.TEN).build())
				.expectEvents(
						TransactionCreatedEvent.builder()
								.id("todo1")
								.fromAccount("todo1")
								.toAccount("todo2")
								.amount(BigDecimal.TEN).build());
	}
}
