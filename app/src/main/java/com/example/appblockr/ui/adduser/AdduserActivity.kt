package com.example.appblockr.ui.adduser

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.appblockr.AdminActivity
import com.example.appblockr.R
import com.example.appblockr.databinding.ActivityAdduserBinding
import com.example.appblockr.firestore.FireStoreManager
import com.example.appblockr.firestore.User
import com.example.appblockr.utils.Utils

class AdduserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdduserBinding
    private lateinit var fireStoreManager: FireStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.title_bar);
        supportActionBar?.elevation = 0F
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adduser)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFBB86FC")))
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.purple_200)
        }
        fireStoreManager = FireStoreManager()
        fireStoreManager.initFireStoreDB()



      binding.btnAddUser.setOnClickListener{
          Utils.hideKeyboard(this@AdduserActivity)
          if (checkAllFields()) {
              val user = User(
                  email = binding.edtEmail.text.toString(),
                  password = binding.edtPassword.text.toString(),
                  user_name = binding.edtUserName.text.toString(),
                  "1"
              )
              val data = hashMapOf(
                  "email" to "${user.email}",
                  "password" to "${user.password}",
                  "user_name" to "${user.user_name}",
                  "user_type" to "${user.user_type}",
              )
              fireStoreManager.addDataToFireStoreDB(data, "add_users")
              clearEditFields()
              val intent = Intent(applicationContext, AdminActivity::class.java)
              startActivity(intent)
              finish()
          }
      }

    }

    private fun clearEditFields() {
        binding.edtEmail.text?.clear()
        binding.edtPassword.text?.clear()
        binding.edtUserName.text?.clear()
    }

    private fun checkAllFields(): Boolean {
        if (binding.edtUserName.length() == 0) {
            binding.edtUserName.error = "User Name is required"
            return false
        }
        if (binding.edtEmail.length() == 0 || !isValidEmail(binding.edtEmail.text.toString())) {
            binding.edtEmail.error = "Email is required"
            return false
        }
        if (binding.edtPassword.length() == 0 || !isValidPassword(binding.edtPassword.text.toString())) {
            binding.edtPassword.error = "Password is required & minimum six digits"
            return false
        }
        return true
    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}