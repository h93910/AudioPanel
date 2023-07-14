/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ban.audiopanel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.collection.ArraySet
import java.io.File
import java.lang.ref.SoftReference

/**
 * 混合功能模块
 */
object MultiUtils {
    private var mediaPlayer: SoftReference<MediaPlayer>? = null
    private val mLastTimeDeniedPermissions = ArraySet<String>()
    private const val TAG = "MultiUtils"

    /**
     * 播放声音
     * @param context Context
     * @param file File
     * @param listener OnCompletionListener? 可侦听声音播放结束
     */
    fun playWavSound(
        context: Context,
        file: File,
        listener: MediaPlayer.OnCompletionListener? = null
    ) {
        stopWanSound()
        val player = MediaPlayer.create(context, Uri.fromFile(file))
        mediaPlayer = SoftReference(player)
        player?.apply {
            setVolume(1f, 1f)
            if (isPlaying) {
                stop()
                reset()
            }
            start()
            setOnCompletionListener(listener)
        }
    }

    fun stopWanSound() {
        val player = mediaPlayer?.get()
        player?.apply {//先全停止
            if (isPlaying) {
                stop()
            }
            release()
            mediaPlayer?.clear()
            mediaPlayer = null
        }
    }
}