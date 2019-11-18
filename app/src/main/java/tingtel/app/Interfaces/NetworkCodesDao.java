package tingtel.app.Interfaces;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tingtel.app.Models.NetworksCode;

@Dao
public interface NetworkCodesDao {

    @Query("SELECT * FROM NetworksCode")
    List<NetworksCode> getAllItems();

    @Insert
    void insertAll(NetworksCode... NetworksCode);

    @Query("DELETE FROM NetworksCode")
    void delete();

    @Query("SELECT * FROM NetworksCode WHERE name = :name")
    public List<NetworksCode> getItemsByName(String name);

}
