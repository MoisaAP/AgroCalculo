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

import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.moisespellegrin.agrocalculo.database.DataBaseHelper

class SementesActivity : AppCompatActivity() {

    private fun showResultadoDialog(
        totalSementes: Double,
        sementesHa: Double,
        plantasM2: Double,
        kgHa: Double
    ) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_result_sementes, null, false)
        dialog.setContentView(view)

        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Resultado"
        view.findViewById<TextView>(R.id.tvTotalSem).text = "Total Sementes: %.0f".format(totalSementes)
        view.findViewById<TextView>(R.id.tvSemHa).text = "Sementes/ha: %.0f".format(sementesHa)
        view.findViewById<TextView>(R.id.tvPlantM2).text = "Plantas/m²: %.2f".format(plantasM2)
        view.findViewById<TextView>(R.id.tvKgHa).text = "Kg/ha: %.3f".format(kgHa)

        view.findViewById<Button>(R.id.btnDialogOk).setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(true)
        dialog.show()
    }

    private fun showAtencaoDialog(mensagem: String) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_result_sementes, null, false)
        dialog.setContentView(view)

        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Atenção"

        val tvTotalSem = view.findViewById<TextView>(R.id.tvTotalSem)
        val tvSemHa = view.findViewById<TextView>(R.id.tvSemHa)
        val tvPlantM2 = view.findViewById<TextView>(R.id.tvPlantM2)
        val tvKgHa = view.findViewById<TextView>(R.id.tvKgHa)

        tvTotalSem.text = mensagem
        tvTotalSem.textSize = 18f
        tvSemHa.visibility = android.view.View.GONE
        tvPlantM2.visibility = android.view.View.GONE
        tvKgHa.visibility = android.view.View.GONE

        val btnOk = view.findViewById<Button>(R.id.btnDialogOk)
        btnOk.text = "Ok"
        btnOk.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(true)
        dialog.show()
    }


    private lateinit var btnCalPopPlanta: Button
    private lateinit var editPopLinSem: EditText
    private lateinit var editEspLinha: EditText
    private lateinit var editPodGermi: EditText
    private lateinit var editPMS: EditText
    private lateinit var editArea: EditText
    private lateinit var db: DataBaseHelper

    private fun nowIso(): String =
        java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sementes)

        // ToolBar
        window.statusBarColor = getColor(R.color.Verde)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.setTitleTextAppearance(this, R.style.TitleLarge_Custom)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Calcular População de Plantas"
            setDisplayHomeAsUpEnabled(true) // mostra o botão de voltar
            setHomeButtonEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        db = DataBaseHelper(this)

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

                val linha = "\nPop/m²: %.2f\nSementes/ha: %.0f\nKg/ha: %.2f\nSementes área: %.0f"
                    .format(popM2, sementesHa, kgha, semArea)

                db.inserirHistoricoGeral("POPULAÇÂO PLANTAS", linha, nowIso())

                showResultadoDialog(
                    totalSementes = semArea,
                    sementesHa = sementesHa,
                    plantasM2 = popM2,
                    kgHa = kgha
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