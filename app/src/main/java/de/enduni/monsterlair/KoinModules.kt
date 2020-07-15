package de.enduni.monsterlair

import de.enduni.monsterlair.common.datasource.hazard.HazardAssetDataSource
import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.datasource.monsters.MonsterAssetDataSource
import de.enduni.monsterlair.common.datasource.monsters.MonsterDataSource
import de.enduni.monsterlair.common.datasource.treasure.TreasureAssetDataSource
import de.enduni.monsterlair.common.datasource.treasure.TreasureDataSource
import de.enduni.monsterlair.common.persistence.database.DatabaseInitializer
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.creator.domain.*
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModelMapper
import de.enduni.monsterlair.creator.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.DeleteEncounterUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.persistence.EncounterEntityMapper
import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.encounters.view.EncounterDisplayModelMapper
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.hazards.domain.RetrieveHazardsUseCase
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardDisplayModelMapper
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.monsters.domain.DeleteMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.domain.SaveMonsterUseCase
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModelMapper
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import de.enduni.monsterlair.treasure.repository.TreasureEntityMapper
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import de.enduni.monsterlair.treasure.view.TreasureDisplayModelMapper
import de.enduni.monsterlair.treasure.view.TreasureViewModel
import de.enduni.monsterlair.update.UpdateManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module(createdAtStart = true) {

    single<MonsterDataSource> {
        MonsterAssetDataSource(
            androidApplication()
        )
    }
    single<HazardDataSource> {
        HazardAssetDataSource(
            androidApplication()
        )
    }
    single<TreasureDataSource> {
        TreasureAssetDataSource(
            androidApplication()
        )
    }
    single(createdAtStart = true) { MonsterDatabase.buildDatabase(androidApplication()) }
    single { get<MonsterDatabase>().monsterDao() }
    single { get<MonsterDatabase>().encounterDao() }
    single { get<MonsterDatabase>().hazardDao() }
    single { get<MonsterDatabase>().treasureDao() }
    single {
        DatabaseInitializer(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val appModule = module {

    single { UpdateManager(androidApplication()) }

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
    single { SaveMonsterUseCase(get()) }
    single { DeleteMonsterUseCase(get()) }


    // persistence
    single { MonsterRepository(get(), get()) }


    // view
    single { MonsterListDisplayModelMapper() }
    viewModel { MonsterViewModel(get(), get(), get(), get(), get()) }
}

val encounterModule = module {

    // persistence
    single { EncounterEntityMapper() }
    single { MonsterWithRoleMapper() }
    single { HazardWithRoleMapper() }
    single { EncounterRepository(get(), get(), get(), get(), get(), get()) }

    // domain
    single { ShowUserHintUseCase(androidApplication()) }
    single { CreateRandomEncounterUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get(), get()) }
    single { RetrieveHazardsWithRoleUseCase(get(), get()) }
    single { EncounterCreatorDisplayModelMapper() }
    single { RetrieveEncountersUseCase(get()) }
    single { DeleteEncounterUseCase(get()) }
    single { RetrieveEncounterUseCase(get()) }
    single { CreateEncounterTemplateUseCase(androidApplication()) }
    single { StoreEncounterUseCase(get()) }
    single { CreateTreasureRecommendationUseCase(androidApplication()) }

    // view
    single { EncounterDisplayModelMapper(androidApplication()) }
    viewModel {
        EncounterCreatorViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { EncounterViewModel(get(), get(), get(), get()) }
}

val treasureModule = module {

    single { TreasureEntityMapper() }
    single { TreasureDisplayModelMapper() }
    single { TreasureRepository(get(), get()) }

    viewModel { TreasureViewModel(get(), get()) }


}