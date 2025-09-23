package com.moisespellegrin.agrocalculo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moisespellegrin.agrocalculo.database.DataBaseHelper
import java.text.SimpleDateFormat
import java.util.Locale


class HistoricoActivity : AppCompatActivity() {

    private lateinit var db: DataBaseHelper

    private fun preencherHistoricoGeral() {
        val list = findViewById<LinearLayout>(R.id.listGeral)
        list.removeAllViews()

        val historico = db.listarHistoricoGeral()

        val inFmt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        val outFmt = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        val inflater = LayoutInflater.from(this)

        if (historico.isEmpty()) {
            val v = inflater.inflate(R.layout.item_historico, list, false)
            v.findViewById<TextView>(R.id.txtConteudo).apply {
                text = "Sem registros."
                setTextColor(android.graphics.Color.WHITE)
            }
            list.addView(v)
            return
        }

        historico.forEach { h ->
            val v = inflater.inflate(R.layout.item_historico, list, false)
            val txt = v.findViewById<TextView>(R.id.txtConteudo)
            val dataBonita = try { outFmt.format(inFmt.parse(h.createdAt)!!) } catch (_: Exception) { h.createdAt }

            txt.text = "${h.mensagem}\nData: $dataBonita"
            txt.setTextColor(android.graphics.Color.WHITE)

            list.addView(v)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historico)

        db = DataBaseHelper(this)

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

        preencherHistoricoGeral()


        val btnDelete = findViewById<ImageButton>(R.id.btn_deletHist)
        btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Limpar histórico")
                .setMessage("Tem certeza que deseja remover todos os registros do histórico?")
                .setPositiveButton("Limpar") { _, _ ->
                    val apagados = db.deletarTodoHistoricoGeral()
                    Toast.makeText(this, "Registros removidos: $apagados", Toast.LENGTH_SHORT).show()
                    preencherHistoricoGeral()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        preencherHistoricoGeral()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onResume() {
        super.onResume()
        preencherHistoricoGeral()
    }
}