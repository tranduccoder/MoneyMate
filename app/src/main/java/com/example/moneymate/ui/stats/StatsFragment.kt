package com.example.moneymate.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.local.entity.TransactionFull
import com.example.moneymate.ui.transaction.TransactionAdapter
import com.example.moneymate.ui.transaction.TransactionFragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.util.Locale

class StatsFragment : Fragment(R.layout.fragment_stats) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvAllTransactions)

        val dao = AppDatabase.getInstance(requireContext()).TransactionDao()

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)

        val tvIncome=view.findViewById<TextView>(R.id.tvIncome)
        val tvExpense=view.findViewById<TextView>(R.id.tvExpense)
        // 🔥 BIẾN NÀY GIỮ LIVEDATA HIỆN TẠI (để không bị chồng observe)
        var currentLiveData: LiveData<List<TransactionFull>>? = null

        fun formatMoney(amount: Double): String {
            val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
            return formatter.format(amount) + " đ"
        }

//        fun loadChart(type: String) {
//            dao.getAllTransactionFull().observe(viewLifecycleOwner) { list ->
//
//                val income = list
//                    .filter { it.type == "income" }
//                    .sumOf { it.amount }
//
//                val expense = list
//                    .filter { it.type == "expense" }
//                    .sumOf { it.amount }
//
//                val entries = ArrayList<PieEntry>()
//
//                if (income > 0) {
//                    entries.add(PieEntry(income.toFloat(), "Thu nhập"))
//                }
//
//                if (expense > 0) {
//                    entries.add(PieEntry(expense.toFloat(), "Chi tiêu"))
//                }
//
//                val dataSet = PieDataSet(entries, "Tổng quan tài chính")
//
//                dataSet.setColors(
//                    Color.parseColor("#4CAF50"), // xanh thu nhập
//                    Color.parseColor("#F44336")  // đỏ chi tiêu
//                )
//
//                val data = PieData(dataSet)
//                data.setValueTextSize(14f)
//
//                pieChart.data = data
//                pieChart.description.isEnabled = false
//                pieChart.centerText = "Tổng thu/chi"
//                pieChart.setEntryLabelColor(Color.BLACK)
//                pieChart.animateY(800)
//
//                pieChart.invalidate()
//            }
//        }
        // 👉 LOAD CHI TIÊU MẶC ĐỊNH
//        loadChart("expense")
//        loadChart("income")

//        dao.getAllTransactionFull().observe(viewLifecycleOwner) { list ->
//
//            val income = list
//                .filter { it.type == "income" }
//                .sumOf { it.amount }
//
//            val expense = list
//                .filter { it.type == "expense" }
//                .sumOf { it.amount }
//
//            tvIncome.text = "+${formatMoney(income)}"
//            tvExpense.text = "-${formatMoney(expense)}"
//        }



        val adapter = TransactionAdapter(emptyList()) {
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

        fun updateUI(list: List<TransactionFull>) {

            val income = list.filter { it.type == "income" }.sumOf { it.amount }
            val expense = list.filter { it.type == "expense" }.sumOf { it.amount }

            tvIncome.text = "+${formatMoney(income)}"
            tvExpense.text = "-${formatMoney(expense)}"

            // 👉 PIE CHART
            val entries = ArrayList<PieEntry>()

            if (income > 0) entries.add(PieEntry(income.toFloat(), "Thu nhập"))
            if (expense > 0) entries.add(PieEntry(expense.toFloat(), "Chi tiêu"))

            val dataSet = PieDataSet(entries, "Tổng quan")
            dataSet.setColors(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"))

            pieChart.data = PieData(dataSet)
            pieChart.description.isEnabled = false
            pieChart.centerText = "Tổng thu/chi"
            pieChart.invalidate()

            // 👉 LIST
            adapter.updateData(list)
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TransactionFragment())
                .addToBackStack(null)
                .commit()
        }
        // =========================
        // 🔥 THÊM FILTER NGÀY / TUẦN / THÁNG
        // =========================

        fun loadByRange(start: String, end: String) {

            // 🔥 XÓA OBSERVER CŨ (QUAN TRỌNG)
            currentLiveData?.removeObservers(viewLifecycleOwner)

            // 🔥 LẤY DATA THEO DATE
            currentLiveData = dao.getTransactionByDateRange(start, end)

            currentLiveData?.observe(viewLifecycleOwner) { list ->
                updateUI(list)
            }
        }

        // 🔥 HÀM DATE
        fun getToday(): Pair<String, String> {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = sdf.format(java.util.Date())
            return Pair(today, today)
        }

        fun getMonthRange(): Pair<String, String> {
            val cal = java.util.Calendar.getInstance()

            cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
            val start = cal.time

            cal.add(java.util.Calendar.MONTH, 1)
            cal.add(java.util.Calendar.DAY_OF_MONTH, -1)
            val end = cal.time

            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return Pair(sdf.format(start), sdf.format(end))
        }
        fun getWeekRange(): Pair<String, String> {
            val cal = java.util.Calendar.getInstance()
            cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY)

            val start = cal.time
            cal.add(java.util.Calendar.DAY_OF_WEEK, 6)
            val end = cal.time

            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return Pair(sdf.format(start), sdf.format(end))
        }

        // =========================
        // 🔥 LOAD MẶC ĐỊNH (THÁNG)
        // =========================
        val (start, end) = getMonthRange()
        loadByRange(start, end)

        // =========================
        // 🔥 BUTTON FILTER (PHẢI CÓ TRONG XML)
        // =========================
        val btnDay = view.findViewById<TextView>(R.id.btnDay)
        val btnWeek = view.findViewById<TextView>(R.id.btnWeek)
        val btnMonth = view.findViewById<TextView>(R.id.btnMonth)

        btnDay.setOnClickListener {
            val (s, e) = getToday()
            loadByRange(s, e)
        }
        btnWeek.setOnClickListener {
            val (s, e) = getWeekRange()
            loadByRange(s, e)
        }

        btnMonth.setOnClickListener {
            val (s, e) = getMonthRange()
            loadByRange(s, e)
        }
//        dao.getAllTransactionFull().observe(viewLifecycleOwner) {
//            adapter.updateData(it)
//        }

    }
}