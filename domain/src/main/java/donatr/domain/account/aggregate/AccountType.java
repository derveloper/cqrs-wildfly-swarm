package donatr.domain.account.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {
	USER("USER"),
	BANK("BANK"),
	BEEN("BEEN");

	private final String value;

	AccountType(String value) { this.value = value; }

	@JsonValue
	public String getValue() { return value; }

	@JsonCreator
	public static AccountType parse(String id) {
		AccountType accountType = null;
		for (AccountType item : AccountType.values()) {
			if (item.getValue().equalsIgnoreCase(id)) {
				accountType = item;
				break;
			}
		}
		return accountType;
	}
}
