package donatr.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vileda on 18.10.15.
 */
public class UserListAdapter extends BaseAdapter {
    private List<UserAccount> body = new ArrayList<>();
    protected Context context;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public UserListAdapter(Activity context) {
        this.context = context;
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
    }

    @Override
    public int getCount() {
        return body.size();
    }

    @Override
    public Object getItem(int position) {
        return body.get(position);
    }

    @Override
    public long getItemId(int position) {
        return body.get(position).hashCode();
    }

    static class ViewHolder
    {
        TextView textView;
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
        final UserAccount userAccount = body.get(position);
        holder.textView.setText(userAccount.name);
        imageLoader
                .displayImage(getGravatarUri(userAccount.email),
                        holder.imageView, options);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    public static String getGravatarUri(String email) {
        return "http://www.gravatar.com/avatar/" + hash(email) + "??s=85";
    }

    public void setBody(List<UserAccount> body) {
        this.body = body;
        update();
    }

    public void addUser(UserAccount user) {
        this.body.add(user);
        update();
    }

    private void update() {
        imageLoader = ImageLoader.getInstance();
        notifyDataSetChanged();
    }

    private static String hash(String toEnc) {
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
