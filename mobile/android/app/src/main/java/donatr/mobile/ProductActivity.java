package donatr.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductActivity extends AppCompatActivity {
    ProductListAdapter adapter;
    TextView balanceView;
    UserAccount currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.178.32:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final DonatrRestClient donatrRestClient = retrofit.create(DonatrRestClient.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setTitle("Add Product");
                LinearLayout layout = new LinearLayout(ProductActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameBox = new EditText(ProductActivity.this);
                nameBox.setHint("Name");
                layout.addView(nameBox);

                final EditText priceBox = new EditText(ProductActivity.this);
                priceBox.setHint("Price");
                layout.addView(priceBox);

                builder.setView(layout);
// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        donatrRestClient.createProduct(
                                new ProductAccount(null, nameBox.getText().toString(),
                                        new BigDecimal(priceBox.getText().toString()))).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                Log.i("RestClient", response.body());
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e("RestClient", t.getMessage());
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentUser = (UserAccount) intent.getSerializableExtra("current_user");

        View userView = findViewById(R.id.userView);
        TextView usernameTextView = (TextView) userView.findViewById(R.id.account_name);
        ImageView userImageView = (ImageView) userView.findViewById(R.id.account_image);
        balanceView = (TextView) userView.findViewById(R.id.decimal_value);
        ImageLoader.getInstance().displayImage(UserListAdapter.getGravatarUri(currentUser.email), userImageView);
        usernameTextView.setText(currentUser.name);
        balanceView.setText(currentUser.balance.toString());
        balanceView.setVisibility(View.VISIBLE);

        final GridView gridLayout = (GridView) findViewById(R.id.usergrid);
        adapter = new ProductListAdapter(this, currentUser);
        gridLayout.setAdapter(adapter);
        donatrRestClient.listProduct().enqueue(new Callback<List<ProductAccount>>() {
            @Override
            public void onResponse(Response<List<ProductAccount>> response, Retrofit retrofit) {
                adapter.setAccounts(response.body());
                Log.i("RestClient", "fetched " + adapter.getCount());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("DonatrRestClient", t.getMessage());
            }
        });
        connectWebSocket();
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://192.168.178.32:8080/domain/socket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        WebSocketClient mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Websocket Client", message);
                        if(message.contains("ProductAccountCreated")) {
                            ProductAccount productAccount = new Gson().fromJson(message, ProductAccount.class);
                            adapter.addProduct(productAccount);
                        }
                        else if(message.contains("AccountDebited")) {
                            AccountDebitedEvent debitedEvent = new Gson().fromJson(message, AccountDebitedEvent.class);
                            if(currentUser.id.equals(debitedEvent.id)) {
                                BigDecimal balance = currentUser.balance.subtract(debitedEvent.amount);
                                currentUser = new UserAccount(currentUser.id, currentUser.name, balance, currentUser.email);
                                balanceView.setText(currentUser.balance.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

}
