package com.moisespellegrin.agrocalculo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.moisespellegrin.agrocalculo.database.DataBaseHelper
import java.text.SimpleDateFormat
import java.util.Locale


class HistoricoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historico)

        // ToolBar
        window.statusBarColor = getColor(R.color.Verde)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.setTitleTextAppearance(this, R.style.TitleLarge_Custom)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Histórico"
            setDisplayHomeAsUpEnabled(true) // mostra o botão de voltar
            setHomeButtonEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val listPms = findViewById<LinearLayout>(R.id.listPms)
        val db = DataBaseHelper(this)
        val historicoPms = db.listarHistorico()

        android.util.Log.d("HIST", "registros=${historicoPms.size}")
        historicoPms.forEach {
            android.util.Log.d("HIST", "qtd=${it.qtd} peso=${it.peso} res=${it.resultado} at=${it.createdAt}")
        }

        val inFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outFmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val inflater = LayoutInflater.from(this)

        historicoPms.forEach { h ->
            val v = inflater.inflate(R.layout.item_historico, listPms, false)
            val txt = v.findViewById<TextView>(R.id.txtConteudo)
            val dataBonita = try { outFmt.format(inFmt.parse(h.createdAt)!!) } catch (_:Exception){ h.createdAt }
            txt.text = "PMS:\nQuantidade de sementes: %.0f\nPeso das sementes: %.2fg\nResultado PMS: %.2fg\nData: %s"
                .format(h.qtd, h.peso, h.resultado, dataBonita)
            listPms.addView(v)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}