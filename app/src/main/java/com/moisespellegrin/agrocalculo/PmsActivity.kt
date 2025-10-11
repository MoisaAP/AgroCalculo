package com.moisespellegrin.agrocalculo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

import com.moisespellegrin.agrocalculo.database.DataBaseHelper


class PmsActivity : AppCompatActivity() {

    private fun showCustomDialog(title: String, message: String, buttonText: String = "Fechar") {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_result_pms, null, false)
        dialog.setContentView(view)

        // Escurecer o fundo
        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f) // intensidade do escurecimento
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        val tvTitle = view.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = view.findViewById<TextView>(R.id.tvDialogMessage)
        val btnOk = view.findViewById<Button>(R.id.btnDialogOk)

        tvTitle.text = title
        tvMessage.text = message
        btnOk.text = buttonText

        btnOk.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(true)
        dialog.show()
    }



    private  lateinit  var btnCalcPMS: Button
    private  lateinit  var editQtdSementes: EditText
    private  lateinit  var editPesoSementes: EditText
    private lateinit var db: DataBaseHelper

    private fun nowIso(): String =
        java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pms)

        // ToolBar
        window.statusBarColor = getColor(R.color.Verde)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.setTitleTextAppearance(this, R.style.TitleLarge_Custom)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Calcular Peso de Mil Sementes"
            setDisplayHomeAsUpEnabled(true) // mostra o botão de voltar
            setHomeButtonEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        db = DataBaseHelper(this)

        btnCalcPMS = findViewById(R.id.btn_CalcPMS)
        editQtdSementes = findViewById(R.id.edit_QtdSementes)
        editPesoSementes= findViewById(R.id.edit_PesoSementes)

        btnCalcPMS.setOnClickListener {

            val QtdSementes = editQtdSementes.text.toString().toDoubleOrNull()
            val PesoSementes = editPesoSementes.text.toString().toDoubleOrNull()

            if (QtdSementes != null && PesoSementes != null) {

                val PMS = (1000.0 * PesoSementes) / QtdSementes

                val linha = "\nQuantidade: %.0f\nPeso: %.2fg\nResultado: %.2fg"
                    .format(QtdSementes, PesoSementes, PMS)

                db.inserirHistoricoGeral("PMS", linha, nowIso())

                val mensagem = "PMS: %.2fg".format(PMS)
                showCustomDialog(title = "Resultado", message = mensagem, buttonText = "Fechar")

            } else {
                showCustomDialog(title = "Atenção", message = "Preencha todos os campos corretamente.", buttonText = "Ok")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}