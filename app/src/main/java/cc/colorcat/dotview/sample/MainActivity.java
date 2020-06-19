package cc.colorcat.dotview.sample;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cc.colorcat.dotview.DotLayout;

public class MainActivity extends AppCompatActivity {
    private DotLayout mButtonDotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(mClick);

        View button = findViewById(R.id.button);
        button.setOnClickListener(mClick);
        mButtonDotLayout = DotLayout.inject(button, true);
        mButtonDotLayout.setCount(6);
        mButtonDotLayout.setDotViewTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    mButtonDotLayout.increaseCount();
                    break;
                case R.id.text:
                    mButtonDotLayout.decreaseCount();
                    break;
                default:
                    break;
            }
        }
    };
}