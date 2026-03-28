package com.example.moneymate.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.R
import com.example.moneymate.data.local.entity.User
import com.example.moneymate.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    lateinit var edt_email: EditText
    lateinit var edt_username: EditText
    lateinit var edt_password: EditText
    lateinit var btn_register: Button
    private lateinit var viewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_ui)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Register_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        edt_email=findViewById<EditText>(R.id.edt_EmailReg)
        edt_username=findViewById<EditText>(R.id.edt_Username)
        edt_password=findViewById<EditText>(R.id.edt_PasswordReg)
        btn_register=findViewById<Button>(R.id.btn_Reg)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        btn_register.setOnClickListener {
            val email = edt_email.text.toString().trim()
            val username = edt_username.text.toString().trim()
            val password = edt_password.text.toString().trim()
            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.checkEmail(email){existedUser ->
                if (existedUser != null) {
                    Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show()
                    return@checkEmail
                } else {
                    val user= User(
                        name = edt_username.text.toString(),
                        email = email,
                        password = edt_password.text.toString()
                    )
                    viewModel.register(user)
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }
}