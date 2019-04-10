package com.rumeysaturker.supermomkotlinapp.game

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.activity_list.*
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener








class ListActivity : AppCompatActivity(){
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        //  setSupportActionBar(toolbar)
        //toolbar.setTitle("Anneler Yarisiyor")

      /*  val dataSnapshot:DataSnapshot?=null
        val contactSnapshot = dataSnapshot!!.child("links")
        val contactChildren = contactSnapshot.getChildren()
        var link: ArrayList<Links>?=null
        for (contact in contactChildren) {
            val c = contact.getValue<Links>(Links::class.java!!)

          link!!.add(c!!)
        }*/
        getLastKey()
        setupToolBar()
        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(listView.context, layoutManager.orientation)
        listView.addItemDecoration(dividerItemDecoration)
        adapter = MyAdapter(this)
        listView.adapter = adapter


        getLinks("saglik")
     /*  mDatabase.addValueEventListener(object : ValueEventListener {
           override fun onCancelled(p0: DatabaseError) {
               TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
           }

           override fun onDataChange(dataSnapshot: DataSnapshot) {
                val td = dataSnapshot.value as HashMap<String, Any>?

                val values = td!!.values

                //notifyDataSetChanged();
            }


        })*/
        listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                total_item = layoutManager.itemCount
                last_visible_item = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && total_item <= last_visible_item + ITEM_COUNT)
                        getLinks("saglik")

                   // getLinks()

                isLoading = true
            }

        })

    }

   /* override fun onClick(v: View) {
        when (v.id) {
            R.id.btnSaglik -> {
                val intent = Intent(applicationContext, GameActivity::class.java)
                startActivity(intent)
                getLinks()
            }
            else -> {
                // else condition
            }
        }
    }*/
    private fun getLinks(kategori: String) {
        if (!isMaxData) {
            val query: Query
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().reference
                       .child("links")
                        .child(kategori)
                       // .orderByKey()
                        .limitToFirst(ITEM_COUNT)
            else
                query = FirebaseDatabase.getInstance().reference
                        .child("links")
                        .child(kategori)
                       // .orderByKey()
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
    private fun setupToolBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        var intent = Intent(this@ListActivity, GameActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
    private fun getLastKey() {
        val get_last_key = FirebaseDatabase.getInstance().getReference()
                .child("links")
                .child("saglik")
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


}
