package com.hlprmnky.widgco.deciders.statemachine;

import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.hlprmnky.widgco.deciders.statemachine.states.Failed;
import com.hlprmnky.widgco.deciders.statemachine.states.WorkflowStart;

import java.util.List;

public abstract class State {

    // uuuuuugh and yet, since you can't switch on class name...
    public enum STATE_TYPE { INITIAL, COMPLETED, FAILED, START, VALIDATE_WIDGETS, VALIDATE_INVENTORY }

    public static State runStateMachine(List<HistoryEvent> events) {
        State state = new WorkflowStart();
        while(!(state instanceof Failed) && !events.isEmpty()) {
            state = state.process(events.remove(0));
        }
        return state;
    }

    private HistoryEvent event;

    public State() {}
    public State withEvent(HistoryEvent event) {
        this.event = event;
        return this;
    }

    public HistoryEvent getEvent() { return this.event; }

    public abstract State process(HistoryEvent event);
    public abstract STATE_TYPE getType();
}
