package com.example.ayesha.echo


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class SongPlayingFragment : Fragment() {

    var myActivity:Activity?=null
    var mediaplayer:MediaPlayer?=null
    var startTimeText:TextView?=null
    var endTimeText:TextView?=null
    var playpauseImageButton:ImageButton?=null
    var previousImageButton:ImageButton?=null
    var nextImageButton:ImageButton?=null
    var loopImageButton:ImageButton?=null
    var seekBar:SeekBar?=null
    var songArtistView:TextView?=null
    var shuffleImageButton:ImageButton?=null
    var songTitleView:TextView?=null

    var currentPosition:Int=0
    var fetchSongs:ArrayList<Songs>?=null
    var CurrentSongHelper:CurrentSongHelper?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      var view=inflater!!.inflate(R.layout.fragment_song_playing, container, false)
        seekBar=view?.findViewById(R.id.seekBar)
        startTimeText=view?.findViewById(R.id.startTime)
        endTimeText=view?.findViewById(R.id.endTime)
        playpauseImageButton=view?.findViewById(R.id.playPauseButton)
        nextImageButton=view?.findViewById(R.id.nextButton)
        previousImageButton=view?.findViewById(R.id.previousButton)
        loopImageButton=view?.findViewById(R.id.loopButton)
        shuffleImageButton=view?.findViewById(R.id.shuffleButton)
        songArtistView=view?.findViewById(R.id.songArtist)
        songTitleView=view?.findViewById(R.id.songTitle)

        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity =context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity=activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        CurrentSongHelper= CurrentSongHelper()
        CurrentSongHelper?.isplaying=true
        CurrentSongHelper?.isloop=false
        CurrentSongHelper?.isShuffle=false

        var path:String?=null
        var songTitle:String?=null
        var songArtist:String?=null
        var songId:Long=0

        try {
            path=arguments.getString("path")
            songTitle=arguments.getString("songTitle")
            songArtist=arguments.getString("songArtist")
            songId=arguments.getInt("songID").toLong()
            currentPosition=arguments.getInt("songPosition")
            fetchSongs=arguments.getParcelableArrayList("songData")

            CurrentSongHelper?.songPath=path
            CurrentSongHelper?.songTitle=songTitle
            CurrentSongHelper?.songArtist=songArtist
            CurrentSongHelper?.songId=songId
            CurrentSongHelper?.currentPosition=currentPosition

            updateTextViews(CurrentSongHelper?.songTitle as String,CurrentSongHelper?.songArtist as String)

        }catch (e:Exception){
            e.printStackTrace()
        }
        mediaplayer?.start()

        if(CurrentSongHelper?.isplaying as Boolean){
            mediaplayer?.pause()
            CurrentSongHelper?.isplaying=false
            playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        }else{
            CurrentSongHelper?.isplaying=true
            playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)

        }
            mediaplayer?.setOnCompletionListener {

                onSongComplete()

            }

        clickHandler()
    }



    fun clickHandler() {
        shuffleImageButton?.setOnClickListener({
            if(CurrentSongHelper?.isShuffle as Boolean){
                shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                CurrentSongHelper?.isShuffle=false
            }else{
                CurrentSongHelper?.isShuffle=true
                CurrentSongHelper?.isloop=false
                shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
                loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
            }


        })
        nextImageButton?.setOnClickListener({
            CurrentSongHelper?.isplaying=true
            if (CurrentSongHelper?.isShuffle as Boolean){
                playNext("PlayNextLikeNormalShuffle")
            }else{
                playNext("PlayNextNormal")
            }

        })
        previousImageButton?.setOnClickListener({
            CurrentSongHelper?.isplaying=true
            if(CurrentSongHelper?.isloop as Boolean) {
                loopImageButton?.setBackgroundResource(R.drawable.loop_icon)
            }
            playPrevious()
        })
        loopImageButton?.setOnClickListener({
                if(CurrentSongHelper?.isloop as Boolean){
                    loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
                    shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                }
        })
        playpauseImageButton?.setOnClickListener({
            if(mediaplayer?.isPlaying as Boolean){
                mediaplayer?.pause()
                playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }else{
                mediaplayer?.start()
                playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })

    }

    private fun playPrevious() {
        currentPosition
    }

    fun playNext(check:String) {
        if (check.equals("PlayNextNormal", true)) {
            currentPosition = currentPosition + 1
        } else if (check.equals("PlayNextLikeNormalShuffle", true)) {
            var randomObject = Random()
            var randomPosition = randomObject.nextInt(fetchSongs?.size?.plus(1) as Int)
            currentPosition = randomPosition


        }
        if (currentPosition == fetchSongs?.size) {
            currentPosition = 0
        }
        var nextSong = fetchSongs?.get(currentPosition)
        CurrentSongHelper?.currentPosition = currentPosition
        CurrentSongHelper?.songTitle = nextSong?.songTitle
        CurrentSongHelper?.songPath = nextSong?.songData
        CurrentSongHelper?.songId = nextSong?.songID as Long


        updateTextViews(CurrentSongHelper?.songTitle as String,CurrentSongHelper?.songArtist as String)
        mediaplayer?.reset()
        try {
            mediaplayer?.setDataSource(myActivity, Uri.parse(CurrentSongHelper?.songPath))
            mediaplayer?.prepare()
            mediaplayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onSongComplete(){
        if(CurrentSongHelper?.isShuffle as Boolean){
                playNext("PlayNextLikeNormalShuffle")
                CurrentSongHelper?.isplaying=true
        }else{
            if(CurrentSongHelper?.isloop as Boolean){

                CurrentSongHelper?.isplaying = true

                var nextSong = fetchSongs?.get(currentPosition)
                CurrentSongHelper?.songTitle = nextSong?.songTitle
                CurrentSongHelper?.songPath = nextSong?.songData
                CurrentSongHelper?.songId = nextSong?.songID as Long
                CurrentSongHelper?.currentPosition = currentPosition
                mediaplayer?.reset()

                try {
                    mediaplayer?.setDataSource(myActivity, Uri.parse(CurrentSongHelper?.songPath))
                    mediaplayer?.prepare()
                    mediaplayer?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }else{
                playNext("PlayNextNormal")
                CurrentSongHelper?.isplaying=true
            }
        }
    }

    fun updateTextViews(songTitle:String ,songArtist : String ){
        songTitleView?.setText(songTitle)
        songArtistView?.setText(songArtist)
    }

    }// Required empty public constructor

