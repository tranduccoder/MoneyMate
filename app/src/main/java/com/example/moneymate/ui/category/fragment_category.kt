package com.example.moneymate.ui.category

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.local.entity.CategoryEntity
import com.example.moneymate.data.repository.CategoryRepository
import com.example.moneymate.viewmodel.CategoryViewModel

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var viewModel: CategoryViewModel
    private var selectedIconView: ImageView? = null
    private var selectedType = "expense"
    private var selectedIcon = R.drawable.ic_food

    private var categoryId = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val iconList = listOf(
            R.drawable.ic_food,
            R.drawable.ic_shopping,
            R.drawable.ic_car,
            R.drawable.ic_home,
            R.drawable.ic_game,
            R.drawable.ic_health,
            R.drawable.ic_study,
            R.drawable.ic_gift
        )


        // 👉 ánh xạ đúng id XML
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val etName = view.findViewById<EditText>(R.id.etName)
        val btnExpense = view.findViewById<Button>(R.id.btnExpense)
        val btnIncome = view.findViewById<Button>(R.id.btnIncome)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val gridIcon = view.findViewById<GridLayout>(R.id.gridIcon)

        // 👉 init DB
        val dao = AppDatabase.getInstance(requireContext()).CategoryDao()
        val repo = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repo)

        categoryId = arguments?.getInt("id", -1) ?: -1
        val nameArg = arguments?.getString("name")
        val typeArg = arguments?.getString("type")
        val iconArg = arguments?.getInt("icon")
        if (categoryId != -1) {
            tvTitle.text = "Sửa danh mục"
            etName.setText(nameArg)
        }else{
            tvTitle.text = "Thêm danh mục"
        }

        viewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

        // 👉 chọn loại
        btnExpense.setOnClickListener {
            selectedType = "expense"
            btnExpense.setBackgroundResource(R.drawable.bg_btn_selected)
            btnIncome.setBackgroundResource(android.R.color.transparent)
        }

        btnIncome.setOnClickListener {
            selectedType = "income"
            btnIncome.setBackgroundResource(R.drawable.bg_btn_selected)
            btnExpense.setBackgroundResource(android.R.color.transparent)
        }

        // 👉 chọn icon
        for (i in 0 until gridIcon.childCount) {
            val icon = gridIcon.getChildAt(i) as ImageView

            icon.setOnClickListener {

                // reset icon cũ
                selectedIconView?.setBackgroundResource(0)

                // set icon mới
                icon.setBackgroundResource(R.drawable.bg_btn_save)

                selectedIconView = icon
                selectedIcon = iconList[i]

                Toast.makeText(requireContext(), "Chọn icon $i", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔥 FILL DATA KHI EDIT
        if (categoryId != -1) {
            etName.setText(nameArg)

            // chọn type
            if (typeArg == "income") {
                btnIncome.performClick()
            } else {
                btnExpense.performClick()
            }

            // chọn icon đúng
            val index = iconList.indexOf(iconArg)
            if (index != -1) {
                val iconView = gridIcon.getChildAt(index) as ImageView
                iconView.performClick()
            }
        }


        // 👉 lưu
        btnSave.setOnClickListener {
            val name = etName.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Nhập tên!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = CategoryEntity(
                // 🔥 FIX: id cho update
                id = if (categoryId == -1) 0 else categoryId,
                user_id = 1,
                name = name,
                type = selectedType,
                icon = selectedIcon
            )

            // 🔥 PHÂN BIỆT ADD vs UPDATE
            if (categoryId == -1) {
                viewModel.insert(category)
            } else {
                viewModel.update(category)
            }


            Toast.makeText(requireContext(), "Đã lưu!", Toast.LENGTH_SHORT).show()
            etName.setText("")
            parentFragmentManager.popBackStack()
        }
    }
}