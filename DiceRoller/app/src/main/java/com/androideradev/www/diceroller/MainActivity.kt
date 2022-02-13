package com.androideradev.www.diceroller

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


private const val ROLL_DICE_RESOURCE_KEY = "roll_dice_number_key"

class MainActivity : AppCompatActivity() {
    private lateinit var diceImageView: ImageView
    private var diceDrawableResource = R.drawable.dice_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton = findViewById<Button>(R.id.roll_button)
        diceImageView = findViewById(R.id.dice_image_view)

        diceDrawableResource =
            savedInstanceState?.getInt(ROLL_DICE_RESOURCE_KEY) ?: R.drawable.dice_1

        diceImageView.setImageResource(diceDrawableResource)

        rollButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                rollDice()
            }
        })
    }

    private fun rollDice() {
        val dice = Dice()
        when (dice.roll()) {
            1 -> diceDrawableResource = R.drawable.dice_1
            2 -> diceDrawableResource = R.drawable.dice_2
            3 -> diceDrawableResource = R.drawable.dice_3
            4 -> diceDrawableResource = R.drawable.dice_4
            5 -> diceDrawableResource = R.drawable.dice_5
            6 -> diceDrawableResource = R.drawable.dice_6
        }
        diceImageView.setImageResource(diceDrawableResource)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ROLL_DICE_RESOURCE_KEY, diceDrawableResource)
    }
}


class Dice(private val numSides: Int = 6) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}