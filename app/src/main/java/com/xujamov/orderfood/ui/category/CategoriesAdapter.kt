package com.xujamov.orderfood.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.xujamov.orderfood.R
import com.xujamov.orderfood.databinding.CategoryCardBinding
import com.xujamov.orderfood.data.models.Categories

import com.xujamov.orderfood.common.loadImage

class CategoriesAdapter(private var categoriesList: ArrayList<Categories>) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryCardDesign>() {

    var onClick: (Categories) -> Unit = {}

    class CategoryCardDesign(val categoryCardBinding: CategoryCardBinding) :
        RecyclerView.ViewHolder(categoryCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryCardDesign {
        val layoutInflater = LayoutInflater.from(parent.context)
        val categoryCardBinding = CategoryCardBinding.inflate(layoutInflater, parent, false)
        return CategoryCardDesign(categoryCardBinding)
    }

    override fun onBindViewHolder(holder: CategoryCardDesign, position: Int) {
        val category = categoriesList[position]

        holder.categoryCardBinding.apply {
            categoryText.text = category.name
            categoryImageView.loadImage(category.imagePath)
            root.applyClickShrink()

            root.setOnClickListener {
                onClick(category)
            }
        }


        holder.categoryCardBinding.root.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recyclerview_anim)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }


}