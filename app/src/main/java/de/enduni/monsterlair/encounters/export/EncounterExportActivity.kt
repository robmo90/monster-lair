package de.enduni.monsterlair.encounters.export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Base64
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import de.enduni.monsterlair.databinding.ActivityEncounterExportBinding


class EncounterExportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEncounterExportBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncounterExportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = intent.getStringExtra(EXTRA_ENCOUNTER_NAME)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent.getStringExtra(EXTRA_ENCOUNTER_PAGE)?.let {
            val encodedHtml = Base64.encodeToString(it.toByteArray(), Base64.NO_PADDING)
            binding.webView.loadData(encodedHtml, "text/html", "base64")
            binding.webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }

            }
        }
        binding.exportFab.setOnClickListener { createWebPrintJob(binding.webView) }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    companion object {

        private const val EXTRA_ENCOUNTER_PAGE = "encounterPage"
        private const val EXTRA_ENCOUNTER_NAME = "encounterName"

        fun intent(context: Context, encounterName: String, exportPage: String) =
            Intent(context, EncounterExportActivity::class.java).apply {
                putExtra(EXTRA_ENCOUNTER_NAME, encounterName)
                putExtra(EXTRA_ENCOUNTER_PAGE, exportPage)
            }

    }

    private fun createWebPrintJob(webView: WebView) {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val jobName = intent.getStringExtra(EXTRA_ENCOUNTER_NAME) ?: "Encounter"
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }
}