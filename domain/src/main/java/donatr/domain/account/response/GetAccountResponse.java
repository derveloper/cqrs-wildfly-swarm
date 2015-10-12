package donatr.domain.account.response;

import donatr.domain.account.aggregate.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountResponse {
	private String id;
	private String name;
	private String email;
	private BigDecimal balance;
	private AccountType accountType;
	private Long version;
}