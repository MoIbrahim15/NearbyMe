package com.mohamedibrahim.nearbyme.activities;

import android.os.Bundle;
import com.mohamedibrahim.nearbyme.R;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class HomeActivity extends ParentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
        changeTitle(R.string.app_name);
    }


}
