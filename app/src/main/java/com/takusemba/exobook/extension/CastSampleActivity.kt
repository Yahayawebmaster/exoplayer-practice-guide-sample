package com.takusemba.exobook.extension

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.takusemba.exobook.R

class CastSampleActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val castContext = CastContext.getSharedInstance(this)
        val castPlayer = CastPlayer(castContext)

        castPlayer.setSessionAvailabilityListener(object : SessionAvailabilityListener {

            override fun onCastSessionAvailable() {
                val item = MediaItem.fromUri(URI)
                castPlayer.setMediaItem(item)
                castPlayer.prepare()
                castPlayer.play()
            }

            override fun onCastSessionUnavailable() {
                castPlayer.stop()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_cast, menu)
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.item_cast)
        return true
    }

    private fun initializePlayer() {
        val player = SimpleExoPlayer.Builder(this).build()

        val playerView = findViewById<StyledPlayerView>(R.id.player_view)
        playerView.player = player

        val mediaItem = MediaItem.fromUri(URI)

        player.setAudioAttributes(AudioAttributes.DEFAULT, true)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        this.player = player
    }

    private fun releasePlayer() {
        player?.stop()
        player?.release()
        player = null
    }

    companion object {

        private val URI =
            Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }
}