package ai.giskard.exercise.agendamanagement.model.persistance.enums;

import java.io.Serializable;

public enum RepetitionType implements Serializable {
    NOT_REPEATED((short) 0),
    DAILY((short)1),
    WEEKLY((short)2),
    MONTHLY((short)3);

    private final short val;

    RepetitionType(short val) {
        this.val = val;
    }

    public static RepetitionType GetValue(short id){
        for (RepetitionType value :
                RepetitionType.values()) {
            if (value.val == id) return value;
        }
        return  null;
    }
}
