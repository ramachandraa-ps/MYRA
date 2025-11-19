package com.myrafriend.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.myrafriend.database.dao.MedicationDao;
import com.myrafriend.database.dao.MedicationDao_Impl;
import com.myrafriend.database.dao.MedicationIntakeDao;
import com.myrafriend.database.dao.MedicationIntakeDao_Impl;
import com.myrafriend.database.dao.SymptomLogDao;
import com.myrafriend.database.dao.SymptomLogDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SymptomLogDao _symptomLogDao;

  private volatile MedicationDao _medicationDao;

  private volatile MedicationIntakeDao _medicationIntakeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `symptom_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patient_id` INTEGER NOT NULL, `pain_level` INTEGER NOT NULL, `joint_count` INTEGER NOT NULL, `morning_stiffness` TEXT, `fatigue_level` INTEGER NOT NULL, `swelling` INTEGER NOT NULL, `warmth` INTEGER NOT NULL, `functional_ability` TEXT, `notes` TEXT, `log_date` TEXT, `created_at` TEXT, `synced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medications` (`id` INTEGER NOT NULL, `patient_id` INTEGER NOT NULL, `medication_name` TEXT, `dosage` TEXT, `frequency` TEXT, `timing` TEXT, `duration_weeks` INTEGER NOT NULL, `instructions` TEXT, `side_effects` TEXT, `prescribed_by` TEXT, `status` TEXT, `start_date` TEXT, `end_date` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medication_intake_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assigned_medication_id` INTEGER NOT NULL, `patient_id` INTEGER NOT NULL, `medication_name` TEXT, `status` TEXT, `intake_date` TEXT, `intake_time` TEXT, `notes` TEXT, `synced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '04e3ea71596f37aea5c21c92cb4ae724')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `symptom_logs`");
        db.execSQL("DROP TABLE IF EXISTS `medications`");
        db.execSQL("DROP TABLE IF EXISTS `medication_intake_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSymptomLogs = new HashMap<String, TableInfo.Column>(13);
        _columnsSymptomLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("patient_id", new TableInfo.Column("patient_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("pain_level", new TableInfo.Column("pain_level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("joint_count", new TableInfo.Column("joint_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("morning_stiffness", new TableInfo.Column("morning_stiffness", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("fatigue_level", new TableInfo.Column("fatigue_level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("swelling", new TableInfo.Column("swelling", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("warmth", new TableInfo.Column("warmth", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("functional_ability", new TableInfo.Column("functional_ability", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("log_date", new TableInfo.Column("log_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomLogs.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSymptomLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSymptomLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSymptomLogs = new TableInfo("symptom_logs", _columnsSymptomLogs, _foreignKeysSymptomLogs, _indicesSymptomLogs);
        final TableInfo _existingSymptomLogs = TableInfo.read(db, "symptom_logs");
        if (!_infoSymptomLogs.equals(_existingSymptomLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "symptom_logs(com.myrafriend.database.entities.SymptomLogEntity).\n"
                  + " Expected:\n" + _infoSymptomLogs + "\n"
                  + " Found:\n" + _existingSymptomLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsMedications = new HashMap<String, TableInfo.Column>(13);
        _columnsMedications.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("patient_id", new TableInfo.Column("patient_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("medication_name", new TableInfo.Column("medication_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("dosage", new TableInfo.Column("dosage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("frequency", new TableInfo.Column("frequency", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("timing", new TableInfo.Column("timing", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("duration_weeks", new TableInfo.Column("duration_weeks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("instructions", new TableInfo.Column("instructions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("side_effects", new TableInfo.Column("side_effects", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("prescribed_by", new TableInfo.Column("prescribed_by", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("start_date", new TableInfo.Column("start_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("end_date", new TableInfo.Column("end_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedications = new TableInfo("medications", _columnsMedications, _foreignKeysMedications, _indicesMedications);
        final TableInfo _existingMedications = TableInfo.read(db, "medications");
        if (!_infoMedications.equals(_existingMedications)) {
          return new RoomOpenHelper.ValidationResult(false, "medications(com.myrafriend.database.entities.MedicationEntity).\n"
                  + " Expected:\n" + _infoMedications + "\n"
                  + " Found:\n" + _existingMedications);
        }
        final HashMap<String, TableInfo.Column> _columnsMedicationIntakeLogs = new HashMap<String, TableInfo.Column>(9);
        _columnsMedicationIntakeLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("assigned_medication_id", new TableInfo.Column("assigned_medication_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("patient_id", new TableInfo.Column("patient_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("medication_name", new TableInfo.Column("medication_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("intake_date", new TableInfo.Column("intake_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("intake_time", new TableInfo.Column("intake_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationIntakeLogs.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedicationIntakeLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedicationIntakeLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedicationIntakeLogs = new TableInfo("medication_intake_logs", _columnsMedicationIntakeLogs, _foreignKeysMedicationIntakeLogs, _indicesMedicationIntakeLogs);
        final TableInfo _existingMedicationIntakeLogs = TableInfo.read(db, "medication_intake_logs");
        if (!_infoMedicationIntakeLogs.equals(_existingMedicationIntakeLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "medication_intake_logs(com.myrafriend.database.entities.MedicationIntakeEntity).\n"
                  + " Expected:\n" + _infoMedicationIntakeLogs + "\n"
                  + " Found:\n" + _existingMedicationIntakeLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "04e3ea71596f37aea5c21c92cb4ae724", "90bda6ac22c10663119678d1f8872ac2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "symptom_logs","medications","medication_intake_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `symptom_logs`");
      _db.execSQL("DELETE FROM `medications`");
      _db.execSQL("DELETE FROM `medication_intake_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SymptomLogDao.class, SymptomLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicationDao.class, MedicationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicationIntakeDao.class, MedicationIntakeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SymptomLogDao symptomLogDao() {
    if (_symptomLogDao != null) {
      return _symptomLogDao;
    } else {
      synchronized(this) {
        if(_symptomLogDao == null) {
          _symptomLogDao = new SymptomLogDao_Impl(this);
        }
        return _symptomLogDao;
      }
    }
  }

  @Override
  public MedicationDao medicationDao() {
    if (_medicationDao != null) {
      return _medicationDao;
    } else {
      synchronized(this) {
        if(_medicationDao == null) {
          _medicationDao = new MedicationDao_Impl(this);
        }
        return _medicationDao;
      }
    }
  }

  @Override
  public MedicationIntakeDao medicationIntakeDao() {
    if (_medicationIntakeDao != null) {
      return _medicationIntakeDao;
    } else {
      synchronized(this) {
        if(_medicationIntakeDao == null) {
          _medicationIntakeDao = new MedicationIntakeDao_Impl(this);
        }
        return _medicationIntakeDao;
      }
    }
  }
}
