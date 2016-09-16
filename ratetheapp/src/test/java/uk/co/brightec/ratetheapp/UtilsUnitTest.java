/*
 * Copyright 2016 Brightec Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.brightec.ratetheapp;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {

    @Test
    public void formatDeviceName_normalInputs() throws Exception {
        Map<String[], String> testCases = new HashMap<>();
        testCases.put(new String[]{"Samsung", "S6"}, "Samsung S6"); // manufacturer and model, concat
        testCases.put(new String[]{"Samsung", "Samsung S6"}, "Samsung S6"); // manufacturer and model, combine

        for (String[] input : testCases.keySet()) {
            assertEquals(testCases.get(input), Utils.formatDeviceName(input[0], input[1]));
        }
    }

    @Test
    public void formatDeviceName_nullInputs() throws Exception {
        Map<String[], String> testCases = new HashMap<>();
        testCases.put(new String[]{"", "S6"}, "S6"); // empty manufacturer
        testCases.put(new String[]{"Samsung", ""}, "Samsung"); // empty model
        testCases.put(new String[]{"", ""}, ""); // empty model

        for (String[] input : testCases.keySet()) {
            assertEquals(testCases.get(input), Utils.formatDeviceName(input[0], input[1]));
        }
    }

    @Test
    public void capitalize_normalStrings() throws Exception {
        Map<String, String> testCases = new HashMap<>();
        testCases.put("t", "T"); // Single char only, change
        testCases.put("T", "T"); // Single char only, no change
        testCases.put("test", "Test"); // Single word only, change
        testCases.put("Test", "Test"); // Single word only, no change
        testCases.put("test test", "Test test"); // Single sentence only, change
        testCases.put("test Test", "Test Test"); // Single sentence only, no change
        testCases.put("testTest", "TestTest"); // Mixed case, change

        for (String input : testCases.keySet()) {
            assertEquals(testCases.get(input), Utils.capitalize(input));
        }
    }

    @Test
    public void capitalize_weirdStrings() throws Exception {
        Map<String, String> testCases = new HashMap<>();
        testCases.put(" t", " t"); // first char space, no change
        testCases.put("1", "1"); // only char number, no change
        testCases.put("1t", "1t"); // first char number, no change
        testCases.put("?", "?"); // only special char, no change
        testCases.put("?t", "?t"); // first special char, no change
        testCases.put("&amp;", "&amp;"); // only encoded char, no change
        testCases.put("&amp;t", "&amp;t"); // first encoded char, no change
        testCases.put("\u0061", "A"); // unicode lower case char, change
        testCases.put("\u2056", "\u2056"); // only unicode char, no change
        testCases.put("\u2056t", "\u2056t"); // first unicode char, no change

        for (String input : testCases.keySet()) {
            assertEquals(testCases.get(input), Utils.capitalize(input));
        }
    }

    @Test
    public void capitalize_nullStrings() throws Exception {
        Map<String, String> testCases = new HashMap<>();
        testCases.put(null, ""); // null returns empty
        testCases.put("", ""); // empty returns empty

        for (String input : testCases.keySet()) {
            assertEquals(testCases.get(input), Utils.capitalize(input));
        }
    }
}
