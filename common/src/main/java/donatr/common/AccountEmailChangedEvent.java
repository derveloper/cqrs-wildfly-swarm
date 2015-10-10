package donatr.common;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountEmailChangedEvent {
	private final String id;
	private final String email;
}
