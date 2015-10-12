package donatr.domain.account.request;

import donatr.domain.account.aggregate.AccountType;
import lombok.Data;

@Data
public class ChangeAccountTypeRequest {
	private AccountType type;
}
