package com.example.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    TextView txt_dashname,txt_wename,txt_mintemp,txt_maxtemp,txt_acttemp,txt_humidity,txt_predect;
    ImageView img_we;
    ArrayList<ConsolidatedWeather> weathers;
    RecyclerView recyclerView ;
    int id;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(this.getArguments() != null){
            id = this.getArguments().getInt("id");
        }else{
            id = 3534;
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_dashname = view.findViewById(R.id.dash_name);
        txt_wename = view.findViewById(R.id.dash_wename);
        img_we = view.findViewById(R.id.dash_actimg);
        txt_mintemp = view.findViewById(R.id.txt_mintemp);
        txt_maxtemp = view.findViewById(R.id.txt_maxtemp);
        txt_acttemp = view.findViewById(R.id.txt_actemp);

        txt_humidity = view.findViewById(R.id.txt_humidity);
        txt_predect = view.findViewById(R.id.txt_prec);

        recyclerView = view.findViewById(R.id.recycleV);

        getWeather();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getWeather()
    {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<WeatherData> call = service.getWeatherDataCall(id);

        //System.out.println("ID: " + getArguments().getInt("cityid"));

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                System.out.println("Response Called!");

                WeatherData weatherData = response.body();

                weathers = new ArrayList<>(weatherData.getConsolidatedWeather());

                System.out.println("Check Size :"+weathers.size());

                txt_dashname.setText(weatherData.getTitle().toUpperCase());

                setData(weathers);
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

                System.out.println("Failure Called! :" +t.getMessage());
            }
        });

    }

    public void setData(ArrayList<ConsolidatedWeather> wearray)
    {
        System.out.println("Size From method :"+wearray.size());
        txt_wename.setText(wearray.get(0).getWeatherStateName());
        setImage(img_we,getWeImage(wearray.get(0).getWeatherStateAbbr()));

        String thetemp = String.format("%.2f",wearray.get(0).getTheTemp());
        String mintemp = String.format("%.2f",wearray.get(0).getMinTemp());
        String maxtemp = String.format("%.2f",wearray.get(0).getMaxTemp());

        txt_mintemp.setText(mintemp);
        txt_maxtemp.setText(maxtemp);
        txt_acttemp.setText(thetemp);

        txt_humidity.setText("Humidity : "+wearray.get(0).getHumidity().toString());
        txt_predect.setText("Predictability : "+wearray.get(0).getPredictability().toString()+"%");

        initView(wearray);

    }

    public String getWeImage(String code)
    {
        return "https://www.metaweather.com/static/img/weather/png/"+code+".png";
    }

    public void setImage(ImageView img,String link)
    {
        Picasso.get().load(link).into(img);
    }

    public void initView(ArrayList<ConsolidatedWeather> wearray)
    {
        wearray.remove(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        Adapter adapter = new Adapter(wearray,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }


}
