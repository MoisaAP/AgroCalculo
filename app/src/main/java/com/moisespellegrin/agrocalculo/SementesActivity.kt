package com.moisespellegrin.agrocalculo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SementesActivity : AppCompatActivity() {




    private lateinit var btnCalPopPlanta: Button
    private lateinit var editPopLinSem: EditText
    private lateinit var editEspLinha: EditText
    private lateinit var editPodGermi: EditText
    private lateinit var textResultSemHa: TextView
    private lateinit var textResultMQuad: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sementes)

        btnCalPopPlanta = findViewById(R.id.btn_CalPopPlanta)
        editPopLinSem = findViewById(R.id.edit_PopLinSem)
        editEspLinha = findViewById(R.id.edit_EspLinha)
        editPodGermi = findViewById(R.id.edit_PodGermi)
        textResultSemHa = findViewById(R.id.Result_PopSemHa)
        textResultMQuad = findViewById(R.id.result_PopMQuad)

        btnCalPopPlanta.setOnClickListener {
            val popLinSem = editPopLinSem.text.toString().toDoubleOrNull()
            val espLinhaCm = editEspLinha.text.toString().toDoubleOrNull()
            val podGermi = editPodGermi.text.toString().toDoubleOrNull()

            if (popLinSem != null && espLinhaCm != null && podGermi != null) {
                // converte cm → m
                val espLinhaM = espLinhaCm / 100.0

                // população de plantas por m²
                val popM2 = popLinSem / espLinhaM

                // quantidade de sementes por hectare (ajustada pelo poder germinativo)
                val sementesHa = (popM2 * 10000) / (podGermi / 100.0)

                // mostra resultados
                textResultMQuad.text = "População: %.2f plantas/m²".format(popM2)
                textResultSemHa.text = "Sementes: %.0f sementes/ha".format(sementesHa)

            } else {
                textResultMQuad.text = "Preencha todos os campos!"
                textResultSemHa.text = ""
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}