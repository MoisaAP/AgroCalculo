package com.moisespellegrin.agrocalculo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "agrocalc.db"
private const val DB_VERSION = 1

// Tabela de histórico unificado
private const val TABLE_HIST_ALL = "historico_geral"
private const val ALL_ID = "_id"
private const val ALL_TIPO = "tipo"            // "PMS" | "SEMENTES" | "SEMEADEIRA"
private const val ALL_MSG = "mensagem"         // texto pronto para exibir
private const val ALL_TIMESTAMP = "created_at" // ISO-8601 "yyyy-MM-dd'T'HH:mm:ss"

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createAll = """
            CREATE TABLE $TABLE_HIST_ALL (
                $ALL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ALL_TIPO TEXT NOT NULL,
                $ALL_MSG TEXT NOT NULL,
                $ALL_TIMESTAMP TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createAll)
        db.execSQL("CREATE INDEX idx_all_created_at ON $TABLE_HIST_ALL($ALL_TIMESTAMP DESC)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Como estamos começando do zero, estratégia simples:
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HIST_ALL")
        onCreate(db)
    }

    // CRUD mínimo para timeline
    fun inserirHistoricoGeral(tipo: String, mensagem: String, timestampIso: String): Long {
        val cv = ContentValues().apply {
            put(ALL_TIPO, tipo)
            put(ALL_MSG, mensagem)
            put(ALL_TIMESTAMP, timestampIso)
        }
        return writableDatabase.insert(TABLE_HIST_ALL, null, cv)
    }

    fun listarHistoricoGeral(): List<HistoricoGeral> {
        val items = mutableListOf<HistoricoGeral>()
        val cols = arrayOf(ALL_ID, ALL_TIPO, ALL_MSG, ALL_TIMESTAMP)
        readableDatabase.query(
            TABLE_HIST_ALL, cols, null, null, null, null, "$ALL_TIMESTAMP DESC"
        ).use { c ->
            while (c.moveToNext()) {
                items.add(
                    HistoricoGeral(
                        id = c.getLong(c.getColumnIndexOrThrow(ALL_ID)),
                        tipo = c.getString(c.getColumnIndexOrThrow(ALL_TIPO)),
                        mensagem = c.getString(c.getColumnIndexOrThrow(ALL_MSG)),
                        createdAt = c.getString(c.getColumnIndexOrThrow(ALL_TIMESTAMP))
                    )
                )
            }
        }
        return items
    }

    fun deletarTodoHistoricoGeral(): Int {
        return writableDatabase.delete(TABLE_HIST_ALL, null, null)
    }
}

data class HistoricoGeral(
    val id: Long,
    val tipo: String,
    val mensagem: String,
    val createdAt: String
)