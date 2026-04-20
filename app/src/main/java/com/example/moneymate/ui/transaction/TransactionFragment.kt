package com.example.moneymate.ui.transaction

import android.app.AlertDialog
import com.example.moneymate.viewmodel.TransactionViewModelFactory

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.R
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.local.entity.CategoryEntity
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.repository.CategoryRepository
import com.example.moneymate.data.repository.TransactionRepository
import com.example.moneymate.ui.category.CategoryViewModelFactory
import com.example.moneymate.viewmodel.CategoryViewModel
import com.example.moneymate.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment(R.layout.fragment_transaction) {

    private lateinit var viewModel: TransactionViewModel
    private var selectedType = "expense"
    private var selectedDateMillis: Long = System.currentTimeMillis()
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryList: List<CategoryEntity>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val etNote = view.findViewById<EditText>(R.id.etNote)
        val btnExpense = view.findViewById<Button>(R.id.btnExpense)
        val btnIncome = view.findViewById<Button>(R.id.btnIncome)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val spCategory = view.findViewById<Spinner>(R.id.spCategory)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnDelete = view.findViewById<ImageView>(R.id.btnDelete)
        val categoryDao = AppDatabase.getInstance(requireContext()).CategoryDao()
        val categoryRepo = CategoryRepository(categoryDao)
        val categoryFactory = CategoryViewModelFactory(categoryRepo)
        val layoutType = view.findViewById<View>(R.id.layoutType)
        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]

        // Nhận DATA EDIT
        val id = arguments?.getInt("id", -1) ?: -1
        if (id != -1) {
            // 👉 SỬA → ẨN chọn loại
            layoutType.visibility = View.INVISIBLE
            btnExpense.visibility = View.GONE
            btnIncome.visibility = View.GONE
        }
        val noteArg = arguments?.getString("note")
        val amountArg = arguments?.getDouble("amount")
        val typeArg = arguments?.getString("type")
        val dateArg = arguments?.getString("date")
        val categoryIdArg = arguments?.getInt("category_id", -1)



        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        if (id == -1) {
            // 👉 FORM THÊM
            btnDelete.visibility = View.GONE
        } else {
            // 👉 FORM SỬA
            btnDelete.visibility = View.VISIBLE
        }
        btnDelete.setOnClickListener {

            if (id != -1) {

                val transaction = TransactionEntity(
                    id = id,
                    user_id = 1,
                    category_id = categoryIdArg ?: 1,
                    amount = amountArg ?: 0.0,
                    type = typeArg ?: "expense",
                    note = noteArg ?: "",
                    date = dateArg ?: "",
                    created_at = ""
                )

                AlertDialog.Builder(requireContext())
                    .setTitle("Xoá giao dịch")
                    .setMessage("Bạn có chắc chắn muốn xoá giao dịch này không?")
                    .setPositiveButton("Xoá") { _, _ ->

                        viewModel.delete(transaction)

                        Toast.makeText(requireContext(), "Đã xoá!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .setNegativeButton("Huỷ", null)
                    .show()
            }
        }
//        Title + button
        if (id == -1) {
            tvTitle.text = "Thêm giao dịch"
            btnSave.text = "Lưu"
        } else {
            tvTitle.text = "Sửa giao dịch"
            btnSave.text = "Cập nhật"
        }


        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]
        fun loadCategory(type: String) {
            categoryViewModel.getByType(type).observe(viewLifecycleOwner) { list ->

                categoryList = list

                val names = list.map { it.name }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    names
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCategory.adapter = adapter

//                set slected khi edit
                if (categoryIdArg != -1) {
                    // 👉 SỬA
                    val selectedPosition = list.indexOfFirst { it.id == categoryIdArg }
                    if (selectedPosition != -1) {
                        spCategory.setSelection(selectedPosition)
                    }
                } else {
                    // 👉 THÊM → chọn mặc định item đầu
                    if (list.isNotEmpty()) {
                        spCategory.setSelection(0)
                    }
                }
            }
        }
        // DB
        val dao = AppDatabase.getInstance(requireContext()).TransactionDao()
        val repo = TransactionRepository(dao)
        val factory = TransactionViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        // ======================
        // 📅 HIỂN THỊ NGÀY HIỆN TẠI (FORMAT VN)
        // ======================
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))

        if(id == -1){
            val today = Date()
            selectedDateMillis = today.time
            tvDate.text = dateFormat.format(today)
        }

        // ======================
        // 📅 CHỌN NGÀY
        // ======================
        tvDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    val cal = Calendar.getInstance()
                    cal.set(year, month, day)

                    selectedDateMillis = cal.timeInMillis

                    tvDate.text = dateFormat.format(cal.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.show()
        }

        // ======================
        // 💰 FORMAT TIỀN
        // ======================
        etAmount.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {

                    etAmount.removeTextChangedListener(this)

                    // bỏ VNĐ + dấu chấm
                    val cleanString = s.toString()
                        .replace("₫", "")
                        .replace(".", "")
                        .trim()

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toLong()

                        val formatted = NumberFormat
                            .getInstance(Locale("vi", "VN"))
                            .format(parsed) + " ₫"

                        current = formatted
                        etAmount.setText(formatted)
                        etAmount.setSelection(formatted.length - 2) // giữ cursor trước ₫
                    }

                    etAmount.addTextChangedListener(this)
                }
            }
        })


        // ======================
        // 🔘 CHỌN LOẠI
        // ======================
        btnExpense.setOnClickListener {
            selectedType = "expense"
            btnExpense.setBackgroundResource(R.drawable.bg_btn_selected)
            btnIncome.setBackgroundResource(android.R.color.transparent)
            loadCategory("expense")

        }

        btnIncome.setOnClickListener {
            selectedType = "income"
            btnIncome.setBackgroundResource(R.drawable.bg_btn_selected)
            btnExpense.setBackgroundResource(android.R.color.transparent)
            loadCategory("income")
        }
