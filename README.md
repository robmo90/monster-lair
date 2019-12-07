# Monster Lair

Monster Lair is an Android app to build and calculate encounters for Pathfinder 2nd Edition by Paizo. Its written mostly to ease my GM life and test some technology.

The app uses Kotlin and AndroidX, and relies heavily on Koin, Coroutines, Room and Navigation Components.

Since the app also uses ViewBinding by Google, you need to use Android Studio 3.6+, currently only in Beta.

## Contributing

Feel free to contribute and create a pull request if you want to. I will get to work on it as soon as possible. Please understand that since the app is published under my name, I will take extra care to check the code. :)

The app roughly follows clean architecture and uses separate models for separate domains. The structure for a feature is:

* fragment
* view package - viewState, display models, Mapping from domain to display models, list adapters, etc.
* domain package - useCases with domain / business logic, Domain models
* repository package - repository with DAOs, mapping from entity to domain models

Classes that can and will be used in the whole application are in the common package.

## TODOs

If you want, grab one of these todos, or add your own. They are listed in the priority I have for them.

### Possible Features
* More filters for monsters and hazards, especially in the encounter creator fragment (e.g. only monsters / only hazards / all, filter by type, etc)
* Create random encounters from filtered monsters
* Environmental filters (e.g. urban, mountain, jungle, etc.) - would need some categorizing
* Search in App Bar to make more efficient use of the phone layout OR make app bar disappear on scroll
* Treasure list with filters
* Add treasures to an encounter
* 'Campaign' tracker - plan out encounters with treasures for certain levels of your campaign
* Offline statblocks
* Printing statblocks in the Web View / PDF
* Monster and Hazard creation

### Technical Stuff
* Save encounters automatically and make the lists etc immutable
* Tests