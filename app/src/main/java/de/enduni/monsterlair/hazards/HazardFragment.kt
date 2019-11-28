package de.enduni.monsterlair.hazards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.FragmentHazardsBinding
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.hazards.view.adapter.HazardListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HazardFragment : Fragment() {

    private val viewModel: HazardViewModel by viewModel()

    private lateinit var binding: FragmentHazardsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_hazards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHazardsBinding.bind(view)
        val listAdapter = HazardListAdapter(activity!!.layoutInflater, viewModel)

        binding.hazardRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(this, Observer {
            listAdapter.submitList(it.hazards)
        })
    }
}