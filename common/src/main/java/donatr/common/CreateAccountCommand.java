package donatr.common;

import com.zanox.rabbiteasy.cdi.ContainsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountCommand implements ContainsId<String> {
	private String id;
}
