package donatr.mobile;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vileda on 18.10.15.
 */
public class ProductListAdapter extends AccountListAdapter {
    private List<ProductAccount> accounts = new ArrayList<>();
    UserAccount currentUser;
    private final DonatrRestClient donatrRestClient;

    public ProductListAdapter(Activity context, UserAccount currentUser, DonatrRestClient donatrRestClient) {
        super(context);
        this.currentUser = currentUser;
        this.donatrRestClient = donatrRestClient;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accounts.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProductAccount productAccount = accounts.get(position);
        String decimalValue = productAccount.fixedAmount.toString();
        String name = productAccount.name;
        ViewHolder holder = new ViewHolder();

        View inflated = createAccountView(convertView, parent, decimalValue, name, holder);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donatrRestClient.createTransaction(
                        new CreateTransactionRequest(currentUser.id, productAccount.id, productAccount.fixedAmount)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        Log.i("RestClient", String.valueOf(response.code()));
                        Toast.makeText(context, "you've just donated to " + productAccount.toString() + " !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("RestClient", String.valueOf(t.getMessage()));
                    }
                });
                Log.i(AccountListAdapter.class.getSimpleName(), "click prod");
            }
        });

        return inflated;
    }

    public void setAccounts(List<ProductAccount> accounts) {
        this.accounts = accounts;
        update();
    }

    public void addProduct(ProductAccount account) {
        this.accounts.add(account);
        update();
    }
}
