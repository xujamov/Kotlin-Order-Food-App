package com.xujamov.orderfood.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xujamov.orderfood.ui.MainActivity
import com.xujamov.orderfood.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

        Firebase.auth.currentUser?.let {
            it.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tokenResult = task.result?.token
                    Log.d("TOKEN", tokenResult.toString())
                    // Use the tokenResult as needed
                    // The token is available here within the onCompleteListener
                } else {
                    // Handle error in getting the token
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.tabBar.setupWithViewPager2(binding.viewPager)
    }


}