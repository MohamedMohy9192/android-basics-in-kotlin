package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _score = 0
    val score: Int
        get() = _score
    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        // Call getNextWord() to initialize the late init properties (_currentScrambledWord, currentWord)
        // while the view model instance is created
        getNextWord()
    }


    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    fun getNextWord() {
        // Get random word from the words list
        currentWord = allWordsList.random()
        // Convert the word string to characters array
        val tempWord = currentWord.toCharArray()
        // keep shuffling the word characters if it's as the original word
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord)) {
            // If the word already exists in the wordsList which tracks the used words
            // Call getNextWork method to generate another random word from the original words list
            getNextWord()
        } else {
            // The word not used before assign it to the _currentScrambledWord
            _currentScrambledWord = String(tempWord)
            // Increase the currentWordCount
            _currentWordCount++
            // Add the world to tracking list
            wordsList.add(currentWord)
        }
    }

    /* Returns true if the current word count is less than MAX_NO_OF_WORDS.
     Updates the next word.
     */
    fun nextWord() = if (currentWordCount < MAX_NO_OF_WORDS) {
        getNextWord()
        true
    } else false


    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    /*Helper method to validate player word*/
    fun isUserWordCorrect(playerWord: String): Boolean {
        //  Validate the player's word and increase the score if the guess is correct.
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializedData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }
}