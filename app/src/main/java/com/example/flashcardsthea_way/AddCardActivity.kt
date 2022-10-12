package com.example.flashcardsthea_way

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.media.Image
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        //variables
        val cancelBtn = findViewById<ImageView>(R.id.redxBtn)
        val saveBtn = findViewById<ImageView>(R.id.saveBtn)

        //actions
        cancelBtn.setOnClickListener {
           val data = Intent()
            data.putExtra(
                "status",
                "card cancelled"
            )
            setResult(RESULT_OK, data) // set result code and bundle data for response
            finish()
        }

        //save button
        saveBtn.setOnClickListener {
            val fr = findViewById<EditText>(R.id.Front_entry).text.toString()
            val bk = findViewById<EditText>(R.id.Back_entry).text.toString()

            val data = Intent() // create a new Intent, this is where we will put our data

            data.putExtra(
                "question",
                fr
            ) // puts one string into the Intent, with the key as 'string1'

            data.putExtra(
                "answer",
                bk
            ) // puts another string into the Intent, with the key as 'string2

            if ((bk == "") && (fr == "")) {
                data.putExtra(
                    "status",
                    "card empty"
                )
            }
            else{

                data.putExtra(
                    "status",
                    "card saved"
                )
            }

            setResult(RESULT_OK, data) // set result code and bundle data for response

            finish() // closes this activity and pass data to the original activity that launched this activity
        }

    }
}