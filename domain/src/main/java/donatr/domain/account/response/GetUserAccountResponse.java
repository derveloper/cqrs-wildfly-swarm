package donatr.domain.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserAccountResponse {
	private String id;
	private String name;
	private String email;
	private BigDecimal balance;
	private Long version;
}