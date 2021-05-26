package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val REQUEST_LOGIN_INFO = "login"
        val TAG = "LOGINACTIVITY"
        val USER = "user"
    }

    var musicPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Backendless.initApp(this, Constants.API_ID, Constants.API_KEY)

        musicPlayer = MediaPlayer.create(this, R.raw.colosseum)
        musicPlayer!!.isLooping = true
        musicPlayer!!.setVolume(100F, 100F)
        musicPlayer!!.start()

        button_login_login.setOnClickListener {

            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()

            Backendless.UserService.login(username, password, object: AsyncCallback<BackendlessUser> {

                override fun handleFault(fault: BackendlessFault?) {
                    Toast.makeText(this@LoginActivity, "Something went wrong, check the logs.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "handleFault: " + fault?.message)
                }

                override fun handleResponse(response: BackendlessUser?) {
                    Toast.makeText(this@LoginActivity, "${response?.userId} has logged in.", Toast.LENGTH_SHORT).show()

                    Log.d(TAG, response?.getProperty("createdCharacter").toString())

                    if (response?.getProperty("createdCharacter") == true){
                        //val loginIntent = Intent(this@LoginActivity, HomePage::class.java)
                        val loginIntent = Intent(this@LoginActivity, HomePage::class.java)
                        startActivity(loginIntent)
                        //to close the login screen so it's not there when user clicks bacc
                        finish()
                    }
                    else{
                        val loginIntent = Intent(this@LoginActivity, CreationActivity::class.java)
                        startActivity(loginIntent)
                        finish()
                    }


                }

            })

        }

        button_login_createAccount.setOnClickListener {

            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()

            val registrationIntent = Intent(this, RegistrationActivity::class.java).apply {

                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
            }

            startActivityForResult(registrationIntent, 1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //accountMade = data?.getBooleanExtra(RegistrationActivity.CREATION_SUCCESS, false)!!
        if (requestCode == 1) {
            val createdUsername = data?.getStringExtra(RegistrationActivity.CREATED_USERNAME)
            val createdPassword = data?.getStringExtra(RegistrationActivity.CREATED_PASSWORD)
            editText_login_username.setText(createdUsername)
            editText_login_password.setText(createdPassword)
        }
    }

    override fun onPause(){
        super.onPause()
        musicPlayer!!.pause()
    }

    override fun onResume() {
        super.onResume()
        musicPlayer!!.start()
    }
}