package com.example.moneymate.ui.stats

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.ui.transaction.TransactionAdapter

class TransactionFragment : Fragment(R.layout.fragment_stats) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvAllTransactions)

        val dao = AppDatabase.getInstance(requireContext()).TransactionDao()

        val adapter = TransactionAdapter(emptyList())

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TransactionFragment())
                .addToBackStack(null)
                .commit()
        }
        dao.getAllTransactionFull().observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
    }
}