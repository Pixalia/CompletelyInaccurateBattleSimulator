package com.example.completelyinaccuratebattlesimulator

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : AppCompatActivity() {

    companion object{
        val TAG = "LeaderboardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val whereClause = "str >= 0"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        textView_strength_ranking1.text = "Rank 1:"
        textView_strength_ranking2.text = "Rank 2:"
        textView_strength_ranking3.text = "Rank 3:"
        textView_strength_ranking4.text = "Rank 4:"
        textView_strength_ranking5.text = "Rank 5:"
        textView_leaderboard_rank1wins.visibility = View.INVISIBLE
        textView_leaderboard_rank2wins.visibility = View.INVISIBLE
        textView_leaderboard_rank3wins.visibility = View.INVISIBLE
        textView_leaderboard_rank4wins.visibility = View.INVISIBLE
        textView_leaderboard_rank5wins.visibility = View.INVISIBLE

        button_leaderboard_return.setOnClickListener {
            val leaderboardIntent = Intent(this@LeaderboardActivity, HomePage::class.java)
            startActivity(leaderboardIntent)
            finish()
        }

        Backendless.Data.of(Character::class.java).find(queryBuilder, object:
            AsyncCallback<List<Character?>?> {
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(LeaderboardActivity.TAG, "handleFault : ${fault?.detail}")
            }

            override fun handleResponse(response: List<Character?>?) {
                response?.forEach{
                    if (it!!.wins > textView_leaderboard_rank1wins.text.toString().toInt()){
                        textView_leaderboard_rank5.text = textView_leaderboard_rank4.text
                        textView_leaderboard_rank5wins.text = textView_leaderboard_rank4wins.text
                        textView_leaderboard_rank4.text = textView_leaderboard_rank3.text
                        textView_leaderboard_rank4wins.text = textView_leaderboard_rank3wins.text
                        textView_leaderboard_rank3.text = textView_leaderboard_rank2.text
                        textView_leaderboard_rank3wins.text = textView_leaderboard_rank2wins.text
                        textView_leaderboard_rank2.text = textView_leaderboard_rank1.text
                        textView_leaderboard_rank2wins.text = textView_leaderboard_rank1wins.text
                        textView_leaderboard_rank1.text = "${it!!.name} || Wins: ${it!!.wins}"
                        textView_leaderboard_rank1wins.text = it!!.wins.toString()
                    } else if (it!!.wins > textView_leaderboard_rank2wins.text.toString().toInt()){
                        textView_leaderboard_rank5.text = textView_leaderboard_rank4.text
                        textView_leaderboard_rank5wins.text = textView_leaderboard_rank4wins.text
                        textView_leaderboard_rank4.text = textView_leaderboard_rank3.text
                        textView_leaderboard_rank4wins.text = textView_leaderboard_rank3wins.text
                        textView_leaderboard_rank3.text = textView_leaderboard_rank2.text
                        textView_leaderboard_rank3wins.text = textView_leaderboard_rank2wins.text
                        textView_leaderboard_rank2.text = "${it!!.name} || Wins: ${it!!.wins}"
                        textView_leaderboard_rank2wins.text = it!!.wins.toString()
                    } else if (it!!.wins > textView_leaderboard_rank3wins.text.toString().toInt()){
                        textView_leaderboard_rank5.text = textView_leaderboard_rank4.text
                        textView_leaderboard_rank5wins.text = textView_leaderboard_rank4wins.text
                        textView_leaderboard_rank4.text = textView_leaderboard_rank3.text
                        textView_leaderboard_rank4wins.text = textView_leaderboard_rank3wins.text
                        textView_leaderboard_rank3.text = "${it!!.name} || Wins: ${it!!.wins}"
                        textView_leaderboard_rank3wins.text = it!!.wins.toString()
                    } else if (it!!.wins > textView_leaderboard_rank4wins.text.toString().toInt()){
                        textView_leaderboard_rank5.text = textView_leaderboard_rank4.text
                        textView_leaderboard_rank5wins.text = textView_leaderboard_rank4wins.text
                        textView_leaderboard_rank4.text = "${it!!.name} || Wins: ${it!!.wins}"
                        textView_leaderboard_rank4wins.text = it!!.wins.toString()
                    } else if (it!!.wins > textView_leaderboard_rank5wins.text.toString().toInt()){
                        textView_leaderboard_rank5.text = "${it!!.name} || Wins: ${it!!.wins}"
                        textView_leaderboard_rank5wins.text = it!!.wins.toString()
                    }

                }
            }
        })
    }
}