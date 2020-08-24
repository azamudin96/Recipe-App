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
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryActivity : AppCompatActivity() {

    var category = ""
    var name1 = ""
    var image1 = ""
    var prepTime = ""
    var cookTime = ""
    var ingredients = ""


    var adapter: CategoryAdapter? = null
    var category_gv : GridView? = null
    var foodsList = ArrayList<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        findViewById<ImageView>(R.id.btn_back).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener(View.OnClickListener {
            finish()
        })
        findViewById<ImageView>(R.id.btn_setting).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_setting).setOnClickListener(View.OnClickListener {
            showExitDialog()
        })
        findViewById<ImageView>(R.id.add_img).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.add_img).setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            intent.putExtra("type", "Add")
            intent.putExtra("category", category)
            intent.putExtra("foodName", name1)
            intent.putExtra("prepTime", prepTime)
            intent.putExtra("cookTime", cookTime)
            intent.putExtra("ingredients", ingredients)
            intent.putExtra("image", image1)
            startActivity(intent)
        })
        (findViewById(R.id.app_title_tv) as TextView).setText(R.string.app_name)

        //llOverall.setOnTouchListener(this);
        //nV.setNavigationItemSelectedListener(this);

        //** calling method from listing page
        val iin = intent
        val b = iin.extras
        if (b != null) {
            category = iin.getStringExtra("category")!!

            Log.d("TAG" + "azamudin_category", category)
        }
    }

    private fun loadData(){
        foodsList.clear()

        category_gv = findViewById(R.id.category_gv) as GridView

        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("Category").child(category)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snap in dataSnapshot.children) {
                    val category = snap.key!!

                    val name = snap.child("name").getValue(String::class.java)!!
                    val image = snap.child("image").getValue(String::class.java)!!
                    ingredients = snap.child("ingredients").getValue(String::class.java)!!
                    prepTime = snap.child("prep_time").getValue(String::class.java)!!
                    cookTime = snap.child("cook_time").getValue(String::class.java)!!
                    name1 = name
                    image1 = image


//                    prepTime = snap.child("prep time").getValue(String::class.java)!!
//                    cookTime = snap.child("cook time").getValue(String::class.java)!!
//                    ingredients = snap.child("ingredients").getValue(String::class.java)!!


                    Log.d("TAG" + "azamudin", name + image)

                    foodsList.add(Food(name, image))

                    adapter = CategoryAdapter(this@CategoryActivity, foodsList)
                    adapter!!.notifyDataSetChanged();
                    category_gv!!.adapter = adapter
                }
                System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        yourRef.addListenerForSingleValueEvent(eventListener)

        category_gv!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, v, position, id ->

            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("foodName", foodsList.get(position).name)
            startActivity(intent)
//            Toast.makeText(
//                this@MainActivity,
//                "" + position,
//                Toast.LENGTH_SHORT
//            ).show()
        })
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
                val intent = Intent(this@CategoryActivity, LoginActivity::class.java)
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
        loadData()
    }

//    override fun onPause() {
//        mShimmerViewContainer!!.stopShimmerAnimation()
//        super.onPause()
//    }

}