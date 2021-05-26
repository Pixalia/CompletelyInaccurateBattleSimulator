package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle_prep.*
import kotlinx.android.synthetic.main.activity_character.*

class CharacterActivity : AppCompatActivity() {

    companion object {
        val TAG = "CHARACTERACTIVITY"
    }

    private val userId = Backendless.UserService.CurrentUser().userId
    private var reallyDelete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        button_character_return.setOnClickListener {
            val characterIntent = Intent(this@CharacterActivity, HomePage::class.java)
            startActivity(characterIntent)
            finish()
        }

        button_character_delete.setOnClickListener {
            if (reallyDelete){
                Backendless.Data.of(Character::class.java).find(queryBuilder, object :
                    AsyncCallback<List<Character?>?> {
                    override fun handleFault(fault: BackendlessFault?) {
                        Log.d(CharacterActivity.TAG, "handleFault : ${fault?.detail}")
                    }

                    override fun handleResponse(response: List<Character?>?) {
                        val ded = response!!.elementAt(0)
                        Backendless.Data.of(Character::class.java).remove(ded, object: AsyncCallback<Long?> {
                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d(CharacterActivity.TAG, "handleFault : ${fault?.detail}")
                            }

                            override fun handleResponse(response: Long?) {
                                val characterIntent = Intent(this@CharacterActivity, CreationActivity::class.java)
                                startActivity(characterIntent)
                                finish()
                            }

                        })
                    }

                })
            }
            else{
                Toast.makeText(this@CharacterActivity, "Press again to delete character!", Toast.LENGTH_SHORT).show()
                reallyDelete = true
            }
        }

        Backendless.Data.of(Character::class.java).find(queryBuilder, object :
            AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(CharacterActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null) {
                    val item = response.elementAt(0)
                    textView_character_str.text = "Str: " + item!!.str
                    textView_character_str.setTextColor(Color.RED)
                    textView_character_dex.text = "Dex: " + item!!.dex
                    textView_character_dex.setTextColor(Color.CYAN)
                    textView_character_int.text = "Int: " + item!!.int.toString()
                    textView_character_int.setTextColor(Color.YELLOW)
                    textView_character_luk.text = "Luk: " + item!!.luk.toString()
                    textView_character_luk.setTextColor(Color.GREEN)

                    textView_character_wins.text = "Wins: ${item!!.wins}"
                    textView_character_name.text = item!!.name



                    if (item.str > item.dex && item.str > item.int && item.str > item.luk){
                        imageView_character_avatar.setImageResource(R.drawable.redheavy)
                    }
                    else if (item.dex > item.str && item.dex > item.int && item.dex > item.luk){
                        imageView_character_avatar.setImageResource(R.drawable.redscout)
                    }
                    else if (item.int > item.dex && item.int > item.str && item.int > item.luk){
                        imageView_character_avatar.setImageResource(R.drawable.redengi)
                    }
                    else if (item.luk > item.dex && item.luk > item.str && item.luk > item.int){
                        imageView_character_avatar.setImageResource(R.drawable.redspy)
                    }
                    else {
                        imageView_character_avatar.setImageResource(R.drawable.redpyro)
                    }
                }
            }

        })
    }
}