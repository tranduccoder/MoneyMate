package com.example.moneymate.ui.home

import android.R.attr.onClick
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.repository.TransactionRepository
import com.example.moneymate.ui.transaction.TransactionAdapter
import com.example.moneymate.ui.transaction.TransactionFragment
import com.example.moneymate.viewmodel.HomeViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: TransactionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val format = NumberFormat.getInstance(Locale("vi", "VN"))
        val rv = view.findViewById<RecyclerView>(R.id.rv_transactions)
        val tvBalance = view.findViewById<TextView>(R.id.tv_balance)
        val tvIncome = view.findViewById<TextView>(R.id.tv_Income)
        val tvExpense = view.findViewById<TextView>(R.id.tv_Expense)
        val btnMenu = view.findViewById<ImageView>(R.id.btnMenu)
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val dao = AppDatabase.getInstance(requireContext()).TransactionDao()

        fun loadChart(type: String) {
            dao.getAllTransactionFull().observe(viewLifecycleOwner) { list ->

                val income = list
                    .filter { it.type == "income" }
                    .sumOf { it.amount }

                val expense = list
                    .filter { it.type == "expense" }
                    .sumOf { it.amount }

                val entries = ArrayList<PieEntry>()

                if (income > 0) {
                    entries.add(PieEntry(income.toFloat(), "Thu nhập"))
                }

                if (expense > 0) {
                    entries.add(PieEntry(expense.toFloat(), "Chi tiêu"))
                }

                val dataSet = PieDataSet(entries, "Tổng quan tài chính")

                dataSet.setColors(
                    Color.parseColor("#4CAF50"), // xanh thu nhập
                    Color.parseColor("#F44336")  // đỏ chi tiêu
                )

                val data = PieData(dataSet)
                data.setValueTextSize(14f)

                pieChart.data = data
                pieChart.description.isEnabled = false
                pieChart.centerText = "Tổng thu/chi"
                pieChart.setEntryLabelColor(Color.BLACK)
                pieChart.animateY(800)

                pieChart.invalidate()
            }
        }
        // 👉 LOAD CHI TIÊU MẶC ĐỊNH
        loadChart("expense")


        btnMenu.setOnClickListener {
            // Tìm DrawerLayout từ Activity chứa Fragment này
            val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)

            // Mở menu từ bên trái (START)
            drawerLayout.openDrawer(GravityCompat.START)
        }

        adapter = TransactionAdapter(emptyList()){
            item ->
            val bundle = Bundle().apply {
                putInt("id", item.id)
                putString("note", item.note)
                putDouble("amount", item.amount)
                putString("type", item.type)
                putString("date", item.date)
                putInt("category_id", item.category_id  )
            }

            val fragment = TransactionFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)
        val repo = TransactionRepository(dao)
        val factory = HomeViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        viewModel.transactions.observe(viewLifecycleOwner) {
            adapter.updateData(it.take(5))
        }

        viewModel.income.observe(viewLifecycleOwner) {
            val value = it ?: 0.0
            val formatted = format.format(value.toLong())

            tvIncome.text = "+ $formatted ₫"
            tvIncome.setTextColor(Color.parseColor("#27AE60"))
        }

        viewModel.expense.observe(viewLifecycleOwner) {
            val value = it ?: 0.0
            val formatted = format.format(value.toLong())

            tvExpense.text = "- $formatted ₫"
            tvExpense.setTextColor(Color.parseColor("#E74C3C"))
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            val value = it ?: 0.0
            val formatted = format.format(value.toLong())

            tvBalance.text = "$formatted ₫"
        }


    }
}