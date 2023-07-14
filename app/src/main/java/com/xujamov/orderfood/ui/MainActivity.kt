package com.xujamov.orderfood.ui

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.xujamov.orderfood.R
import com.xujamov.orderfood.databinding.ActivityMainBinding
import com.xujamov.orderfood.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is signed in
            // Add your logic for the signed-in user here
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            setupSmoothBottomMenu()
        } else {
            // User is not signed in, navigate to LoginActivity
            goToLoginActivity()
        }
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