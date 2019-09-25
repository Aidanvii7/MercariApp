package com.mercariapp.feature.viewproduct.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mercariapp.core.domain.Product
import com.mercariapp.feature.viewproduct.databinding.FragmentViewProductBinding
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ViewProductFragment : Fragment() {

    data class Arguments(val product: Product) {
        interface FromBundle {
            operator fun invoke(bundle: Bundle): Arguments
        }
    }

    private val argumentsFromBundle by inject<Arguments.FromBundle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentViewProductBinding.inflate(inflater, container, false).run {
        lifecycleOwner = this@ViewProductFragment
        arguments?.let { arguments ->
            viewModel = get { parametersOf(argumentsFromBundle(arguments).product) }
        }
        root
    }

}

