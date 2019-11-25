package de.enduni.monsterlair

import de.enduni.monsterlair.common.persistence.MonsterRepository
import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.common.persistence.database.MonsterDatabaseInitializer
import de.enduni.monsterlair.encounters.monsters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.monsters.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorDisplayModelMapper
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.monsters.datasource.MonsterAssetDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterEntityMapper
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModelMapper
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val monsterModule = module {

    // data source
    single<MonsterDataSource> { MonsterAssetDataSource(androidApplication()) }
    single { MonsterEntityMapper() }

    // domain
    single { RetrieveMonstersUseCase(get()) }


    // persistence
    single(createdAtStart = true) { MonsterDatabase.buildDatabase(androidApplication()) }
    single { get<MonsterDatabase>().monsterDao() }
    single { MonsterRepository(get(), get()) }
    single {
        MonsterDatabaseInitializer(
            get(),
            get(),
            get()
        )
    }

    // view
    single { MonsterListDisplayModelMapper() }
    viewModel { MonsterViewModel(get(), get()) }
}

val encounterModule = module {

    // domain
    single { CalculateEncounterBudgetUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get()) }
    single { EncounterCreatorDisplayModelMapper() }

    // view
    viewModel { EncounterCreatorViewModel(get(), get(), get()) }
    viewModel { EncounterViewModel() }
}