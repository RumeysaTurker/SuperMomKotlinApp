package com.rumeysaturker.supermomkotlinapp.Profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader

import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {

    lateinit var circleProfileImageFragment: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        circleProfileImageFragment = view.findViewById(R.id.circleProfileImage)

        setupProfilePicture()
        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }
        return view
    }

    private fun setupProfilePicture() {
        var imgURL = "media.licdn.com/dms/image/C5103AQEdDEx9EvxrvA/profile-displayphoto-shrink_200_200/0?e=1546473600&v=beta&t=-sE_0QPVIn7Pfaen9Fq_9lcYfw4pwByTKOxm2qfBf20"
        UniversalImageLoader.setImage(imgURL, circleProfileImageFragment, null, "https://")
    }


}
