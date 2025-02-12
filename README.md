## MyBinder - a digital Pokémon trading card binder

### Getting Started

#### APK

To get started simply download the latest provided APK
available [here](https://github.com/mf-jpeg/MyBinder/releases).

```
This APK is perfectly usable but does not provides an API key.
This means requests are subject to rate limitation.
```

#### Cloning

To avoid being rate limited you can clone the project and add a key.
<br />You can get one for free by following [this](https://docs.pokemontcg.io) link.

```
git clone https://github.com/mf-jpeg/MyBinder
```

And change the ```local.properties``` file.

```
sdk.dir=<path to sdk>
API_KEY=<API key>
```

### Instructions

Tap the buttons on the bottom navigation bar to navigate to each feature.

#### Deck (WIP)

Craft your next tournament-worthy deck.

#### Binder (WIP)

Manage your personal card collection.

#### Card Search

Look up a card by setting any combination of the following parameters:

* Name
* Type
* Set
* Order

#### Settings (WIP)

Customize how the app behaves on your device.

## Libraries & Frameworks

* UI: Jetpack Compose, Material
* Threading: Kotlin Coroutines
* Navigation: Jetpack Navigation
* Networking: Retrofit (GSON)
* Image Loading: Coil
* Database: Room
* Background Work: WorkManager

## License

Licensed under the MIT license. See ```LICENSE``` for details.