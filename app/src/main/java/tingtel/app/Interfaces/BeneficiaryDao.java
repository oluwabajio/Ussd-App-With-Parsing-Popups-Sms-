package tingtel.app.Interfaces;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tingtel.app.Models.Beneficiary;

@Dao
public interface BeneficiaryDao {

    @Query("SELECT * FROM Beneficiary")
    List<Beneficiary> getAllItems();

    @Insert
    void insertAll(Beneficiary... Beneficiary);

    @Insert
    void insert(Beneficiary Beneficiary);


    @Query("DELETE FROM Beneficiary")
    void delete();
}
