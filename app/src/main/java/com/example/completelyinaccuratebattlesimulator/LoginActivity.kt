package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Backendless.initApp(this, Constants.API_ID, Constants.API_KEY)

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
                    //startActivity()
                    finish()

                }

            })

        }

        button_login_createAccount.setOnClickListener {

        }
    }
}