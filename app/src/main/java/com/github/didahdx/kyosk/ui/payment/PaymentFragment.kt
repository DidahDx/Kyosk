package com.github.didahdx.kyosk.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.ui.BaseFragment

class PaymentFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }


}