package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_creation.*
import kotlinx.android.synthetic.main.activity_strengthening.*
import kotlin.random.Random



class StrengtheningActivity : AppCompatActivity() {

    private var current = ""

    companion object{
        val TAG = "StrengtheningActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strengthening)

        generateQuote()

        imageView_strength_strength.setOnClickListener {
            textView_strength_current.text = "Currently Selected: Strength"
            current = "strength"
        }
        imageView_strength_dexterity.setOnClickListener {
            textView_strength_current.text = "Currently Selected: Dexterity"
            current = "dexterity"
        }
        imageView_strength_intelligence.setOnClickListener {
            textView_strength_current.text = "Currently Selected: Intelligence"
            current = "intelligence"
        }
        imageView_strength_luck.setOnClickListener {
            textView_strength_current.text = "Currently Selected: Luck"
            current = "luck"
        }
        button_strength_none.setOnClickListener {
            textView_strength_current.text = "Are you sure you don't want to strengthen yourself?"
            current = "none"
        }

        button_strength_confirm.setOnClickListener{
            if (current == ""){
                Toast.makeText(this, "Error: No option has been selected!", Toast.LENGTH_SHORT).show()
            }
            else{
                ascension()
            }
        }
    }

    fun ascension(){

        val userId = Backendless.UserService.CurrentUser().userId
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(StrengtheningActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (current == "strength"){
                    if (response?.elementAt(0) != null) {

                        val curCharacter = response.elementAt(0)
                        val newStr = curCharacter!!.str + 1
                        curCharacter.str = newStr

                        Backendless.Data.of(Character::class.java).save(curCharacter, object: AsyncCallback<Character>{
                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(StrengtheningActivity.TAG, "handleFault : ${fault?.detail}")
                            }

                            override fun handleResponse(response: Character?) {
                                Toast.makeText(this@StrengtheningActivity, "Strength increased!", Toast.LENGTH_SHORT).show()
                                finishingUp()
                            }

                        })
                    }
                }
                else if (current == "dexterity"){
                    if (response?.elementAt(0) != null) {

                        val curCharacter = response.elementAt(0)
                        val newStr = curCharacter!!.dex + 1
                        curCharacter.dex = newStr

                        Backendless.Data.of(Character::class.java).save(curCharacter, object: AsyncCallback<Character>{
                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(StrengtheningActivity.TAG, "handleFault : ${fault?.detail}")
                            }

                            override fun handleResponse(response: Character?) {
                                Toast.makeText(this@StrengtheningActivity, "Dexterity increased!", Toast.LENGTH_SHORT).show()
                                finishingUp()

                            }

                        })
                    }
                }
                else if (current == "intelligence"){
                    if (response?.elementAt(0) != null) {

                        val curCharacter = response.elementAt(0)
                        val newStr = curCharacter!!.int + 1
                        curCharacter.int = newStr

                        Backendless.Data.of(Character::class.java).save(curCharacter, object: AsyncCallback<Character>{
                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(StrengtheningActivity.TAG, "handleFault : ${fault?.detail}")
                            }

                            override fun handleResponse(response: Character?) {
                                Toast.makeText(this@StrengtheningActivity, "Intelligence increased!", Toast.LENGTH_SHORT).show()
                                finishingUp()
                            }

                        })
                    }
                }
                else if (current == "luck"){
                    if (response?.elementAt(0) != null) {

                        val curCharacter = response.elementAt(0)
                        val newStr = curCharacter!!.luk + 1
                        curCharacter.luk = newStr

                        Backendless.Data.of(Character::class.java).save(curCharacter, object: AsyncCallback<Character>{
                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(StrengtheningActivity.TAG, "handleFault : ${fault?.detail}")
                            }

                            override fun handleResponse(response: Character?) {
                                Toast.makeText(this@StrengtheningActivity, "Luck increased!", Toast.LENGTH_SHORT).show()
                                finishingUp()
                            }

                        })
                    }
                }
                else{
                    Toast.makeText(this@StrengtheningActivity, "No stat has been increased.", Toast.LENGTH_SHORT).show()
                    finishingUp()
                }

            }
        })

    }

    fun finishingUp(){
        val powerIntent = Intent(this@StrengtheningActivity, HomePage::class.java)
        startActivity(powerIntent)
        //to close the login screen so it's not there when user clicks bacc
        finish()
    }

    fun generateQuote(){

        var rand = (1..10).random()
        if (rand == 1){
            textView_strength_quote.text = resources.getString(R.string.zhugeLiangSpeech)
        }
        if (rand == 2){
            textView_strength_quote.text = resources.getString(R.string.alexanderSpeech)
        }
        if (rand == 3){
            textView_strength_quote.text = resources.getString(R.string.caesarSpeech)
        }
        if (rand == 4){
            textView_strength_quote.text = resources.getString(R.string.caoCaoSpeech)
        }
        if (rand == 5){
            textView_strength_quote.text = resources.getString(R.string.genghisKhanSpeech)
        }
        if (rand == 6){
            textView_strength_quote.text = resources.getString(R.string.hannibalBarcaSpeech)
        }
        if (rand == 7){
            textView_strength_quote.text = resources.getString(R.string.napoleonSpeech)
        }
        if (rand == 8){
            textView_strength_quote.text = resources.getString(R.string.pattonSpeech)
        }
        if (rand == 9){
            textView_strength_quote.text = resources.getString(R.string.rommelSpeech)
        }
        if (rand == 10){
            textView_strength_quote.text = resources.getString(R.string.sunTzuSpeech)
        }

    }



}