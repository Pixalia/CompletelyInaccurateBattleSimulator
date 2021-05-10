package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.UserService
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_creation.*

class CreationActivity : AppCompatActivity() {

    private val user = Backendless.UserService.CurrentUser()

    companion object{
        val TAG = "CreationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)

        button_creation_backToMain.setOnClickListener {
            if (user.getProperty("createdCharacter") == false){
                Toast.makeText(this, "Error: No character data found!", Toast.LENGTH_SHORT).show()
            }
            else{
                creationFinished()
            }
        }

        button_creation_finish.setOnClickListener {
            safetyCheck()
        }
    }

    private fun safetyCheck(){
        if (editText_creation_strength.text.isNullOrEmpty()){
            Toast.makeText(this, "What a weakling! Give yourself some strength!", Toast.LENGTH_SHORT).show()
        }
        else if (editText_creation_dexterity.text.isNullOrEmpty()){
            Toast.makeText(this, "How clumsy! Give yourself some dexterity!", Toast.LENGTH_SHORT).show()
        }
        else if (editText_creation_intelligence.text.isNullOrEmpty()){
            Toast.makeText(this, "What a fool! Give yourself some intelligence!", Toast.LENGTH_SHORT).show()
        }
        else if (editText_creation_luck.text.isNullOrEmpty()){
                Toast.makeText(this, "How unlucky! Give yourself some luck!", Toast.LENGTH_SHORT).show()
        }
        else{
            checkStats()
        }
    }

    private fun checkStats(){

        val strValue = editText_creation_strength.text.toString().toInt()
        val dexValue = editText_creation_dexterity.text.toString().toInt()
        val intValue = editText_creation_intelligence.text.toString().toInt()
        val lukValue = editText_creation_luck.text.toString().toInt()

        if (strValue + dexValue + intValue + lukValue > 12){
            Toast.makeText(this, "Your stats are too high!", Toast.LENGTH_SHORT).show()
        }
        else if (strValue + dexValue + intValue + lukValue < 12){
            Toast.makeText(this, "Your stats are too low!", Toast.LENGTH_SHORT).show()
        }
        else {
            registerCharacter()
        }
    }

    private fun registerCharacter(){
        val character = Character(str = editText_creation_strength.text.toString().toInt(),
            dex = editText_creation_dexterity.text.toString().toInt(),
            int = editText_creation_intelligence.text.toString().toInt(),
            luk = editText_creation_luck.text.toString().toInt()
        )
        character.ownerId = user.userId

        Backendless.Data.of(Character::class.java).save(character, object: AsyncCallback<Character?>{
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: Character?) {
                Toast.makeText(this@CreationActivity, "Character created!", Toast.LENGTH_SHORT).show()
                creationFinished()
            }

        })
    }

    private fun creationFinished(){
        user.setProperty("createdCharacter", true)
        Backendless.UserService.update(user, object: AsyncCallback<BackendlessUser>{
            override fun handleFault(fault: BackendlessFault?) {

                Log.d(TAG, "handleFault : ${fault?.detail}")

            }

            override fun handleResponse(response: BackendlessUser?) {

                val creationIntent = Intent(this@CreationActivity, HomePage::class.java)
                startActivity(creationIntent)
                //to close the login screen so it's not there when user clicks bacc
                finish()

            }

        })
    }
}