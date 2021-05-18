package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View

class HomePage : AppCompatActivity() {

    var kyantaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kyantaPlayer = MediaPlayer.create(this, R.raw.kyanta)
        kyantaPlayer!!.isLooping = true
        kyantaPlayer!!.start()

        testButton.setOnClickListener {
            val mainIntent = Intent(this@HomePage, StrengtheningActivity::class.java)
            startActivity(mainIntent)
            //to close the login screen so it's not there when user clicks bacc
            finish()
        }
        testButton2.setOnClickListener {
            val mainIntent = Intent(this@HomePage, BattlePrepActivity::class.java)
            startActivity(mainIntent)
            //to close the login screen so it's not there when user clicks bacc
            finish()
        }
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