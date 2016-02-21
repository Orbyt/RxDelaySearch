package orbyt.rxdelaysearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "RxDelaySearch";

    @Bind(R.id.searchField)
    EditText mSearchField;
    @Bind(R.id.searchOutput)
    TextView mSearchOutput;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSubscription = RxTextView.textChangeEvents(mSearchField)
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                    //Called every time theres a text change and then a pause for at least 600ms.
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mSearchOutput.setText("Searched for: " + textViewTextChangeEvent.text());
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Lets unsubscribe here
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
