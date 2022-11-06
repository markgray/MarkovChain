package com.example.android.markovchain

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.markovchain.shakespeare.ShakespeareSonnets

/**
 * Just an [AppCompatActivity] that was created to test some aspect of the API.
 */
class TestBedActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bed)

        val myButton: Button = findViewById(R.id.button)
        val string = "Button Label"
        myButton.text = string
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            ShakespeareSonnets.SONNETS)
        val listView: ListView = findViewById(R.id.listview)
        listView.adapter = adapter
        val list = mutableListOf("a", "b", "c")

        val pair: Pair<Int, Int> = Pair(1, 2)
        list.sortWith { _, _ -> pair.first }
    }
}
