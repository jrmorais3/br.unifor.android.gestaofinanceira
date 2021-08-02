package br.unifor.cct.gestaofinanceira.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unifor.cct.gestaofinanceira.R
import br.unifor.cct.gestaofinanceira.model.Lancamento

class LancamentoAdapter(val lancamentos: List<Lancamento>): RecyclerView.Adapter<LancamentoAdapter.TaskViewHolder>()  {

    private var listener: LancamentoItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lancamento, parent, false)
        return TaskViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.name.text = lancamentos[position].name
        holder.value.text = lancamentos[position].value

        if (lancamentos[position].isReceita) {
            holder.isReceita.setBackgroundColor(Color.GREEN)
        } else {
            holder.isReceita.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return lancamentos.size
    }

    fun setTaskItemListener(listener: LancamentoItemListener) {
        this.listener = listener
    }

    class TaskViewHolder(itemView: View, listener: LancamentoItemListener?):RecyclerView.ViewHolder(itemView) {
        val name:TextView = itemView.findViewById(R.id.item_lancamento_textview_name)
        val value: TextView = itemView.findViewById(R.id.item_lancamento_textview_value)
        val isReceita: View = itemView.findViewById(R.id.item_lancamento_view_isdone)

        init {
            itemView.setOnClickListener{
                listener?.onTaskItemClick(it, adapterPosition)
            }

            itemView.setOnLongClickListener{
                listener?.onTaskItemLongClick(it, adapterPosition)
                true
            }
        }
    }
}