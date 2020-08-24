package com.azamudin.recipeapp.Activity

import Util.LoginUtil
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class RecipeActivity : AppCompatActivity() {

    var food_img: ImageView? = null
    var product_name_tv : TextView? = null
    var product_detail_tv:TextView? = null

    var foodName = ""
    var category = ""

    var prepTime = ""
    var cookTime = ""
    var ingredients = ""
    var image = ""

    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        findViewById<ImageView>(R.id.btn_back).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener(View.OnClickListener {
            finish()
        })
        findViewById<ImageView>(R.id.btn_setting).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_setting).setOnClickListener(View.OnClickListener {
            showExitDialog()
        })
        findViewById<ImageView>(R.id.edit_img).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.edit_img).setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            intent.putExtra("type", "Update")
            intent.putExtra("category", category)
            intent.putExtra("foodName", foodName)
            intent.putExtra("prepTime", prepTime)
            intent.putExtra("cookTime", cookTime)
            intent.putExtra("ingredients", ingredients)
            intent.putExtra("image", image)

            startActivity(intent)
        })
        (findViewById(R.id.app_title_tv) as TextView).setText(R.string.app_name)
        findViewById<ImageView>(R.id.delete_img).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.delete_img).setOnClickListener(View.OnClickListener {
                showDeleteDialog()
        })

        food_img = findViewById(R.id.food_img)
        product_name_tv = findViewById(R.id.product_name_tv)
        product_detail_tv = findViewById(R.id.product_detail_tv)


        val iin = intent
        val b = iin.extras
        if (b != null) {
            category = iin.getStringExtra("category")!!
            foodName = iin.getStringExtra("foodName")!!

            Log.d("TAG" + "azamudin_category", foodName)
        }

        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("Category").child(category).child(foodName)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

//
                    val name = dataSnapshot.child("name").getValue(String::class.java)!!
                    image = dataSnapshot.child("image").getValue(String::class.java)!!
                    ingredients = dataSnapshot.child("ingredients").getValue(String::class.java)!!
                    prepTime = dataSnapshot.child("prep_time").getValue(String::class.java)!!
                    cookTime = dataSnapshot.child("cook_time").getValue(String::class.java)!!

                    Picasso.get().load(image).into(food_img);
                    product_name_tv!!.setText(name)
                    product_detail_tv!!.setText(ingredients)

//                    prepTime = dataSnapshot.child("prep time").getValue(String::class.java)!!
//                    cookTime = dataSnapshot.child("cook time").getValue(String::class.java)!!
//                    ingredients = dataSnapshot.child("ingredients").getValue(String::class.java)!!


                    Log.d("TAG" + "azamudin", name + image)
                    System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."

//                    coursesNames.add(
//                        snap.child("Course Details").child("courseName")
//                            .getValue(String::class.java)
//                    )
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        yourRef.addListenerForSingleValueEvent(eventListener)
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
                val intent = Intent(this, LoginActivity::class.java)
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

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure want to delete $foodName?")
        builder.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, which ->
                deleteData(foodName)
                dialog.dismiss()
            })
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.create().show()
    }

    private fun deleteData(strTitle: String) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Category");

        val deleteQuery: Query = mDatabase!!.child(category).child(strTitle).orderByChild(strTitle)
        deleteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (delData in dataSnapshot.children) {
                    delData.ref.removeValue()
                }
                Toast.makeText(this@RecipeActivity, "Data Deleted", Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RecipeActivity, databaseError.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

}