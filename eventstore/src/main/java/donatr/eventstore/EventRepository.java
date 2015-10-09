package donatr.eventstore;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.UserTransaction;

@Stateless
public class EventRepository {
	@PersistenceContext(unitName = "MyPU", type = PersistenceContextType.TRANSACTION)
	EntityManager entityManager;

	private UserTransaction getUserTransaction() throws NamingException {
		return (UserTransaction) new InitialContext().lookup("java:jboss/UserTransaction");
	}
}
