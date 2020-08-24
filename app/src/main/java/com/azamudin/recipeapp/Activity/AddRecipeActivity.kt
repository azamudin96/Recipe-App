package com.azamudin.recipeapp.Activity

import Adapter.FoodModel
import Util.LoginUtil
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class AddRecipeActivity : AppCompatActivity() {

    var selectphoto_imageview_register: ImageView? = null
    var selectphoto_button_register: Button? = null
    var food_name:EditText? = null
    var category:EditText? = null
    var cooktime:EditText? = null
    var preptime:EditText? = null
    var ingredients:EditText? = null
    var addButton:Button? = null

    var type:String? = null
    var category2:String? = null
    var foodName : String? = null
    var prepTime : String? = null
    var cookTime : String? = null
    var ingredients1 : String? = null
    var image:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        findViewById<ImageView>(R.id.btn_setting).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_setting).setOnClickListener(View.OnClickListener {
            showExitDialog()
        })
        findViewById<ImageView>(R.id.btn_back).setVisibility(View.VISIBLE)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener(View.OnClickListener {
            finish()
        })
        (findViewById(R.id.app_title_tv) as TextView).setText(R.string.app_name)

        val iin = intent
        val b = iin.extras
        if (b != null) {
            type = iin.getStringExtra("type")!!
            category2 = iin.getStringExtra("category")!!
            foodName = iin.getStringExtra("foodName")!!
            prepTime = iin.getStringExtra("prepTime")!!
            cookTime = iin.getStringExtra("cookTime")!!
            ingredients1 = iin.getStringExtra("ingredients")!!
            image = iin.getStringExtra("image")!!
            (findViewById<Button>(R.id.addButton) as Button).setText(type)

            Log.d("TAG" + "azamudin_type", type + category2)
        }

        selectphoto_imageview_register = findViewById(R.id.selectphoto_imageview_register)
        selectphoto_button_register = findViewById(R.id.selectphoto_button_register)
        food_name = findViewById(R.id.food_name)
        category = findViewById(R.id.category)
        cooktime = findViewById(R.id.cooktime)
        preptime = findViewById(R.id.preptime)
        ingredients = findViewById(R.id.ingredients)
        addButton = findViewById(R.id.addButton)

        if (addButton!!.text.equals("Add")){
            addRecipe()
        } else if(addButton!!.text.equals("Update")) {
            updateRecipe()
        }

        ingredients!!.setOnClickListener(View.OnClickListener {
            ingredientDialog()
        })

        selectphoto_button_register!!.setOnClickListener {
            Log.d("TAG", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d("TAG", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register!!.setImageBitmap(bitmap)

            selectphoto_button_register!!.alpha = 0f

//      val bitmapDrawable = BitmapDrawable(bitmap)
//      selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    var imagetoUpload : String? = null

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("TAG", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")

                    savetodb(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun uploadImageToFirebaseStorage1() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("TAG", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")

                    updatedb(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
            }
    }



    private fun addRecipe(){

        (findViewById(R.id.header_text) as TextView).setText("Add Recipe")

        category!!.setText(category2)

        addButton!!.setOnClickListener(View.OnClickListener {

            uploadImageToFirebaseStorage()
    })
    }

     private fun savetodb(imgURL : String){

         var database = FirebaseDatabase.getInstance().reference

         var foodname = food_name!!.text.toString()
         var image = imgURL
         var category1 = category!!.text.toString()
         var cooktime1 = cooktime!!.text.toString()
         var preptime1 = preptime!!.text.toString()
         var ingredients1 = ingredients!!.text.toString()

         database.child("Category").child(category1).child(foodname).setValue(
             FoodModel(
                 foodname,
                 image,
                 category1,
                 cooktime1,
                 preptime1,
                 ingredients1
             )
         ).addOnCompleteListener(OnCompleteListener<Void?> { task ->
             if (task.isSuccessful) {
                 //Do what you need to do
                 Toast.makeText(
                     this@AddRecipeActivity,
                     "Recipe Added Successfully",
                     Toast.LENGTH_SHORT
                 ).show()
                 food_name!!.setText("")
                 cooktime!!.setText("")
                 preptime!!.setText("")
                 ingredients!!.setText("")

             } else {
                 Log.d("TAG", task.exception!!.message)
             }
         })
     }

    private fun updateRecipe(){

        (findViewById(R.id.header_text) as TextView).setText("Update Recipe")

        food_name!!.setText(foodName)
        food_name!!.isFocusableInTouchMode = false
        food_name!!.isLongClickable = false
        category!!.setText(category2)
        preptime!!.setText(prepTime)
        cooktime!!.setText(cookTime)
        ingredients!!.setText(ingredients1)

        if (image != ""){
            Picasso.get().load(image).into(selectphoto_imageview_register)
            selectphoto_button_register!!.alpha = 0f
        }


        addButton!!.setOnClickListener(View.OnClickListener {
            uploadImageToFirebaseStorage()
        })

    }

    private fun updatedb(imgURL: String){
        var database = FirebaseDatabase.getInstance().reference

        var foodname = food_name!!.text.toString()
        var image = imgURL
        var category1 = category!!.text.toString()
        var cooktime1 = cooktime!!.text.toString()
        var preptime1 = preptime!!.text.toString()
        var ingredients1 = ingredients!!.text.toString()

        database.child("Category").child(category1).child(foodname).setValue(
            FoodModel(
                foodname,
                image,
                category1,
                cooktime1,
                preptime1,
                ingredients1
            )
        ).addOnCompleteListener(OnCompleteListener<Void?> { task ->
            if (task.isSuccessful) {
                //Do what you need to do
                Toast.makeText(
                    this@AddRecipeActivity,
                    "Recipe Updated Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                food_name!!.setText("")
                cooktime!!.setText("")
                preptime!!.setText("")
                ingredients!!.setText("")

            } else {
                Log.d("TAG", task.exception!!.message)
            }
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

    private fun ingredientDialog(){
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        val mView: View = layoutInflater.inflate(R.layout.success_dialog, null)
        //mView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        //mView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        mBuilder.setView(mView)
        val edt_ingredients = mView.findViewById<EditText>(R.id.edt_ingredients)
        val button_ok = mView.findViewById<Button>(R.id.button_ok)
        val mDialog = mBuilder.create()
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window!!.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        mDialog.show()

        button_ok!!.setOnClickListener(View.OnClickListener {
            ingredients!!.setText(edt_ingredients.text.toString())
            mDialog.cancel()
        })

        mDialog.setOnDismissListener { //AppManager.getAppManager().ToOtherActivity(LeaveListingActivity.class);
            Toast.makeText(
                this@AddRecipeActivity,
                "Cancel",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}