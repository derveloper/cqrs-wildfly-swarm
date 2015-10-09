package donatr.domain.account;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

public class AccountRepository extends EventSourcingRepository<Account> {
	public AccountRepository(EventStore eventStore) {
		super(Account.class, eventStore);
	}

	private UserTransaction getUserTransaction() throws NamingException {
		return (UserTransaction) new InitialContext().lookup("java:jboss/UserTransaction");
	}
}
