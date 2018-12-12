package com.rumeysaturker.supermomkotlinapp.Profile


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.firebase.auth.FirebaseAuth



class SignOutFragment : DialogFragment(){


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alert=AlertDialog.Builder(this!!.activity!!)
                .setTitle("Çıkış Yap")
                .setMessage("Çıkış yapmak istediğinizden emin misiniz?")
                .setPositiveButton("Çıkış Yap", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        FirebaseAuth.getInstance().signOut()
                        activity!!.finish()
                        Log.e("HATA","ÇıkışYap dialogu gösterildi,SignOutFragmenttesin")

                    }
                })
                .setNegativeButton("İptal",object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                         dismiss()//pencereyi kapatmak için
                    }

                }).create()
        return alert
    }


}
