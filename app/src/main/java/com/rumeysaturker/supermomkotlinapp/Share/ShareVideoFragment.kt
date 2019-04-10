package com.rumeysaturker.supermomkotlinapp.share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Facing
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_video.*
import kotlinx.android.synthetic.main.fragment_share_video.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File


class ShareVideoFragment : Fragment() {

    var videoView:CameraView?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view =inflater.inflate(R.layout.fragment_share_video, container, false)

        videoView=view.videoView

        var olusacakVideoDosyaAdi=System.currentTimeMillis()
        var olusacakVideoDosya= File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Camera/"+olusacakVideoDosyaAdi+".mp4")

        videoView!!.addCameraListener(object : CameraListener(){

            override fun onVideoTaken(video: File?) {
                super.onVideoTaken(video)

                activity!!.anaLayout.visibility= View.GONE
                activity!!.fragmentContainerLayout.visibility=View.VISIBLE
                var transaction=activity!!.supportFragmentManager.beginTransaction()

                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(video!!.absolutePath.toString(),false))
                transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()

            }

        })

//videoya basılı tutulduğu sürece çek
        view.imgCamSwitch.setOnClickListener {

            if (videoView!!.facing == Facing.BACK) {//arka kamera açıksa
                videoView!!.facing = Facing.FRONT//butona tıklanıldığında ön yap
            } else {
               videoView!!.facing = Facing.BACK
            }

        }

        view.imgVideoCek.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    if (event!!.action == MotionEvent.ACTION_DOWN) {//basılıyken

                        videoView!!.startCapturingVideo(olusacakVideoDosya)
                        Toast.makeText(activity, "Video kaydediliyor", Toast.LENGTH_SHORT).show()
                        return true

                    } else if (event!!.action == MotionEvent.ACTION_UP) {//parmağımızı çektiğimizde
                        Toast.makeText(activity, "Video kaydedildi", Toast.LENGTH_SHORT).show()
                        videoView!!.stopCapturingVideo()
                        return true
                    }


                return false
            }

        })

        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        //Log.e("HATA2"," VIDEO FRAGMENTI ON RESUME")
        videoView!!.start()
    }

    override fun onPause() {
        super.onPause()
        //Log.e("HATA2"," VIDEO FRAGMENTI ON PAUSE")
        videoView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Log.e("HATA2"," VIDEO FRAGMENTI ON DESTROY")
        if(videoView!=null)
            videoView!!.destroy()
    }

}