package com.bignerdranch.android.geomain

private const val WAV = ".wav"
class Sound(val assetPath: String, var soundId: Int? = null) {
    val name = assetPath.split("/").last().removeSuffix(WAV)
}
