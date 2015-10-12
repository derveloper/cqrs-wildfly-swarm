package donatr.domain.account.request;

import donatr.domain.account.aggregate.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
	private String name;
	private String email;
	private AccountType type;
}