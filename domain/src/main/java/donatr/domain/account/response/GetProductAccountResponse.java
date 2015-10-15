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
public class GetProductAccountResponse {
	private String id;
	private String name;
	private BigDecimal balance;
	private BigDecimal fixedAmount;
	private Long version;
}