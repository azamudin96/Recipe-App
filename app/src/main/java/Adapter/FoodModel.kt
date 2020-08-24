package Adapter

class FoodModel {
    var name: String? = null
    var image: String? = null
    var category: String? = null
    var cook_time: String? = null
    var prep_time: String? = null
    var ingredients: String? = null


    constructor(
        name: String?,
        image: String?,
        category: String?,
        cook_time: String?,
        prep_time: String?,
        ingredients: String?
    ) {
        this.name = name
        this.image = image
        this.category = category
        this.cook_time = cook_time
        this.prep_time = prep_time
        this.ingredients = ingredients
    }



}