package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*


class ShareActivityGridViewAdapter(context: Context?, resource: Int, var klasordekiDosyalar: ArrayList<String>) : ArrayAdapter<String>(context, resource, klasordekiDosyalar) {


    var inflater: LayoutInflater
    var tekSutunResim:View? = null
    lateinit var viewHolder:ViewHolder

    init {
        inflater=LayoutInflater.from(context)
    }

    inner class ViewHolder(){
        lateinit var imageView:GridImageView
        lateinit var progressBar: ProgressBar
        lateinit var tvSure :TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        tekSutunResim=convertView//her eleman için tekrar inflater işlemi yapmamak için kontrol yapıyoruz


        if(tekSutunResim== null){//ilk geldiğinde null olarak gelip çalışacak sonrasında null gelmediği için tekrar tekrar işlem yapılmayacak
            tekSutunResim=inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)
            viewHolder=ViewHolder()

            viewHolder.imageView=tekSutunResim!!.imgTekSutunImage
            viewHolder.progressBar=tekSutunResim!!.progressBar
            viewHolder!!.tvSure=tekSutunResim!!.tvSure

            tekSutunResim!!.setTag(viewHolder)

        }else {

            viewHolder= tekSutunResim!!.getTag() as ViewHolder

        }
//fotoğraf mı video mu diye kontrol ediyoruz**burası videoların sağ altına süreleri yazdırmak için gridte
        var dosyaYolu=klasordekiDosyalar.get(position)
        var dosyaTuru=dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
//video dosyasıysa
        if(dosyaTuru.equals(".mp4")){

            viewHolder.tvSure.visibility=View.VISIBLE
            var retriver=MediaMetadataRetriever()
            retriver.setDataSource(context, Uri.parse("file://"+dosyaYolu))

            var videoSuresi=retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoSuresiLong=videoSuresi.toLong()

            //Log.e("HATA6","VİDEO SURESı:"+videoSuresiLong)

            viewHolder.tvSure.setText(convertDuration(videoSuresiLong))
            UniversalImageLoader.setImage(klasordekiDosyalar.get(position), viewHolder.imageView, viewHolder.progressBar,"file:/")

        }else {//fotoğraf dosyasıysa

            viewHolder.tvSure.visibility=View.GONE
            UniversalImageLoader.setImage(klasordekiDosyalar.get(position), viewHolder.imageView, viewHolder.progressBar,"file:/")


        }




        return tekSutunResim!!

    }

    fun convertDuration(duration: Long): String {
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24

        var time=""
        if(hour>0){
            time = String.format("%02d:%02d:%02d", hour, minute, second)
        }else {
            time = String.format("%02d:%02d", minute, second)
        }

        return time

    }


}