package donatr.domain.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AccountEntry {
	@Id
	private String id;
	private String name;
	private String email;
}
