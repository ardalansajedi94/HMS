package ir.hotelsys.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.hotelsys.androidapp.Adapters.AboutCityListAdapter;
import ir.hotelsys.androidapp.Models.BlogContent;
import ir.hotelsys.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelsys.androidapp.Server.RequestInterface;
import ir.hotelsys.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutHotelFragment extends Fragment {

    private ListView aboutHotelList;
    private ArrayList<BlogContent> blogContents;
    private AboutCityListAdapter aboutHotelListAdapter;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    ProgressDialog progress;
    int category;
    public AboutHotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about_hotel, container, false);
        db=new DatabaseHandler(getActivity());
        this.category=getArguments().getInt("category");
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        aboutHotelList = (ListView) view.findViewById(R.id.about_hotel_list);
        blogContents=new ArrayList<>();
        getAboutHotelContent();
        aboutHotelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new PostViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", blogContents.get(i).getId());
                bundle.putInt("type", 1);  // 1 for about hotel content and 2 for about city content
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    public static AboutHotelFragment newInstance(int category) {
        AboutHotelFragment myFragment = new AboutHotelFragment();

        Bundle args = new Bundle();
        args.putInt("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }
    private void getAboutHotelContent()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response;
        response  = requestInterface.hotel_info(category,user_detail.getInt(Constants.LANGUAGE_ID,1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            blogContents=resp.getInformations();
                            aboutHotelListAdapter = new AboutCityListAdapter(blogContents,getActivity());
                            aboutHotelList.setAdapter(aboutHotelListAdapter);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });
    }

}
