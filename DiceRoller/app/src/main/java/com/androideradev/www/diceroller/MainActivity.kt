package com.androideradev.www.diceroller

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


private const val ROLL_DICE_NUMBER_KEY = "roll_dice_number_key"

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton = findViewById<Button>(R.id.roll_button)
        resultTextView = findViewById(R.id.result_text_view)
        resultTextView.text =
            if (savedInstanceState == null) "0" else savedInstanceState.getString(
                ROLL_DICE_NUMBER_KEY
            )
        rollButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                rollDice()
            }
        })
    }

    private fun rollDice() {
        val dice = Dice()
        val diceRoll = dice.roll()
        resultTextView.text = diceRoll.toString()

       Toast.makeText(this@MainActivity, "Dice Rolled! $diceRoll", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(ROLL_DICE_NUMBER_KEY, resultTextView.text.toString())
    }
}


class Dice(private val numSides: Int = 6) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}