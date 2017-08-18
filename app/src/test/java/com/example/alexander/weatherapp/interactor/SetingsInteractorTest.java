package com.example.alexander.weatherapp.interactor;

import com.yamblz.voltek.weather.data.platform.jobs.JobWrapper;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class SetingsInteractorTest {


    SettingsInteractor settingsInteractor;
    JobWrapper jobWrapper;
    StorageRepository storageRepository;

    @Before
    public void beforeEachTest() {

        jobWrapper = mock(JobWrapper.class);
        storageRepository = mock(StorageRepository.class);

        settingsInteractor = new SettingsInteractor(jobWrapper, storageRepository);

    }

    @Test
    public void updateJobEnableTest() throws InterruptedException {
        when(jobWrapper.tryToStartWeatherJob()).thenReturn(Completable.complete());
        when(jobWrapper.disableWeatherJob()).thenReturn(Completable.complete());
        when(storageRepository.getUpdateEnabled()).thenReturn(Single.just(true));

        TestObserver testObserver = TestObserver.create();

        settingsInteractor.updateJob().subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(jobWrapper, times(1)).tryToStartWeatherJob();
        verify(jobWrapper, times(0)).disableWeatherJob();
    }

    @Test
    public void updateJobDisableTest() throws InterruptedException {
        when(jobWrapper.tryToStartWeatherJob()).thenReturn(Completable.complete());
        when(jobWrapper.disableWeatherJob()).thenReturn(Completable.complete());
        when(storageRepository.getUpdateEnabled()).thenReturn(Single.just(false));

        TestObserver testObserver = TestObserver.create();

        settingsInteractor.updateJob().subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(jobWrapper, times(0)).tryToStartWeatherJob();
        verify(jobWrapper, times(1)).disableWeatherJob();
    }



}
