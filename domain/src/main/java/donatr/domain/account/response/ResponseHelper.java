package donatr.domain.account.response;

import donatr.domain.account.aggregate.Account;

public class ResponseHelper
{
	public static GetAccountResponse createGetAccountResponse(Account account)
	{
		return GetAccountResponse.builder()
				.id(account.getId())
				.email(account.getEmail())
				.name(account.getName())
				.accountType(account.getAccountType())
				.balance(account.getBalance())
				.version(account.getVersion())
				.build();
	}
}
