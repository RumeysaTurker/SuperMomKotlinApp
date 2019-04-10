package com.rumeysaturker.supermomkotlinapp.home

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.*
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.share.ShareNextFragment
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileOutputStream


class CameraFragment : Fragment() {

    var myCamera: CameraView? = null
    var kameraIzniVerildiMi = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater?.inflate(R.layout.fragment_camera, container, false)

        myCamera = view!!.camera_view
        myCamera!!.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        myCamera!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)


        myCamera!!.addCameraListener(object : CameraListener() {

            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)

                var cekilenFotoAdi = System.currentTimeMillis()
                var cekilenFotoKlasor = File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/AnnelerYarisiyorApp/compressed" + cekilenFotoAdi + ".jpg")

                //if(cekilenFotoKlasor.isDirectory || cekilenFotoKlasor.mkdirs()){
                //  var dosyaTamYolu=File(Environment.getExternalStorageDirectory().absolutePath +"/DCIM/Camera/"+cekilenFotoAdi+".jpg")
                var dosyaOlustur = FileOutputStream(cekilenFotoKlasor)
                dosyaOlustur.write(jpeg)
                //Log.e("HATA2","cekilen resim buraya kaydedildi :"+dosyaTamYolu.absolutePath.toString())
                dosyaOlustur.close()
                activity!!.tasiyici.visibility = View.GONE
                activity!!.camera_view.visibility = View.GONE
                activity!!.frameLayout.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                var transaction = activity!!.supportFragmentManager.beginTransaction()

                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(cekilenFotoKlasor.absolutePath.toString(), true))
                transaction.replace(R.id.frameLayout, ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()

                //}


            }


        })

        view.imgCameraSwitch.setOnClickListener {

            if (myCamera!!.facing == Facing.BACK) {//arka kamera açıksa
                myCamera!!.facing = Facing.FRONT//butona tıklanıldığında ön yap
            } else {
                myCamera!!.facing = Facing.BACK
            }

        }

        view.imgFotoCek.setOnClickListener {

            if (myCamera!!.facing == Facing.BACK) {
                myCamera!!.capturePicture()
            } else {
                myCamera!!.captureSnapshot()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        //Log.e("HATA2"," CAMERA FRAGMENTI ON RESUME")
        if (kameraIzniVerildiMi == true)
            myCamera!!.start()
    }

    override fun onPause() {
        super.onPause()
        //Log.e("HATA2"," CAMERA FRAGMENTI ON PAUSE")
        myCamera!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Log.e("HATA2"," CAMERA FRAGMENTI ON DESTROY")

        if (myCamera != null)
            myCamera!!.destroy()
    }

    //EVENTBUS
    @Subscribe(sticky = true)
    internal fun onKameraIzinEvent(izinDurumu: EventBusDataEvents.KameraIzinBilgisiGonder) {
        kameraIzniVerildiMi = izinDurumu.kameraIzniVerildiMi!!
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