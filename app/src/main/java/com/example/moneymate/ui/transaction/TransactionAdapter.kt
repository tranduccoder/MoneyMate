package com.example.moneymate.ui.transaction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.local.entity.TransactionFull
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private var list: List<TransactionFull>,
    private val onClick: (TransactionFull) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNote = view.findViewById<TextView>(R.id.tvNote)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val tvAmount = view.findViewById<TextView>(R.id.tvAmount)
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val formatted = NumberFormat
            .getInstance(Locale("vi", "VN"))
            .format(item.amount.toLong())

        // 👉 tên danh mục thay vì note
        holder.tvNote.text = item.categoryName

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
        val dateText = item.date

        try {
            val date = if (dateText.all { it.isDigit() }) {
                // 👉 dữ liệu cũ (millis)
                Date(dateText.toLong())
            } else {
                // 👉 dữ liệu mới (yyyy-MM-dd)
                inputFormat.parse(dateText)
            }

            holder.tvDate.text = outputFormat.format(date!!)
        } catch (e: Exception) {
            holder.tvDate.text = ""
        }

        // 👉 icon từ category
        holder.imgIcon.setImageResource(item.categoryIcon)
        holder.itemView.setOnClickListener {
            onClick(item)
        }


        // 👉 tiền
        if (item.type == "expense") {
            holder.tvAmount.text = "- $formatted ₫"
            holder.tvAmount.setTextColor(0xFFE74C3C.toInt())
        } else {
            holder.tvAmount.text = "+ $formatted ₫"
            holder.tvAmount.setTextColor(0xFF27AE60.toInt())
        }
    }

    fun updateData(newList: List<TransactionFull>) {
        list = newList
        notifyDataSetChanged()
    }
}