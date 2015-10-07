package donatr.common;

import com.google.gson.Gson;
import donatr.common.domain.model.AccountModel;

public class AccountEvent extends DonatrEvent<AccountModel> {
	public AccountEvent() {
	}

	public AccountEvent(AccountModel accountModel) {
		setPayload(accountModel);
	}

	@Override
	public void setContent(String content) {
		System.out.println(content);
		setPayload(new Gson().fromJson(content, AccountModel.class));
	}
}
