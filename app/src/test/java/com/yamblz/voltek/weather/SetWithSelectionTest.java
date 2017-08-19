package com.yamblz.voltek.weather;

import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created on 09.08.2017.
 */

public class SetWithSelectionTest {

    SetWithSelection<Integer> integers;

    @Before
    public void beforeEachTest() {
        integers = new SetWithSelection<>();
    }

    @Test
    public void selectedItemInTheSet() {

        Integer[] array = {1, 3, 2, 4, 6, 7};
        Integer selected = 10;

        Set<Integer> arr = new TreeSet<>(Arrays.asList(array));

        integers = new SetWithSelection<>(arr, selected);

        assertEquals(selected, integers.getSelectedItem());
        assertTrue(integers.getItems().contains(selected));

    }

    @Test
    public void selectedItemInTheSetWhileAlreadyIn() {

        Integer[] array = {1, 3, 2, 4, 6, 7, 10};
        Integer selected = 10;

        Set<Integer> arr = new TreeSet<>(Arrays.asList(array));

        integers = new SetWithSelection<>(arr, selected);

        assertEquals(selected, integers.getSelectedItem());
        assertTrue(integers.getItems().contains(selected));

    }

    @Test
    public void addAsSelectedTest() {

        Integer[] array = {1, 3, 2, 4, 6, 7, 10};
        Integer selected = 10;

        Set<Integer> arr = new TreeSet<>(Arrays.asList(array));

        integers = new SetWithSelection<>(arr, selected);

        selected = 11;
        integers.addAsSelected(selected);
        assertEquals(selected, integers.getSelectedItem());
        assertTrue(integers.getItems().contains(selected));

        selected = 12;
        integers.addAsSelected(selected);
        assertEquals(selected, integers.getSelectedItem());
        assertTrue(integers.getItems().contains(selected));

    }

    @Test
    public void deleteFromSet() {

        Integer[] array = {1, 3, 2, 4, 6, 7, 10};
        Integer selected = 10;

        Set<Integer> arr = new TreeSet<>(Arrays.asList(array));

        integers = new SetWithSelection<>(arr, selected);

        integers.delete(selected);

        assertEquals(null, integers.getSelectedItem());
        assertFalse(integers.getItems().contains(selected));

    }

    @Test
    public void deleteAndSelect() {

        Integer[] array = {1, 3, 2, 4, 6, 7, 10};
        Integer selected = 10;
        Integer selectedAfter = 11;

        Set<Integer> arr = new TreeSet<>(Arrays.asList(array));

        integers = new SetWithSelection<>(arr, selected);

        integers.deleteAndSetSelected(selected,selectedAfter);

        assertEquals(selectedAfter, integers.getSelectedItem());
        assertFalse(integers.getItems().contains(selected));
        assertTrue(integers.getItems().contains(selectedAfter));

    }

}
