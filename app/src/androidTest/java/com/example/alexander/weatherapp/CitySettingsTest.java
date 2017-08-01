package com.example.alexander.weatherapp;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.orhanobut.hawk.Hawk;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class CitySettingsTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void beforeEachTest() {
        Hawk.deleteAll();
    }

    @Test
    public void citySettingsDefaltValue() throws InterruptedException {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        onView(withId(R.id.city_name_settings_view)).check(matches(not(withText(""))));

    }

    @Test
    public void citySettingsSuggestionsWork() throws InterruptedException {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        onView(withId(R.id.city_name_settings_view)).perform(clearText(), typeText("Magnitogor"));

        Thread.sleep(600);

        String expectedSuggest = "Magnitogorsk";

        onData(instanceOf(CityUIModel.class)).inRoot(RootMatchers.isPlatformPopup()).atPosition(0)
                .check(matches(withText(expectedSuggest)))
                .perform(click());

        onView(withId(R.id.city_name_settings_view)).check(matches(withText(expectedSuggest)));

        //go away and come back
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_about));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        //check the city was saved
        onView(withId(R.id.city_name_settings_view)).check(matches(withText(expectedSuggest)));
    }


    @Test
    public void citySettingsHandwriteWorks() throws InterruptedException {

        String typedCity = "Magnitogorsk";

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        onView(withId(R.id.city_name_settings_view)).perform(clearText(), typeText(typedCity), pressImeActionButton());

        Thread.sleep(600);


        //go away and come back
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_about));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        //check the city was saved
        onView(withId(R.id.city_name_settings_view)).check(matches(withText(typedCity)));
    }

    @Test
    public void citySettingsAutocorrctionWorks() throws InterruptedException {

        if (ApiUtils.isConnected(activityTestRule.getActivity().getBaseContext())) {

            String typedCity = "Magnitogorsk12345";
            String correctCity = "Magnitogorsk";

            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
            onView(withId(R.id.city_name_settings_view)).perform(clearText(), typeText(typedCity), pressImeActionButton());

            Thread.sleep(600);


            //go away and come back
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_about));
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));

            //check the city was saved
            onView(withId(R.id.city_name_settings_view)).check(matches(withText(correctCity)));
        }
    }

}
