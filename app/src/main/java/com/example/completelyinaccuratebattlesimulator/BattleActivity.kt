package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle.*
import kotlinx.android.synthetic.main.activity_battle_prep.*
import kotlinx.coroutines.delay

class BattleActivity : AppCompatActivity() {

    private var foeId : String? = null
    private val userId = Backendless.UserService.CurrentUser().userId
    private var userHealth = 50
    private var foeHealth = 50
    private var foeDodge = 0
    private var userDodge = 0
    private var currentTurn = 1
    var enemyStatTotal : Int = 0
    var userStatTotal : Int = 0
    var upHillBattle : Boolean = false
    var equalBattle : Boolean = false
    var advantageBattle : Boolean = false
    var musicPlayer : MediaPlayer? = null

    companion object{
        val TAG = "BattleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)
        textView_battle_header.setTextColor(Color.BLACK)

        foeId = intent.getStringExtra(BattlePrepActivity.ENEMY_ID)

        val whereClauseFoe = "ownerId = '$foeId'"
        val queryBuilderFoe = DataQueryBuilder.create()
        queryBuilderFoe.whereClause = whereClauseFoe

        textView_battle_log1.text = ""
        textView_battle_log2.text = ""
        textView_battle_log3.text = ""

        // get foe's dex for later usage
        Backendless.Data.of(Character::class.java).find(queryBuilderFoe, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val itemFoe = response.elementAt(0)
                    if (itemFoe != null){
                        //foeDodge = itemFoe.dex

                        val enemyStatTotal = itemFoe.str + itemFoe.dex + itemFoe.int + itemFoe.luk

                        if (itemFoe.str > itemFoe.dex && itemFoe.str > itemFoe.int && itemFoe.str > itemFoe.luk){
                            imageView_battle_foeAvatar.setImageResource(R.drawable.bluheavy)
                        }
                        else if (itemFoe.dex > itemFoe.str && itemFoe.dex > itemFoe.int && itemFoe.dex > itemFoe.luk){
                            imageView_battle_foeAvatar.setImageResource(R.drawable.bluscout)
                        }
                        else if (itemFoe.int > itemFoe.dex && itemFoe.int > itemFoe.str && itemFoe.int > itemFoe.luk){
                            imageView_battle_foeAvatar.setImageResource(R.drawable.blueengi)
                        }
                        else if (itemFoe.luk > itemFoe.dex && itemFoe.luk > itemFoe.str && itemFoe.luk > itemFoe.int){
                            imageView_battle_foeAvatar.setImageResource(R.drawable.bluspy)
                        }
                        else {
                            imageView_battle_foeAvatar.setImageResource(R.drawable.blupyro)
                        }

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
                Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val itemUser = response.elementAt(0)

                    if (itemUser != null){
                        userDodge = itemUser.dex

                        userStatTotal = itemUser.str + itemUser.dex + itemUser.int + itemUser.luk

                        if (itemUser.str > itemUser.dex && itemUser.str > itemUser.int && itemUser.str > itemUser.luk){
                            imageView_battle_avatar.setImageResource(R.drawable.redheavy)
                        }
                        else if (itemUser.dex > itemUser.str && itemUser.dex > itemUser.int && itemUser.dex > itemUser.luk){
                            imageView_battle_avatar.setImageResource(R.drawable.redscout)
                        }
                        else if (itemUser.int > itemUser.dex && itemUser.int > itemUser.str && itemUser.int > itemUser.luk){
                            imageView_battle_avatar.setImageResource(R.drawable.redengi)
                        }
                        else if (itemUser.luk > itemUser.dex && itemUser.luk > itemUser.str && itemUser.luk > itemUser.int){
                            imageView_battle_avatar.setImageResource(R.drawable.redspy)
                        }
                        else {
                            imageView_battle_avatar.setImageResource(R.drawable.redpyro)
                        }
                    }
                }
            }
        })

        foeId = intent.getStringExtra(BattlePrepActivity.ENEMY_ID)


        var layout = RelativeLayout(this@BattleActivity)

        if (userStatTotal > (enemyStatTotal + 5)){
            advantageBattle = true
            layout.background = ContextCompat.getDrawable(this, R.drawable.backgroundc)
            musicPlayer = MediaPlayer.create(this, R.raw.easybattle)
            musicPlayer!!.isLooping = true
            musicPlayer!!.start()

            calculateFirstTurn()
        }
        else if ((userStatTotal) + 5 < enemyStatTotal){
            upHillBattle = true
            layout.background = ContextCompat.getDrawable(this, R.drawable.backgroundb)
            musicPlayer = MediaPlayer.create(this, R.raw.uphillbattle)
            musicPlayer!!.isLooping = true
            musicPlayer!!.start()

            calculateFirstTurn()
        }
        else {
            equalBattle = true
            layout.background = ContextCompat.getDrawable(this, R.drawable.backgrounda)
            musicPlayer = MediaPlayer.create(this, R.raw.regularbattle)
            musicPlayer!!.isLooping = true
            musicPlayer!!.start()

            calculateFirstTurn()
        }



    }

    fun calculateFirstTurn(){
        val turn = (0..1).random()
        if (turn == 0){
            textView_battle_log1.text = "You go first!"
            Thread.sleep(1500)
            Handler().postDelayed(
                {
                    playerPhase()
                },
                1000
            )
        }
        else if (turn == 1){
            textView_battle_log1.text = "The enemy goes first!"
            Handler().postDelayed(
                {
                    enemyPhase()
                },
                1000
            )
        }
    }

    fun playerPhase(){

        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause


        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val item = response.elementAt(0)
                    var critTrue = false

                    if (item != null){

                        val hitChance = (item.int + (0..20).random())
                        val foeDodgeChance = (foeDodge + (0..20).random())
                        var damage = (item.str + (0..(item.str/2)).random()) + 1
                        val critChance = (0..100).random()

                        if (critChance < (5 + item.luk)){
                            critTrue = true
                        }

                        if (hitChance >= foeDodgeChance){
                            if (critTrue){
                                damage *= 2
                                foeHealth -= damage
                                textView_battle_log3.text = textView_battle_log2.text
                                textView_battle_log2.text = textView_battle_log1.text
                                textView_battle_log1.text = "Turn ${currentTurn}: Critical! '${item.name}' dealt '$damage' damage!"
                                if (foeHealth <= 45){
                                    imageView_battle_foeHeart5.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 40){
                                    imageView_battle_foeHeart5.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 35){
                                    imageView_battle_foeHeart4.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 30){
                                    imageView_battle_foeHeart4.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 25){
                                    imageView_battle_foeHeart3.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 20){
                                    imageView_battle_foeHeart3.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 15){
                                    imageView_battle_foeHeart2.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 10){
                                    imageView_battle_foeHeart2.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 5){
                                    imageView_battle_foeHeart1.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 0){
                                    imageView_battle_foeHeart1.setImageResource(R.drawable.heartempty)
                                }
                                Handler().postDelayed(
                                    {
                                        currentTurn++
                                        if (foeHealth <= 0){
                                            userVictory()
                                        }
                                        else{
                                            enemyPhase()
                                        }
                                    },
                                    500
                                )

                            }
                            else{
                                foeHealth -= damage
                                textView_battle_log3.text = textView_battle_log2.text
                                textView_battle_log2.text = textView_battle_log1.text
                                textView_battle_log1.text = "Turn ${currentTurn}: '${item.name}' dealt '$damage' damage."
                                if (foeHealth <= 45){
                                    imageView_battle_foeHeart5.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 40){
                                    imageView_battle_foeHeart5.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 35){
                                    imageView_battle_foeHeart4.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 30){
                                    imageView_battle_foeHeart4.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 25){
                                    imageView_battle_foeHeart3.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 20){
                                    imageView_battle_foeHeart3.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 15){
                                    imageView_battle_foeHeart2.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 10){
                                    imageView_battle_foeHeart2.setImageResource(R.drawable.heartempty)
                                }
                                if (foeHealth <= 5){
                                    imageView_battle_foeHeart1.setImageResource(R.drawable.hearthalfmirror)
                                }
                                if (foeHealth <= 0){
                                    imageView_battle_foeHeart1.setImageResource(R.drawable.heartempty)
                                }
                                Handler().postDelayed(
                                    {
                                        currentTurn++
                                        if (foeHealth <= 0){
                                            userVictory()
                                        }
                                        else{
                                            enemyPhase()
                                        }
                                    },
                                    500
                                )
                            }
                        }
                        else {
                            textView_battle_log3.text = textView_battle_log2.text
                            textView_battle_log2.text = textView_battle_log1.text
                            textView_battle_log1.text = "Turn ${currentTurn}: You missed!"
                            Handler().postDelayed(
                                {
                                    currentTurn++
                                    enemyPhase()
                                },
                                500
                            )
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
                Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    val item = response.elementAt(0)
                    var critTrue = false

                    if (item != null){

                        val hitChance = (item.int + (0..20).random())
                        val userDodgeChance = (userDodge + (0..20).random())
                        var damage = (item.str + (0..(item.str/2)).random()) + 1
                        val critChance = (0..100).random()

                        if (critChance < (5 + item.luk)){
                            critTrue = true
                        }

                        if (hitChance >= userDodgeChance){
                            if (critTrue){
                                damage *= 2
                                userHealth -= damage
                                textView_battle_log3.text = textView_battle_log2.text
                                textView_battle_log2.text = textView_battle_log1.text
                                textView_battle_log1.text = "Turn ${currentTurn}: Critical! '${item.name}' dealt '$damage' damage!"
                                if (userHealth <= 45){
                                    imageView_battle_userHeart5.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 40){
                                    imageView_battle_userHeart5.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 35){
                                    imageView_battle_userHeart4.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 30){
                                    imageView_battle_userHeart4.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 25){
                                    imageView_battle_userHeart3.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 20){
                                    imageView_battle_userHeart3.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 15){
                                    imageView_battle_userHeart2.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 10){
                                    imageView_battle_userHeart2.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 5){
                                    imageView_battle_userHeart1.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 0){
                                    imageView_battle_userHeart1.setImageResource(R.drawable.heartempty)
                                }
                                Handler().postDelayed(
                                    {
                                        currentTurn++
                                        if (userHealth <= 0){
                                            foeVictory()
                                        }
                                        else{
                                            playerPhase()
                                        }
                                    },
                                    500
                                )
                            }
                            else{
                                userHealth -= damage
                                textView_battle_log3.text = textView_battle_log2.text
                                textView_battle_log2.text = textView_battle_log1.text
                                textView_battle_log1.text = "Turn ${currentTurn}: '${item.name}' dealt '$damage' damage."
                                if (userHealth <= 45){
                                    imageView_battle_userHeart5.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 40){
                                    imageView_battle_userHeart5.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 35){
                                    imageView_battle_userHeart4.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 30){
                                    imageView_battle_userHeart4.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 25){
                                    imageView_battle_userHeart3.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 20){
                                    imageView_battle_userHeart3.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 15){
                                    imageView_battle_userHeart2.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 10){
                                    imageView_battle_userHeart2.setImageResource(R.drawable.heartempty)
                                }
                                if (userHealth <= 5){
                                    imageView_battle_userHeart1.setImageResource(R.drawable.hearthalf)
                                }
                                if (userHealth <= 0){
                                    imageView_battle_userHeart1.setImageResource(R.drawable.heartempty)
                                }
                                Handler().postDelayed(
                                    {
                                        currentTurn++
                                        if (userHealth <= 0){
                                            foeVictory()
                                        }
                                        else{
                                            playerPhase()
                                        }
                                    },
                                    500
                                )
                            }
                        }
                        else {
                            textView_battle_log3.text = textView_battle_log2.text
                            textView_battle_log2.text = textView_battle_log1.text
                            textView_battle_log1.text = "Turn ${currentTurn}: The enemy missed!"
                            Handler().postDelayed(
                                {
                                    currentTurn++
                                    playerPhase()
                                },
                                500
                            )
                        }
                    }
                }
            }

        })
    }

    fun userVictory(){
        textView_battle_log3.text = textView_battle_log2.text
        textView_battle_log2.text = textView_battle_log1.text
        textView_battle_log1.text = "Player wins!"
        Handler().postDelayed(
            {
                val whereClauseUser = "ownerId = '$userId'"
                val queryBuilderUser = DataQueryBuilder.create()
                queryBuilderUser.whereClause = whereClauseUser

                Backendless.Data.of(Character::class.java).find(queryBuilderUser, object : AsyncCallback<List<Character?>?> {
                    override fun handleFault(fault: BackendlessFault?) {
                        Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
                    }

                    override fun handleResponse(response: List<Character?>?) {
                        if (response != null){
                            val itemUser = response.elementAt(0)

                            if (itemUser != null){
                               if (itemUser.winStreak == 3){
                                   val battleIntent = Intent(this@BattleActivity, StrengtheningActivity::class.java)
                                   startActivity(battleIntent)
                                   //to close the login screen so it's not there when user clicks bacc
                                   finish()
                               }
                               else{
                                   itemUser.winStreak++
                                   Backendless.Data.of(Character::class.java).save(itemUser, object: AsyncCallback<Character>{
                                       override fun handleFault(fault: BackendlessFault?) {
                                           Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
                                       }

                                       override fun handleResponse(response: Character?) {
                                           val battleIntent = Intent(this@BattleActivity, HomePage::class.java)
                                           startActivity(battleIntent)
                                           //to close the login screen so it's not there when user clicks bacc
                                           finish()
                                       }

                                   })
                               }
                            }
                        }
                    }
                })
            },
            1500
        )
    }

    fun foeVictory(){
        textView_battle_log3.text = textView_battle_log2.text
        textView_battle_log2.text = textView_battle_log1.text
        textView_battle_log1.text = "Foe wins!"
        Handler().postDelayed({
            val whereClauseUser = "ownerId = '$userId'"
            val queryBuilderUser = DataQueryBuilder.create()
            queryBuilderUser.whereClause = whereClauseUser

            Backendless.Data.of(Character::class.java).find(queryBuilderUser, object : AsyncCallback<List<Character?>?> {
                override fun handleFault(fault: BackendlessFault?) {
                    Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
                }

                override fun handleResponse(response: List<Character?>?) {
                    if (response != null){
                        val itemUser = response.elementAt(0)

                        if (itemUser != null){
                                itemUser.winStreak = 0
                                Backendless.Data.of(Character::class.java).save(itemUser, object: AsyncCallback<Character>{
                                    override fun handleFault(fault: BackendlessFault?) {
                                        Log.d(BattleActivity.TAG, "handleFault : ${fault?.detail}")
                                    }

                                    override fun handleResponse(response: Character?) {
                                        val battleIntent = Intent(this@BattleActivity, HomePage::class.java)
                                        startActivity(battleIntent)
                                        //to close the login screen so it's not there when user clicks bacc
                                        finish()
                                    }

                                })
                            }
                        }
                    }

            })
        },
            1500
        )
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