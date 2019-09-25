package com.mercariapp.mercari.databinding

import android.os.Build
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.mercariapp.core.domain.Product
import com.mercariapp.feature.browseproducts.presentation.BrowseProductsFragmentDirections
import com.mercariapp.feature.common.databinding.OnClickBindingAdapters
import com.mercariapp.mercari.R

class AppOnClickBindingAdapters : OnClickBindingAdapters() {

    override fun View.onClickProduct(product: Product) {
        when (id) {
            R.id.card_product -> navigateFromBrowseProductsToViewProduct(product)
            else -> throwUnknownViewId<Product>()
        }
    }

    private fun View.navigateFromBrowseProductsToViewProduct(product: Product) {
        val directions = BrowseProductsFragmentDirections.actionDestinationBrowseProductsToDestinationViewProduct(product)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val extras = FragmentNavigatorExtras(this to "product_photo_transition")
            findNavController().navigate(directions, extras)
        } else {
            findNavController().navigate(directions)
        }
    }

    private inline fun <reified T> View.throwUnknownViewId(): Nothing =
        throw IllegalStateException("unknown view ID '$id' for type ${T::class.java.simpleName}")
}