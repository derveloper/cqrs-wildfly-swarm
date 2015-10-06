package donatr.common;

import com.zanox.rabbiteasy.cdi.ContainsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent implements ContainsId<String>, DonatrEvent {
	private String id;
}
