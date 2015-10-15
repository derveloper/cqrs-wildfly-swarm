package donatr.domain.account.response;

import donatr.domain.account.aggregate.ProductAccount;
import donatr.domain.account.aggregate.UserAccount;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseHelper
{
	public static GetUserAccountResponse createGetAccountResponse(UserAccount account)
	{
		return GetUserAccountResponse.builder()
				.id(account.getId())
				.name(account.getName())
				.email(account.getEmail())
				.balance(account.getBalance())
				.version(account.getVersion())
				.build();
	}

	public static List<GetUserAccountResponse> createGetAccountResponse(List<UserAccount> accounts)
	{
		return accounts.stream().map(ResponseHelper::createGetAccountResponse).collect(Collectors.toList());
	}

	public static GetProductAccountResponse createGetProductAccountResponse(ProductAccount account)
	{
		return GetProductAccountResponse.builder()
				.id(account.getId())
				.name(account.getName())
				.balance(account.getBalance())
				.fixedAmount(account.getFixedAmount())
				.version(account.getVersion())
				.build();
	}

	public static List<GetProductAccountResponse> createGetProductAccountResponse(List<ProductAccount> accounts)
	{
		return accounts.stream().map(ResponseHelper::createGetProductAccountResponse).collect(Collectors.toList());
	}
}
