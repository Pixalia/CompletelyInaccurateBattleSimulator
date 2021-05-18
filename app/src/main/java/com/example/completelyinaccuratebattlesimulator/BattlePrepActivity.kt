package com.example.completelyinaccuratebattlesimulator

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle_prep.*

class BattlePrepActivity : AppCompatActivity() {

    companion object{
        val TAG = "BattlePrepActivity"
    }

    val userId = Backendless.UserService.CurrentUser().userId
    var kyantaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_prep)

        kyantaPlayer = MediaPlayer.create(this, R.raw.kyanta)
        kyantaPlayer!!.isLooping = true
        kyantaPlayer!!.start()


        generatePlayer()
        generateFoe()

        }

    fun generatePlayer(){
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        generateFoe()

        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null) {
                    val item = response.elementAt(0)
                    textView_prep_yourStats.text = item!!.name + "'s Stats:"
                    textView_prep_str.text = "Str: " + item!!.str
                    textView_prep_dex.text = "Dex: " + item!!.dex
                    textView_prep_int.text = "Int: " + item!!.int.toString()
                    textView_prep_luk.text = "Luk: " + item!!.luk.toString()

                    if (item!!.str > item!!.dex && item!!.str > item!!.int && item!!.str > item.luk){
                        imageView_prep_player.setImageResource(R.drawable.redheavy)
                    }
                    else if (item!!.dex > item!!.str && item!!.dex > item!!.int && item!!.dex > item.luk){
                        imageView_prep_player.setImageResource(R.drawable.redscout)
                    }
                    else if (item!!.int > item!!.dex && item!!.int > item!!.str && item!!.int > item.luk){
                        imageView_prep_player.setImageResource(R.drawable.redengi)
                    }
                    else if (item!!.luk > item!!.dex && item!!.luk > item!!.str && item!!.luk > item.int){
                        imageView_prep_player.setImageResource(R.drawable.redspy)
                    }
                    else {
                        imageView_prep_player.setImageResource(R.drawable.redpyro)
                    }
                }
            }

        })
    }

    fun generateFoe(){

        val whereClause = "str >= 0"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        Backendless.Data.of(Character::class.java).find(queryBuilder, object: AsyncCallback<List<Character?>?>{
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){

                    var random = (0 until response.size).random()

                    while (response.elementAt(random)!!.ownerId == userId){
                        random = (0 until response.size).random()
                    }

                    val item = response.elementAt(random)
                    textView_prep_foeStats.text = item!!.name + "'s Stats:"
                    textView_prep_foeStr.text = "Str: " + item!!.str.toString()
                    textView_prep_foeDex.text = "Dex: " + item!!.dex.toString()
                    textView_prep_foeInt.text = "Int: " + item!!.int.toString()
                    textView_prep_foeLuk.text = "Luk: " + item!!.luk.toString()

                    if (item!!.str > item!!.dex && item!!.str > item!!.int && item!!.str > item.luk){
                        imageView_prep_foe.setImageResource(R.drawable.bluheavy)
                    }
                    else if (item!!.dex > item!!.str && item!!.dex > item!!.int && item!!.dex > item.luk){
                        imageView_prep_foe.setImageResource(R.drawable.bluscout)
                    }
                    else if (item!!.int > item!!.dex && item!!.int > item!!.str && item!!.int > item.luk){
                        imageView_prep_foe.setImageResource(R.drawable.blueengi)
                    }
                    else if (item!!.luk > item!!.dex && item!!.luk > item!!.str && item!!.luk > item.int){
                        imageView_prep_foe.setImageResource(R.drawable.bluspy)
                    }
                    else {
                        imageView_prep_foe.setImageResource(R.drawable.blupyro)
                    }

                }
            }

        })
    }

    override fun onPause(){
        super.onPause()
        kyantaPlayer!!.pause()
    }

    override fun onResume() {
        super.onResume()
        kyantaPlayer!!.start()
    }

}


