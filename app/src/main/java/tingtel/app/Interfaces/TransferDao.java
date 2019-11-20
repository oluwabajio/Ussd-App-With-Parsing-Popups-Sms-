package tingtel.app.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tingtel.app.Models.Transfer;

@Dao
public interface TransferDao {


    @Query("SELECT * FROM Transfer")
    List<Transfer> getAllItems();

    @Insert
    void insertAll(Transfer... Transfer);

    @Insert
    void insert(Transfer Transfer);


    @Query("DELETE FROM Transfer")
    void delete();
}
