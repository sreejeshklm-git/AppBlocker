package com.example.appblockr.firestore

data class User(var email:String?,var password : String?,var user_name :String?,var user_type:String?){
    constructor() : this("","","","")
}
