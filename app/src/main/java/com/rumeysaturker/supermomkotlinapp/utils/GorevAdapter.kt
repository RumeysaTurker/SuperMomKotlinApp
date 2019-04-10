package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.models.Gorev
import kotlinx.android.synthetic.main.gorev_item.view.*
import com.rumeysaturker.supermomkotlinapp.models.Gorevler
import com.rumeysaturker.supermomkotlinapp.models.Posts
import kotlinx.android.synthetic.main.activity_gorev.view.*
import kotlinx.android.synthetic.main.gorev_item.*
import org.greenrobot.eventbus.EventBus

class GorevAdapter(context: Context, val todos: List<Gorev>):
        ArrayAdapter<Gorev>(context, 0, todos) {
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var gorevAtayanUserId: String

   // lateinit var gorevId: String
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.gorev_item, parent, false)

        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference

       gorevAtayanUserId = mAuth.currentUser!!.uid.toString()
       var gorevId=mRef.child("gorevler").child(gorevAtayanUserId).child("gorev_id").push().key//push la yeni ID oluşturuluyor


       // var gorevId=mRef.child("gorevler").child(gorevAtayanUserId).child("gorev_id").key//push la yeni ID oluşturuluyor

          //  view.checkBox.setOnClickListener{

           // }
     //   gorevId=mRef.child("gorevler").child(gorevAtayanUserId).child("gorev").key//push la yeni ID oluşturuluyor

        //var gorevAlan = HashMap<String, Any>()
        view.gorevler.text = todos[position].text
        view.checkBox.isChecked = todos[position].status



            //gorevAlan.put("yapıldı", view.checkBox.isChecked)//false ya da true donduruyor

           // gorevId.push().setValue(gorevAlan)//push la yeni ID oluşturuluyor



        return view
    }

    override fun getItemId(position: Int): Long=todos[position].id

    }
