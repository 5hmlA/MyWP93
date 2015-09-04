package com.refuse.mchar.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.refuse.mchar.R;
import com.refuse.mchar.view.pie.MPie;

import java.util.ArrayList;
import java.util.List;

public class MchartActivity extends AppCompatActivity {

    private MPie rpie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchart);
        rpie = (MPie) findViewById(R.id.pie);
        setdata();
    }

    private void setdata() {
        List<Float> data = new ArrayList<Float>();
        data.add(45f);
        data.add(35f);
        data.add(42f);
        data.add(15f);
        data.add(35f);
        rpie.setPiedata(data);
        List<String> desc = new ArrayList<>();
        desc.add("33");
        desc.add("23");
        desc.add("13");
        desc.add("13");
        desc.add("13");
        rpie.setDescPiedata(desc);
        //		rar.setAniCurrentProgress(10);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
