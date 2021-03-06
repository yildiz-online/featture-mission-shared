/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */

package be.yildizgames.engine.feature.mission.task;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Grégory Van den Borre
 */
public class TaskIdTest {

    @Nested
    public class Constructor {

        @Test
        public void happyFlow() {
            TaskId.valueOf(1);
        }

        @Test
        public void withNegativeValue() {
            TaskId.valueOf(-1);
        }

    }

    /**
     * Different values can have same hc, so not tested.
     */
    @Nested
    public class Hashcode {

        @Test
        public void sameValues() {
            int i1 = TaskId.valueOf(1).hashCode();
            int i2 = TaskId.valueOf(1).hashCode();
            assertEquals(i1, i2);
        }
    }

    @Nested
    public class Equals {

        @Test
        public void sameValues() {
            TaskId i1 = TaskId.valueOf(1);
            TaskId i2 = TaskId.valueOf(1);
            assertEquals(i1, i2);
        }

        @Test
        public void differentId() {
            TaskId i1 = TaskId.valueOf(1);
            TaskId i2 = TaskId.valueOf(2);
            assertNotEquals(i1, i2);
        }

        @Test
        public void nullValue() {
            assertNotEquals(TaskId.valueOf(1), null);
        }

        @Test
        public void differentClass() {
            assertNotEquals(TaskId.valueOf(1), "ok");
        }
    }

}
