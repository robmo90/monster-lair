package de.enduni.monsterlair

import de.enduni.monsterlair.common.persistence.EncounterEntityMapper
import de.enduni.monsterlair.common.persistence.EncounterRepository
import de.enduni.monsterlair.common.persistence.MonsterRepository
import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.common.persistence.database.MonsterDatabaseInitializer
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModelMapper
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.MonsterWithRoleMapper
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
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

    // persistence
    single { get<MonsterDatabase>().encounterDao() }
    single { EncounterEntityMapper() }
    single { EncounterRepository(get(), get()) }

    // domain
    single { MonsterWithRoleMapper() }
    single { CalculateEncounterBudgetUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get(), get()) }
    single { EncounterCreatorDisplayModelMapper() }
    single { RetrieveEncountersUseCase(get(), get(), get()) }
    single { RetrieveEncounterUseCase(get(), get(), get()) }

    // view
    viewModel { EncounterCreatorViewModel(get(), get(), get(), get(), get()) }
    viewModel { EncounterViewModel(get(), get()) }
}