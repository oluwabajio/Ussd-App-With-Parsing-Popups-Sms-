package tingtel.app.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tingtel.app.Models.Balance;

@Dao
public interface BalanceDao {
    @Query("SELECT * FROM Balance")
    List<Balance> getAllItems();

    @Insert
    void insertAll(Balance... Balance);

    @Insert
    void insert(Balance Balance);


    @Query("DELETE FROM Balance")
    void delete();

//    @Query("SELECT * FROM Balance WHERE HistoryDate = :HistoryDate")
//    public List<Balance> getItemsByDate(String HistoryDate);

//    @Query("SELECT * FROM Balance WHERE sim LIKE :sim")
//    public List<Balance> getItemsBySim(String sim);
//
//    @Query("SELECT * FROM Balance WHERE HistoryId = :HistoryId")
//    public List<Balance> getItemsById(String HistoryId);

    @Query("SELECT * FROM Balance WHERE SimUuid = :iccid AND type = :type  ORDER BY id DESC LIMIT 1")
    public Balance getLastAirtimeOrData(String iccid, String type);

    @Query("SELECT * FROM  Balance WHERE simuuid LIKE :simiccid")
    public List<Balance> getItemsByUuid(String simiccid);

    @Query("SELECT * FROM Balance WHERE SimUuid = :iccid AND type = :type")
    public List<Balance>  getAirtimeOrDataList(String iccid, String type);

}
