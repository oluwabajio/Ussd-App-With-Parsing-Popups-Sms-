package tingtel.app.Models;

import android.animation.PropertyValuesHolder;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Airtime {

    @PrimaryKey(autoGenerate = true)
    int id;

     String SimName;

    String SimUuid;

    String Message;

    Date date;

    float AirtimeBalance;


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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAirtimeBalance() {
        return AirtimeBalance;
    }

    public void setAirtimeBalance(float airtimeBalance) {
        AirtimeBalance = airtimeBalance;
    }
}
