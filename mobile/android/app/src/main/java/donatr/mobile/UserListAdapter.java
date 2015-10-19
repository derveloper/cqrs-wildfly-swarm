package donatr.mobile;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vileda on 18.10.15.
 */
public class UserListAdapter extends AccountListAdapter {
    private List<UserAccount> accounts = new ArrayList<>();

    public UserListAdapter(Activity context) {
        super(context);
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
        final UserAccount userAccount = accounts.get(position);
        String name = userAccount.name;
        AccountListAdapter.ViewHolder holder = new AccountListAdapter.ViewHolder();

        View inflated = createAccountView(convertView, parent, null, name, holder);
        inflated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("current_user", userAccount);
                context.startActivity(intent);
            }
        });

        return inflated;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
        update();
    }

    public void addUser(UserAccount user) {
        this.accounts.add(user);
        update();
    }
}
