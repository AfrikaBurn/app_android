package net.maiatoday.afrikaburn.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import net.maiatoday.afrikaburn.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_theme_camps:
                    mTextMessage.setText(R.string.title_theme);
                    return true;
                case R.id.navigation_artworks:
                    mTextMessage.setText(R.string.title_theme);
                    return true;
                case R.id.navigation_infrastructure:
                    mTextMessage.setText(R.string.title_infrastructure);
                    return true;
//                case R.id.navigation_performance:
//                    mTextMessage.setText(R.string.title_performance);
//                    return true;
                case R.id.navigation_burn:
                    mTextMessage.setText(R.string.title_burns);
                    return true;
                case R.id.navigation_mutant_vehicles:
                    mTextMessage.setText(R.string.title_mutant_vehicles);
                    return true;
//                case R.id.navigation_favourites:
//                    mTextMessage.setText(R.string.title_mutant_vehicles);
//                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
