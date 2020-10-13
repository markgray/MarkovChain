package com.example.android.markovchain

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TestBedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bed)

        val myButton: Button = findViewById(R.id.button)
        val string = "Button Label"
        myButton.text = string
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                Shakespeare.SONNETS)
        val listView: ListView = findViewById(R.id.listview)
        listView.adapter = adapter
        val list = mutableListOf("a", "b", "c")

        val pair: Pair<Int, Int> = Pair(1,2)
        @Suppress("RedundantSamConstructor")
        list.sortWith(Comparator { _, _ -> pair.first })
    }
}
