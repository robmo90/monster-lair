package de.enduni.monsterlair.treasure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.FragmentTreasureBinding
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.view.TreasureViewModel
import de.enduni.monsterlair.treasure.view.adapter.TreasureListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TreasureFragment : Fragment(R.layout.fragment_treasure) {

    private lateinit var binding: FragmentTreasureBinding

    private lateinit var listAdapter: TreasureListAdapter

    private val viewModel: TreasureViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTreasureBinding.bind(view)

        listAdapter = TreasureListAdapter(requireActivity().layoutInflater, viewModel)

        binding.treasureRecyclerView.adapter = listAdapter
        viewModel.treasures.observe(viewLifecycleOwner, Observer { listAdapter.submitList(it) })
        viewModel.filter.observe(viewLifecycleOwner, Observer { adjustUi(it) })

        viewModel.start()
        bindUi()
    }

    private fun adjustUi(filter: TreasureFilter) {
        binding.levelButton.update(filter.lowerLevel, filter.upperLevel)
        binding.sortButton.update(filter.sortBy)
        binding.searchButton.update(filter.searchString)
    }

    private fun bindUi() {
        binding.searchButton.setup(requireActivity(), viewModel)
        binding.levelButton.setup(requireActivity(), viewModel)
        binding.sortButton.setup(requireActivity(), viewModel)
    }


}