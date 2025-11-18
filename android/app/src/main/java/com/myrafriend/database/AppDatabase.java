package com.myrafriend.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myrafriend.database.dao.MedicationDao;
import com.myrafriend.database.dao.MedicationIntakeDao;
import com.myrafriend.database.dao.SymptomLogDao;
import com.myrafriend.database.entities.MedicationEntity;
import com.myrafriend.database.entities.MedicationIntakeEntity;
import com.myrafriend.database.entities.SymptomLogEntity;

/**
 * Room Database for My RA Friend
 * Provides offline data caching and sync capability
 */
@Database(
        entities = {
                SymptomLogEntity.class,
                MedicationEntity.class,
                MedicationIntakeEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "myrafriend_db";
    private static volatile AppDatabase instance;

    // DAOs
    public abstract SymptomLogDao symptomLogDao();
    public abstract MedicationDao medicationDao();
    public abstract MedicationIntakeDao medicationIntakeDao();

    /**
     * Get database instance (singleton)
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }

    /**
     * Clear all data from database
     */
    public void clearAllData() {
        new Thread(() -> {
            symptomLogDao().deleteByPatientId(0); // Clears all
            medicationDao().deleteByPatientId(0);
            medicationIntakeDao().deleteByPatientId(0);
        }).start();
    }
}
