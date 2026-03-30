package ru.fefu.pokedex.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val PokemonNormal = Color(0xFFA8A77A)
val PokemonFire = Color(0xFFEE8130)
val PokemonWater = Color(0xFF6390F0)
val PokemonElectric = Color(0xFFF7D02C)
val PokemonGrass = Color(0xFF7AC74C)
val PokemonIce = Color(0xFF96D9D6)
val PokemonFighting = Color(0xFFC22E28)
val PokemonPoison = Color(0xFFA33EA1)
val PokemonGround = Color(0xFFE2BF65)
val PokemonFlying = Color(0xFFA98FF3)
val PokemonPsychic = Color(0xFFF95587)
val PokemonBug = Color(0xFFA6B91A)
val PokemonRock = Color(0xFFB6A136)
val PokemonGhost = Color(0xFF735797)
val PokemonDark = Color(0xFF705746)
val PokemonDragon = Color(0xFF6F35FC)
val PokemonSteel = Color(0xFFB7B7CE)
val PokemonFairy = Color(0xFFD685AD)

fun getTypeColor(typeName: String): Long {
    return when (typeName.lowercase()) {
        "normal" -> 0xFFA8A77A
        "fire" -> 0xFFEE8130
        "water" -> 0xFF6390F0
        "electric" -> 0xFFF7D02C
        "grass" -> 0xFF7AC74C
        "ice" -> 0xFF96D9D6
        "fighting" -> 0xFFC22E28
        "poison" -> 0xFFA33EA1
        "ground" -> 0xFFE2BF65
        "flying" -> 0xFFA98FF3
        "psychic" -> 0xFFF95587
        "bug" -> 0xFFA6B91A
        "rock" -> 0xFFB6A136
        "ghost" -> 0xFF735797
        "dark" -> 0xFF705746
        "dragon" -> 0xFF6F35FC
        "steel" -> 0xFFB7B7CE
        "fairy" -> 0xFFD685AD
        else -> 0xFF777777
    }
}