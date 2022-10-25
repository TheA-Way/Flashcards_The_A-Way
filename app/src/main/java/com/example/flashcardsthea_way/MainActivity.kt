package com.example.flashcardsthea_way

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material
    .snackbar
    .Snackbar;

lateinit var flashcardDatabase: FlashcardDatabase
var allFlashcards = mutableListOf<Flashcard>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Buttons
        val plusBtn = findViewById<ImageView>(R.id.plusBtn)
        val nextBtn = findViewById<ImageView>(R.id.nextBtn)
        val backBtn = findViewById<ImageView>(R.id.backBtn)

        //regular vars
        var currentCardDisplayedIndex = 0

        //establish and get flashcard database
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        //if there's something in the flashcard database display it first
        if(allFlashcards.size > 0)
        {
            findViewById<TextView>(R.id.Flashcard_Question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.Flashcard_Answer).text = allFlashcards[0].answer
        }

        //get values of flashards
        var flashcardQuestion = findViewById<TextView>(R.id.Flashcard_Question)
        var flashcardAnswer = findViewById<TextView>(R.id.Flashcard_Answer)


        //get values of new card
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data: Intent? = result.data

            if (data != null) { // Check that we have data returned
                val question = data.getStringExtra("question") // 'string1' needs to match the key we used when we put the string in the Intent
                val answer = data.getStringExtra("answer")

                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "question: $question")
                Log.i("MainActivity", "answer: $answer")

                // Display newly created flashcard
                if (question != "" && answer != "")
                {
                    findViewById<TextView>(R.id.Flashcard_Question).text = question
                    findViewById<TextView>(R.id.Flashcard_Answer).text = answer
                }

                // Save newly created flashcard to database
                if ((question != null && answer != null)&&(question != "" && answer != "")) {
                    flashcardDatabase.insertCard(Flashcard(question, answer))
                    // Update set of flashcards to include new card
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
                else {
                    Log.e("TAG", "Missing question or answer to input into database. Question is $question and answer is $answer")
                }
            }
            else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }

            var status = data?.getStringExtra("status")
            Snackbar.make(findViewById(R.id.Flashcard_Question),
                status.toString(),
                Snackbar.LENGTH_SHORT)
                .show()
        }

        //flashcard actions
        flashcardQuestion.setOnClickListener {
            val answerSideView = findViewById<View>(R.id.Flashcard_Answer)

            //get the center for the clipping circle
            val cx = answerSideView.width/2
            val cy = answerSideView.height/2

            //get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            //create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)

            //hide the question adn show the answer to prepare for playing the animation.
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE

            anim.duration = 1000
            anim.start()
        }
        flashcardAnswer.setOnClickListener {
            val questionSideView = findViewById<View>(R.id.Flashcard_Question)

            //get the center for the clipping circle
            val cx = questionSideView.width/2
            val cy = questionSideView.height/2

            //get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            //create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(questionSideView, cx, cy, 0f, finalRadius)

            //hide the answer and show the question to prepare for playing the animation.
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE

            anim.duration = 1000
            anim.start()
        }

        //button actions
        nextBtn.setOnClickListener {
            // advance our pointer index so we can show the next card
            Log.i("Next Button Index b4 loop: ", currentCardDisplayedIndex.toString())
            currentCardDisplayedIndex += 1
            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.Flashcard_Question), // This should be   the TextView for displaying your flashcard question
                    "You've reached the end of the cards, going back to start.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }

            if(currentCardDisplayedIndex < allFlashcards.size)
            {
                Log.i("Next Button Index after loop: ", currentCardDisplayedIndex.toString())
                // set the question and answer TextViews with data from the database
                findViewById<TextView>(R.id.Flashcard_Question).text = allFlashcards[currentCardDisplayedIndex].question
                findViewById<TextView>(R.id.Flashcard_Answer).text = allFlashcards[currentCardDisplayedIndex].answer
            }

            //animation
            val leftOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_in)

            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })

            findViewById<View>(R.id.Flashcard_Question).startAnimation(leftOutAnim)

            findViewById<View>(R.id.Flashcard_Question).startAnimation(rightInAnim)
        }
        backBtn.setOnClickListener {
            // advance our pointer index so we can show the next card
            Log.i("Back Button Index b4 loop: ", currentCardDisplayedIndex.toString())
            if(currentCardDisplayedIndex == 0) {
                Snackbar.make(
                    findViewById<TextView>(R.id.Flashcard_Question), // This should be the TextView for displaying your flashcard question
                    "You're at the beginning of the set!!!",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }

            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex > 0)
            {
                currentCardDisplayedIndex -= 1
                Log.i("Back Button Index after loop: ", currentCardDisplayedIndex.toString())
                // set the question and answer TextViews with data from the database
                findViewById<TextView>(R.id.Flashcard_Question).text = allFlashcards[currentCardDisplayedIndex].question
                findViewById<TextView>(R.id.Flashcard_Answer).text = allFlashcards[currentCardDisplayedIndex].answer
            }

            //animation
            val leftInAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_in)
            val rightOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_out)

            leftInAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })


            findViewById<View>(R.id.Flashcard_Question).startAnimation(rightOutAnim)
            findViewById<View>(R.id.Flashcard_Question).startAnimation(leftInAnim)
        }
        plusBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            // Launch EndingActivity with the resultLauncher so we can execute more code
            // once we come back here from EndingActivity
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

    }


}