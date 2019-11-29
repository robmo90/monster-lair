package de.enduni.monsterlair.encounters.export

import android.content.Context.PRINT_SERVICE
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.FragmentEncounterExportBinding
import timber.log.Timber


class EncounterExportFragment : Fragment() {

    private lateinit var binding: FragmentEncounterExportBinding

    private val exportArgs: EncounterExportFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_encounter_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEncounterExportBinding.bind(view)
        Timber.d("this is my encoutner page: ${exportArgs.encounterPage}")

        binding.webView.loadData(exportArgs.encounterPage, "text/HTML", "UTF-8")
        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.exportButton.visibility = View.VISIBLE
            }
        }

        binding.exportButton.setOnClickListener {
            createWebPrintJob(binding.webView)
        }
    }

    private fun createWebPrintJob(webView: WebView) {
        val printManager = context!!.getSystemService(PRINT_SERVICE) as PrintManager
        val jobName = exportArgs.encounterName
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }
}