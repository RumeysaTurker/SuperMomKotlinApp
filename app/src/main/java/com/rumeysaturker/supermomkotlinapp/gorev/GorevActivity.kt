package com.rumeysaturker.supermomkotlinapp.gorev

import android.arch.core.util.Function
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.models.*
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.rumeysaturker.supermomkotlinapp.utils.MesajRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_gorev.*
import kotlinx.android.synthetic.main.activity_gorev.view.*
import kotlinx.android.synthetic.main.gorev_item.view.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.selector

class GorevActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var gorevAtayanUserId: String
    // var tumGorevler: ArrayList<Gorevler> = ArrayList<Gorevler>()
    lateinit var mUser: FirebaseUser
    lateinit var myRecyclerViewAdapter: MesajRecyclerViewAdapter
    lateinit var myRecyclerView: RecyclerView
    lateinit var myAdapter: FirebaseRecyclerAdapter<Gorevler, GorevViewHolder>
    var gorevAtananID: String? = null
    var gorevId = FirebaseDatabase.getInstance().reference.child("gorevler").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).push().key//push la yeni ID oluşturuluyor
    var mDatabase = FirebaseDatabase.getInstance().reference
    private val ACTIVITY_NO = 3
    private lateinit var store: GorevStore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gorev)
        setupNavigationView()
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        mUser = mAuth.currentUser!!
        setupGorevlerRecyclerview()
        addButton.setOnClickListener {

            var yeniGorev = hashMapOf<String, Any>("user_id" to mUser.uid,
                    "gorev" to gorevText.text.toString(), "yapildi" to "false", "gorev_id" to gorevId.toString())

            mDatabase.child("gorevler").child(mUser.uid!!).child(gorevId!!).push().setValue(yeniGorev)


            gorevText.setText("")

            listView.smoothScrollToPosition(listView.adapter!!.itemCount)
            // var myLinearLayoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            //myLinearLayoutManager.stackFromEnd=true


            /*   var gorev_id=""
               var yuklenme_tarih = null
               var gorev = gorevText.text.toString()
               var yapildi=false
               gorevAtayanUserId = mAuth.currentUser!!.uid.toString()*/

            //oturum açan kullanıcının verilerini database'e kaydet
            // var kaydedilecekKullanici = Gorevler(user_id, gorevId, gorev, yapildi)
            /*    mRef.child("gorevler").child(user_id).child(gorevId!!).setValue(kaydedilecekKullanici).addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        if (p0!!.isSuccessful) {
                            Toast.makeText(this@GorevActivity, "Kullanıcı kaydedildi", Toast.LENGTH_SHORT).show()

                        }}})*/

            //  store.dispatch(AddTodo(gorevText.text.toString()))
            // gorevText.setText("")

        }


        //gorevText.text = null
        /*    if (!TextUtils.isEmpty(gorevText.toString())) {


                gorevAlan.put("gorev", gorevText.text.toString())
                // gorevAlan.put("yapıldı", checkBox.isChecked)//false ya da true donduruyor
                gorevAlan.put("yuklenme_tarih", ServerValue.TIMESTAMP)
                gorevAlan.put("gorev_id", gorevId.toString())
                gorevAlan.put("yapildi", "false")

                mRef.child("gorevler").child(gorevAtayanUserId).push().setValue(gorevAlan)//push la yeni ID oluşturuluyor

                //mRef.child("gorevler").child(gorevAtayanUserId).child(yeniGorevKey!!).setValue(gorevAlan)
                store.dispatch(AddTodo(gorevText.text.toString()))
                gorevText.setText("")
            }*/



        fab.setOnClickListener { openDialog() }

        /* listView.adapter = GorevAdapter(this, listOf())
         listView.setOnItemClickListener({ _, _, _, id ->
             store.dispatch(ToggleTodo(id))
         })

         listView.setOnItemLongClickListener { _, _, _, id ->
             store.dispatch(RemoveTodo(id))
             true
         }*/
    }

    private fun openDialog() {
        val options = resources.getStringArray(R.array.filter_options).asList()
        selector(getString(R.string.filter_title), options, { _, i ->
            val visible = when (i) {
                1 -> Visibility.Yapılacaklar()
                2 -> Visibility.Tamamlanmış()
                else -> Visibility.Hepsi()
            }
            store.dispatch(SetVisibility(visible))
        })
    }

    private val mapStateToProps = Function<GorevModel, GorevModel> {
        val keep: (Gorev) -> Boolean = when (it.visibility) {

            is Visibility.Hepsi -> { _ -> true }
            is Visibility.Yapılacaklar -> { t: Gorev -> !t.status }
            is Visibility.Tamamlanmış -> { t: Gorev -> t.status }
        }


        return@Function it.apply { todos = it.todos.filter { keep(it) } }
    }


    private fun mesajlariGetir() {


        mRef.child("gorevler").child(mUser.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                //  tumGorevler.clear()
                if (p0!!.getValue() != null) {
                    for (gorev in p0!!.children) {
                        var okunanGorev = gorev.getValue(Gorevler::class.java)
                        //tumGorevler.add(okunanGorev!!)
                    }
                    setupGorevlerRecyclerview()
                }
            }
        })

        /*     childEventListener= mRef.child("gorevler").child(mUser.uid).child(gorevId!!).limitToLast(SAYFA_BASI_GONDERI_SAYISI).addChildEventListener(object : ChildEventListener{
                 override fun onCancelled(p0: DatabaseError) {
                 }

                 override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                 }

                 override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                 }

                 override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                     var okunanMesaj=p0!!.getValue(Gorevler::class.java)
                     tumGorevler.add(okunanMesaj!!)

                     if(mesajPos==0){

                         ilkGetirilenMesajID=p0!!.key!!
                         zatenListedeOlanMesajID=p0!!.key!!

                     }
                     mesajPos++





                     myRecyclerViewAdapter.notifyItemInserted(tumGorevler.size-1)
                     myRecyclerView.scrollToPosition(tumGorevler.size-1)

                     //Log.e("KONTROL","İLK OKUNAN MESAJ ID :"+ilkGetirilenMesajID)


                 }

                 override fun onChildRemoved(p0: DataSnapshot) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }



             })*/


    }


    private fun setupGorevlerRecyclerview() {
        mRef = FirebaseDatabase.getInstance().reference.child("gorevler").child(mUser.uid!!).child(gorevId!!)


        val options = FirebaseRecyclerOptions.Builder<Gorevler>()
                .setQuery(mRef, Gorevler::class.java)
                .build()

        myAdapter = object : FirebaseRecyclerAdapter<Gorevler, GorevViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GorevViewHolder {


                /*
                var layoutService=LayoutInflater.from(parent!!.context)
                layoutService.inflate()
                */
                var gorevViewHolder = LayoutInflater.from(parent.context).inflate(R.layout.gorev_item, parent, false)

                return GorevViewHolder(gorevViewHolder, gorevId!!)
            }

            override fun onBindViewHolder(holder: GorevActivity.GorevViewHolder, position: Int, model: Gorevler) {
                holder.setData(model)


                //Log.e("HATA","YORUM YAPILACAK FOTO:"+yorumYapilacakGonderininID)
                //Log.e("HATA","yorum ıd:"+getRef(0).key)
                //ilk yorum foto paylasırken yapılan acıklama ise begen iconu kaldırılır
                // if(position==0 && (mAuth.currentUser!!.uid.toString()!!.equals(getRef(0).key))){
                //holder.yorumBegen.visibility=View.INVISIBLE
                // }


            }

        }

        listView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listView.adapter = myAdapter


        // mesajlariGetir()


    }

    class GorevViewHolder(itemView: View?, gorevId: String?) : RecyclerView.ViewHolder(itemView!!) {

        var tumCommentLayoutu = itemView as ConstraintLayout
        var gorevAdi = tumCommentLayoutu.gorevler
        var gorev = tumCommentLayoutu.gorevText
        var gorevEkle = tumCommentLayoutu.addButton
        var filtrele = tumCommentLayoutu.fab
        var checkBox = tumCommentLayoutu.checkBox
        var gorevId = gorevId


        fun setData(oanOlusturulanGorevler: Gorevler) {
            if (gorevAdi != null)
//yorumSure.setText(TimeAgo.getTimeAgoForComments(oanOlusturulanYorum!!.yorum_tarih!!))
                gorevAdi!!.setText(oanOlusturulanGorevler.gorev!!)
            checkBox.setOnClickListener {
                if (checkBox.isChecked == true)
                    oanOlusturulanGorevler.yapildi = "true"
            }


            gorevBilgileriniGetir(oanOlusturulanGorevler.user_id!!, oanOlusturulanGorevler.gorev!!, checkBox!!, gorevId)


        }

        private fun gorevBilgileriniGetir(user_id: String?, gorev: String?, checkBox: CheckBox?, gorevId: String?) {
            var mRef = FirebaseDatabase.getInstance().reference
            var tumGorevler: ArrayList<Gorevler> = ArrayList<Gorevler>()
            mRef.child("gorevler")!!.child(user_id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0!!.hasChildren()) {
                        //  var okunangorevBilgileri = p0!!.getValue(Gorevler::class.java)
                        //EventBus.getDefault().postSticky(EventBusDataEvents.GorevBilgileriniGonder(okunangorevBilgileri))

                        //Log.e("HATA","COCUK VAR")
                        for (ds in p0!!.children) {

                            var eklenecekUserPosts = Gorevler()
                            eklenecekUserPosts.yapildi = ds!!.getValue(Gorevler::class.java)!!.yapildi
                            eklenecekUserPosts.gorev = gorev
                            eklenecekUserPosts.gorev_id = ds!!.getValue(Gorevler::class.java)!!.gorev_id
                            eklenecekUserPosts.user_id = ds!!.getValue(Gorevler::class.java)!!.user_id
                            //eklenecekUserPosts.yuklenme_tarih= ds.getValue(Gorevler::class.java)!!.yuklenme_tarih

                            //  tumGorevler.add(eklenecekUserPosts)
                            //var gorevId = FirebaseDatabase.getInstance().reference.child("gorevler").child(user_id!!).push().key
                            checkBox!!.setOnClickListener() {
                                if (checkBox.isChecked == true) {
                                    eklenecekUserPosts.yapildi = "true"
                                    //  eklenecekUserPosts.gorev = gorev
                                    //eklenecekUserPosts.gorev_id = ds!!.getValue(Gorevler::class.java)!!.gorev_id
                                    //eklenecekUserPosts.user_id = ds!!.getValue(Gorevler::class.java)!!.user_id
                                    FirebaseDatabase.getInstance().reference.child("gorevler").child(user_id!!).child(gorevId!!).push().setValue(eklenecekUserPosts)


                                } else {
                                    eklenecekUserPosts.yapildi = "false"
                                    //eklenecekUserPosts.gorev = gorev
                                    //eklenecekUserPosts.gorev_id = ds!!.getValue(Gorevler::class.java)!!.gorev_id
                                    //eklenecekUserPosts.user_id = ds!!.getValue(Gorevler::class.java)!!.user_id

                                    //    FirebaseDatabase.getInstance().reference.child("gorevler").child(user_id!!).child(FirebaseDatabase.getInstance().reference.child("gorevler").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).push().key!!).push().setValue(eklenecekUserPosts)

                                }

                            }
                            //mRef.child("gorevler").child(gorevAtayanUserId).child("gorev_id").child("yapildi").setValue(eklenecekUserPosts)

                            //  }
                        }
                    }


                    //Log.e("HATA", "TÜM TAKIP ETTİKLERİM:" + tumTakipEttiklerim.toString())
                    // kullaniciPostlariniGetir()

                }
            })
        }
    }

    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    /////////////////////////// EVENTBUS /////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onGorevYapilacakGonderi(gonderi: EventBusDataEvents.GorevIDsiniGonder) {
        gorevAtananID = gonderi!!.gorevID!!

    }

    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }
}
