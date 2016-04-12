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
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.linear_layout);
        addButton(ShakespeareRecylcler.class, "Shakespeare Recycler", linearLayout);
        addButton(ShakespeareMarkovRecycler.class, "Shakespeare Markov Recycler", linearLayout);
        addButton(BibleFragment.class, "Bible retained fragment", linearLayout);
        addButton(BibleMarkovRecycler.class, "Bible Markov Recycler", linearLayout);
        addButton(FragmentVersionSkeleton.class, "Fragment Skeleton", linearLayout);
        addButton(BibleMarkovFragment.class, "Buggy Bible Markov Fragment", linearLayout);
        addButton(ShakespeareActivity.class, "Shakespeare", linearLayout);
        addButton(ShakespeareMarkovActivity.class, "Shakespeare Markov", linearLayout);
        addButton(BibleActivity.class, "King James Bible", linearLayout);
        addButton(BibleMarkovActivity.class, "King James Bible Markov", linearLayout);
        addButton(BibleRecycler.class, "King James Recycler", linearLayout);
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
