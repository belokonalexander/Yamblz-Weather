package com.yamblz.voltek.weather.interactor;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

import org.greenrobot.greendao.DaoException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class FavoritesInteractorTest {

    FavoritesInteractor favoritesInteractor;
    RxMapper rxMapper = new RxMapper();
    DatabaseRepository databaseRepository;
    StorageRepository storageRepository;

    @Before
    public void beforeEachTest() {
        databaseRepository = mock(DatabaseRepository.class);
        storageRepository = mock(StorageRepository.class);
        favoritesInteractor = new FavoritesInteractor(databaseRepository, storageRepository, rxMapper);
    }


    @Test
    public void getFavoritesSearchesInSharedAndDB() throws InterruptedException {

        FavoriteCityModel fdbOne = new FavoriteCityModel("1", 1);
        FavoriteCityModel fdbTwo = new FavoriteCityModel("2", 2);
        FavoriteCityModel fdbThree = new FavoriteCityModel("3", 3);
        CityUIModel selected = new CityUIModel(3, "3");

        List<FavoriteCityModel> favoriteCityModels = new ArrayList<>(Arrays.asList(fdbOne, fdbTwo, fdbThree));


        when(databaseRepository.getFavorite()).thenReturn(Single.just(favoriteCityModels));
        when(storageRepository.getSelectedCity()).thenReturn(Single.just(selected));

        TestObserver<SetWithSelection<CityUIModel>> testObserver = TestObserver.create();

        favoritesInteractor.getFavorites().subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(databaseRepository, times(1)).getFavorite();
        verify(storageRepository, times(1)).getSelectedCity();
        testObserver.assertValue(new Predicate<SetWithSelection<CityUIModel>>() {
            @Override
            public boolean test(@NonNull SetWithSelection<CityUIModel> cityUIModels) throws Exception {
                return cityUIModels.getSelectedItem().equals(selected) && cityUIModels.getItems().size() == 3;
            }
        });
    }

    @Test
    public void selectCitySavesToSharedPrefs() throws InterruptedException {
        when(storageRepository.putSelectedCity(any(CityUIModel.class))).thenReturn(Completable.complete());
        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.selectCity(new CityUIModel(1, "")).subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();

        verify(storageRepository, times(1)).putSelectedCity(any());

    }

    @Test
    public void initDefaultSettingsAfterStartOneTime() throws InterruptedException {

        when(storageRepository.getApplicationStartFirst()).thenReturn(Single.just(false));
        when(storageRepository.fillByDefaultData()).thenReturn(Completable.complete());

        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.initDefaultsSettings().subscribe(testObserver);

        testObserver.await();
        testObserver.onComplete();
        ;
        verify(storageRepository, times(0)).fillByDefaultData();

    }

    @Test
    public void initDefaultSettingsStartOneTime() throws InterruptedException {

        when(storageRepository.getApplicationStartFirst()).thenReturn(Single.just(true));
        when(storageRepository.fillByDefaultData()).thenReturn(Completable.complete());

        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.initDefaultsSettings().subscribe(testObserver);
        testObserver.await();
        testObserver.onComplete();

        verify(storageRepository, times(1)).fillByDefaultData();

    }

    @Test
    public void deleteFromFavoritesWhenIsSelectedAndExistsAnother() throws InterruptedException {

        CityUIModel selected = new CityUIModel(1, "1");
        FavoriteCityModel topFavorite = new FavoriteCityModel("1", 1);
        CityUIModel favorite = new CityUIModel(topFavorite.getCityId(), topFavorite.getAlias());

        when(databaseRepository.deleteFromFavorites(any(CityUIModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getTopFavorite(1)).thenReturn(Single.just(topFavorite));
        when(storageRepository.putSelectedCity(favorite)).thenReturn(Completable.complete());

        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.deleteFromFavorites(selected, selected).subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(databaseRepository, times(1)).deleteFromFavorites(any());
        verify(databaseRepository, times(1)).getTopFavorite(1);
        verify(storageRepository, times(1)).putSelectedCity(favorite);
    }

    @Test
    public void deleteFromFavoritesWhenIsSelectedAndAnotherNotExists() throws InterruptedException {

        CityUIModel selected = new CityUIModel(1, "1");
        FavoriteCityModel topFavorite = new FavoriteCityModel("1", 1);
        CityUIModel favorite = new CityUIModel(topFavorite.getCityId(), topFavorite.getAlias());

        when(databaseRepository.deleteFromFavorites(any(CityUIModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getTopFavorite(1)).thenReturn(Single.error(new DaoException("no value")));
        when(storageRepository.putSelectedCity(favorite)).thenReturn(Completable.complete());

        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.deleteFromFavorites(selected, selected).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNotComplete();
        verify(databaseRepository, times(0)).deleteFromFavorites(any());
        verify(databaseRepository, times(1)).getTopFavorite(1);
        verify(storageRepository, times(0)).putSelectedCity(favorite);
    }

    @Test
    public void deleteFromFavoritesWhenIsNotSelected() throws InterruptedException {

        CityUIModel selected = new CityUIModel(1, "1");
        CityUIModel selected2 = new CityUIModel(2, "1");
        FavoriteCityModel topFavorite = new FavoriteCityModel("1", 1);
        CityUIModel favorite = new CityUIModel(topFavorite.getCityId(), topFavorite.getAlias());

        when(databaseRepository.deleteFromFavorites(any(CityUIModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getTopFavorite(1)).thenReturn(Single.error(new DaoException("no value")));
        when(storageRepository.putSelectedCity(favorite)).thenReturn(Completable.complete());

        TestObserver testObserver = TestObserver.create();

        favoritesInteractor.deleteFromFavorites(selected, selected2).subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(databaseRepository, times(1)).deleteFromFavorites(any());
        verify(databaseRepository, times(0)).getTopFavorite(1);
        verify(storageRepository, times(0)).putSelectedCity(favorite);
    }

    @Test
    public void favoriteDataAddedSubjectLinkedWithRepository() throws InterruptedException {
        when(databaseRepository.getFavoritesAddedSubject()).thenReturn(PublishSubject.create());
        TestObserver testObserver = TestObserver.create();
        databaseRepository.getFavoritesAddedSubject().subscribe(testObserver);

        testObserver.await(200, TimeUnit.MILLISECONDS);
        verify(databaseRepository, times(1)).getFavoritesAddedSubject();
    }

}
