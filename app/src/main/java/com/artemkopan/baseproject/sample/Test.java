package com.artemkopan.baseproject.sample;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.dialog.DialogProvider;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.widget.ProgressButtonView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class Test extends BaseActivity implements OnDismissListener{

    DialogProvider mDialogProvider = new DialogProvider();

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ProgressButtonView mProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mProgressButton = (ProgressButtonView) findViewById(R.id.progress_btn);

//        ExRecyclerView list = (ExRecyclerView) findViewById(R.id.list);
//
//        list.showProgress();

        /*mDialogProvider.showProgressDialog(this, new Runnable() {
            @Override
            public void run() {
                Test2Activity.startActivity(Test.this);
            }
        });
*/
        mCompositeDisposable.add(
                testObservable("Composite")
                        .takeUntil(mDestroySubject).subscribe());

        testObservable("Tale Until")
                .takeUntil(mDestroySubject).subscribe();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private Observable<Object> testObservable(final String test) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                e.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        Log.i("onDispose: " + test);
                    }
                });
            }
        });
    }

    public void switchProgress(View view) {
        mProgressButton.showProgress(!mProgressButton.isShowProgress());
    }

    public void openDialog(View view) {

        mDialogProvider.showProgressDialog(this);

        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mDialogProvider.showMessageDialog(Test.this, "asdasdasasdko");
            }
        });
        Observable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mDialogProvider.showProgressDialog(Test.this);
            }
        });
        Observable.timer(3300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mDialogProvider.showMessageDialog(Test.this, "qwe12e21");
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.i("onDismiss: ");
    }
}
