package de.enduni.monsterlair

import android.content.Context
import de.enduni.monsterlair.common.datasource.hazard.HazardAssetDataSource
import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.datasource.monsters.MonsterAssetDataSource
import de.enduni.monsterlair.common.datasource.monsters.MonsterDataSource
import de.enduni.monsterlair.common.datasource.treasure.TreasureAssetDataSource
import de.enduni.monsterlair.common.datasource.treasure.TreasureDataSource
import de.enduni.monsterlair.common.persistence.database.DatabaseInitializer
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.common.persistence.database.MonsterDatabase
import de.enduni.monsterlair.common.sources.SourceManager
import de.enduni.monsterlair.common.view.CreateMonsterViewModel
import de.enduni.monsterlair.common.view.DarkModeManager
import de.enduni.monsterlair.creator.domain.*
import de.enduni.monsterlair.creator.view.*
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.DeleteEncounterUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.persistence.EncounterEntityMapper
import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.encounters.view.EncounterDisplayModelMapper
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardDisplayModelMapper
import de.enduni.monsterlair.hazards.view.HazardFilterStore
import de.enduni.monsterlair.hazards.view.HazardFilterViewModel
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.monsters.domain.DeleteMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.domain.SaveMonsterUseCase
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterFilterStore
import de.enduni.monsterlair.monsters.view.MonsterFilterViewModel
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModelMapper
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import de.enduni.monsterlair.settings.SettingsViewModel
import de.enduni.monsterlair.treasure.domain.CreateRandomTreasureTextUseCase
import de.enduni.monsterlair.treasure.domain.CreateRandomTreasureUseCase
import de.enduni.monsterlair.treasure.domain.CreateTreasureRecommendationUseCase
import de.enduni.monsterlair.treasure.repository.TreasureEntityMapper
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import de.enduni.monsterlair.treasure.view.TreasureDisplayModelMapper
import de.enduni.monsterlair.treasure.view.TreasureFilterStore
import de.enduni.monsterlair.treasure.view.TreasureFilterViewModel
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

    single {
        androidApplication().getSharedPreferences(
            "MonsterLair",
            Context.MODE_PRIVATE
        )
    }

    single { UpdateManager(get(), get()) }
    single { SourceManager(get()) }
    single { DarkModeManager(get()) }

    viewModel { SettingsViewModel(get(), get()) }

}

val hazardsModule = module {

    single { HazardEntityMapper() }
    single { HazardFilterStore() }

    single { HazardRepository(get(), get(), get()) }

    single { HazardDisplayModelMapper() }

    viewModel { HazardViewModel(get(), get(), get()) }
    viewModel { HazardFilterViewModel(get(), get()) }

}

val monsterModule = module {

    // data source
    single { MonsterEntityMapper() }

    single { MonsterFilterStore() }

    // domain
    single { RetrieveMonstersUseCase(get()) }
    single { RetrieveMonsterUseCase(get()) }
    single { SaveMonsterUseCase(get()) }
    single { DeleteMonsterUseCase(get(), get()) }


    // persistence
    single { MonsterRepository(get(), get(), get()) }


    // view
    single { MonsterListDisplayModelMapper() }

    viewModel { MonsterViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { MonsterFilterViewModel(get(), get()) }
    viewModel { CreateMonsterViewModel(get(), get(), get(), get()) }
}

val encounterModule = module {

    // persistence
    single { EncounterEntityMapper() }
    single { MonsterWithRoleMapper() }
    single { HazardWithRoleMapper() }
    single { EncounterRepository(get(), get(), get(), get(), get(), get()) }

    // domain
    single { EncounterCreatorFilterStore() }
    single { EncounterStore() }
    single { CreateRandomEncounterUseCase() }
    single { RetrieveMonstersWithRoleUseCase(get(), get()) }
    single { RetrieveHazardsWithRoleUseCase(get(), get()) }
    single { EncounterCreatorDisplayModelMapper() }
    single { RetrieveEncountersUseCase(get()) }
    single { DeleteEncounterUseCase(get()) }
    single { RetrieveEncounterUseCase(get()) }
    single { CreateEncounterTemplateUseCase(androidApplication()) }
    single { StoreEncounterUseCase(get()) }

    single { CreateTreasureRecommendationTextUseCase(androidApplication(), get()) }

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
            get()
        )
    }
    viewModel { EncounterCreatorFilterViewModel(get(), get(), get()) }
    viewModel { EncounterViewModel(get(), get(), get(), get()) }
}

val treasureModule = module {

    single { CreateTreasureRecommendationUseCase() }
    single { CreateRandomTreasureUseCase(get(), get()) }
    single { CreateRandomTreasureTextUseCase(androidApplication(), get()) }
    single { TreasureEntityMapper() }
    single { TreasureDisplayModelMapper() }
    single { TreasureRepository(get(), get(), get()) }
    single { TreasureFilterStore() }

    viewModel { TreasureViewModel(get(), get(), get(), get()) }
    viewModel { TreasureFilterViewModel(get(), get()) }


}