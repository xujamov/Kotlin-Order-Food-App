package com.xujamov.orderfood.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.xujamov.orderfood.databinding.ActivityLoginBinding
import com.xujamov.orderfood.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            goToMainActivity()
        } ?: run {
            binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            binding.tabBar.setupWithViewPager2(binding.viewPager)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}