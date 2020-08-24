package App

import Util.LoginUtil
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.Activity.LoginActivity
import com.azamudin.recipeapp.Activity.MainActivity
import com.azamudin.recipeapp.R

class AppStart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_start)

        init()
    }

    fun init() {
        /**
         * 判断是否登录
         */
        if (isLogin()) {
            startActivity(Intent(this@AppStart, MainActivity::class.java))
        }
        else {
            startActivity(Intent(this@AppStart, LoginActivity::class.java))
        }
        finish()
    }

    fun isLogin(): Boolean {
        val member_code = LoginUtil["username", ""] as String?
        //        String status = (String) SharedPreferenceUtil.get("status", "");
        return if (member_code == "") {
            false
        } else true
    }
}