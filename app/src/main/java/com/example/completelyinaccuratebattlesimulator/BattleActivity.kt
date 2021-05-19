package com.example.completelyinaccuratebattlesimulator

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle.*
import kotlinx.android.synthetic.main.activity_battle_prep.*

class BattleActivity : AppCompatActivity() {

    private var foeId : String? = null
    private val userId = Backendless.UserService.CurrentUser().userId
    private var userHealth = 50
    private var foeHealth = 50
    private var foeDodge = 0
    private var userDodge = 0

    companion object{
        val TAG = "BattleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        val whereClauseFoe = "ownerId = '$foeId'"
        val queryBuilderFoe = DataQueryBuilder.create()
        queryBuilderFoe.whereClause = whereClauseFoe

        // get foe's dex for later usage
        Backendless.Data.of(Character::class.java).find(queryBuilderFoe, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val itemFoe = response.elementAt(0)
                    if (itemFoe != null){
                        foeDodge = itemFoe.dex
                    }
                }
            }

        })

        // gets user's dex for later usage
        val whereClauseUser = "ownerId = '$userId'"
        val queryBuilderUser = DataQueryBuilder.create()
        queryBuilderUser.whereClause = whereClauseUser

        Backendless.Data.of(Character::class.java).find(queryBuilderUser, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val itemUser = response.elementAt(0)

                    if (itemUser != null){
                        userDodge = itemUser.dex
                    }
                }
            }
        })

        foeId = intent.getStringExtra(BattlePrepActivity.ENEMY_ID)
        calculateFirstTurn()

        //this code here is to make the health bar go down

        var healthBar = imageView_battle_userHealth.drawable
        healthBar.level = 5000
    }

    fun calculateFirstTurn(){
        val turn = (0..1).random()
        if (turn == 0){
            playerPhase()
        }
        else if (turn == 1){
            enemyPhase()
        }
    }

    fun playerPhase(){

        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause


        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val item = response.elementAt(0)

                    if (item != null){

                        val hitChance = (item.int + (0..20).random())
                        val foeDodgeChance = (foeDodge + (0..20).random())
                        var damage = (item.str + (0..(item.str/2)).random()) + 1
                        val critChance = (0..100).random()

                        if (critChance < (5 + item.luk)){
                            damage *= 2
                        }

                        if (hitChance >= foeDodgeChance){
                            foeHealth -= damage
                            // edit health bar
                            turnDelay()
                        }
                        else {
                            Toast.makeText(this@BattleActivity, "You missed!", Toast.LENGTH_SHORT).show()
                            turnDelay()
                        }
                    }

                }

            }

        })
    }

    fun enemyPhase(){

        val whereClause = "ownerId = '$foeId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val item = response.elementAt(0)

                    if (item != null){

                        val hitChance = (item.int + (0..20).random())
                        val foeDodgeChance = (userDodge + (0..20).random())
                        var damage = (item.str + (0..(item.str/2)).random()) + 1
                        val critChance = (0..100).random()

                        if (critChance < (5 + item.luk)){
                            damage *= 2
                        }

                        if (hitChance >= foeDodgeChance){
                            userHealth -= damage
                            // edit health bar
                            turnDelay()
                        }
                        else {
                            Toast.makeText(this@BattleActivity, "You missed!", Toast.LENGTH_SHORT).show()
                            turnDelay()
                        }
                    }
                }
            }

        })
    }

    fun turnDelay(){
        // Implement turn delay here
    }
}