package tingtel.app.Methods;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import tingtel.app.Interfaces.BalanceDao;
import tingtel.app.Models.Balance;
import tingtel.app.Models.DateConverter;

@Database(entities = {Balance.class},version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    private static final String DATABASE_NAME = "production";

    //livedata to monitor when database is being populated
    private final MutableLiveData<String> mIsDatabaseCreated = new MutableLiveData<>();


    public abstract BalanceDao balanceDao();


    public synchronized static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = getDatabaseInstance(context);
        }
        return mInstance;
    }



    public static AppDatabase getDatabaseInstance(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                //   getInstance(context).databaseInterface().insertAll(BanksCode.populateBanksCodes());

                                AppDatabase database = AppDatabase.getInstance(context);

                                database.mIsDatabaseCreated.postValue("populated");
                            }

                        });
                    }
                })
                .build();

    }

    public static void destroyInstance() {
        mInstance = null;

    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

}
