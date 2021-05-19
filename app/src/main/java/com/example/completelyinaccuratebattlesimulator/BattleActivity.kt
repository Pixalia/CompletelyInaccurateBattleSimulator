package com.example.completelyinaccuratebattlesimulator

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle_prep.*

class BattleActivity : AppCompatActivity() {

    private var foeId : String? = null
    private val userId = Backendless.UserService.CurrentUser().userId
    private var userHealth = 50
    private var foeHealth = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        foeId = intent.getStringExtra(BattlePrepActivity.ENEMY_ID)
        calculateFirstTurn()
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
                }
            }

        })
    }

    fun turnDelay(){
        // Implement turn delay here
    }
}