package com.example.c4q.capstone.network.barzz;

import android.util.Log;

import com.example.c4q.capstone.network.barzz.barzzmodel.BarzzModel;
<<<<<<< Updated upstream
import com.example.c4q.capstone.userinterface.user.userprofilefragments.PreferencesFragment;
=======
import com.example.c4q.capstone.userinterface.user.onboarding.BarPreferencesFragment;
import com.example.c4q.capstone.userinterface.user.onboarding.CreateProfileFragment;
>>>>>>> Stashed changes
import com.example.c4q.capstone.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by c4q on 3/17/18.
 */

public class BarzzNetworkCall {

    public static void start(final String zipcode) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BARZZ_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        final BarzzService barzzService = retrofit.create(BarzzService.class);

<<<<<<< Updated upstream
        for (int i = 0; i < PreferencesFragment.selectedPrefs.size(); i++) {


            Call<BarzzModel> call = barzzService.getBarzz(zipcode, PreferencesFragment.selectedPrefs.get(i));
=======
        for (int i = 0; i < CreateProfileFragment.selectedPrefs.size(); i++) {


            Call<BarzzModel> call = barzzService.getBarzz(zipcode, CreateProfileFragment.selectedPrefs.get(i));
>>>>>>> Stashed changes

            call.enqueue(new Callback<BarzzModel>() {
                @Override
                public void onResponse(Call<BarzzModel> call, Response<BarzzModel> response) {

                    Log.d("SUCESSSS", response.body().getSuccess().getResults().get(0).getName());


                }

                @Override
                public void onFailure(Call<BarzzModel> call, Throwable t) {

                    t.printStackTrace();

                }
            });


        }


    }


}
