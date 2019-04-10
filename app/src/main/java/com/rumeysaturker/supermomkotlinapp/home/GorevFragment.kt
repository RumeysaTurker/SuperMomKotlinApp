package com.rumeysaturker.supermomkotlinapp.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.models.AddTodo
import com.rumeysaturker.supermomkotlinapp.models.Gorev
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.fragment_gorev.*
import kotlinx.android.synthetic.main.fragment_gorev.view.*
import kotlinx.android.synthetic.main.gorev_item.*

class GorevFragment : Fragment() {
    lateinit var fragmentView: View
    private val ACTIVITY_NO = 3
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var gorevAtayanUserId: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.activity_gorev, container, false)
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        gorevAtayanUserId = mAuth.currentUser!!.uid.toString()


        setupNavigationView()
        fragmentView.addButton.setOnClickListener {
        /*    if(!TextUtils.isEmpty(gorevText.toString())){
                var gorevAlan = HashMap<String, Any>()
                gorevAlan.put("gorev", gorevText)
                gorevAlan.put("yapıldı", checkBox.isChecked)//false ya da true donduruyor
                gorevAlan.put("time", ServerValue.TIMESTAMP)
                gorevAlan.put("user_id", gorevAtayanUserId)
                var yeniGorevKey = mRef.child("gorevler").child(gorevAtayanUserId).push().key//push la yeni ID oluşturuluyor

                mRef.child("mesajlar").child(gorevAtayanUserId).child(yeniGorevKey!!).setValue(gorevAlan)

                gorevText.setText("")
            }*/

        }
        return fragmentView


    }


    fun setupNavigationView() {

        var fragmentBottomNavView = fragmentView.bottomNavigationView

        BottomNavigationViewHelper.setupBottomNavigationView(fragmentBottomNavView)
        BottomNavigationViewHelper.setupNavigation(activity!!, fragmentBottomNavView)
        var menu = fragmentBottomNavView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }
}