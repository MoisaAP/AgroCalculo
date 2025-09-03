package com.moisespellegrin.agrocalculo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val btnSementes = findViewById<Button>(R.id.btn_sementes)
        val btnFertilizantes = findViewById<Button>(R.id.btn_pms)
        val btnRegSameadeira = findViewById<Button>(R.id.btn_reg_sameadeira)
        // Ação do botão sementes
        btnSementes.setOnClickListener {
            val intent = Intent(this, SementesActivity::class.java)
            startActivity(intent)
        }
        // Ação do botão fertilizantes
        btnFertilizantes.setOnClickListener {
            val intent = Intent(this, PmsActivity::class.java)
            startActivity(intent)
        }
        // Ação do botão regulagem da sameadeira
        btnRegSameadeira.setOnClickListener {
            val intent = Intent(this, RegSameadeiraActivity::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}