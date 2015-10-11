package donatr.domain.account.repository;

import donatr.domain.account.aggregate.Transaction;
import lombok.Getter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;

import javax.persistence.EntityManager;

@Getter
public class TransactionRepository extends DonatrRepository<Transaction> {
	public TransactionRepository(EventStore eventStore, EventBus eventBus, EntityManager entityManager) {
		super(eventStore, eventBus, entityManager, Transaction.class);
	}
}
