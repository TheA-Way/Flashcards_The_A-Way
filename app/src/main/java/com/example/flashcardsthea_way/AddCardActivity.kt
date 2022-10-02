package com.example.flashcardsthea_way

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.media.Image
import android.widget.EditText

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        //variables
        val cancelBtn = findViewById<ImageView>(R.id.redxBtn)
        val saveBtn = findViewById<ImageView>(R.id.saveBtn)

        //actions
        cancelBtn.setOnClickListener {
            finish()
        }

        //save button
        saveBtn.setOnClickListener {
            val fr = findViewById<EditText>(R.id.Front_entry).text.toString()
            val bk = findViewById<EditText>(R.id.Back_entry).text.toString()

            val data = Intent() // create a new Intent, this is where we will put our data

            data.putExtra(
                "string1",
                fr
            ) // puts one string into the Intent, with the key as 'string1'

            data.putExtra(
                "string2",
                bk
            ) // puts another string into the Intent, with the key as 'string2

            setResult(RESULT_OK, data) // set result code and bundle data for response

            finish() // closes this activity and pass data to the original activity that launched this activity

        }

    }
}