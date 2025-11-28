package com.example.android.markovchain

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.example.android.markovchain.benchmark.TestJavaActivity
import com.example.android.markovchain.benchmark.TestKotlinActivity
import com.example.android.markovchain.benchmark.TestSplitActivity
import com.example.android.markovchain.bible.BibleActivity
import com.example.android.markovchain.biblemarkov.BibleMarkovActivity
import com.example.android.markovchain.clocktrisect.ClockTrisectActivity
import com.example.android.markovchain.shakespeare.ShakespeareActivity
import com.example.android.markovchain.shakespearemarkov.ShakespeareMarkovActivity
import com.example.android.markovchain.transcend.TranscendActivity
import com.example.android.markovchain.whatisman.WhatIsManActivity

/**
 * This is the main Activity which launches all the other Activity's when their "launching Button"
 * is clicked. Its layout file (R.layout.activity_main) consists only of a vertical `LinearLayout`
 * wrapped in a `ScrollView` and `Button`'s and `TextView`'s are added to the `LinearLayout` using
 * java code.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. First we call [enableEdgeToEdge] to enable edge to
     * edge display, then we call our super's implementation of `onCreate`, and set our content
     * view to our layout file `R.layout.activity_main`. We initialize our `val linearLayout` by
     * finding the [ViewGroup] in our content view with the id R.id.linear_layout. We then use our
     * [addButton] method to add buttons that will launch each of our activities to `linearLayout`
     * as well as using our method [addText] to add a [TextView] to separate the "featured"
     * Activity's from experimental ones added to the end of `linearLayout`.
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayout = findViewById<ViewGroup>(R.id.linear_layout)
        addButton(ShakespeareActivity::class.java, "Shakespeare Sonnets", linearLayout)
        addButton(ShakespeareMarkovActivity::class.java, "Shakespeare Markov", linearLayout)
        addButton(BibleActivity::class.java, "Bible", linearLayout)
        addButton(BibleMarkovActivity::class.java, "Bible Markov", linearLayout)
        addButton(ClockTrisectActivity::class.java, "Clock Trisect", linearLayout)
        addButton(WhatIsManActivity::class.java, "What is man?", linearLayout)
        addButton(TranscendActivity::class.java, "Transcendental", linearLayout)
        addText("Testing activities", linearLayout)
        addButton(TestSplitActivity::class.java, "Test Split Speed", linearLayout)
        addButton(TestKotlinActivity::class.java, "Test Kotlin", linearLayout)
        addButton(TestJavaActivity::class.java, "Test Java", linearLayout)
        addButton(TestBedActivity::class.java, "Kotlin Test Bed", linearLayout)
        addButton(RetainedFragmentFunActivity::class.java, "Retained Fragment", linearLayout)
    }

    /**
     * Constructs and adds a [Button] to the [ViewGroup] parameter [parent] designed to launch a
     * different Activity when it is clicked. First we initialize our `val button` to a new instance
     * of [Button], then we set its text to the [String] parameter [description], and we
     * set its `OnClickListener` to a lambda which will (when the [Button] is clicked) create an
     * [Intent] to launch the [Activity] whose `Class` is given in our [Class] parameter [destination]
     * and start that [Activity]. Finally we add  the [Button] `button` to the parameter [parent]
     * (the `LinearLayout` in our layout file in our case).
     *
     * @param destination [Class] of the [Activity] to be started by the Intent we create
     * @param description text for the [Button]
     * @param parent the [ViewGroup] we are adding the [Button] to using **ViewGroup.addView**
     */
    private fun addButton(destination: Class<*>, description: String, parent: ViewGroup) {
        val button = Button(this)
        button.text = description
        /*
         * Called when the button is clicked. We initialize our `val intent` with a new new instance
         * of `Intent` whose application package `Context` is that of 'this' instance of `MainActivity`
         * and whose component class that is to be executed is the `Class` parameter `destination`
         * of the `addButton` method. We then launch the activity specified by `intent`.
         *
         * Parameter: `View` that was clicked
         */
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, destination)
            startActivity(intent)
        }
        parent.addView(button)
    }

    /**
     * Adds a [TextView] displaying the [String] parameter [text] to the [ViewGroup] parameter
     * [parent]. First we initialize our `val mText` with a new instance of [TextView], then we
     * set its text to our [String] parameter [text], and finally we add it to our [ViewGroup]
     * parameter [parent].
     *
     * @param text text to display in the `TextView` we add to `ViewGroup parent`
     * @param parent `ViewGroup` to add our `TextView` to
     */
    private fun addText(text: String, parent: ViewGroup) {
        val mText = TextView(this)
        mText.text = text
        parent.addView(mText)
    }
}
