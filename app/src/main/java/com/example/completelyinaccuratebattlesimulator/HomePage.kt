package com.example.completelyinaccuratebattlesimulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.media.AudioManager
import android.media.MediaPlayer

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kyantaPlayer = MediaPlayer.create(this, R.raw.kyanta)

        kyantaPlayer.start()
    }

}