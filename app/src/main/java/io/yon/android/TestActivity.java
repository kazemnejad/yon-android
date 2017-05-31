package io.yon.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by amirhosein on 5/31/17.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("g", "onCrate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("g", "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("g", "onCreateOptionMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test, menu);

        return true;
    }
}
