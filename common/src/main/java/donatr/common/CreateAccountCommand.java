package donatr.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountCommand extends DonatrEvent {
	private String id;
	private String name;
	private String email;
}
