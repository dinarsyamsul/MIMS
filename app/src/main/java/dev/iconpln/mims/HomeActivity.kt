package dev.iconpln.mims

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.iconpln.mims.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.fab1.setOnClickListener {}

        val navView: BottomNavigationView = binding.navViewPabrikan

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        navView.setupWithNavController(navController)
    }

    companion object {
        private var instance: HomeActivity? = null
        fun getInstance() = instance ?: HomeActivity().also {
            instance = it
        }

        var data: Any? = null
    }
}