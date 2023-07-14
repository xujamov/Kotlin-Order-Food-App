package com.xujamov.orderfood.ui.category

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.xujamov.orderfood.R
import com.xujamov.orderfood.common.gone
import com.xujamov.orderfood.common.visible
import com.xujamov.orderfood.databinding.FragmentCategoriesBinding
import com.xujamov.orderfood.data.models.Categories
import com.xujamov.orderfood.data.repo.Repository
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    private val viewModel by lazy { CategoriesViewModel(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tryAgain: Button = requireView().findViewById<View>(R.id.tryAgain) as Button

        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            resources.getString(R.string.categories)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        binding.apply {
            viewModel.apply {
                getCategories()
                initObservers()
                tryAgain.setOnClickListener {
                    getCategories()
                }
            }
        }


    }

    private fun initObservers() {
        binding.apply {
            viewModel.apply {
                val errorLayout: LinearLayout =
                    requireView().findViewById<View>(R.id.errorLayout) as LinearLayout
                isLoading.observe(viewLifecycleOwner) {
                    when (it!!) {
                        Repository.LOADING.LOADING -> {
                            shimmerLayout.apply {
                                startShimmer()
                                visible()
                            }
                            categoriesRecyclerView.gone()
                            errorLayout.gone()
                        }
                        Repository.LOADING.DONE -> {
                            shimmerLayout.apply {
                                stopShimmer()
                                gone()
                            }
                            categoriesRecyclerView.visible()
                            errorLayout.gone()
                        }

                        Repository.LOADING.ERROR -> {
                            shimmerLayout.apply {
                                stopShimmer()
                                gone()
                            }
                            categoriesRecyclerView.gone()
                            errorLayout.visible()

                        }
                    }

                }

                categoryList.observe(viewLifecycleOwner) { categories ->
                    val productAdapter = CategoriesAdapter(categories as ArrayList<Categories>)
                    categoriesRecyclerView.adapter = productAdapter
                    productAdapter.onClick = ::clickCategory

                }
                categoriesRecyclerView.layoutManager = GridLayoutManager(context, 2)
                categoriesRecyclerView.setHasFixedSize(true)
            }
        }
    }

    private fun clickCategory(category: Categories) {
        val categoryNavigation =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(
                category
            )

        findNavController().navigate(categoryNavigation)

    }

}