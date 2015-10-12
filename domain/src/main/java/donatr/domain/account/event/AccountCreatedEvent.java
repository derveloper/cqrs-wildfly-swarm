package donatr.domain.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AccountCreatedEvent {
	private final String id;
	private final String name;
	private final String email;
}
