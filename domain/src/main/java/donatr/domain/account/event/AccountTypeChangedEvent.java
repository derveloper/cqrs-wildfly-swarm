package donatr.domain.account.event;

import donatr.domain.account.aggregate.AccountType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountTypeChangedEvent {
	private final String id;
	private final AccountType accountType;
}
