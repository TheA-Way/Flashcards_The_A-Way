package com.example.flashcardsthea_way

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material
    .snackbar
    .Snackbar;


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashcardQuestion = findViewById<TextView>(R.id.Flashcard_Question)
        val flashcardAnswer = findViewById<TextView>(R.id.Flashcard_Answer)
        val plusBtn = findViewById<ImageView>(R.id.plusBtn)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                val string1 = data.getStringExtra("string1") // 'string1' needs to match the key we used when we put the string in the Intent
                val string2 = data.getStringExtra("string2")

                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "string1: $string1")
                Log.i("MainActivity", "string2: $string2")

                findViewById<TextView>(R.id.Flashcard_Question).text = string1
                findViewById<TextView>(R.id.Flashcard_Answer).text = string2
            }

            else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }

            Snackbar.make(findViewById(R.id.Flashcard_Question),
                "card saved",
                Snackbar.LENGTH_SHORT)
                .show()
        }

        //actions
        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }
        flashcardAnswer.setOnClickListener {
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }

        plusBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            // Launch EndingActivity with the resultLauncher so we can execute more code
            // once we come back here from EndingActivity
            resultLauncher.launch(intent)


        }

    }
}