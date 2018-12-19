package com.rumeysaturker.supermomkotlinapp.Profile


import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.fragment_yukleniyor.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class YukleniyorFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_yukleniyor, container, false)
        view.progressBar2.indeterminateDrawable.setColorFilter(ContextCompat.getColor(activity!!,R.color.colorAccent),PorterDuff.Mode.SRC_IN)
        return view
    }


}
