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

import be.yildizgames.common.model.PlayerCreationListener;
import be.yildizgames.common.model.PlayerId;
import be.yildizgames.engine.feature.mission.reward.RewardManager;
import be.yildizgames.engine.feature.mission.task.TaskFactory;
import be.yildizgames.engine.feature.mission.task.TaskId;
import be.yildizgames.engine.feature.mission.task.TaskStatus;
import be.yildizgames.engine.feature.mission.task.TaskStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The mission manager will handle the mission creation and assignment to ad hoc players.
 *
 * @author Grégory Van den Borre
 */
public class MissionManager <T extends Mission> implements TaskStatusListener, PlayerCreationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionManager.class);

    /**
     * The list of all possible missions.
     */
    private final Map<MissionId, T> availableMissions = new HashMap<>();

    private final Map<PlayerId, Set<MissionId>> missionsReady = new HashMap<>();

    private final Map<PlayerId, Set<MissionId>> activeMissions = new HashMap<>();

    private final List<MissionStatusListener<T>> listeners = new ArrayList<>();

    private final Map<PlayerId, Set<TaskStatus>> taskStatus = new HashMap<>();

    /**
     * Factory to build the tasks required by the missions.
     */
    private final TaskFactory taskFactory;

    private final RewardManager rewardManager;

    public MissionManager(final TaskFactory taskFactory, RewardManager rewardManager) {
        super();
        this.taskFactory = taskFactory;
        this.taskFactory.addTaskListener(this);
        this.rewardManager = rewardManager;
    }

    public final void registerMission(final T mission) {
        this.availableMissions.put(mission.getId(), mission);
    }

    public final void prepareMission(final MissionId missionId, final PlayerId playerId) {
        this.missionsReady.computeIfAbsent(playerId, PlayerId -> new HashSet<>()).add(missionId);
        T mission = this.availableMissions.get(missionId);
        mission.getTasks().forEach(t -> this.taskFactory.createTask(t, playerId, mission.getId()));
        this.listeners.forEach(l->l.missionReady(mission, playerId));
    }

    public final void startMission(final MissionId missionId, final PlayerId playerId) {
        this.missionsReady.get(playerId).remove(missionId);
        this.activeMissions.computeIfAbsent(playerId, PlayerId -> new HashSet<>()).add(missionId);
        T mission = this.availableMissions.get(missionId);
        this.listeners.forEach(l->l.missionStarted(mission, playerId));
    }

    public final void initialize(final PlayerMissionStatus pms) {
        switch (pms.status) {
            case STARTED:
                this.activeMissions.computeIfAbsent(pms.player, PlayerId -> new HashSet<>()).add(pms.id);
                break;
            case WAITING_FOR_ACCEPTANCE:
                this.missionsReady.computeIfAbsent(pms.player, PlayerId -> new HashSet<>()).add(pms.id);
                T mission = this.availableMissions.get(pms.id);
                mission.getTasks().forEach(t -> this.taskFactory.createTask(t, pms.player, pms.id));
                break;
            default:
                throw new IllegalArgumentException(pms.status + " is not a valid option.");
        }
    }

    @Override
    public final void taskCompleted(final TaskId taskId, MissionId missionId, final PlayerId playerId) {
        TaskStatus status = new TaskStatus(taskId, missionId, "SUCCESS");
        Set<TaskStatus> statusSet = this.taskStatus.computeIfAbsent(playerId, playerId1 -> new HashSet<>());
        statusSet.remove(status);
        statusSet.add(status);
        boolean success = this.checkMissionCompleted(missionId, playerId);
        if(success) {
            T mission = this.availableMissions.get(missionId);
            this.activeMissions.get(playerId).remove(mission.getId());
            this.listeners.forEach(l -> l.missionSuccess(mission, playerId));
            this.rewardManager.rewardPlayer(playerId, mission.getReward());
        }
    }

    @Override
    public final void taskFailed(TaskId taskId, MissionId missionId, PlayerId playerId) {
        TaskStatus status = new TaskStatus(taskId, missionId, "FAILED");
        Set<TaskStatus> statusSet = this.taskStatus.computeIfAbsent(playerId, playerId1 -> new HashSet<>());
        statusSet.remove(status);
        statusSet.add(status);
        T mission = this.availableMissions.get(missionId);
        if(mission.hasTask(taskId)) {
            this.activeMissions.get(playerId).remove(mission.getId());
            this.listeners.forEach(l -> l.missionFailed(mission, playerId));
        } else {
            LOGGER.warn("{} does not exists for mission {} task failed for player: {}",taskId, missionId, playerId);
        }
    }

    public void updateTaskStatus(TaskId taskId, MissionId missionId, PlayerId playerId, String status) {
        if(status.equalsIgnoreCase("SUCCESS")) {
            this.taskCompleted(taskId, missionId, playerId);
        } else if(status.equalsIgnoreCase("FAILED")) {
            this.taskFailed(taskId, missionId, playerId);
        } else {
            this.taskStatus.computeIfAbsent(playerId, playerId1 -> new HashSet<>())
                    .add(new TaskStatus(taskId, missionId, status));
        }
    }

    public final void addMissionListener(MissionStatusListener<T> listener) {
        assert listener != null;
        this.listeners.add(listener);
    }

    @Override
    public final void playerCreated(PlayerId player) {
        availableMissions.values()
                .stream()
                .filter(m -> m.canStartFor(player))
                .forEach(m -> {
                    this.prepareMission(m.getId(), player);
                    this.startMission(m.getId(), player);
                });
    }

    public final Set<MissionId> getMissionReady(PlayerId p) {
        return this.missionsReady.getOrDefault(p, new HashSet<>());
    }

    public final Set<MissionId> getMissionActive(PlayerId p) {
        return this.activeMissions.getOrDefault(p, new HashSet<>());
    }

    public final Set<TaskStatus> getTaskStatus(PlayerId p) {
        return this.taskStatus.get(p);
    }

    public final Set<TaskStatus> getTaskStatusByMission(PlayerId p, MissionId id) {
        return this.taskStatus.get(p).stream().filter(t -> t.missionId.value == id.value)
                .collect(Collectors.toSet());
    }

    private boolean checkMissionCompleted(final MissionId missionId, final PlayerId playerId) {
        T m = this.availableMissions.get(missionId);
        for(TaskId tid : m.getTasks()) {
            TaskStatus task = this.taskStatus.computeIfAbsent(playerId, PlayerId -> new HashSet<>())
                    .stream()
                    .filter(t -> t.id.equals(tid))
                    .findFirst().orElseThrow(AssertionError::new);
            if(!task.status.equalsIgnoreCase("SUCCESS")) {
                return false;
            }
        }
        return true;
    }
}