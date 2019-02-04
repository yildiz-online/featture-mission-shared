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

package be.yildizgames.engine.feature.mission;

import be.yildizgames.common.model.PlayerId;
import be.yildizgames.engine.feature.mission.reward.RewardId;
import be.yildizgames.engine.feature.mission.task.TaskId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Grégory Van den Borre
 */
public class MissionTest {

    static Mission givenANewMission() {
        List<TaskId> l = Collections.singletonList(TaskId.valueOf(5L));
        return new BaseMission(MissionId.valueOf(1), l, p -> true, RewardId.valueOf(1));
    }

    @Nested
    public class Constructor {

        private final MissionId id = MissionId.valueOf(2);

        @Test
        public void happyFlow() {
            givenANewMission();
        }

        @Test
        public void withNullList() {
            Assertions.assertThrows(AssertionError.class, () -> new BaseMission(id, null, p -> true, RewardId.valueOf(1)));
        }

        @Test
        public void withNullPrerequisite() {
            List<TaskId> l = Collections.singletonList(TaskId.valueOf(5L));
            Assertions.assertThrows(AssertionError.class, () -> new BaseMission(id, l, null, RewardId.valueOf(1)));
        }

        @Test
        public void withEmptyTaskList() {
            List<TaskId> l = new ArrayList<>();
            Assertions.assertThrows(IllegalArgumentException.class, () -> new BaseMission(id, l, p -> true, RewardId.valueOf(1)));
        }

    }

    @Nested
    public class CanStart {

        private final MissionId id = MissionId.valueOf(2);

        @Test
        public void withTruePrerequisite() {
            List<TaskId> l = new ArrayList<>();
            l.add(TaskId.valueOf(5L));
            Mission m = new BaseMission(id, l, p -> true, RewardId.valueOf(1));
            assertTrue(m.canStartFor(PlayerId.WORLD));
        }

        @Test
        public void withFalsePrerequisite() {
            List<TaskId> l = new ArrayList<>();
            l.add(TaskId.valueOf(5L));
            Mission m = new BaseMission(id, l, p -> false, RewardId.valueOf(1));
            assertFalse(m.canStartFor(PlayerId.WORLD));
        }
    }
}
