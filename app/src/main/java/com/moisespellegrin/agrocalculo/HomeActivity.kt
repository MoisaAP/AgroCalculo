package com.moisespellegrin.agrocalculo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // ToolBar
        window.statusBarColor = getColor(R.color.Verde)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.setTitleTextAppearance(this, R.style.TitleLarge_Custom)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "AgroCálculo"
            setDisplayHomeAsUpEnabled(true) // mostra o botão de voltar
            setHomeButtonEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val btnSementes = findViewById<Button>(R.id.btn_sementes)
        val btnPMS = findViewById<Button>(R.id.btn_pms)
        val btnRegSameadeira = findViewById<Button>(R.id.btn_reg_sameadeira)
        val btnHistorico = findViewById<Button>(R.id.btn_calc_salvos)

        // Ação do botão sementes
        btnSementes.setOnClickListener {
            val intent = Intent(this, SementesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        // Ação do botão fertilizantes
        btnPMS.setOnClickListener {
            val intent = Intent(this, PmsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        // Ação do botão regulagem da Semeadora
        btnRegSameadeira.setOnClickListener {
            val intent = Intent(this, RegSameadeiraActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        // Ação do botão histórico
        btnHistorico.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}