package com.example.ayesha.echo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ayesha.echo.*
import com.example.ayesha.echo.activities.MainActivity

/**
 * Created by Ayesha on 22-03-2018.
 */

class NavigationDrawerAdapter(_contentList:ArrayList<String>,_getImages:IntArray,_context:Context)
    : RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>() {
    var contentList:ArrayList<String>?=null
    var getImages:IntArray?=null
    var mContext:Context?=null

    init{
        this.contentList =_contentList
        this.getImages = _getImages
        this.mContext=_context
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_navigationdrawer,parent,false)
        val returnThis =   NavViewHolder(itemView)
        return returnThis
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: NavViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            holder?.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
            holder?.text_GET?.setText(contentList?.get(position))
            holder?.contentHolder?.setOnClickListener({
                if(position==0){
                    val mainScreenFragment=MainScreenFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment,mainScreenFragment)
                            .commit()
                }
                else if(position == 1){
                    val favoriteFragment=FavoriteFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment,favoriteFragment)
                            .commit()
                }
                else if(position == 2){
                    val settingsFragment=SettingsFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment,settingsFragment)
                            .commit()
                }
                else {
                    val aboutUsFragment=AboutUsFragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.details_fragment,aboutUsFragment)
                            .commit()
                }
                MainActivity.Satisfied.drawerLayout?.closeDrawers()
            })

    }

    class NavViewHolder(itemView : View?) :RecyclerView.ViewHolder(itemView) {
            var icon_GET:ImageView?=null
            var text_GET:TextView?=null
            var contentHolder : RelativeLayout?=null

        init {
            icon_GET=itemView?.findViewById(R.id.icon_navdrawer)
            text_GET=itemView?.findViewById(R.id.text_navdrawer)
            contentHolder=itemView?.findViewById(R.id.navdrawer_item_content_holder)
        }
    }

}