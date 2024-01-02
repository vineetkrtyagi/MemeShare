package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUrl: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadMeme()
    }

    private fun loadMeme() {
        // RequestQueue initialized
        binding.progressBar.visibility= View.VISIBLE
        val mRequestQueue = Volley.newRequestQueue(this)
        val url= "https://meme-api.com/gimme"

        // String Request initialized
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
             response ->
                run {
                    currentImageUrl = response.getString("url")
                    Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility= View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility= View.GONE
                            return false
                        }
                    }).into(binding.memeImageView)
                }
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        })

        mRequestQueue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share thos meme using ...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}