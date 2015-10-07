package donatr.common;

import com.google.gson.Gson;
import donatr.common.domain.model.AccountModel;

public class AccountCommand extends Command<AccountModel> {
	public AccountCommand() {
	}

	public AccountCommand(AccountModel accountModel) {
		setPayload(accountModel);
	}

	@Override
	public void setContent(String content) {
		System.out.println(content);
		setPayload(new Gson().fromJson(content, AccountModel.class));
	}
}
