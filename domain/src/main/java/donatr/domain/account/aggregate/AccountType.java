package donatr.domain.account.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {
	USER("user"),
	BANK("bank"),
	BEEN("bean");

	private final String value;

	AccountType(String value) { this.value = value; }

	@JsonValue
	public String getValue() { return value; }

	@JsonCreator
	public static AccountType parse(String id) {
		AccountType accountType = null;
		id = id.toLowerCase();
		for (AccountType item : AccountType.values()) {
			if (item.getValue().equals(id)) {
				accountType = item;
				break;
			}
		}
		return accountType;
	}
}
