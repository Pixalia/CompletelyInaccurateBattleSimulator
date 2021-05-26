package com.example.completelyinaccuratebattlesimulator

data class Character (
    var name: String = "",
    var str : Int = 0,
    var dex : Int = 0,
    var int : Int = 0,
    var luk : Int = 0,
    var wins : Int = 0,
    var objectId : String? = null,
    var ownerId: String? = null,
    var winStreak : Int = 0
) {

}