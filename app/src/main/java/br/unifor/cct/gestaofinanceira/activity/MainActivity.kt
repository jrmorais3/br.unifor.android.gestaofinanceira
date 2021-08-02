package br.unifor.cct.gestaofinanceira.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unifor.cct.gestaofinanceira.R
import br.unifor.cct.gestaofinanceira.adapter.LancamentoAdapter
import br.unifor.cct.gestaofinanceira.adapter.LancamentoItemListener
import br.unifor.cct.gestaofinanceira.database.LancamentoDAO
import br.unifor.cct.gestaofinanceira.database.UserDAO
import br.unifor.cct.gestaofinanceira.model.Lancamento
import br.unifor.cct.gestaofinanceira.model.UserWithLancamentos
import br.unifor.cct.gestaofinanceira.util.DatabaseUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener, LancamentoItemListener {

    private lateinit var mUserDAO: UserDAO
    private lateinit var mLancamentoDAO: LancamentoDAO
    private lateinit var mRecycleView: RecyclerView
    private lateinit var mUserWithLancamentos: UserWithLancamentos
    private lateinit var mAddLancamento: FloatingActionButton
    private lateinit var lancamentoAdapter: LancamentoAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val mLancamentoList = mutableListOf<Lancamento>()

    private var mUserId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUserDAO = DatabaseUtil.getInstance(applicationContext).getUserDao()
        mLancamentoDAO = DatabaseUtil.getInstance(applicationContext).getTaskDAO()

        mRecycleView = findViewById(R.id.main_recyclerview_tasks)
        mAddLancamento = findViewById(R.id.main_floatingActionButton_add_task)
        mAddLancamento.setOnClickListener(this)

        mUserId = intent.getIntExtra("userId", -1)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            if (mUserId != -1) {
                mUserWithLancamentos = mUserDAO.findTasksByUserId(mUserId)

                mLancamentoList.clear()
                mLancamentoList.addAll(mUserWithLancamentos.lancamentos)

                lancamentoAdapter = LancamentoAdapter(mLancamentoList)
                lancamentoAdapter.setTaskItemListener(this@MainActivity)

                val llm = LinearLayoutManager(applicationContext)

                handler.post {
                    mRecycleView.apply {
                        adapter = lancamentoAdapter
                        layoutManager = llm
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.main_floatingActionButton_add_task -> {
                val it = Intent(applicationContext, LancamentoFormActivity::class.java)
                it.putExtra("userId", mUserId)
                startActivity(it)
            }
        }
    }

    override fun onTaskItemClick(v: View, pos: Int) {
        val it = Intent(applicationContext, LancamentoFormActivity::class.java)
        it.putExtra("userId", mUserId)
        it.putExtra("lancamentoId", mUserWithLancamentos.lancamentos[pos].tid)

        startActivity(it)
    }

    override fun onTaskItemLongClick(v: View, pos: Int) {
        val task = mUserWithLancamentos.lancamentos[pos]
        val alert = AlertDialog.Builder(this)
        alert
            .setTitle("Gestão Financeira")
            .setMessage("Deseja excluir o lançamento ${task.name}?")
            .setPositiveButton("Sim") { dialog, _ ->
                dialog.dismiss()

                GlobalScope.launch {
                    mLancamentoDAO.delete(task)
                    mUserWithLancamentos = mUserDAO.findTasksByUserId(mUserId)

                    mLancamentoList.removeAt(pos)
                    handler.post {
                        lancamentoAdapter.notifyItemRemoved(pos)
                    }
                }
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alert.show()
    }
}