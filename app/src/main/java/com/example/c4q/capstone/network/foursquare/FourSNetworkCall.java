package com.example.c4q.capstone.network.foursquare;

import android.util.Log;

import com.example.c4q.capstone.network.foursquare.foursquaremodel.FourSquareModel;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< Updated upstream
import com.example.c4q.capstone.userinterface.user.userprofilefragments.PreferencesFragment;
=======
import com.example.c4q.capstone.userinterface.user.onboarding.BarPreferencesFragment;
import com.example.c4q.capstone.userinterface.user.onboarding.CreateProfileFragment;
>>>>>>> Stashed changes
=======
import com.example.c4q.capstone.userinterface.user.onboarding.BarPreferencesFragment;
>>>>>>> e78fbaa914bf5df5bdcd537990f9686e8c782387
=======
import com.example.c4q.capstone.userinterface.user.userprofilefragments.PreferencesFragment;
>>>>>>> parent of aba0275... prefs update
import com.example.c4q.capstone.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by c4q on 3/20/18.
 */

public class FourSNetworkCall {


    public static void start(final String zipcode) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FOUR_SQUARE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        final FourSService fourSService = retrofit.create(FourSService.class);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< Updated upstream
=======
>>>>>>> parent of aba0275... prefs update
        for (int i = 0; i < PreferencesFragment.selectedPrefs.size(); i++) {


            Call<FourSquareModel> call = fourSService.getVenues(PreferencesFragment.selectedPrefs.get(i));
<<<<<<< HEAD
=======
        for (int i = 0; i < CreateProfileFragment.selectedPrefs.size(); i++) {


            Call<FourSquareModel> call = fourSService.getVenues(CreateProfileFragment.selectedPrefs.get(i));
>>>>>>> Stashed changes
=======
        for (int i = 0; i < BarPreferencesFragment.selectedPrefs.size(); i++) {


            Call<FourSquareModel> call = fourSService.getVenues(BarPreferencesFragment.selectedPrefs.get(i));
>>>>>>> e78fbaa914bf5df5bdcd537990f9686e8c782387
=======
>>>>>>> parent of aba0275... prefs update

            call.enqueue(new Callback<FourSquareModel>() {
                @Override
                public void onResponse(Call<FourSquareModel> call, Response<FourSquareModel> response) {

                    Log.d("SUCESSSS", response.body().getResponse().getVenues().get(0).getName());


                }

                @Override
                public void onFailure(Call<FourSquareModel> call, Throwable t) {

                    t.printStackTrace();

                }
            });


        }


    }


}
