package tingtel.app.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Data {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String SimName;

    private String SimUuid;

    private String Message;

    private float DataBalance;


    public Data(String simName, String simUuid, String message, float dataBalance) {
        SimName = simName;
        SimUuid = simUuid;
        Message = message;
        DataBalance = dataBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSimName() {
        return SimName;
    }

    public void setSimName(String simName) {
        SimName = simName;
    }

    public String getSimUuid() {
        return SimUuid;
    }

    public void setSimUuid(String simUuid) {
        SimUuid = simUuid;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public float getDataBalance() {
        return DataBalance;
    }

    public void setDataBalance(float dataBalance) {
        DataBalance = dataBalance;
    }
}
