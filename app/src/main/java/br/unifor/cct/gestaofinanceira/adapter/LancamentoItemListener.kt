package br.unifor.cct.gestaofinanceira.adapter

import android.view.View

interface LancamentoItemListener {
    fun onTaskItemClick(v: View, pos: Int)
    fun onTaskItemLongClick(v: View, pos: Int)
}