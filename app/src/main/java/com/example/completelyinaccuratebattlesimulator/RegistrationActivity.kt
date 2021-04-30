package com.example.completelyinaccuratebattlesimulator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    companion object{
        val CREATED_USERNAME = "createdName"
        val CREATED_PASSWORD = "createdPassword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD)

        editText_registration_username.setText(username)
        editText_registration_password.setText(password)

        button_registratoin_create.setOnClickListener {
            if (editText_registration_username.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
            else if (editText_registration_email.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
            else if (editText_registration_password.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else if (editText_registration_confirmPassword.text.isNullOrEmpty()){
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
            }
            else if (editText_registration_password.text.toString().compareTo((editText_registration_confirmPassword.text.toString())) == 0){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else{
                registerUser()
            }
        }

        button_registration_return.setOnClickListener {
            val newUsername = editText_registration_username.text.toString()
            val newPassword = editText_registration_password.text.toString()
            val registrationReturnIntent = Intent(this, LoginActivity::class.java).apply {
                putExtra(CREATED_USERNAME, newUsername)
                putExtra(CREATED_PASSWORD, newPassword)
            }
            setResult(Activity.RESULT_OK, registrationReturnIntent)
            finish()
        }
    }

    private fun registerUser() {
        val user = BackendlessUser()
        user.setProperty("email", editText_registration_email.text.toString())
        user.setProperty("username", editText_registration_username.text.toString())
        user.setProperty("password", editText_registration_password.text.toString())

        Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(registeredUser: BackendlessUser?) {
                val intent = Intent().apply {
                    putExtra(CREATED_USERNAME, editText_registration_username.text.toString())
                    putExtra(CREATED_PASSWORD, editText_registration_password.text.toString())
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(this@RegistrationActivity, fault.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}