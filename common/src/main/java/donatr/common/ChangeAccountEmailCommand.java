package donatr.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChangeAccountEmailCommand {
	@TargetAggregateIdentifier
	private String id;
	private String email;
}
