package dev.iconpln.mims.ui.pnerimaan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPenerimaanUlpactivityBinding

class PenerimaanULPActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityPenerimaanUlpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityPenerimaanUlpactivityBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_penerimaan_ulpactivity)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_penerimaan_ulpactivity)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }
}