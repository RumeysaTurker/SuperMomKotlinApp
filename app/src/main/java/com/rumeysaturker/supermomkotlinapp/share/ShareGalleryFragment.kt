package com.rumeysaturker.supermomkotlinapp.share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File


class ShareGalleryFragment : Fragment() {

    var secilenDosyaYolu:String?=null
    var dosyaTuruResimMi : Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view=inflater.inflate(R.layout.fragment_share_gallery, container, false)

        var klasorPaths=ArrayList<String>()
        var klasorAdlari=ArrayList<String>()//kullanıcıya göstereceğimiz adlar

        var root= Environment.getExternalStorageDirectory().path
        var kameraResimleri= root+"/DCIM/Camera"
        var indirilenResimler=root+"/Download"
        var whatsappResimleri=root+"/WhatsApp/Media/WhatsApp Images"
        var screenShot=root+"/PICTURES/Screenshots"
        var twitter=root+"/PICTURES/Twitter"
        var buUygulama = root+"/DCIM/AnnelerYarisiyorApp/compressed"

        klasorAdlari.add("Galeri")

        if(FileOperations.klasorMevcutMu(kameraResimleri)){
            klasorPaths.add(kameraResimleri)
            klasorAdlari.add("Kamera")//position 0'da kamera resimleri var
        }
        if(FileOperations.klasorMevcutMu(indirilenResimler)){
            klasorPaths.add(indirilenResimler)
            klasorAdlari.add("Indirilenler")
        }
        if(FileOperations.klasorMevcutMu(whatsappResimleri)){
            klasorPaths.add(whatsappResimleri)
            klasorAdlari.add("Whatsapp")
        }
        if(FileOperations.klasorMevcutMu(screenShot)){
            klasorPaths.add(screenShot)
            klasorAdlari.add("Ekran Alıntıları")
        }
        if(FileOperations.klasorMevcutMu(twitter)){
            klasorPaths.add(twitter)
            klasorAdlari.add("Twitter")
        }
        if(FileOperations.klasorMevcutMu(buUygulama)){
            klasorPaths.add(buUygulama)
            klasorAdlari.add("Anneler Yarisiyor App")
        }


        var spinnerArrayAdapter=ArrayAdapter(activity, android.R.layout.simple_spinner_item, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//fragmentte olduğumuz için başa view geliyor
        view.spnKlasorAdlari.adapter=spinnerArrayAdapter

        //ilk açıldıgında en son dosya gösterilir
        view.spnKlasorAdlari.setSelection(0)


        view.spnKlasorAdlari.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                // setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))

                if(position==0){
                    setupRecyclerView(FileOperations.galeridekiTumFotograflariGetir(activity!!))
                }else{
                    if(klasorPaths.isNotEmpty())
                    setupRecyclerView(FileOperations.klasordekiDosyalariGetir(klasorPaths.get(position-1)))
                }



            }

        }


        view.tvIleriButton.setOnClickListener {

            activity!!.anaLayout.visibility= View.GONE//fragmentte old.muz için analayouta activity üzerinden ulaşıyoruz
            activity!!.fragmentContainerLayout.visibility=View.VISIBLE
            var transaction=activity!!.supportFragmentManager.beginTransaction()

            if(dosyaTuruResimMi==true){

                var bitmap=imgCropView.croppedImage
                if(bitmap != null){
                    //Log.e("GALERI","BİTMAP OLUSMUS")
                    var croppedImagePath=FileOperations.cropImageandSave(bitmap)
                    EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(croppedImagePath,dosyaTuruResimMi))
                    transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
                    transaction.addToBackStack("shareNextFragmentEklendi")
                    transaction.commit()
                }else{
                    //Log.e("GALERI","BİTMAP olusmamıs")
                }



            }else {
                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(secilenDosyaYolu,dosyaTuruResimMi))
                videoView.stopPlayback()
                transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()

            }





        }

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()

        }




        return view
    }

    private fun setupRecyclerView(klasordekiDosyalar: ArrayList<String>) {

        var recyclerViewAdapter=ShareGalleryRecyclerAdapter(klasordekiDosyalar, this.activity!!)
        recyclerViewDosyalar.adapter=recyclerViewAdapter
//verileri gridview olarak gösteriyoruz
        var layoutManager=GridLayoutManager(this.activity,4)
        recyclerViewDosyalar.layoutManager= (layoutManager as RecyclerView.LayoutManager?)!!
//recyclerView ın hızlı olması için
        recyclerViewDosyalar.setHasFixedSize(true);
        recyclerViewDosyalar.setItemViewCacheSize(10);
        recyclerViewDosyalar.setDrawingCacheEnabled(true);
        recyclerViewDosyalar.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        //ilk açıldıgında ilk dosya gösterilir
        if(secilenDosyaYolu!=null) {
            //secilenDosyaYolu null
            secilenDosyaYolu = klasordekiDosyalar.get(0)
            resimVeyaVideoGoster(secilenDosyaYolu!!)
        }

    }

   /* fun setupGridView(secilenKlasordekiDosyalar : ArrayList<String>){
        var gridAdapter=ShareActivityGridViewAdapter(activity,R.layout.tek_sutun_grid_resim,secilenKlasordekiDosyalar)
        recyclerViewDosyalar.adapter=gridAdapter
        //ilk açıldıgında ilk dosya gösterilir
        secilenDosyaYolu=secilenKlasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))
        recyclerViewDosyalar.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               secilenDosyaYolu= secilenKlasordekiDosyalar.get(position)
               resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))
            }
        })
    }*/

    private fun resimVeyaVideoGoster(dosyaYolu: String) {

        var dosyaTuru=dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        //file://assdadsada.mp4


        if(dosyaTuru != null){
            if(dosyaTuru.equals(".mp4")){

                videoView.visibility=View.VISIBLE
                imgCropView.visibility=View.GONE
                dosyaTuruResimMi=false
                videoView.setVideoURI(Uri.parse("file://"+dosyaYolu))
                //Log.e("HATA","Video : "+"file://"+dosyaYolu)
                videoView.start()

            }else {
                videoView.visibility=View.GONE
                imgCropView.visibility=View.VISIBLE
                dosyaTuruResimMi=true
                UniversalImageLoader.setImage(dosyaYolu,imgCropView,null,"file://")

            }
        }







    }

    override fun onResume() {
        super.onResume()
        //Log.e("HATA2"," GALERY FRAGMENTI ON RESUME")

    }

    override fun onPause() {
        super.onPause()
        //Log.e("HATA2"," GALERY FRAGMENTI ON PAUSE")

    }

    override fun onDestroy() {
        super.onDestroy()
        //Log.e("HATA2"," GALERY FRAGMENTI ON DESTROY")
    }

    //////////////////////////// EVENTBUS /////////////////////////////////
    @Subscribe
    internal fun onSecilenDosyaEvent(secilenDosya: EventBusDataEvents.GalerySecilenDosyaYolunuGonder) {
        secilenDosyaYolu=secilenDosya!!.dosyaYolu

        resimVeyaVideoGoster(secilenDosyaYolu!!)

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
