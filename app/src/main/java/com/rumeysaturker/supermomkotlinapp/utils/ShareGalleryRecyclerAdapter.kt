package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*
import org.greenrobot.eventbus.EventBus

import android.os.Environment
import java.io.File





class ShareGalleryRecyclerAdapter(var klasordekiDosyalar: ArrayList<String>, var myContext: Context) : RecyclerView.Adapter<ShareGalleryRecyclerAdapter.MyViewHolder>() {

    lateinit var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(myContext)
    }

    override fun getItemCount(): Int {

        return klasordekiDosyalar.size

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var tekSutunDosya = inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)

        return MyViewHolder(tekSutunDosya)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var dosyaYolu = klasordekiDosyalar.get(position)
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if (dosyaTuru.equals(".mp4")) {

            holder.videoSure.visibility = View.VISIBLE

            try {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(myContext, Uri.parse("file://" + dosyaYolu)) // /storage/337C 1C15/Music/Edguy/Speedhoven.mp3
            } catch (ex: RuntimeException) {
                // something went wrong with the file, ignore it and continue
            }


            var retriver = MediaMetadataRetriever()
            //   retriver.setDataSource(myContext, Uri.parse("file://"+dosyaYolu))


            var videoSuresi = retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoSuresiLong = 0.toLong();
            if (videoSuresi != null)
                videoSuresiLong = videoSuresi!!.toLong()
            holder.videoSure.setBackgroundResource(R.drawable.ic_video_cam)

             //   holder.videoSure.setText(convertDuration(videoSuresiLong))
                UniversalImageLoader.setImage(dosyaYolu, holder.dosyaResim, holder.dosyaProgressBar, "file:/")

            } else {

                holder.videoSure.visibility = View.GONE
                UniversalImageLoader.setImage(dosyaYolu, holder.dosyaResim, holder.dosyaProgressBar, "file:/")

            }

        holder.tekSutunDosya.setOnClickListener {
            EventBus.getDefault().post(EventBusDataEvents.GalerySecilenDosyaYolunuGonder(dosyaYolu))
        }

    }


    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var tekSutunDosya = itemView as ConstraintLayout

        var dosyaResim = tekSutunDosya.imgTekSutunImage
        var videoSure = tekSutunDosya.tvSure
        var dosyaProgressBar = tekSutunDosya.progressBar


    }

    fun convertDuration(duration: Long): String {
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24

        var time = ""
        if (hour > 0) {
            time = String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            time = String.format("%02d:%02d", minute, second)
        }

        return time

    }


}