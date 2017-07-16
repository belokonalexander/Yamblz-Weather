# Yamblz.Weather
Приложение для первого этапа проекта Яндекс.Мобилизация 2017.

## Скриншоты
![Screenshot](https://raw.githubusercontent.com/IvanAntsiferov/Yamblz-Weather/master/docs/images/Screenshot1.png)
![Screenshot](https://raw.githubusercontent.com/IvanAntsiferov/Yamblz-Weather/master/docs/images/Screenshot2.png)
![Screenshot](https://raw.githubusercontent.com/IvanAntsiferov/Yamblz-Weather/master/docs/images/Screenshot3.png)

## Сборка проекта
Приложение использует [OpenWeatherMap API](https://openweathermap.org/api) для получения данных. Запишите свой ключ в `secret.properties` как переменную `ApiKey`.

Для того, чтобы собрать release конфигурацию, нужно создать файл `keystore.properties` в корневой папке проекта с переменными `storeFile`, `storePassword`, `keyAlias`, `keyPassword` содержащими информацию о KeyStore.

## Используемые библиотеки
* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [LeakCanary](https://github.com/square/leakcanary)
* [Retrofit](https://github.com/square/retrofit)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Hawk](https://github.com/orhanobut/hawk)
* [Timber](https://github.com/JakeWharton/timber)
* [AndroidJob](https://github.com/evernote/android-job)
