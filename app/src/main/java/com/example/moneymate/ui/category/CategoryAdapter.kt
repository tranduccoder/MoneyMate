package com.example.moneymate.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.entity.CategoryEntity

class CategoryAdapter(
    private var list: List<CategoryEntity>,
    private val onDelete: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvType = view.findViewById<TextView>(R.id.tvType)
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon)
        val btnDelete = view.findViewById<ImageView>(R.id.btnDelete)

        val btnUpdate = view.findViewById<ImageView>(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvName.text = item.name
        holder.tvType.text =
            if (item.type == "expense") "Chi tiêu" else "Thu nhập"

        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }
        holder.imgIcon.setImageResource(item.icon)

        // ✅ DELETE
        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }

        // ✅ EDIT
        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }
        holder.btnDelete.setOnClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa \"${item.name}\" không?")
                .setPositiveButton("Xóa") { _, _ ->
                    onDelete(item)
                    Toast.makeText(
                        holder.itemView.context,
                        "Đã xóa!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
        // ✅ EDIT
        holder.btnUpdate.setOnClickListener {
            val bundle= Bundle().apply {
                putInt("id",item.id)
                putString("name",item.name)
                putString("type",item.type)
                putInt("icon",item.icon)
            }
            val fragment=CategoryFragment()
            fragment.arguments=bundle
            (holder.itemView.context as androidx.fragment.app.FragmentActivity)
                .supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


    fun updateData(newList: List<CategoryEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}