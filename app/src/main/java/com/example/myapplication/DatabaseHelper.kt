package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addResult(result: GameResult) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLAYER, result.player)
            put(COLUMN_SCORE, result.score)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllResults(): List<GameResult> {
        val resultList = mutableListOf<GameResult>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_PLAYER, COLUMN_SCORE),
            null, null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val player = it.getString(it.getColumnIndexOrThrow(COLUMN_PLAYER))
                val score = it.getInt(it.getColumnIndexOrThrow(COLUMN_SCORE))
                resultList.add(GameResult(id, player, score))
            }
        }
        return resultList
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DiceGameDB"
        const val TABLE_NAME = "Results"
        const val COLUMN_ID = "_id"
        const val COLUMN_PLAYER = "Player"
        const val COLUMN_SCORE = "Score"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_PLAYER TEXT, $COLUMN_SCORE INTEGER)"
    }
}


