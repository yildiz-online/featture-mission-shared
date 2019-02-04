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
import be.yildizgames.engine.feature.mission.reward.Reward;
import be.yildizgames.engine.feature.mission.reward.RewardFactory;
import be.yildizgames.engine.feature.mission.reward.RewardId;
import be.yildizgames.engine.feature.mission.reward.RewardManager;
import be.yildizgames.engine.feature.mission.task.Task;
import be.yildizgames.engine.feature.mission.task.TaskFactory;
import be.yildizgames.engine.feature.mission.task.TaskId;
import be.yildizgames.engine.feature.mission.task.TaskStatus;
import be.yildizgames.engine.feature.mission.task.TaskStatusListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Grégory Van den Borre
 */
public class MissionManagerTest {

    @Nested
    public class TaskCompleted {

        @Test
        public void noExistingStatus() {
            MissionManager<BaseMission> m = givenAMissionManager();
            m.prepareMission(MissionId.valueOf(1), PlayerId.valueOf(1));
            m.startMission(MissionId.valueOf(1), PlayerId.valueOf(1));
            m.taskCompleted(TaskId.valueOf(1), MissionId.valueOf(1), PlayerId.valueOf(1));
            Set<TaskStatus> status = m.getTaskStatusByMission(PlayerId.valueOf(1), MissionId.valueOf(1));
            Assertions.assertEquals(1, status.size());
            assertEquals("SUCCESS", status.iterator().next().status);
        }

        @Test
        public void replaceExistingStatus() {
            MissionManager<BaseMission> m = givenAMissionManager();
            m.prepareMission(MissionId.valueOf(1), PlayerId.valueOf(1));
            m.startMission(MissionId.valueOf(1), PlayerId.valueOf(1));
            m.updateTaskStatus(TaskId.valueOf(1), MissionId.valueOf(1), PlayerId.valueOf(1), "1/2");
            Set<TaskStatus> status = m.getTaskStatusByMission(PlayerId.valueOf(1), MissionId.valueOf(1));
            Assertions.assertEquals(1, status.size());
            assertEquals("1/2", status.iterator().next().status);
            m.taskCompleted(TaskId.valueOf(1), MissionId.valueOf(1), PlayerId.valueOf(1));
            status = m.getTaskStatusByMission(PlayerId.valueOf(1), MissionId.valueOf(1));
            Assertions.assertEquals(1, status.size());
            assertEquals("SUCCESS", status.iterator().next().status);
        }

    }

    private static MissionManager<BaseMission> givenAMissionManager() {
        MissionManager mm = new MissionManager<>(new TaskFactory() {
            @Override
            public Task createTask(TaskId id, PlayerId p, MissionId missionId) {
                return new Task() {
                    @Override
                    public void addListener(TaskStatusListener taskStatusListener) {

                    }

                    @Override
                    public TaskId getId() {
                        return id;
                    }

                    @Override
                    public boolean isCompleted() {
                        return false;
                    }

                    @Override
                    public boolean isFailed() {
                        return false;
                    }
                };
            }
        },
        new RewardManager(new RewardFactory() {
            @Override
            public void createReward(RewardId id) {

            }

            @Override
            public Reward getReward(RewardId id) {
                return player -> {};
            }
        }));
        BaseMission bm = new BaseMission(MissionId.valueOf(1), List.of(TaskId.valueOf(1)), p -> true, RewardId.valueOf(1));
        mm.registerMission(bm);
        return mm;
    }

}
