package com.azamudin.recipeapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.azamudin.recipeapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
//    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: TextView? = null
    private var progressBar: ProgressBar? = null
    private var tv_login : TextView? = null

    private var auth : FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

//        btnSignIn = findViewById(R.id.sign_in_button) as Button
        btnSignUp = findViewById(R.id.registerButton) as Button
        inputEmail = findViewById(R.id.editTextEmail) as EditText
        inputPassword = findViewById(R.id.editTextPassword) as EditText
        btnResetPassword = findViewById(R.id.tv_reset) as TextView
        tv_login = findViewById(R.id.tv_signin) as TextView

//        val username = findViewById<View>(R.id.username) as EditText
//        val phone = findViewById<View>(R.id.phone) as EditText
//        val email = findViewById<View>(R.id.email) as EditText
//        val password = findViewById<View>(R.id.password) as EditText

//        val myButton = findViewById<Button>(R.id.registerButton) as Button

        btnResetPassword!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity, ResetPassword::class.java))
        })

        tv_login!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        })


        //set listener
        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Enter your email Address!!", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Enter your Password",Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (password.length < 6){
                Toast.makeText(applicationContext,"Password too short, enter mimimum 6 characters" , Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
//            progressBar!!.setVisibility(View.VISIBLE)

            //create user
            auth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, OnCompleteListener {
                        task ->
                    Toast.makeText(this@RegisterActivity,"createUserWithEmail:onComplete"+task.isSuccessful,Toast.LENGTH_SHORT).show()
//                    progressBar!!.setVisibility(View.VISIBLE)

                    if (!task.isSuccessful){
                        Toast.makeText(this@RegisterActivity,"User Not created",Toast.LENGTH_SHORT).show()
                        return@OnCompleteListener
                    }else{
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }


                })

        })
    }

//    override fun onResume() {
//        super.onResume()
//        progressBar!!.setVisibility(View.GONE)
//    }
}