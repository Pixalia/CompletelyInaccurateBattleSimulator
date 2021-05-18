package com.example.completelyinaccuratebattlesimulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_battle_prep.*

class BattlePrepActivity : AppCompatActivity() {

    companion object{
        val TAG = "BattlePrepActivity"
    }

    val userId = Backendless.UserService.CurrentUser().userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_prep)



        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        generateFoe()

        Backendless.Data.of(Character::class.java).find(queryBuilder, object : AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null) {
                    val item = response.elementAt(0)
                    textView_prep_yourStats.text = item!!.name + "'s Stats:"
                    textView_prep_str.text = "Str: " + item!!.str
                    textView_prep_dex.text = "Dex: " + item!!.dex
                    textView_prep_int.text = "Int: " + item!!.int.toString()
                    textView_prep_luk.text = "Luk: " + item!!.luk.toString()

                }
            }

        })
        }

    fun generateFoe(){

        val whereClause = "str >= 0"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        Backendless.Data.of(Character::class.java).find(queryBuilder, object: AsyncCallback<List<Character?>?>{
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(BattlePrepActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                if (response != null){
                    var random = (0 until response.size).random()
                    while (response.elementAt(random)!!.ownerId == userId){
                        random = (0 until response.size).random()
                    }
                    val item = response.elementAt(random)
                    textView_prep_foeStats.text = item!!.name + "'s Stats:"
                    textView_prep_foeStr.text = "Str: " + item!!.str.toString()
                    textView_prep_foeDex.text = "Dex: " + item!!.dex.toString()
                    textView_prep_foeInt.text = "Int: " + item!!.int.toString()
                    textView_prep_foeLuk.text = "Luk: " + item!!.luk.toString()
                }
            }

        })
    }
}


