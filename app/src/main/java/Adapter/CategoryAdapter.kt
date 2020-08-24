package Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.azamudin.recipeapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_entry.view.*


class CategoryAdapter : BaseAdapter {
    var foodsList = ArrayList<Food>()
    var context: Context? = null

    constructor(context: Context, foodsList: ArrayList<Food>) : super() {
        this.context = context
        this.foodsList = foodsList
    }

    override fun getCount(): Int {
        return foodsList.size
    }

    override fun getItem(position: Int): Any {
        return foodsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val food = this.foodsList[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var foodView = inflator.inflate(R.layout.food_entry, null)
//        val imgUri: Uri = Uri.parse(food.image)
        Picasso.get().load(food.image).into(foodView.food_img);
        //foodView.food_img.setImageURI(imgUri)
        foodView.food_name.text = food.name!!

        return foodView
    }
}