package com.moisespellegrin.agrocalculo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "agrocalc.db"
private const val DB_VERSION = 1

private const val TABLE_HIST = "historico_pms"
private const val COL_ID = "_id"
private const val COL_QTD = "qtd_sementes"
private const val COL_PESO = "peso_sementes"
private const val COL_RESULT = "pms_result"
private const val COL_TIMESTAMP = "created_at" // ISO-8601 string

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_HIST (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_QTD REAL NOT NULL,
                $COL_PESO REAL NOT NULL,
                $COL_RESULT REAL NOT NULL,
                $COL_TIMESTAMP TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createTable)

        // Índices para consultas ordenadas/filtradas
        db.execSQL("CREATE INDEX idx_hist_created_at ON $TABLE_HIST($COL_TIMESTAMP DESC)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // migração
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HIST")
        onCreate(db)
    }

    fun inserirHistorico(qtd: Double, peso: Double, resultado: Double, timestampIso: String): Long {
        val values = ContentValues().apply {
            put(COL_QTD, qtd)
            put(COL_PESO, peso)
            put(COL_RESULT, resultado)
            put(COL_TIMESTAMP, timestampIso)
        }
        return writableDatabase.insert(TABLE_HIST, null, values)
    }

    fun listarHistorico(): List<HistoricoPms> {
        val items = mutableListOf<HistoricoPms>()
        val cols = arrayOf(COL_ID, COL_QTD, COL_PESO, COL_RESULT, COL_TIMESTAMP)
        val c = readableDatabase.query(
            TABLE_HIST, cols,
            null, null, null, null,
            "$COL_TIMESTAMP DESC"
        )
        c.use {
            while (it.moveToNext()) {
                items.add(
                    HistoricoPms(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                        qtd = it.getDouble(it.getColumnIndexOrThrow(COL_QTD)),
                        peso = it.getDouble(it.getColumnIndexOrThrow(COL_PESO)),
                        resultado = it.getDouble(it.getColumnIndexOrThrow(COL_RESULT)),
                        createdAt = it.getString(it.getColumnIndexOrThrow(COL_TIMESTAMP))
                    )
                )
            }
        }
        return items
    }
}

data class HistoricoPms(
    val id: Long,
    val qtd: Double,
    val peso: Double,
    val resultado: Double,
    val createdAt: String
)