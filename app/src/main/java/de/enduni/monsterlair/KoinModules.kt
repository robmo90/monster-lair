package de.enduni.monsterlair

import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.common.persistence.database.MonsterDatabaseInitializer
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveHazardsWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.domain.StoreEncounterUseCase
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModelMapper
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.persistence.EncounterEntityMapper
import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.hazards.datasource.HazardAssetDataSource
import de.enduni.monsterlair.hazards.datasource.HazardDataSource
import de.enduni.monsterlair.hazards.domain.RetrieveHazardsUseCase
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardDisplayModelMapper
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.monsters.datasource.MonsterAssetDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.domain.RetrieveMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModelMapper
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module(createdAtStart = true) {

    single<MonsterDataSource> { MonsterAssetDataSource(androidApplication()) }
    single<HazardDataSource> { HazardAssetDataSource(androidApplication()) }
    single(createdAtStart = true) { MonsterDatabase.buildDatabase(androidApplication()) }
    single { get<MonsterDatabase>().monsterDao() }
    single { get<MonsterDatabase>().encounterDao() }
    single { get<MonsterDatabase>().hazardDao() }
    single {
        MonsterDatabaseInitializer(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val hazardsModule = module {

    single { HazardEntityMapper() }

    single { RetrieveHazardsUseCase(get()) }

    single { HazardRepository(get(), get()) }

    single { HazardDisplayModelMapper() }

    viewModel { HazardViewModel(get(), get()) }

}

val monsterModule = module {

    // data source
    single { MonsterEntityMapper() }

    // domain
    single { RetrieveMonstersUseCase(get()) }
    single { RetrieveMonsterUseCase(get()) }


    // persistence
    single { MonsterRepository(get(), get()) }


    // view
    single { MonsterListDisplayModelMapper() }
    viewModel { MonsterViewModel(get(), get(), get()) }
}

val encounterModule = module {

    // persistence
    single { EncounterEntityMapper() }
    single { MonsterWithRoleMapper() }
    single { HazardWithRoleMapper() }
    single { EncounterRepository(get(), get(), get(), get(), get(), get()) }

    // domain
    single { CalculateEncounterBudgetUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get(), get()) }
    single { RetrieveHazardsWithRoleUseCase(get(), get()) }
    single { EncounterCreatorDisplayModelMapper() }
    single { RetrieveEncountersUseCase(get()) }
    single { RetrieveEncounterUseCase(get()) }
    single { CreateEncounterTemplateUseCase(androidApplication(), get()) }
    single { StoreEncounterUseCase(get()) }

    // view
    viewModel { EncounterCreatorViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { EncounterViewModel(get(), get(), get(), get()) }
}