package com.example.android.markovchain

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * This is the main Activity which launches all the other Activity's when their "launching Button"
 * is clicked. Its layout file (R.layout.activity_main) consists only of a vertical `LinearLayout`
 * wrapped in a `ScrollView` and `Button`'s and `TextView`'s are added to the `LinearLayout` using
 * java code.
 */
@Suppress("MemberVisibilityCanBePrivate")
class MainActivity : Activity() {

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * `onCreate`, then we set our content view to our layout file R.layout.activity_main. We
     * initialize our `val linearLayout` by finding the [ViewGroup] in our content view with the
     * id R.id.linear_layout. We then use our [addButton] method to add buttons that will
     * launch each of our activities to `linearLayout` as well as using our method [addText]
     * to add a [TextView] to separate the "featured" Activity's from experimental ones added
     * to the end of `linearLayout`.
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayout = findViewById<ViewGroup>(R.id.linear_layout)
        addButton(ShakespeareRecycler::class.java, "Shakespeare", linearLayout)
        addButton(ShakespeareMarkovRecycler::class.java, "Shakespeare Markov", linearLayout)
        addButton(BibleMain::class.java, "Bible", linearLayout)
        addButton(BibleMarkovFragment::class.java, "Bible Markov", linearLayout)
        addButton(ClockTrisect::class.java, "Clock Trisect", linearLayout)
        addButton(WhatIsMan::class.java, "What is man?", linearLayout)
        addButton(TranscendActivity::class.java, "Transcendental", linearLayout)
        addText("Testing activities", linearLayout)
        addButton(TestBed::class.java, "Kotlin Test Bed", linearLayout)
        addButton(FragmentVersionSkeleton::class.java, "Fragment Skeleton", linearLayout)
        addButton(TestBenchMark::class.java, "Test BenchMark", linearLayout)
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
    fun addButton(destination: Class<*>, description: String, parent: ViewGroup) {
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
    fun addText(text: String, parent: ViewGroup) {
        val mText = TextView(this)
        mText.text = text
        parent.addView(mText)
    }
}