package donatr.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent extends DonatrEvent {
	private String id;
	private String name;
	private String email;
}
