package com.yamblz.voltek.weather.repository;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteTableLockedException;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.DatabaseRepositoryImpl;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModelDao;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModelDao;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class DatabaseRepositoryTest {

    CityToIDModelDao cityToIDModelDao;
    FavoriteCityModelDao favoriteCityModelDao;
    DatabaseRepositoryImpl databaseRepository;
    DaoSession daoSession;

    @Before
    public void beforeEachTest() {

        favoriteCityModelDao = mock(FavoriteCityModelDao.class);
        cityToIDModelDao = mock(CityToIDModelDao.class);

        daoSession = mock(DaoSession.class);
        when(daoSession.getCityToIDModelDao()).thenReturn(cityToIDModelDao);
        when(daoSession.getFavoriteCityModelDao()).thenReturn(favoriteCityModelDao);
        databaseRepository = new DatabaseRepositoryImpl(daoSession);
    }

    @Test
    public void saveCityAsDublicate() throws InterruptedException {
        TestObserver testObserver = TestObserver.create();
        doThrow(new SQLiteConstraintException()).when(cityToIDModelDao).save(any());
        databaseRepository.saveCity(any()).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNoErrors();
    }

    @Test
    public void saveFavoriteEmitsEventWhenNotifyParameter() throws InterruptedException {

        CityToIDModel whatWeSave = new CityToIDModel("123", -1);

        int counts = 5;
        TestObserver testObserver = TestObserver.create();
        TestObserver completeTaskObserver = TestObserver.create();
        databaseRepository.getFavoritesAddedSubject().subscribe(testObserver);

        for(int i =0; i < counts; i++) {
            databaseRepository.saveAsFavoriteIfNotExists(whatWeSave, true).subscribe(completeTaskObserver);
        }

        completeTaskObserver.await();
        testObserver.assertValueCount(counts);
    }

    @Test
    public void saveFavoriteEmitsEventWhenNotifyParameterFalse() throws InterruptedException {

        CityToIDModel whatWeSave = new CityToIDModel("123", -1);

        int counts = 5;
        TestObserver testObserver = TestObserver.create();
        TestObserver completeTaskObserver = TestObserver.create();
        databaseRepository.getFavoritesAddedSubject().subscribe(testObserver);

        for(int i =0; i < counts; i++) {
            databaseRepository.saveAsFavoriteIfNotExists(whatWeSave, false).subscribe(completeTaskObserver);
        }

        completeTaskObserver.await();
        testObserver.assertNoValues();
    }



}
