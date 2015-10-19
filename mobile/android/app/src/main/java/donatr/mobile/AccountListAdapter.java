package donatr.mobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vileda on 18.10.15.
 */
public abstract class AccountListAdapter extends BaseAdapter {
    protected Context context;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public AccountListAdapter(Activity context) {
        this.context = context;
    }

    static class ViewHolder
    {
        TextView nameView;
        TextView decimalView;
        ImageView imageView;
    }

    protected View createAccountView(View convertView, ViewGroup parent, String decimalValue, String name, ViewHolder holder) {
        View inflated = convertView;
        if(inflated == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflated = inflater.inflate(R.layout.account_view, parent, false);
        }

        holder.nameView = (TextView) inflated.findViewById(R.id.account_name);
        holder.imageView = (ImageView) inflated.findViewById(R.id.account_image);
        holder.decimalView = (TextView) inflated.findViewById(R.id.decimal_value);
        holder.nameView.setText(name);
        if(decimalValue != null) {
            holder.decimalView.setVisibility(View.VISIBLE);
        }
        holder.decimalView.setText(decimalValue);
        holder.imageView.setCropToPadding(true);
        imageLoader
                .displayImage(getGravatarUri(name),
                        holder.imageView, options);
        return inflated;
    }

    protected void update() {
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

    protected static String hash(String toEnc) {
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

    public static String getGravatarUri(String email) {
        return "http://www.gravatar.com/avatar/" + hash(email) + "??s=85";
    }
}
