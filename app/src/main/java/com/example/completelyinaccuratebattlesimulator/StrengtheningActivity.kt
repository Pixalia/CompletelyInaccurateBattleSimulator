package com.example.completelyinaccuratebattlesimulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_creation.*
import kotlinx.android.synthetic.main.activity_strengthening.*
import kotlin.random.Random

class StrengtheningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strengthening)

        generateQuote()


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