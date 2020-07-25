package de.enduni.monsterlair.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.LicenseActivity
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.sources.SelectSourcesDialog
import de.enduni.monsterlair.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        viewModel.sources.observe(viewLifecycleOwner, Observer { sources ->
            if (sources.size == Source.values().size) {
                binding.sourceEntryCaption.text =
                    requireContext().resources.getString(R.string.sources_all_sources)
            } else {
                binding.sourceEntryCaption.text =
                    sources.joinToString { requireContext().resources.getString(it.getStringRes()) }
            }

            binding.sourceEntry.setOnClickListener {
                SelectSourcesDialog().show(
                    requireActivity(),
                    sources,
                    viewModel
                )
            }
        })

        viewModel.listen()

        binding.libraryLicenses.setOnClickListener {
            startActivity(LicenseActivity.libraryIntent(requireContext(), "licenses.html"))
        }
        binding.contentLicenses.setOnClickListener {
            startActivity(LicenseActivity.libraryIntent(requireContext(), "other-licenses.html"))
        }

    }
}