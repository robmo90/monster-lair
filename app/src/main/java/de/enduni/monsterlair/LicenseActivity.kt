package de.enduni.monsterlair

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import de.enduni.monsterlair.databinding.ActivityLicensesBinding

class LicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLicensesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicensesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.getStringExtra(EXTRA_ASSET)?.let {
            val bytes = applicationContext.assets.open(it).readBytes()
            val encodedHtml = Base64.encodeToString(bytes, Base64.NO_PADDING)
            binding.webView.loadData(encodedHtml, "text/html", "base64")
        }
    }


    companion object {

        private const val EXTRA_ASSET = "asset"

        fun libraryIntent(context: Context, asset: String) =
            Intent(context, LicenseActivity::class.java).apply {
                putExtra(
                    EXTRA_ASSET, asset
                )
            }

    }
}