////Load category khi mở fragmet
        if (typeArg != null) {
            selectedType = typeArg
            loadCategory(typeArg)

            if (typeArg == "income") {
                btnIncome.setBackgroundResource(R.drawable.bg_btn_selected)
                btnExpense.setBackgroundResource(android.R.color.transparent)
            } else {
                btnExpense.setBackgroundResource(R.drawable.bg_btn_selected)
                btnIncome.setBackgroundResource(android.R.color.transparent)
            }
        } else {
            loadCategory("expense")
        }
        Log.d("DEBUG_EDIT", "ID = $id")

        // ======================
        // 👉 FILL DATA EDIT
        // ======================
        if (id != -1) {

            etNote.setText(noteArg ?: "")

            val formatted = NumberFormat
                .getInstance(Locale("vi", "VN"))
                .format(amountArg?.toLong() ?: 0) + " ₫"

            etAmount.setText(formatted)

            selectedType = typeArg ?: "expense"

            if (selectedType == "income") {
                btnIncome.setBackgroundResource(R.drawable.bg_btn_selected)
                btnExpense.setBackgroundResource(android.R.color.transparent)
            } else {
                btnExpense.setBackgroundResource(R.drawable.bg_btn_selected)
                btnIncome.setBackgroundResource(android.R.color.transparent)
            }

            loadCategory(selectedType)

            if (!dateArg.isNullOrEmpty()) {
                dateArg.toLongOrNull()?.let {
                    selectedDateMillis = it
                    tvDate.text = dateFormat.format(Date(it))
                }
            }
        }

        // ======================
        // 💾 LƯU
        // ======================
        btnSave.setOnClickListener {

            val amountText = etAmount.text.toString()
            val note = etNote.text.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(requireContext(), "Nhập số tiền!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // bỏ dấu chấm
            val cleanAmount = amountText
                .replace("₫", "")
                .replace(".", "")
                .trim()
            val amountValue = cleanAmount.toDoubleOrNull()

            if (amountValue == null) {
                Toast.makeText(requireContext(), "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!::categoryList.isInitialized || categoryList.isEmpty()) {
                Toast.makeText(requireContext(), "Chưa có danh mục!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = spCategory.selectedItemPosition
            val selectedCategory = categoryList[selectedPosition]
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = sdf.format(Date(selectedDateMillis))

            val transaction = TransactionEntity(
                id = if(id == -1) 0 else id,
                user_id = 1,
                category_id = selectedCategory.id, // sau bạn nâng cấp
                amount = amountValue,
                type = selectedType,
                note = note,
                date = dateString,
                created_at = System.currentTimeMillis().toString()
            )

            if (id == -1) {
                viewModel.insert(transaction)
            } else {
                viewModel.update(transaction)
            }

            Toast.makeText(requireContext(), "Đã lưu!", Toast.LENGTH_SHORT).show()

            parentFragmentManager.popBackStack()
        }

    }
}