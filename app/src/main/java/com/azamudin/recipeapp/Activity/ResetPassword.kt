package com.azamudin.recipeapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.azamudin.recipeapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {
    private var email : EditText? = null
    private var btnresetPass: Button?=null
    private var btnback:Button?=null
//    private var progressbar: ProgressBar? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        email = findViewById(R.id.editTextEmail) as EditText
        btnresetPass = findViewById(R.id.resetButton)as Button
//        btnback = findViewById(R.id.btn_back)as Button

        auth = FirebaseAuth.getInstance()


//        btnback!!.setOnClickListener(View.OnClickListener {
//            finish()
//        })
        btnresetPass!!.setOnClickListener(View.OnClickListener {
            val email = email!!.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Enter your email ",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
//            progressbar!!.setVisibility(View.VISIBLE)

            auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener(OnCompleteListener {
                        task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@ResetPassword,"We have to sent you instraction in your email",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@ResetPassword,"Failed t sent to reset Email",Toast.LENGTH_SHORT ).show()
                    }
//                    progressbar!!.setVisibility(View.GONE)
                })

        })
    }
}