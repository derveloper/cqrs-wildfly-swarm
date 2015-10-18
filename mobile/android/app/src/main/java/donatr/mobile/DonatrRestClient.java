package donatr.mobile;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by vileda on 17.10.15.
 */
public interface DonatrRestClient {
    @GET("/domain/accounts/user")
    Call<List<UserAccount>> listRepos();

    @GET("/domain/accounts/product")
    Call<List<ProductAccount>> listProduct();

    @POST("/domain/accounts/user")
    Call<String> createUser(@Body UserAccount userAccount);

    @POST("/domain/accounts/product")
    Call<String> createProduct(@Body ProductAccount productAccount);

    @POST("/domain/accounts/transaction")
    Call<Void> createTransaction(@Body CreateTransactionRequest transactionRequest);
}
