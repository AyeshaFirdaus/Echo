package com.example.ayesha.echo

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Ayesha on 23-03-2018.
 */
class Songs(var songID: Long,var songTitle : String ,var artist : String,var songData:String,var dateAdded : Long):Parcelable{
    override fun writeToParcel(dest: Parcel?, flags: Int) {
           }

    override fun describeContents(): Int {
       return 0
    }


}