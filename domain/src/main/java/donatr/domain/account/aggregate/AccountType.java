package donatr.domain.account.aggregate;

public enum AccountType {
	USER("user"),
	BANK("bank"),
	BEEN("bean");

	private String value;

	AccountType(String value) { this.value = value; }

	public String getValue() { return value; }

	public static AccountType parse(String id) {
		AccountType accountType = null; // Default
		for (AccountType item : AccountType.values()) {
			if (item.getValue().equals(id)) {
				accountType = item;
				break;
			}
		}
		return accountType;
	}
}
