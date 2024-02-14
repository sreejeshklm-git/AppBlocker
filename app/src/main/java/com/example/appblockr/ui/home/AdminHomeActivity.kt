package com.example.appblockr.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appblockr.R
import com.example.appblockr.databinding.ActivityAdminHomeBinding
import com.example.appblockr.databinding.ItemAppsLayoutBinding
import com.example.appblockr.databinding.ItemUserBinding
import com.example.appblockr.firestore.AppData
import com.example.appblockr.firestore.FireStoreManager
import com.example.appblockr.firestore.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

const val TAG = "AdminHomeActivity"
class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding

    private val userList: ArrayList<User> = ArrayList()
    private val appList: ArrayList<AppData> = ArrayList()

    private lateinit var fireStoreManager: FireStoreManager
    private lateinit var db: FirebaseFirestore
    private lateinit var userAdapter: GenericRecyclerAdapter<User, ItemUserBinding>
    private lateinit var appsAdapter: GenericRecyclerAdapter<AppData, ItemAppsLayoutBinding>
    var userType : String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_home)

        fireStoreManager = FireStoreManager()
        db = fireStoreManager.fireStoreInstance

        if (userType == "2") {
            setupUserData()
            getUsersList()
        }else if (userType == "1"){
            setUpAppData()
            getAppList()
        }

//        GenericRecyclerAdapter
    }

    private fun getAppList() {
        db.collection("apps_list")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val appData = document.toObject(AppData::class.java)
                        appList.add(appData)
                        Log.d(TAG,"App:: "+ appData.appName!!)
                        // hashMap = (Map<String, Object>) document.getData();
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                    Log.d(TAG, "App Size:: " + appList.size)
                    appsAdapter.notifyDataSetChanged()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            })

    }

    private fun setUpAppData() {
        binding.rvUsersList.visibility = View.GONE
        binding.rvAppsList.visibility = View.VISIBLE
        binding.rvAppsList.layoutManager = LinearLayoutManager(this)

        appsAdapter = object : GenericRecyclerAdapter<AppData, ItemAppsLayoutBinding>(appList) {
            override fun getLayoutId(): Int {
                return R.layout.item_apps_layout
            }

            override fun onBinder(model: AppData, viewBinding: ItemAppsLayoutBinding, position: Int) {
                viewBinding.txtAppName.text = model.appName
                viewBinding.txtBundle.text = model.bundle_id
            }

        }

        binding.rvAppsList.adapter = appsAdapter
    }

    private fun setupUserData() {
        binding.rvUsersList.visibility = View.VISIBLE
        binding.rvAppsList.visibility = View.GONE
        binding.rvUsersList.layoutManager = LinearLayoutManager(this)

       userAdapter = object : GenericRecyclerAdapter<User, ItemUserBinding>(userList) {
                override fun getLayoutId(): Int {
                    return R.layout.item_user
                }

                override fun onBinder(model: User, viewBinding: ItemUserBinding, position: Int) {
                    viewBinding.txtEmail.text = model.email
                    viewBinding.txtUser.text = model.user_name
                }

            }

        binding.rvUsersList.adapter = userAdapter

    }

    private fun getUsersList() {

        db.collection("add_users")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val users = document.toObject(
                            User::class.java
                        )
                        userList.add(users)
                        Log.d(TAG, users.email!!)
                        // hashMap = (Map<String, Object>) document.getData();
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                    Log.d(TAG, "Size:: " + userList.size)
                    userAdapter.notifyDataSetChanged()
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            })

    }

}