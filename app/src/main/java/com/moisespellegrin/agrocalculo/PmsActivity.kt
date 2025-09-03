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

class PmsActivity : AppCompatActivity() {

    private  lateinit  var btnCalcPMS: Button
    private  lateinit  var editQtdSementes: EditText
    private  lateinit  var editPesoSementes: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pms)

        btnCalcPMS = findViewById(R.id.btn_CalcPMS)
        editQtdSementes = findViewById(R.id.edit_QtdSementes)
        editPesoSementes= findViewById(R.id.edit_PesoSementes)

        btnCalcPMS.setOnClickListener {

            val QtdSementes = editQtdSementes.text.toString().toDoubleOrNull()
            val PesoSementes = editPesoSementes.text.toString().toDoubleOrNull()

            if (QtdSementes != null && PesoSementes != null) {

                val PMS = (1000.0 * PesoSementes) / QtdSementes

                val mensagem = "PMS: %.2fg"
                    .format(PMS)

                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Resultado")
                    .setMessage(mensagem)
                    .setPositiveButton("Fechar", null)
                    .show()

                dialog.findViewById<TextView>(com.google.android.material.R.id.alertTitle)?.apply {
                    textSize = 22f
                }
                dialog.findViewById<TextView>(android.R.id.message)?.apply {
                    textSize = 20f
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