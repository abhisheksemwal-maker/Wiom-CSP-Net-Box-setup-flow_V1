package com.wiom.partner.util

import android.content.Context
import android.media.MediaPlayer
import android.content.res.AssetFileDescriptor

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(assetPath: String, onProgress: (Float) -> Unit = {}, onComplete: () -> Unit = {}) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            val afd: AssetFileDescriptor = context.assets.openFd(assetPath)
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            prepare()
            setOnCompletionListener { onComplete() }
            start()
        }
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
