package de.enduni.monsterlair

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.enduni.monsterlair.databinding.FragmentLicensesBinding

class LicenseFragment : Fragment() {

    private lateinit var binding: FragmentLicensesBinding

    private val exportArgs: LicenseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLicensesBinding.bind(view)
        val encodedHtml = Base64.encodeToString(exportArgs.page.toByteArray(), Base64.NO_PADDING)
        binding.webView.loadData(encodedHtml, "text/html", "base64")
    }
}