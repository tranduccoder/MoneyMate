package com.example.moneymate.ui.category

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.repository.CategoryRepository
import com.example.moneymate.viewmodel.CategoryViewModel

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCategory)
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)

        // DB
        val dao = AppDatabase.getInstance(requireContext()).CategoryDao()
        val repo = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

        adapter = CategoryAdapter(emptyList()) {
            viewModel.delete(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 🔥 LẤY DATA THẬT
        viewModel.categories.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        // 👉 mở màn thêm
        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CategoryFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}