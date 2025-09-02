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

class RegSameadeiraActivity : AppCompatActivity() {

    private  lateinit  var btnCalRegSame: Button
    private  lateinit  var editEspLinha: EditText
    private  lateinit  var editCircRoda: EditText
    private  lateinit  var editPMS: EditText
    private  lateinit  var editKgHa: EditText
    private  lateinit  var editSemLine: EditText
    private  lateinit  var editQtdVoltas: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reg_sameadeira)

        btnCalRegSame = findViewById(R.id.btn_CalRegSame)
        editEspLinha = findViewById(R.id.edit_EspLinha)
        editCircRoda= findViewById(R.id.edit_CircRoda)
        editPMS = findViewById(R.id.edit_PMS)
        editKgHa = findViewById(R.id.edit_KgHa)
        editSemLine = findViewById(R.id.edit_SemLine)
        editQtdVoltas = findViewById(R.id.edit_QtdVoltas)

        btnCalRegSame.setOnClickListener {
            val espLinhaCm = editEspLinha.text.toString().toDoubleOrNull()
            val circRodaM = editCircRoda.text.toString().toDoubleOrNull()
            val PMS = editPMS.text.toString().toDoubleOrNull()
            val KgHa = editKgHa.text.toString().toDoubleOrNull() ?: 0.0
            val SemLine = editSemLine.text.toString().toDoubleOrNull() ?: 0.0
            val qtdVoltas = editQtdVoltas.text.toString().toDoubleOrNull()

            if (espLinhaCm != null && circRodaM != null && PMS != null && qtdVoltas != null) {

                val espLinhaM = espLinhaCm / 100.0

                val distPercorM = circRodaM * qtdVoltas

                val gPorM2 = (KgHa * 1000.0) / 10_000.0

                val gMetroLinear = gPorM2 * espLinhaM

                val gColeta = gMetroLinear * distPercorM

                val semPorMLin = (KgHa * espLinhaCm) / PMS

                val QuiloHa = (SemLine * PMS) / espLinhaCm

                val mensagem = "Coleta/linha: %.2fg\nDistância: %.2fm\nGrão/m: %.1f\nKg/ha: %.3f"
                    .format(gColeta, distPercorM, semPorMLin, QuiloHa)

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