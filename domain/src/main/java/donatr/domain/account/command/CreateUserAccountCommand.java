package donatr.domain.account.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
public class CreateUserAccountCommand {
	@TargetAggregateIdentifier
	private String id;
	private String name;
	private String email;
}
