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
import com.google.android.material.appbar.MaterialToolbar

import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class RegSameadeiraActivity : AppCompatActivity() {

    private fun showResultadoDialog(
        coletaLinhaG: Double,
        distanciaM: Double,
        sementesPorMetro: Double,
        kgHa: Double
    ) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_result_reg_samead, null, false)
        dialog.setContentView(view)

        // Fundo escurecido e sem bordas
        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Resultado"
        view.findViewById<TextView>(R.id.tvColeta).text = "Coleta por linha: %.2f g".format(coletaLinhaG)
        view.findViewById<TextView>(R.id.tvDistancia).text = "Distância percorrida: %.2f m".format(distanciaM)
        view.findViewById<TextView>(R.id.tvSementeMetro).text = "Sementes/m linear: %.1f".format(sementesPorMetro)
        view.findViewById<TextView>(R.id.tvKgHa).text = "Kg/ha: %.3f".format(kgHa)

        view.findViewById<Button>(R.id.btnDialogOk).setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(true)
        dialog.show()
    }

    private fun showAtencaoDialog(mensagem: String) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_result_reg_samead, null, false)
        dialog.setContentView(view)

        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Atenção"

        val tvColeta = view.findViewById<TextView>(R.id.tvColeta)
        val tvDistancia = view.findViewById<TextView>(R.id.tvDistancia)
        val tvSementeMetro = view.findViewById<TextView>(R.id.tvSementeMetro)
        val tvKgHa = view.findViewById<TextView>(R.id.tvKgHa)

        tvColeta.text = mensagem
        tvColeta.textSize = 18f
        tvDistancia.visibility = android.view.View.GONE
        tvSementeMetro.visibility = android.view.View.GONE
        tvKgHa.visibility = android.view.View.GONE

        val btnOk = view.findViewById<Button>(R.id.btnDialogOk)
        btnOk.text = "Ok"
        btnOk.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(true)
        dialog.show()
    }


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

// ToolBar
        window.statusBarColor = getColor(R.color.Verde)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.setTitleTextAppearance(this, R.style.TitleLarge_Custom)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Calcular Regulagem da Semeadora"
            setDisplayHomeAsUpEnabled(true) // mostra o botão de voltar
            setHomeButtonEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }


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

                showResultadoDialog(
                    coletaLinhaG = gColeta,
                    distanciaM = distPercorM,
                    sementesPorMetro = semPorMLin,
                    kgHa = QuiloHa
                )
            } else {
                showAtencaoDialog("Preencha todos os campos corretamente.")
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

    /* val mensagem = "Coleta/linha: %.2fg\nDistância: %.2fm\nGrão/m: %.1f\nKg/ha: %.3f"
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

            } */