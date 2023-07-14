package com.xujamov.orderfood.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.databinding.ActivityLoginBinding
import com.xujamov.orderfood.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, SignInFragment())
//                .commit()
//        }


        supportActionBar?.hide()

        // Initialize TokenManager
        tokenManager = TokenManager(this)

        val storedToken = tokenManager.getToken()

//        if (storedToken != null) {
//            // Token is valid, proceed to the main activity
//            goToMainActivity()
//        } else {
            // Token is not valid or doesn't exist, retrieve a new one from Firebase
        Firebase.auth.currentUser?.let { user ->
            user.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tokenResult = task.result?.token
                    // Store the new token
                    tokenManager.saveToken(tokenResult.toString())

                    goToMainActivity()
                } else {
                    // Handle error in getting the token
                }
            }
        }
//        }

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.tabBar.setupWithViewPager2(binding.viewPager)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}