package dev.iconpln.mims.ui.role.pabrikan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityDashboardPabrikanBinding
import dev.iconpln.mims.ui.scan.ScanActivity
import java.util.Scanner

class DashboardPabrikanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardPabrikanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab1.setOnClickListener{
            val i = Intent (this@DashboardPabrikanActivity, ScanActivity::class.java)
            startActivity(i)
        }

        val navView: BottomNavigationView = binding.navViewPabrikan

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}