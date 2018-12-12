package com.rumeysaturker.supermomkotlinapp.Profile


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rumeysaturker.supermomkotlinapp.Models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileEditFragment : Fragment() {

    lateinit var circleProfileImageFragment: CircleImageView
    lateinit var gelenkullaniciBilgileri: Users
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        setupKullaniciBilgileri(view)

          view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }
        return view
    }

    private fun setupKullaniciBilgileri(view: View?) {
  view!!.etProfileName.setText(gelenkullaniciBilgileri!!.adi_soyadi)
        view!!.etKullaniciAdi.setText(gelenkullaniciBilgileri!!.user_name)
   if(!gelenkullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()){
       view!!.etBiyografi.setText(gelenkullaniciBilgileri!!.user_detail!!.biography)
   }
        if(!gelenkullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()){
            view!!.etWebSitesi.setText(gelenkullaniciBilgileri!!.user_detail!!.web_site)
        }
        //setupProfilePicture
        var imgURL=gelenkullaniciBilgileri!!.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgURL!!, view!!.circleProfileImage,view!!.progressBar, "")

    }



    /***************************EVENTBUS*****************************/
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullanıcıBilgileri: EventBusDataEvents.KulaniciBilgileriniGonder) {

        gelenkullaniciBilgileri = kullanıcıBilgileri!!.kullanici!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}
