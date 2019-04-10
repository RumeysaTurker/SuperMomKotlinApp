package com.rumeysaturker.supermomkotlinapp.game


import android.support.v7.app.AppCompatActivity

import android.widget.Button
import android.widget.RelativeLayout
import com.google.firebase.database.*
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ListView
import com.google.firebase.database.*
import com.hoanganhtuan95ptit.autoplayvideorecyclerview.AutoPlayVideoRecyclerView
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.CenterLayoutManager
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper

import com.rumeysaturker.supermomkotlinapp.utils.LinksAdapter
import com.rumeysaturker.supermomkotlinapp.utils.ProfilePostListRecyclerAdapter
import kotlinx.android.synthetic.main.activity_game.*



class GameActivity : AppCompatActivity(){
    lateinit var mRecyclerView: RecyclerView
    lateinit var mDatabase: DatabaseReference
    val ITEM_COUNT = 21
    var total_item = 0
    var last_visible_item = 0
    lateinit var adapter: MyAdapter
    var isLoading = false
    var isMaxData = false

    var last_node: String? = ""
    var last_key: String? = ""

    lateinit var tumGonderiler: ArrayList<Links>

    lateinit var mRef: DatabaseReference

    private val ACTIVITY_NO = 2
    val layoutManager = LinearLayoutManager(this)
    var myRecyclerView: AutoPlayVideoRecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setupNavigationView()
        val layoutManager = LinearLayoutManager(this)
       profileRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(profileRecyclerView.context, layoutManager.orientation)
        profileRecyclerView.addItemDecoration(dividerItemDecoration)
        adapter = MyAdapter(this)
        profileRecyclerView.adapter = adapter
        mRef = FirebaseDatabase.getInstance().reference
        tumGonderiler = ArrayList<Links>()
        init()

    }

    fun init() {
        btnSaglik.setOnClickListener {

            /*   var intent = Intent(this@GameActivity, ListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
           startActivity(intent)
            finish()*/
            setupToolBar()
            profileRecyclerView.visibility=View.VISIBLE
            tumLayout.visibility = View.GONE
            toolbarContainer.visibility = View.VISIBLE
            getLastKey("saglik")
            getLinks("saglik")
            profileRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    total_item = layoutManager.itemCount
                    last_visible_item = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("saglik")
                    isLoading = true
                }

            })

        }
        btnYemek.setOnClickListener {

            /*   var intent = Intent(this@GameActivity, ListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
           startActivity(intent)
            finish()*/
            setupToolBar()
            profileRecyclerView.visibility=View.VISIBLE
            tumLayout.visibility = View.GONE
            toolbarContainer.visibility = View.VISIBLE
            getLastKey("yemek")
            getLinks("yemek")
            profileRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    total_item = layoutManager.itemCount
                    last_visible_item = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("yemek")
                    isLoading = true
                }

            })

        }
        btnBakim.setOnClickListener {

            /*   var intent = Intent(this@GameActivity, ListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
           startActivity(intent)
            finish()*/
            setupToolBar()
            profileRecyclerView.visibility=View.VISIBLE
            tumLayout.visibility = View.GONE
            toolbarContainer.visibility = View.VISIBLE
            getLastKey("bakim")
            getLinks("bakim")
            profileRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    total_item = layoutManager.itemCount
                    last_visible_item = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("bakim")
                    isLoading = true
                }

            })

        }
        btnModa.setOnClickListener {

            /*   var intent = Intent(this@GameActivity, ListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
           startActivity(intent)
            finish()*/
            setupToolBar()
            profileRecyclerView.visibility=View.VISIBLE
            tumLayout.visibility = View.GONE
            toolbarContainer.visibility = View.VISIBLE
            getLastKey("moda")
            getLinks("moda")
            profileRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    total_item = layoutManager.itemCount
                    last_visible_item = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("moda")
                    isLoading = true
                }

            })

        }
        btnPsikoloji.setOnClickListener {

            /*   var intent = Intent(this@GameActivity, ListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
           startActivity(intent)
            finish()*/
            setupToolBar()
            profileRecyclerView.visibility=View.VISIBLE
            tumLayout.visibility = View.GONE
            toolbarContainer.visibility = View.VISIBLE
            getLastKey("psikoloji")
            getLinks("psikoloji")
            profileRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    total_item = layoutManager.itemCount
                    last_visible_item = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("psikoloji")
                    isLoading = true
                }

            })

        }
    }
    private fun getLinks() {
        if (!isMaxData) {
            val query: Query
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().reference
                        .child("links")
                        .orderByKey()
                        .limitToFirst(ITEM_COUNT)
            else
                query = FirebaseDatabase.getInstance().reference
                        .child("links")
                        .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_COUNT)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        val linkList = ArrayList<Links>()
                        for (snapshot in p0.children)
                            linkList.add(snapshot.getValue(Links::class.java)!!)

                        last_node = linkList[linkList.size - 1].id
                        if (!last_node.equals(last_key))
                            linkList.removeAt(linkList.size - 1)
                        else
                            last_node = "end"
                        adapter!!.addAll(linkList)
                        isLoading = false
                    } else
                        isLoading = false
                    isMaxData = true
                }

                override fun onCancelled(p0: DatabaseError) {
                }

            })
        }
    }
    private fun setupToolBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {

        tumLayout.visibility = View.VISIBLE
        toolbarContainer.visibility = View.GONE
        profileRecyclerView.visibility=View.GONE
    }
    fun setupNavigationView(){

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
    private fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        profileRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(profileRecyclerView.context, layoutManager.orientation)
        profileRecyclerView.addItemDecoration(dividerItemDecoration)
      //  adapter = MyAdapter(this)
        profileRecyclerView.adapter = LinksAdapter(this,tumGonderiler)

    }
    private fun getLinks(kategori: String) {
        if (!isMaxData) {
            val query: Query
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().reference
                        .child(kategori)
                        .orderByKey()
                        .limitToFirst(ITEM_COUNT)
            else
                query = FirebaseDatabase.getInstance().reference
                        .child(kategori)
                         .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_COUNT)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        val linkList = ArrayList<Links>()
                        for (snapshot in p0.children)
                            linkList.add(snapshot.getValue(Links::class.java)!!)

                        last_node = linkList[linkList.size - 1].id
                        if (!last_node.equals(last_key))
                            linkList.removeAt(linkList.size - 1)
                        else
                            last_node = "end"
                        adapter.addAll(linkList)
                        isLoading = false
                    } else
                        isLoading = false
                    isMaxData = true
                }

                override fun onCancelled(p0: DatabaseError) {
                }

            })
        }
    }

   /* override fun onBackPressed() {
        var intent = Intent(this@GameActivity, GameActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }*/
    private fun getLastKey(kategori: String) {
        val get_last_key = FirebaseDatabase.getInstance().getReference()
                .child(kategori)
                .limitToLast(1)
        get_last_key.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (linkSnapShot in p0.children)
                    last_key = linkSnapShot.key
            }

        })


    }
    private fun kullaniciPostlariniGetir() {





                mRef.child("links").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0!!.hasChildren()) {
                            //Log.e("HATA","COCUK VAR")
                            for (ds in p0!!.children) {

                             //   var eklenecekUserPosts = Links()
                                val item = ds.getValue(Links::class.java)
                                 ds.getValue(Links::class.java)!!.baslik
                                 ds.getValue(Links::class.java)!!.id
                                 ds.getValue(Links::class.java)!!.image
                                 ds.getValue(Links::class.java)!!.link

                                tumGonderiler.add(item!!)
                               // adapter!!.notifyDataSetChanged()

                            }


                        }

                        tumLayout.visibility = View.GONE
                        setupRecyclerView()
                    }


                })





    }
}


