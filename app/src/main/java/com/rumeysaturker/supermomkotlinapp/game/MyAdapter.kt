package com.rumeysaturker.supermomkotlinapp.game

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.list_layout.view.*
import java.security.AccessControlContext
import com.squareup.picasso.Picasso






class MyAdapter (internal var context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    internal  var linkList:MutableList<Links>
    val lastItemId:String?
    get()=linkList[linkList.size-1].id
    fun addAll(newLinks:List<Links>){
        val init=linkList.size
        linkList.addAll(newLinks)
        notifyItemRangeChanged(init,newLinks.size)
    }
fun removeLastItem()
{
    linkList.removeAt(linkList.size-1)
}
    init{
        this.linkList=ArrayList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
val itemView=LayoutInflater.from(context).inflate(com.rumeysaturker.supermomkotlinapp.R.layout.list_layout,p0,false)
    return MyViewHolder(itemView)}

    override fun getItemCount(): Int {
        return linkList.size
        }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.txt_name.text=linkList[p1].baslik
        Picasso.get() .load(linkList[p1].image) .resize(50, 50) .centerCrop() .into(p0.txt_image)


    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        internal  var txt_name:TextView
          lateinit var txt_image:ImageView
        init{
            txt_name=itemView.findViewById<TextView>(R.id.linkName)
            txt_image=itemView.findViewById<ImageView>(R.id.linksImageView)

        }
    }

}