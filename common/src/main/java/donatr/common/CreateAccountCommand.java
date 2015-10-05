package donatr.common;

public class CreateAccountCommand {
	private String id;

	public CreateAccountCommand() {
	}

	public CreateAccountCommand(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public CreateAccountCommand setId(String id) {
		this.id = id;
		return this;
	}
}
