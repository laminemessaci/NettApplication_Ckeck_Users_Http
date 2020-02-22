package com.openclassrooms.netapp.Controllers.Fragments;


import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.netapp.Controllers.model.GithubUser;
import com.openclassrooms.netapp.Controllers.utils.GithubCalls;

import com.openclassrooms.netapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements  GithubCalls.Callbacks {

    ProgressDialog prog ;

    // FOR DESIGN
    @BindView(R.id.fragment_main_textview) TextView textView;


    public MainFragment() { }

    //@RequiresApi (api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    // -----------------
    // ACTIONS
    // -----------------

    @OnClick(R.id.fragment_main_button)
    public void submit(View view) {
        this.executeHttpRequestWithRetrofit();
        prog = new ProgressDialog (super.getActivity ());
        showProgresssDialog(prog);
    }

     public  void showProgresssDialog(ProgressDialog progressD){
        progressD.setMessage ("loading...");
        progressD.show ();
     }
    public  void cancelProgresssDialog(ProgressDialog progressD){
        progressD.cancel ();
    }
    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------
    //  Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        GithubCalls.fetchUserFollowing(this, "JakeWharton");
    }

    //Overide callBack methods
    @Override
    public void onResponse (@Nullable List<GithubUser> users) {
        //  When getting response, we update UI
        if (users != null) this.updateUIWithListOfUsers(users);
    }

    @Override
    public void onFailure () {
        //  When getting error, we update UI
        this.updateUIWhenStopingHTTPRequest("An error happened !");
    }

    // ------------------
    //  UPDATE UI
    // ------------------
    private void updateUIWhenStartingHTTPRequest(){
       // this.textView.setText("Downloading...");
    }

    public void updateUIWhenStopingHTTPRequest(String response){
        this.textView.setText(response);
    }

    //  Update UI showing only name of users

    private void updateUIWithListOfUsers(List<GithubUser> users){
        StringBuilder stringBuilder = new StringBuilder();
        for (GithubUser user : users){
            stringBuilder.append("-"+user.getLogin()+"\n");
        }
        updateUIWhenStopingHTTPRequest(stringBuilder.toString());
        cancelProgresssDialog (prog);

    }


}
