package com.rumeysaturker.supermomkotlinapp.search

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.algolia.instantsearch.helpers.InstantSearch
import com.algolia.instantsearch.helpers.Searcher
import com.algolia.instantsearch.utils.ItemClickSupport
import com.google.firebase.auth.FirebaseAuth
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.home.ChatActivity
import com.rumeysaturker.supermomkotlinapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_algolia_search.*

class AlgoliaSearchActivity : AppCompatActivity() {
    private val ALGOLIA_APP_ID="M2GIY05EH5"
    private val ALGOLIA_SEARCH_API_KEY="4e5241cdc367a72c6e4eea7f79a0b5f5"
    private val ALGOLIA_INDEX_NAME="KotlinAnnelerYarisiyor"
    lateinit var searcher: Searcher
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algolia_search)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()

        setupAlgoliaSearch()

    }

    private fun setupAlgoliaSearch() {
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME)
        val helper = InstantSearch(this, searcher)
        helper.search()

        imgBack.setOnClickListener {
            onBackPressed()
        }

        listeAramaSonuclari.setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClick(recyclerView: RecyclerView?, position: Int, v: View?) {
                var secilenUserID = listeAramaSonuclari.get(position).getString("user_id")
                //Toast.makeText(this@AlgoliaSearchActivity,"Secilen:"+secilenUserID,Toast.LENGTH_SHORT).show()

                if (!secilenUserID.equals(mAuth.currentUser!!.uid.toString())) {

                    var intent= Intent(this@AlgoliaSearchActivity, ChatActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.putExtra("secilenUserID",secilenUserID)
                    startActivity(intent)

                }
            }

        })
    }


    override fun onDestroy() {
        searcher.destroy()
        super.onDestroy()
    }

    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {
                    //Log.e("HATA", "Kullanıcı oturum açmamış, HomeActivitydesn")
                    var intent = Intent(this@AlgoliaSearchActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {


                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        //Log.e("HATA", "AlgoliaSearchdesin")
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }
}