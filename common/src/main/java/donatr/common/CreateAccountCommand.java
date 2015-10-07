package donatr.common;

import donatr.common.domain.model.AccountModel;

public class CreateAccountCommand extends AccountCommand {
	public CreateAccountCommand() {
		super();
	}

	public CreateAccountCommand(AccountModel accountModel) {
		super(accountModel);
	}
}
