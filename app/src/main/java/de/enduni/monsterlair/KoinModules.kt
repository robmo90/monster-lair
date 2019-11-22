package de.enduni.monsterlair

import de.enduni.monsterlair.overview.FilterMonstersUseCase
import de.enduni.monsterlair.overview.MonsterRepository
import de.enduni.monsterlair.overview.MonsterViewModel
import de.enduni.monsterlair.overview.RetrieveMonstersUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {

    single { MonsterRepository(get()) }
    single { RetrieveMonstersUseCase(get()) }
    single { FilterMonstersUseCase(get()) }

    viewModel { MonsterViewModel(get(), get()) }


}
