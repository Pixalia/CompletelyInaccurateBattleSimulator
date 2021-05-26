package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_main.*


class HomePage : AppCompatActivity() {

    companion object {
        val TAG = "HOMEPAGE"
    }

    var orchidLove : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orchidLove = MediaPlayer.create(this, R.raw.maplestory)
        orchidLove!!.isLooping = true
        orchidLove!!.setVolume(100F, 100F)
        orchidLove!!.start()


        testButton2.setOnClickListener {
            val mainIntent = Intent(this@HomePage, BattlePrepActivity::class.java)
            startActivity(mainIntent)
            //to close the login screen so it's not there when user clicks bacc
            finish()
        }
        button.setOnClickListener {
            val mainIntent = Intent(this@HomePage, LeaderboardActivity::class.java)
            startActivity(mainIntent)
        }
        button2.setOnClickListener {
            Backendless.UserService.logout(object : AsyncCallback<Void?> {
                override fun handleResponse(response: Void?) {
                    Toast.makeText(this@HomePage, "User has logged out,", Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this@HomePage, LoginActivity::class.java)
                    startActivity(mainIntent)
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(HomePage.TAG, "handleFault: " + fault?.message)
                }
            })
        }
    }

    override fun onPause(){
        super.onPause()
        orchidLove!!.pause()
    }

    override fun onResume() {
        super.onResume()
        orchidLove!!.start()
    }



}