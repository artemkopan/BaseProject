package com.artemkopan.baseproject.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.artemkopan.baseproject.recycler.view.ExRecyclerView;

public class MainActivity extends AppCompatActivity {

    private ExRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (ExRecyclerView) findViewById(R.id.recycler);

        recyclerView.showText();
    }

    public void testClick(View view) {
        recyclerView.setText("ASDasdassads\n\n\n\n\n\n\nasdasdasp[dpalspdlaspldpalsp[dlpa[sldp[alsp[dl[askdoasjpodja[sdp[asopdkasopdpoasjidjasijdipoasopdkopaskdopksaopdkasopkdpokaspokdpoksad\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "asdasdasp[dpalspdlaspldpalsp[dlpa[sldp[alsp[dl[askdoasjpodja[sdp[asopdkasopdpoasjidjasijdipoasopdkopaskdopksaopdkasopkdpokaspokdpoksa\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "asdasdasp[dpalspdlaspldpalsp[dlpa[sldp[alsp[dl[askdoasjpodja[sdp[asopdkasopdpoasjidjasijdipoasopdkopaskdopksaopdkasopkdpokaspokdpoksa");
        recyclerView.showText();
    }
}
