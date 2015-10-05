package cc.vileda.cqrs.common;

public class AccountCreatedEvent {
	private String id;

	public AccountCreatedEvent() {
	}

	public AccountCreatedEvent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public AccountCreatedEvent setId(String id) {
		this.id = id;
		return this;
	}
}
