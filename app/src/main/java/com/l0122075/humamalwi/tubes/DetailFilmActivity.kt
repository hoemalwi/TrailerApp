package com.l0122075.humamalwi.tubes

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.l0122075.humamalwi.tubes.databinding.ActivityDetailFilmBinding
import com.l0122075.humamalwi.tubes.databinding.ActivityUpdateBinding
import com.squareup.picasso.Picasso

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    private lateinit var film: DataFilm
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    lateinit var webView: WebView
    lateinit var video: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        film = intent.getParcelableExtra("film")!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        storageRef = FirebaseStorage.getInstance().getReference("images")

        webView = binding.ytvideo
        val videoId = getYoutubeVideoId(film.linkyt.toString())


        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        if (videoId != null) {
            loadVideo(videoId)
        } else {
            Log.e("DetailFilmActivity", "Link Youtube Tidak Valid| link: ${film.linkyt}")
        }

        binding.detailJudul.setText(film.Judul)
        binding.detailSinopsis.setText(film.Sinopsis)
        binding.detailTahunRilis.setText(film.TahunRilis.toString())
        binding.detailSutradara.setText(film.Sutradara)
        binding.detailDurasi.setText(film.Durasi.toString())
        binding.detailGenre.setText(film.Genre)
        Picasso.get().load(film.imgfilm).into(binding.detailImage)
    }

    private fun getYoutubeVideoId(url: String): String? {
        val regex = "(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})(?:&.*|\\?.*|$)".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value
    }

    private fun loadVideo(videoId: String) {
        val video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$videoId\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
        webView.loadData(video, "text/html", "utf-8")
    }
}
