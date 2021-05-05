package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.*;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HelperTest {

    @Before
    public void init() {
        System.out.println("init executed");
    }

    @After
    public void teardown() {
        System.out.println("teardown executed");
    }

    // This method must be public and static
    @BeforeClass
    public static void initClass() {
        System.out.println("init Class executed");
    }

    @AfterClass
    public static void teardownclass() {
        System.out.println("teardown Class executed");
    }

    @Test
    public void verify_ArrayListTest(){
        int[] yrsOfExperience = {13,4,15,6,17,8,19,1,2,3};
        int[] expected = {13,4,15,6,17,8,19,1,2,3};
        assertArrayEquals(expected, yrsOfExperience);
    }

    @Test
    public void testGetCount() {
        List<String> empNames = Arrays.asList("Ton", "", "John");
        final long count = Helper.getCount(empNames);
        assertEquals(1, count);
    }

    @Test
    public void validate_3lengthString() {
        List<String> empNames = Arrays.asList("sareeta", "", "Jeff","sam");
        assertEquals(1, Helper.getStringsOfLength3(empNames));
    }

    @Test
    public void verify_list_is_squared(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        List<Integer> expected = Arrays.asList(169, 16, 225, 36, 289, 64, 361, 1, 4, 9);
        assertEquals(expected, Helper.getSquareList(yrsOfExperience));
    }

    @Test
    public void testGetStats(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        assertEquals(19, stats.getMax());
        assertEquals(1, stats.getMin());
    }

    @Test
    public void testGetMergedList(){
        List<String> empNames = Arrays.asList("sareeta", "", "john","");
        String merged = Helper.getMergedList(empNames);
        assertEquals("sareeta, john", merged);
    }
}
