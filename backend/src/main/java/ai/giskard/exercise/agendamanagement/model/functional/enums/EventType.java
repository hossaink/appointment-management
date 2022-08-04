package ai.giskard.exercise.agendamanagement.model.functional.enums;

import java.io.Serializable;

public enum EventType implements Serializable {
    UNSET((short) 0),
    AVAILABILITY((short)1),
    APPOINTMENT((short)2),
    ABSENCE((short)3); //TODO

    private final short val;

    EventType(short val) {
        this.val = val;
    }

    public static EventType GetValue(short id){
        for (EventType value :
                EventType.values()) {
            if (value.val == id) return value;
        }
        return  null;
    }
}
