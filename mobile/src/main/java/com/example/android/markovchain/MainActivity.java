package com.example.android.markovchain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.example.android.common.BenchMark;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup rootView = (ViewGroup) findViewById(R.id.main_rootview);
        addButton(ShakespeareActivity.class, "Shakespeare", rootView);
        addButton(ShakespeareMarkovActivity.class, "Shakespeare Markov", rootView);
        addButton(ShakespeareRecylcler.class, "Shakespeare Recycler", rootView);
        addButton(ShakespeareMarkovRecycler.class, "Shakespeare Markov Recycler", rootView);
        addButton(BibleActivity.class, "King James Bible", rootView);
        addButton(BibleMarkovActivity.class, "King James Bible Markov", rootView);
        addButton(BibleRecycler.class, "King James Recycler", rootView);
        addButton(BibleMarkovRecycler.class, "Bible Markov Recycler", rootView);
        addButton(FragmentVersionSkeleton.class, "Fragment Skeleton", rootView);
        addButton(BibleFragment.class, "Bible retained fragment", rootView);
    }

//    BenchMark benchMark = new BenchMark();

    public void addButton(final Class destination, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent problemIntent = new Intent(MainActivity.this, destination);
                startActivity(problemIntent);
            }
        });

        button.setText(description);
        parent.addView(button);
    }
}
