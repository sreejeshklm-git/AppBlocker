package com.example.appblockr.ui.adduser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.appblockr.R
import com.example.appblockr.databinding.ActivityAdduserBinding
import com.example.appblockr.firestore.FireStoreManager
import com.example.appblockr.firestore.User

class AdduserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdduserBinding
    private lateinit var fireStoreManager: FireStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adduser)

        fireStoreManager = FireStoreManager()
        fireStoreManager.initFireStoreDB()



      binding.btnAddUser.setOnClickListener{

          val user = User(
              email = binding.edtEmail.text.toString(),
              password = binding.edtPassword.text.toString(),
              user_name = binding.edtUserName.text.toString(),
              1
          )
          val data = hashMapOf(
              "email" to "${user.email}",
              "password" to "${user.password}",
              "user_name" to "${user.user_name}",
              "user_type" to "${user.user_type}",
          )
          fireStoreManager.addDataToFireStoreDB(data,"add_users")
          clearEditFields()
      }

    }

    private fun clearEditFields() {
        binding.edtEmail.text?.clear()
        binding.edtPassword.text?.clear()
        binding.edtUserName.text?.clear()
    }
}