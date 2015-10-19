package donatr.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class UserActivity extends AppCompatActivity {
    UserListAdapter adapter;
    String donatrHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donatrHost = getDonatrHost();

        initActivity();

        Retrofit retrofit = initRestClient();

        final DonatrRestClient donatrRestClient = retrofit.create(DonatrRestClient.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Add User");
                LinearLayout layout = new LinearLayout(UserActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameBox = new EditText(UserActivity.this);
                nameBox.setHint("Name");
                layout.addView(nameBox);

                final EditText emailBox = new EditText(UserActivity.this);
                emailBox.setHint("Email");
                layout.addView(emailBox);

                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        donatrRestClient.createUser(
                                        new UserAccount(null, nameBox.getText().toString(),
                                                null, emailBox.getText().toString())).enqueue(new Callback<String>() {
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

        connectWebSocket();

        final GridView gridLayout = (GridView) findViewById(R.id.usergrid);
        adapter = new UserListAdapter(UserActivity.this);
        gridLayout.setAdapter(adapter);
        donatrRestClient.listRepos().enqueue(new Callback<List<UserAccount>>() {
            @Override
            public void onResponse(Response<List<UserAccount>> response, Retrofit retrofit) {
                adapter.setAccounts(response.body());
                Log.i("RestClient", "fetched " + adapter.getCount());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("DonatrRestClient", t.getMessage());
            }
        });
    }

    private void initActivity() {
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @NonNull
    private Retrofit initRestClient() {
        return new Retrofit.Builder()
                    .baseUrl("http://" + donatrHost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    private String getDonatrHost() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getString("donatr_host", "donatr");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://"+donatrHost+"/domain/socket");
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
                        if(message.contains("UserAccountCreated")) {
                            UserAccount userAccount = new Gson().fromJson(message, UserAccount.class);
                            adapter.addUser(userAccount);
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
