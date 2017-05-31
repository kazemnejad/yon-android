package io.yon.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.yon.android.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionMenu(true);
    }

    @Override
    public boolean onCreateOptionMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                Log.d("menu", "createNew");
                break;

            case R.id.open:
                Log.d("menu", "opn");
                break;
        }
        return false;
    }
}
