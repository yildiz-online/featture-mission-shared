/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 * Copyright (c) 2017 Grégory Van den Borre
 *
 * More infos available: https://www.yildiz-games.be
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 */

package be.yildizgames.engine.feature.mission.task;

import be.yildizgames.common.model.PlayerId;
import be.yildizgames.engine.feature.mission.MissionId;

/**
 * @author Grégory Van den Borre
 */
public class TaskRegisterer implements TaskStatusListener {

    //TODO the register goes in the factory: factory know true type -> register(ActionListener) register(destructionListener)...
    //The custom listener will be added in the derived class(like default register does)
    //and the custom registerer will be used in the custom factory!
    //keep maps map < taskid, ActionListener>... to remove them properly when required

    /*private final ActionManager action;

    private final Map<Pair<TaskId, PlayerId>, ActionListener> actionListenerRegistered = Maps.newMap();

    private final Map<Pair<TaskId, PlayerId>, DestructionListener> destructionListenerRegistered = Maps.newMap();

    private final Map<Pair<TaskId, PlayerId>, EntityConstructionListener> constructionListenerRegistered = Maps.newMap();

    public TaskRegisterer(ActionManager action) {
        this.action = action;
    }

    public final void registerTask(TaskId id, PlayerId player, ActionListener l) {
        this.action.addListener(l);
        this.actionListenerRegistered.put(new Pair<>(id, player), l);
    }

    public final void registerTask(TaskId id, PlayerId player, DestructionListener<T> l) {
        this.action.addDestructionListener(l);
        this.destructionListenerRegistered.put(new Pair<>(id, player), l);
    }

    public final void registerTask(TaskId id, PlayerId player, EntityConstructionListener<T> l) {
        this.action.addConstructionListener(l);
        this.constructionListenerRegistered.put(new Pair<>(id, player), l);
    }*/


    @Override
    public void taskCompleted(TaskId taskId, MissionId missionId, PlayerId playerId) {
        /*Optional
                .ofNullable(this.actionListenerRegistered.get(new Pair<>(taskId, playerId)))
                .ifPresent(this.action::removeListener);*/
    }

    /**
     * Fired when the task has failed to complete.
     *
     * @param taskId Id of the task failed.
     */
    @Override
    public void taskFailed(TaskId taskId, MissionId missionId, PlayerId playerId) {
        /*Optional
                .ofNullable(this.actionListenerRegistered.get(new Pair<>(taskId, playerId)))
                .ifPresent(this.action::removeListener);*/
    }
}
