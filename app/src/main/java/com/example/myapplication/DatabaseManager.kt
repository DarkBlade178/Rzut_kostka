package com.example.myapplication

import android.content.ContentValues
import android.content.Context

class DatabaseManager(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun addResult(result: GameResult) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PLAYER, result.player)
            put(DatabaseHelper.COLUMN_SCORE, result.score)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    fun getAllResults(): List<GameResult> {
        val resultList = mutableListOf<GameResult>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_PLAYER, DatabaseHelper.COLUMN_SCORE),
            null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val player = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAYER))
                val score = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE))
                resultList.add(GameResult(id, player, score))
            }
            close()
        }
        db.close()
        return resultList
    }
}
