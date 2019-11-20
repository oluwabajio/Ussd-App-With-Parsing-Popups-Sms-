package tingtel.app.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Transfer {

    @PrimaryKey(autoGenerate = true)
    int id;

    String SimName;

    String SimUuid;

    String phoneNumber;

    String Amount;

    Date date;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
