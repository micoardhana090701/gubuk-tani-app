package com.futureengineerdev.gubugtani

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.comment.CommentAdapter
import com.futureengineerdev.gubugtani.comment.CommentFragment
import com.futureengineerdev.gubugtani.comment.CommentFragment.Companion.EXTRA_ID
import com.futureengineerdev.gubugtani.comment.GetCommentViewModel
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ActivityDetailBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItemComment
import com.futureengineerdev.gubugtani.viewmodel.AddCommentViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class DetailActivity : AppCompatActivity(), View.OnClickListener{

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var imageScaleZoom = true
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var addCommentViewModel: AddCommentViewModel
    private lateinit var getCommentViewModel: GetCommentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val article = intent.getParcelableExtra<ArticlesWithImages>(EXTRA_DATA)
        lifecycleScope.launch {
            setupView(article!!)
        }
//        val pref = UserPreferences.getInstance(dataStore)
//        val viewModelFactory = ViewModelFactory(pref)
//        viewModelFactory.setApplication(application)
//        addCommentViewModel = ViewModelProvider(this, viewModelFactory)[AddCommentViewModel::class.java]
//        binding.btnSendComment.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                uploadComment(article!!)
//            }
//        }
    }

    private suspend fun setupView(article: ArticlesWithImages) {
        with(binding){
            tvDetailJudul.text = article.articles.title
            tvDetailIsiArtikel.text = article.articles.content
            val firstImageDisplay = article.article_images
            val loadImageArticles = firstImageDisplay[0].image
            if (loadImageArticles == null) {
                Glide.with(this@DetailActivity)
                    .load("https://app.gubuktani.com/storage/public/gubuk-tani-logo.png")
                    .centerCrop()
                    .into(ivDetailGambar)
            }
            else{
                Glide.with(this@DetailActivity)
                    .load("https://app.gubuktani.com/storage/$loadImageArticles")
                    .centerCrop()
                    .into(ivDetailGambar)
            }
        }

//        getComment(article)

        binding.btnBackDetail.setOnClickListener(this@DetailActivity)
        binding.btnLihatComment.setOnClickListener{
            val targetFragment = CommentFragment()
            val bundle = Bundle().apply {
                putInt(EXTRA_ID, article.articles.id)
            }
            targetFragment.arguments = bundle
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutComment, targetFragment)
            fragmentTransaction.commit()
            binding.btnLihatComment.visibility = View.GONE
            binding.btnTutupComment.visibility = View.VISIBLE
            swipeUp()
            binding.btnTutupComment.setOnClickListener {
                swipeDown()
                binding.btnLihatComment.visibility = View.VISIBLE
                binding.btnTutupComment.visibility = View.GONE
            }
        }
    }

//    private suspend fun getComment(article: ArticlesWithImages) {
//        val articleId = article.articles.id
//        val pref = UserPreferences.getInstance(dataStore)
//        val viewModelFactory = ViewModelFactory(pref)
//        viewModelFactory.setApplication(application)
//        val accessToken = pref.getUserKey().first()
//        getCommentViewModel = ViewModelProvider(this, viewModelFactory)[GetCommentViewModel::class.java]
//        getCommentViewModel.getComment(accessToken = "Bearer $accessToken", articleId = articleId)
//        getCommentViewModel.comment.observe(this) {
//            commentAdapterInitialize(comment = it.result?.data as List<DataItemComment>)
//        }
//    }

    companion object {
        const val EXTRA_DATA = "extra_data"
//        const val EXTRA_ID = "extra_id"
    }

    private fun swipeUp(){
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.frameLayoutComment.startAnimation(slideUp)
        slideUp.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                binding.frameLayoutComment.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    private fun swipeDown(){
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.frameLayoutComment.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.frameLayoutComment.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }
//    @SuppressLint("NotifyDataSetChanged")
//    private fun commentAdapterInitialize(comment: List<DataItemComment>){
//        commentAdapter = CommentAdapter(comment)
//        binding.rvComment.apply {
//            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.VERTICAL, false)
//            adapter = commentAdapter
//        }
//        commentAdapter.notifyDataSetChanged()
//    }
//    @SuppressLint("ServiceCast")
//    private fun hideKeyboard() {
//        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(binding.etAddComment.windowToken, 0)
//    }
//    private fun refresh(){
//        val article = intent.getParcelableExtra<ArticlesWithImages>(EXTRA_DATA)
//        lifecycleScope.launch {
//            getComment(article!!)
//        }
//    }
//    private suspend fun uploadComment(article: ArticlesWithImages){
//        val articleId = article.articles.id
//        try {
//            val nullComment = binding.etAddComment.text.toString()
//            if (nullComment.isNotEmpty()){
//                val comment = nullComment.toRequestBody("text/plain".toMediaType())
//                CoroutineScope(Dispatchers.IO).launch {
//                    addCommentViewModel.addComment(article_id = articleId, comment = comment)
//                }
//                this.runOnUiThread {
//                    Toast.makeText(this, "Komentar berhasil diupload", Toast.LENGTH_SHORT).show()
//                    hideKeyboard()
//                    binding.etAddComment.text?.clear()
//                }
//            }else{
//                this.runOnUiThread {
//                    Toast.makeText(this, "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("CommentFragment", "uploadComment: ${e.message}", )
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        refresh()
//    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnBackDetail -> finish()
            binding.ivDetailGambar -> {
                imageScaleZoom = !imageScaleZoom
                binding.ivDetailGambar.scaleType =
                    if (imageScaleZoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }
        }
    }
}