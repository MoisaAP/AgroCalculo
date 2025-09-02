package com.moisespellegrin.agrocalculo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SementesActivity : AppCompatActivity() {

    private lateinit var btnCalPopPlanta: Button
    private lateinit var editPopLinSem: EditText
    private lateinit var editEspLinha: EditText
    private lateinit var editPodGermi: EditText
    private lateinit var editPMS: EditText
    private lateinit var editArea: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sementes)

        btnCalPopPlanta = findViewById(R.id.btn_CalPopPlanta)
        editPopLinSem = findViewById(R.id.edit_PopLinSem)
        editEspLinha = findViewById(R.id.edit_EspLinha)
        editPodGermi = findViewById(R.id.edit_PodGermi)
        editPMS = findViewById(R.id.edit_PMS)
        editArea = findViewById(R.id.edit_Area)

        btnCalPopPlanta.setOnClickListener {
            val popLinSem = editPopLinSem.text.toString().toDoubleOrNull()
            val espLinhaCm = editEspLinha.text.toString().toDoubleOrNull()
            var podGermi = editPodGermi.text.toString().toDoubleOrNull() ?: 100.0
            val PMS = editPMS.text.toString().toDoubleOrNull() ?: 0.0
            val area = editArea.text.toString().toDoubleOrNull() ?: 0.0

            if (popLinSem != null && espLinhaCm != null) {

                val espLinhaM = espLinhaCm / 100.0

                val popM2 = popLinSem / espLinhaM

                val sementesHa = (popM2 * 10000) / (podGermi / 100.0)

                val kgha = (PMS / 1000000.0) * sementesHa

                val semArea = sementesHa * area


                val mensagem = "Total Sementes: %.0f\nSementes/ha: %.0f\nPlantas/m²: %.2f\nKg/ha: %.3f"
                    .format(semArea, sementesHa, popM2, kgha)

                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Resultado")
                    .setMessage(mensagem)
                    .setPositiveButton("Fechar", null)
                    .show()

                dialog.findViewById<TextView>(com.google.android.material.R.id.alertTitle)?.apply {
                    textSize = 22f
                }
                dialog.findViewById<TextView>(android.R.id.message)?.apply {
                    textSize = 18f
                }

            } else {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Atenção")
                    .setMessage("Preencha todos os campos corretamente.")
                    .setPositiveButton("Ok", null)
                    .show()

                dialog.findViewById<TextView>(com.google.android.material.R.id.alertTitle)?.apply {
                    textSize = 22f
                }
                dialog.findViewById<TextView>(android.R.id.message)?.apply {
                    textSize = 18f
                }

            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}