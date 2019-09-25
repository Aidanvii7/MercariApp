package com.mercariapp.mercari.di

import android.os.Bundle
import com.mercariapp.feature.viewproduct.presentation.ViewProductFragment
import com.mercariapp.feature.viewproduct.presentation.ViewProductFragmentArgs

class ViewProductFragmentArgumentsFromBundle : ViewProductFragment.Arguments.FromBundle {
    override fun invoke(bundle: Bundle) = ViewProductFragment.Arguments(
        product = ViewProductFragmentArgs.fromBundle(bundle).product
    )
}