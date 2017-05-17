package com.artemkopan.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.artemkopan.recycler.view.ExRecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExRecyclerView view = (ExRecyclerView) findViewById(R.id.test);
        view.showText("asdasdokaspodkasdasdasdklnaskdnaklsndasnkdnaskldnlasnddlsadalksnklndsandsankdsandsankldklsanlkndasoaskdoaskokdasopkdsao");
    }
}
