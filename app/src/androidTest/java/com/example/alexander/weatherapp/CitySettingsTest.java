package com.example.alexander.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.alexander.weatherapp.resources.RecyclerIsNotEmptyResource;
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


@RunWith(AndroidJUnit4.class)
public class CitySettingsTest {

    private IdlingResource resource;

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
        IdlingRegistry.getInstance().unregister(resource);
    }

    @Test
    @SuppressLint("RestrictedApi")
    public void stickyItemsAreInMenu() {
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        Context context = activityTestRule.getActivity().getBaseContext();

        MenuBuilder menuBuilder = new MenuBuilder(context);
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.sticky_footer_drawer, menuBuilder);

        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem menuItem = menuBuilder.getItem(i);
            onView(withId(R.id.material_drawer_sticky_footer))
                    .check(matches(hasDescendant(withText(menuItem.getTitle().toString()))));
        }

    }

    @Test
    public void citySettingsDefaultValue() throws InterruptedException {
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        onView(withText(R.string.nav_add_city))
                .perform(click());

        resource = new RecyclerIsNotEmptyResource(activityTestRule.getActivity().findViewById(R.id.cities_recycler));
        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogor"));


        IdlingRegistry.getInstance().register(resource);
        String expectedSuggest = "Magnitogorsk";

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

        resource = new RecyclerIsNotEmptyResource(activityTestRule.getActivity().findViewById(R.id.material_drawer_recycler_view));

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_add_city))
                .perform(click());
        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogorsk"), pressImeActionButton());

        String expectedSuggest = "Magnitogorsk";
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        IdlingRegistry.getInstance().register(resource);

        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expectedSuggest))));

    }

    @Test
    public void citySettingsHandwrittenValueWithImprove() throws InterruptedException {

        resource = new RecyclerIsNotEmptyResource(activityTestRule.getActivity().findViewById(R.id.material_drawer_recycler_view));

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_add_city))
                .perform(click());

        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText("Magnitogorsk555"), pressImeActionButton());
        String expectedSuggest = "Magnitogorsk";

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        IdlingRegistry.getInstance().register(resource);

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

        resource = new RecyclerIsNotEmptyResource(activityTestRule.getActivity().findViewById(R.id.material_drawer_recycler_view));

        //create item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_add_city))
                .perform(click());
        String expected = "Magnitogorsk";
        onView(withId(R.id.filter_edit_text)).perform(clearText(), typeText(expected), pressImeActionButton());

        //delete this item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());

        IdlingRegistry.getInstance().register(resource);
        onView(withId(R.id.material_drawer_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expected)),
                        longClick()));

        IdlingRegistry.getInstance().unregister(resource);
        //dialog is displayed
        onView(withText(R.string.delete_city)).check(matches(isDisplayed()));

        onView(withText(android.R.string.yes)).perform(click());

        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(not(hasDescendant(withText(expected)))));

    }

    @Test
    public void deleteCityIfIsLast() throws InterruptedException {

        String expected = activityTestRule.getActivity().getBaseContext().getString(R.string.default_city_name);

        //create item
        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expected)),
                        longClick()));

        //dialog is displayed
        onView(withText(R.string.delete_city)).check(matches(isDisplayed()));

        onView(withText(android.R.string.yes)).perform(click());

        onView(withText(R.string.cant_delete_last_city)).
                inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));


        onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.material_drawer_recycler_view))
                .check(matches(hasDescendant(withText(expected))));

    }


}
