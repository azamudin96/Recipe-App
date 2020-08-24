package com.azamudin.recipeapp.Activity

import Adapter.CategoryAdapter
import Adapter.Food
import Util.LoginUtil
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    var image = ""

    var adapter: CategoryAdapter? = null
    var adapter1: CategoryAdapter? = null
    var adapter2: CategoryAdapter? = null
    var adapter3: CategoryAdapter? = null
    var adapter4: CategoryAdapter? = null
    var adapter5: CategoryAdapter? = null

    var category_gv : GridView? = null
//    var brunch_gv : GridView? = null
//    var lunch_gv : GridView? = null
//    var teatime_gv : GridView? = null
//    var dinner_gv : GridView? = null
//    var supper_gv : GridView? = null

    var foodsList = ArrayList<Food>()

    private var mShimmerViewContainer: ShimmerFrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.btn_setting).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_setting).setOnClickListener(View.OnClickListener {
            showExitDialog()
        })
        (findViewById(R.id.app_title_tv) as TextView).setText(R.string.app_name)
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

    }

    private fun loadData(){
        foodsList.clear()
        category_gv = findViewById(R.id.category_gv) as GridView

        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("Category")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snap in dataSnapshot.children) {
                    val category = snap.key!!

//                    val image = snap.child("image").getValue(String::class.java)!!

                    for (snap1 in snap.children) {
                        image = snap1.child("image").getValue(String::class.java)!!
                        Log.d("TAG" + "azamudin", category + image)
                    }

                    mShimmerViewContainer!!.stopShimmerAnimation();
                    mShimmerViewContainer!!.setVisibility(View.GONE);

                    foodsList.add(Food(category, image))

                    adapter = CategoryAdapter(this@MainActivity, foodsList)
                    adapter!!.notifyDataSetChanged();
                    category_gv!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        yourRef.addListenerForSingleValueEvent(eventListener)


        category_gv!!.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->

            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("category", foodsList.get(position).name)
            startActivity(intent)
//            Toast.makeText(
//                this@MainActivity,
//                "" + position,
//                Toast.LENGTH_SHORT
//            ).show()
        })
    }

    override fun onBackPressed() {
        showExitDialogue()
    }

    private fun showExitDialogue() {
        //AlertDialog.Builder builder;
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
            .setMessage("Are you sure to exit this App?")
            .setPositiveButton("Exit") { dialog, which -> // continue with delete
               finish()
            }
            .setNegativeButton("Cancel") { dialog, which -> // do nothing
                dialog.dismiss()
            }
            .show()
    }


    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure want to logout?")
        builder.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which ->
//                startTbCart()
//                SharedPreferenceUtil.clear()
                LoginUtil.clear()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                dialog.dismiss()
            })
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.create().show()
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer!!.startShimmerAnimation()
        loadData()
    }

    override fun onPause() {
        loadData()
        mShimmerViewContainer!!.stopShimmerAnimation()
        super.onPause()
    }

}