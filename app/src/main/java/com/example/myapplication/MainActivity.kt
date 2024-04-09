package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var resultTextView: TextView
    private lateinit var player1EditText: EditText
    private lateinit var player2EditText: EditText
    private lateinit var historyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseManager = DatabaseManager(this)
        resultTextView = findViewById(R.id.resultTextView)
        player1EditText = findViewById(R.id.player1EditText)
        player2EditText = findViewById(R.id.player2EditText)
        historyTextView = findViewById(R.id.historyTextView)

        val rollButton: Button = findViewById(R.id.rollButton)
        rollButton.setOnClickListener { playGame() }

        val showHistoryButton: Button = findViewById(R.id.showHistoryButton)
        showHistoryButton.setOnClickListener { showHistory() }

        val addPlayerButton: Button = findViewById(R.id.addPlayerButton)
        addPlayerButton.setOnClickListener { addPlayer() }
    }

    private fun playGame() {
        val player1 = player1EditText.text.toString()
        val player2 = player2EditText.text.toString()

        if (player1.isBlank() || player2.isBlank()) {
            resultTextView.text = "Please enter both players' names"
            return
        }

        val player1Score = rollDice()
        val player2Score = rollDice()

        val winner = if (player1Score > player2Score) player1 else if (player2Score > player1Score) player2 else "Draw"

        // Zapisujemy wynik gry do bazy danych
        databaseManager.addResult(GameResult(0, player1, player1Score))
        databaseManager.addResult(GameResult(0, player2, player2Score))

        // Wyświetlamy wynik w interfejsie użytkownika
        val resultString = "Player 1 ($player1) Score: $player1Score\nPlayer 2 ($player2) Score: $player2Score\nWinner: $winner"
        resultTextView.text = resultString
    }

    private fun showHistory() {
        val history = databaseManager.getAllResults()
        if (history.isEmpty()) {
            historyTextView.text = "No game history available"
            return
        }

        val historyString = StringBuilder()
        for (gameResult in history) {
            historyString.append("Player: ${gameResult.player}, Score: ${gameResult.score}\n")
        }
        historyTextView.text = historyString.toString()
    }

    private fun addPlayer() {
        // Tutaj możesz dodać kod obsługujący dodawanie nowego użytkownika
    }

    private fun rollDice(): Int {
        return Random.nextInt(1, 7) // Losujemy liczbę od 1 do 6 (jak w standardowej kostce)
    }
}
