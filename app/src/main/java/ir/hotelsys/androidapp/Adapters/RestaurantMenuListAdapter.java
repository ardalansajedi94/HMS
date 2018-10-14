package ir.hotelsys.androidapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ir.hotelsys.androidapp.Constants;
import ir.hotelsys.androidapp.Models.CafeMenuItem;
import ir.hotelsys.androidapp.Models.CafeRestaurant;
import ir.hotelsys.androidapp.Models.RestaurantMenuItem;
import ir.hotelsys.androidapp.R;
import ir.hotelsys.androidapp.RetrofitWithRetry;
import ir.hotelsys.androidapp.Server.RequestInterface;
import ir.hotelsys.androidapp.Server.ServerRequest;
import ir.hotelsys.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohammad on 9/25/2017.
 */

public class RestaurantMenuListAdapter extends BaseAdapter {
    private ArrayList<RestaurantMenuItem> _data;
    private Context _c;
    private ImageLoader imageLoader ;
    private ProgressDialog progress;
    private SharedPreferences user_detail;
    public RestaurantMenuListAdapter(ArrayList<RestaurantMenuItem> data, Context c) {
        _data = data;
        _c = c;
        user_detail = _c.getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        final RestaurantMenuItem item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item_menu, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.menu_title);
        ImageView image = (ImageView) v.findViewById(R.id.menu_image);
        TextView material_tv=(TextView)v.findViewById(R.id.menu_material_tv);
        if (item.getTitle()!=null)
            title_tv.setText(item.getTitle());
        imageLoader= ImageLoader.getInstance();
        if (item.getMaterial()!=null)
        {
            String material[]=item.getMaterial().split(":");
            material_tv.setText(TextUtils.join(",",material));
        }
        if (item.getImages()!=null)
        {
            if (!item.getImages().isEmpty())
            {
                imageLoader.displayImage(Constants.MEDIA_BASE_URL+item.getImages().get(0).getImage_source(),image);
            }
        }

        return v;
    }
}