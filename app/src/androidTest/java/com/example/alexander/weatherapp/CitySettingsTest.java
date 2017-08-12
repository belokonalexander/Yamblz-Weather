package com.example.alexander.weatherapp;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.data.database.AppDatabaseHelper;
import com.yamblz.voltek.weather.data.database.models.DaoMaster;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.presentation.ui.main.MainActivity;

import org.greenrobot.greendao.database.Database;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

//тесты некорректно работают при совместном запуске всех тестов в классе, подробнее:
//https://issuetracker.google.com/issues/37082857
//активити следующего теста запускается на непустых данных предыдущего теста
//по отдельности каждый тест работает корректно
@RunWith(AndroidJUnit4.class)
public class CitySettingsTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);


    @After
    public void afterEachTest() throws InterruptedException {
        activityTestRule.getActivity().getBaseContext().getSharedPreferences(StorageRepository.CATEGORY_SETTINGS, 0).edit().clear().apply();
        DaoMaster.OpenHelper helper = new AppDatabaseHelper(activityTestRule.getActivity().getBaseContext(),
                activityTestRule.getActivity().getResources().getString(R.string.database_name), null);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getFavoriteCityModelDao().deleteAll();
    }


    @Test
    public void citySettingsDefaltValue() throws InterruptedException {

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.nav_add_city))
                .perform(click());

        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogor"));

        String expectedSuggest = "Magnitogorsk";

        Thread.sleep(600);

        onView(withId(R.id.cities_recycler))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expectedSuggest)),
                        click()));

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expectedSuggest))));

    }

    @Test
    public void citySettingsHandwrittenValue() throws InterruptedException {

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.nav_add_city))
                .perform(click());

        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogorsk"), pressImeActionButton());

        String expectedSuggest = "Magnitogorsk";

        Thread.sleep(600);

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expectedSuggest))));

    }

    @Test
    public void citySettingsHandwrittenValueWithImprove() throws InterruptedException {

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.nav_add_city))
                .perform(click());

        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogorsk555"), pressImeActionButton());

        String expectedSuggest = "Magnitogorsk";

        Thread.sleep(600);

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expectedSuggest))));

    }

    @Test
    public void clearButtonWorks() throws InterruptedException {

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.nav_add_city))
                .perform(click());

        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogorsk"));

        onView(withId(R.id.clear_button)).perform(click());

        onView(withId(R.id.filter_edit_text))
                .check(matches(withText("")));

    }


    @Test
    public void deleteCityIfIsNotLast() throws InterruptedException {

        //create item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_add_city))
                .perform(click());
        String expected = "Magnitogorsk";
        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText(expected), pressImeActionButton());
        Thread.sleep(200);

        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expected))));

        Thread.sleep(200);
        //delete this item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expected)),
                        longClick()));

        //dialog is displayed
        onView(withText(R.string.delete_city)).check(matches(isDisplayed()));

        onView(withText(android.R.string.yes)).perform(click());

        Thread.sleep(100);

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(not(hasDescendant(withText(expected)))));

    }

    @Test
    public void deleteCityIfIsLast() throws InterruptedException {

        String expected = "Moscow";

        //create item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expected)),
                        longClick()));

        //dialog is displayed
        onView(withText(R.string.delete_city)).check(matches(isDisplayed()));

        onView(withText(android.R.string.yes)).perform(click());

        Thread.sleep(100);

        onView(withText(R.string.cant_delete_last_city)).
                inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));


        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expected))));

    }


}
