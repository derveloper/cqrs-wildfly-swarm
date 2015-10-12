package donatr.domain.account.response;

import donatr.domain.account.aggregate.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountResponse {
	private String id;
	private String name;
	private String email;
	private AccountType type;
	private Long version;
}