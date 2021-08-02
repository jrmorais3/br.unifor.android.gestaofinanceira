package br.unifor.cct.gestaofinanceira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.*
import br.unifor.cct.gestaofinanceira.R
import br.unifor.cct.gestaofinanceira.database.LancamentoDAO
import br.unifor.cct.gestaofinanceira.model.Lancamento
import br.unifor.cct.gestaofinanceira.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LancamentoFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mLancamentoFormTitle: TextView
    private lateinit var mLancamentoFormName: EditText
    private lateinit var mLancamentoFormValue: EditText
    private lateinit var mLancamentoFormIsReceita: Switch
    private lateinit var mLancamentoFormSave: Button

    private var mUserId = -1
    private var mLancamentoId = -1

    private lateinit var mLancamentoDAO: LancamentoDAO

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamento_form)

        mLancamentoDAO = DatabaseUtil.getInstance(applicationContext).getTaskDAO()

        mUserId = intent.getIntExtra("userId", -1)
        mLancamentoId = intent.getIntExtra("lancamentoId", -1)

        mLancamentoFormTitle = findViewById(R.id.task_form_textview_title)
        mLancamentoFormName = findViewById(R.id.lancamento_form_editText_name)
        mLancamentoFormValue = findViewById(R.id.lancamento_form_editText_value)
        mLancamentoFormIsReceita = findViewById(R.id.lancamento_form_switch_isdone)

        mLancamentoFormSave = findViewById(R.id.lancamento_form_button_save)
        mLancamentoFormSave.setOnClickListener(this)

        if (mLancamentoId !== -1) {
            GlobalScope.launch {
                val task = mLancamentoDAO.find(mLancamentoId)

                handler.post{
                    mLancamentoFormTitle.text = Editable.Factory.getInstance().newEditable("Editar Lançamento")
                    mLancamentoFormName.text = Editable.Factory.getInstance().newEditable(task.name)
                    mLancamentoFormValue.text = Editable.Factory.getInstance().newEditable(task.value)
                    mLancamentoFormIsReceita.isChecked = task.isReceita
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.lancamento_form_button_save -> {

                val name = mLancamentoFormName.text.toString()
                val value = mLancamentoFormValue.text.toString()
                val isReceita = mLancamentoFormIsReceita.isChecked

                if (name.isEmpty()) {
                    mLancamentoFormName.error = "Nome do lançamento é obrigatório"
                    return
                }

                if (mLancamentoId == -1) {

                    GlobalScope.launch {
                        val task = Lancamento(
                            name = name,
                            value = value,
                            isReceita = isReceita,
                            userId = mUserId
                        )
                        mLancamentoDAO.insert(task)

                        handler.post {
                            Toast.makeText(
                                applicationContext,
                                "Lançamento ${task.name} adicionado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                } else {

                    GlobalScope.launch {
                        val task = Lancamento(mLancamentoId, name, value, isReceita, mUserId)
                        mLancamentoDAO.update(task)

                        handler.post {
                            Toast.makeText(
                                applicationContext,
                                "Lançamento ${task.name} editada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }
}