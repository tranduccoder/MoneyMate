package com.example.moneymate.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.R
import com.example.moneymate.ui.home.MainActivity
import com.example.moneymate.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var txt_reg: TextView
    lateinit var btn_login: Button
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_ui)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txt_reg=findViewById<TextView>(R.id.register_text)
        btn_login=findViewById<Button>(R.id.btn_Login)
        edt_email=findViewById<EditText>(R.id.edt_EmailLogin)
        edt_password=findViewById<EditText>(R.id.edt_PasswordLogin)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        btn_login.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()
            viewModel.login(email, password) { user ->
                if (user != null) {
                    saveLogin(user.id)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Đăng nhập thất bại! ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txt_reg.setOnClickListener {
            val intent = Intent(this , RegisterActivity::class.java)
            startActivity(intent)
        }

    }
    private fun saveLogin(userId: Int) {
        val pref = getSharedPreferences("USER", MODE_PRIVATE)
        pref.edit().putInt("user_id", userId).apply()
    }
}