package donatr.common;

import donatr.common.domain.model.AccountModel;

public class AccountCreatedEvent extends AccountEvent {
	public AccountCreatedEvent() {
	}

	public AccountCreatedEvent(AccountModel accountModel) {
		super(accountModel);
	}
}
