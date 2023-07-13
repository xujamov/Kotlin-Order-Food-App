package com.xujamov.orderfood.ui

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.xujamov.orderfood.R
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.databinding.ActivityMainBinding
import com.xujamov.orderfood.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize TokenManager
        tokenManager = TokenManager(this)
        if (tokenManager.getToken() == null) {
            goToLoginActivity()
        }

        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu)
        val menu = popupMenu.menu
        binding.navigationView.setupWithNavController(menu, navHostFragment.navController)

    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}