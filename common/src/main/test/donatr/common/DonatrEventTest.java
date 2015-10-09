package donatr.common;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DonatrEventTest
{
	@Test
	public void createAccountCommandConvertsToJson() {
		CreateAccountCommand createAccountCommand = new CreateAccountCommand();
		final String email = "test@bar.tld";
		createAccountCommand.setEmail(email);

		assertThat(createAccountCommand.toString(), CoreMatchers.containsString(email));
	}

	@Test
	public void createAccountCommandLoadsFromJson() {
		CreateAccountCommand createAccountCommand = new CreateAccountCommand();
		CreateAccountCommand createAccountCommand2 = new CreateAccountCommand();
		final String email = "test@bar.tld";
		createAccountCommand.setEmail(email);
		createAccountCommand2.setContent(createAccountCommand.getContent());

		assertThat(createAccountCommand.getEmail(), is(createAccountCommand2.getEmail()));
	}
}