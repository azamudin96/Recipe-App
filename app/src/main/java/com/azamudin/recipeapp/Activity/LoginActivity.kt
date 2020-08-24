package com.azamudin.recipeapp.Activity

import Util.LoginUtil
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.azamudin.recipeapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
//    private var btnSignUp: Button? = null
    private var btnResetPassword: TextView? = null
//    private var progressBar: ProgressBar? = null
    private var tv_signup : TextView? = null

    private var auth : FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

//        btnSignIn = findViewById(R.id.sign_in_button) as Button
        btnSignIn = findViewById(R.id.cirLoginButton) as Button
        inputEmail = findViewById(R.id.editTextEmail) as EditText
        inputPassword = findViewById(R.id.editTextPassword) as EditText
        btnResetPassword = findViewById(R.id.tv_reset) as TextView
        tv_signup = findViewById(R.id.tv_signup) as TextView

        btnResetPassword!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPassword::class.java))
        })

        tv_signup!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        })

        btnSignIn!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please Entre your email.",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please Enter your Password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
//            progressBar!!.setVisibility(View.VISIBLE)

            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener {
                        task ->
//                    progressBar!!.setVisibility(View.VISIBLE)

                    if (!task.isSuccessful){
                        if (password.length < 6){
                            inputPassword!!.setError(getString(R.string.minimum_password))
                        }else{
                            Toast.makeText(this@LoginActivity,getString(R.string.auth_failed),Toast.LENGTH_LONG).show()
                        }
                    }else{
                        LoginUtil.put("username" , email)
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }
                })
        })

    }
}