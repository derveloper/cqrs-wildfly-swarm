package cc.vileda.accounting.query;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class AccountEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
}
