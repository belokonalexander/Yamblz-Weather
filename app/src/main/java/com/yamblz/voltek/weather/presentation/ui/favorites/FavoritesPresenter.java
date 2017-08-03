package com.yamblz.voltek.weather.presentation.ui.favorites;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created on 03.08.2017.
 */

@InjectViewState
public class FavoritesPresenter extends BasePresenter<FavoritesView>{

    private FavoritesInteractor interactor;
    private Disposable getFavoritesTask;

    public FavoritesPresenter(FavoritesInteractor interactor) {
        this.interactor = interactor;

    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        initFavorites();
    }

    private void initFavorites() {

        getFavoritesTask = interactor.getFavorites()
                .subscribe(this::onSuccesLoadFavorites, this::onError);
        compositeDisposable.addAll(getFavoritesTask);

    }


    private void onSuccesLoadFavorites(List<CityUIModel> cityUIModels){
        if(cityUIModels.size()==0) {
            cityUIModels.add(new CityUIModel(524901, "Moscow"));
        }
        getViewState().setFavoritesItems(cityUIModels);
    }

    private void onError(Throwable throwable) {
        LogUtils.log("Error: " + throwable, throwable);
    }

}
