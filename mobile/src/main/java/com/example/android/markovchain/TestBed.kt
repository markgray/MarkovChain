package com.example.android.markovchain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.android.common.Shakespeare

class TestBed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bed)

        val myButton: Button = findViewById(R.id.button)
        val string = "Button Label"
        myButton.text = string
        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                Shakespeare.SONNETS)
        val listView: ListView = findViewById(R.id.listview)
        listView.adapter = adapter


    }
}
