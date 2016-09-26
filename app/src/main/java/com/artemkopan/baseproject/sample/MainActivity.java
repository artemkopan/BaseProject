package com.artemkopan.baseproject.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.recycler.view.ExRecyclerView;
import com.artemkopan.baseproject.rx.BaseRx;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private ExRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (ExRecyclerView) findViewById(R.id.recycler);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.showProgress();
            }
        });
//        Observable.create(
//                new ObservableOnSubscribe<Object>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
//
//                    }
//                })
//                  .compose(BaseRx.applySchedulers())
//                  .doOnSubscribe(new Consumer<Disposable>() {
//                      @Override
//                      public void accept(Disposable disposable) throws Exception {
//                          recyclerView.showProgress();
//                      }
//                  })
//                  .subscribe(new Consumer<Object>() {
//                      @Override
//                      public void accept(Object o) throws Exception {
//
//                      }
//                  });
    }

    public void testClick(View view) {
        recyclerView.setText(
                "ASDasdassads\n\n\n\n\n\n\nasdasdasp[dpalspdlaspldpalsp[dlpa[sldp[alsp[dl[askdoasjpodja[sdp[asopdkasopdpoasjidjasijdipoasopdkopaskdopksaopdkasopkdpokaspokdpoksad\n" +
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
