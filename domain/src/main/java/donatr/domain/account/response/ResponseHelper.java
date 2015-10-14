package donatr.domain.account.response;

import donatr.domain.account.aggregate.Account;

import java.util.List;
import java.util.stream.Collectors;

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

	public static List<GetAccountResponse> createGetAccountResponse(List<Account> accounts)
	{
		return accounts.stream().map(account -> GetAccountResponse.builder()
				.id(account.getId())
				.email(account.getEmail())
				.name(account.getName())
				.accountType(account.getAccountType())
				.balance(account.getBalance())
				.version(account.getVersion())
				.build()
		).collect(Collectors.toList());
	}
}
