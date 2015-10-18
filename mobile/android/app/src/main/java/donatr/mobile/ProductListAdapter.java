package donatr.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vileda on 18.10.15.
 */
public class ProductListAdapter extends BaseAdapter {
    private List<ProductAccount> accounts = new ArrayList<>();
    protected Context context;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    UserAccount currentUser;

    public ProductListAdapter(Activity context, UserAccount currentUser) {
        this.context = context;
        this.currentUser = currentUser;
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

    static class ViewHolder
    {
        TextView textView;
        TextView priceView;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View inflated = convertView;
        if(inflated == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflated = inflater.inflate(R.layout.account_view, parent, false);
        }

        holder.textView = (TextView) inflated.findViewById(R.id.account_name);
        holder.imageView = (ImageView) inflated.findViewById(R.id.account_image);
        holder.priceView = (TextView) inflated.findViewById(R.id.decimal_value);
        final ProductAccount productAccount = accounts.get(position);
        holder.textView.setText(productAccount.name);
        holder.priceView.setVisibility(View.VISIBLE);
        holder.priceView.setText(productAccount.fixedAmount.toString());
        holder.imageView.setCropToPadding(true);
        imageLoader
                .displayImage(UserListAdapter.getGravatarUri(productAccount.name),
                        holder.imageView, options);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.178.32:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final DonatrRestClient donatrRestClient = retrofit.create(DonatrRestClient.class);
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
                Log.i(ProductListAdapter.class.getSimpleName(), "click prod");
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

    private void update() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        notifyDataSetChanged();
    }

    private String hash(String toEnc) {
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
            return new BigInteger(1, mdEnc.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "ff";
    }
}
