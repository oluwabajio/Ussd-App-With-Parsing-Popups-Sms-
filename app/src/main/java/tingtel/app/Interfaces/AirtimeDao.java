package tingtel.app.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tingtel.app.Models.Airtime;

@Dao
public interface AirtimeDao {
    @Query("SELECT * FROM Airtime")
    List<Airtime> getAllItems();

    @Insert
    void insertAll(Airtime... Airtime);

    @Insert
    void insert(Airtime Airtime);


    @Query("DELETE FROM Airtime")
    void delete();

//    @Query("SELECT * FROM Airtime WHERE HistoryDate = :HistoryDate")
//    public List<Airtime> getItemsByDate(String HistoryDate);

//    @Query("SELECT * FROM Airtime WHERE sim LIKE :sim")
//    public List<Airtime> getItemsBySim(String sim);
//
//    @Query("SELECT * FROM Airtime WHERE HistoryId = :HistoryId")
//    public List<Airtime> getItemsById(String HistoryId);

    @Query("SELECT * FROM Airtime WHERE SimUuid = :iccid  ORDER BY id DESC LIMIT 1")
    public Airtime getLastAirtime(String iccid);


}
