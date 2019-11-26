package de.enduni.monsterlair

import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.common.persistence.database.MonsterDatabaseInitializer
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModelMapper
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.persistence.EncounterEntityMapper
import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.monsters.datasource.MonsterAssetDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModelMapper
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module(createdAtStart = true) {

    single(createdAtStart = true) { MonsterDatabase.buildDatabase(androidApplication()) }
    single { get<MonsterDatabase>().monsterDao() }
    single { get<MonsterDatabase>().encounterDao() }
    single {
        MonsterDatabaseInitializer(
            get(),
            get(),
            get()
        )
    }
}

val monsterModule = module {

    // data source
    single<MonsterDataSource> { MonsterAssetDataSource(androidApplication()) }
    single { MonsterEntityMapper() }

    // domain
    single { RetrieveMonstersUseCase(get()) }


    // persistence
    single { MonsterRepository(get(), get()) }


    // view
    single { MonsterListDisplayModelMapper() }
    viewModel { MonsterViewModel(get(), get()) }
}

val encounterModule = module {

    // persistence
    single { EncounterEntityMapper() }
    single { MonsterWithRoleMapper() }
    single { EncounterRepository(get(), get(), get(), get()) }

    // domain
    single { CalculateEncounterBudgetUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get(), get()) }
    single { EncounterCreatorDisplayModelMapper() }
    single { RetrieveEncountersUseCase(get()) }
    single { RetrieveEncounterUseCase(get()) }

    // view
    viewModel { EncounterCreatorViewModel(get(), get(), get(), get(), get()) }
    viewModel { EncounterViewModel(get(), get()) }
}