package com.myrafriend.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.myrafriend.database.entities.MedicationEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MedicationDao_Impl implements MedicationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MedicationEntity> __insertionAdapterOfMedicationEntity;

  private final EntityDeletionOrUpdateAdapter<MedicationEntity> __updateAdapterOfMedicationEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPatientId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMedicationStatus;

  public MedicationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicationEntity = new EntityInsertionAdapter<MedicationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medications` (`id`,`patient_id`,`medication_name`,`dosage`,`frequency`,`timing`,`duration_weeks`,`instructions`,`side_effects`,`prescribed_by`,`status`,`start_date`,`end_date`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MedicationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPatientId());
        if (entity.getMedicationName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMedicationName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDosage());
        }
        if (entity.getFrequency() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFrequency());
        }
        if (entity.getTiming() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTiming());
        }
        statement.bindLong(7, entity.getDurationWeeks());
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        if (entity.getSideEffects() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSideEffects());
        }
        if (entity.getPrescribedBy() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPrescribedBy());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getStatus());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getEndDate());
        }
      }
    };
    this.__updateAdapterOfMedicationEntity = new EntityDeletionOrUpdateAdapter<MedicationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `medications` SET `id` = ?,`patient_id` = ?,`medication_name` = ?,`dosage` = ?,`frequency` = ?,`timing` = ?,`duration_weeks` = ?,`instructions` = ?,`side_effects` = ?,`prescribed_by` = ?,`status` = ?,`start_date` = ?,`end_date` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MedicationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPatientId());
        if (entity.getMedicationName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMedicationName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDosage());
        }
        if (entity.getFrequency() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFrequency());
        }
        if (entity.getTiming() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTiming());
        }
        statement.bindLong(7, entity.getDurationWeeks());
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        if (entity.getSideEffects() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSideEffects());
        }
        if (entity.getPrescribedBy() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPrescribedBy());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getStatus());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getEndDate());
        }
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteByPatientId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medications WHERE patient_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMedicationStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE medications SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final MedicationEntity medication) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMedicationEntity.insert(medication);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<MedicationEntity> medications) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMedicationEntity.insert(medications);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final MedicationEntity medication) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMedicationEntity.handle(medication);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByPatientId(final int patientId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByPatientId.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, patientId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteByPatientId.release(_stmt);
    }
  }

  @Override
  public void updateMedicationStatus(final int medicationId, final String status) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMedicationStatus.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, medicationId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateMedicationStatus.release(_stmt);
    }
  }

  @Override
  public LiveData<List<MedicationEntity>> getActiveMedications(final int patientId) {
    final String _sql = "SELECT * FROM medications WHERE patient_id = ? AND status = 'active' ORDER BY start_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medications"}, false, new Callable<List<MedicationEntity>>() {
      @Override
      @Nullable
      public List<MedicationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTiming = CursorUtil.getColumnIndexOrThrow(_cursor, "timing");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_weeks");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSideEffects = CursorUtil.getColumnIndexOrThrow(_cursor, "side_effects");
          final int _cursorIndexOfPrescribedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribed_by");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final List<MedicationEntity> _result = new ArrayList<MedicationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationEntity _item;
            _item = new MedicationEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpPatientId;
            _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
            _item.setPatientId(_tmpPatientId);
            final String _tmpMedicationName;
            if (_cursor.isNull(_cursorIndexOfMedicationName)) {
              _tmpMedicationName = null;
            } else {
              _tmpMedicationName = _cursor.getString(_cursorIndexOfMedicationName);
            }
            _item.setMedicationName(_tmpMedicationName);
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            _item.setDosage(_tmpDosage);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            _item.setFrequency(_tmpFrequency);
            final String _tmpTiming;
            if (_cursor.isNull(_cursorIndexOfTiming)) {
              _tmpTiming = null;
            } else {
              _tmpTiming = _cursor.getString(_cursorIndexOfTiming);
            }
            _item.setTiming(_tmpTiming);
            final int _tmpDurationWeeks;
            _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            _item.setDurationWeeks(_tmpDurationWeeks);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            _item.setInstructions(_tmpInstructions);
            final String _tmpSideEffects;
            if (_cursor.isNull(_cursorIndexOfSideEffects)) {
              _tmpSideEffects = null;
            } else {
              _tmpSideEffects = _cursor.getString(_cursorIndexOfSideEffects);
            }
            _item.setSideEffects(_tmpSideEffects);
            final String _tmpPrescribedBy;
            if (_cursor.isNull(_cursorIndexOfPrescribedBy)) {
              _tmpPrescribedBy = null;
            } else {
              _tmpPrescribedBy = _cursor.getString(_cursorIndexOfPrescribedBy);
            }
            _item.setPrescribedBy(_tmpPrescribedBy);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            _item.setStartDate(_tmpStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            _item.setEndDate(_tmpEndDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<MedicationEntity>> getAllMedications(final int patientId) {
    final String _sql = "SELECT * FROM medications WHERE patient_id = ? ORDER BY start_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medications"}, false, new Callable<List<MedicationEntity>>() {
      @Override
      @Nullable
      public List<MedicationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTiming = CursorUtil.getColumnIndexOrThrow(_cursor, "timing");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_weeks");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSideEffects = CursorUtil.getColumnIndexOrThrow(_cursor, "side_effects");
          final int _cursorIndexOfPrescribedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribed_by");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final List<MedicationEntity> _result = new ArrayList<MedicationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationEntity _item;
            _item = new MedicationEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpPatientId;
            _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
            _item.setPatientId(_tmpPatientId);
            final String _tmpMedicationName;
            if (_cursor.isNull(_cursorIndexOfMedicationName)) {
              _tmpMedicationName = null;
            } else {
              _tmpMedicationName = _cursor.getString(_cursorIndexOfMedicationName);
            }
            _item.setMedicationName(_tmpMedicationName);
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            _item.setDosage(_tmpDosage);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            _item.setFrequency(_tmpFrequency);
            final String _tmpTiming;
            if (_cursor.isNull(_cursorIndexOfTiming)) {
              _tmpTiming = null;
            } else {
              _tmpTiming = _cursor.getString(_cursorIndexOfTiming);
            }
            _item.setTiming(_tmpTiming);
            final int _tmpDurationWeeks;
            _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            _item.setDurationWeeks(_tmpDurationWeeks);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            _item.setInstructions(_tmpInstructions);
            final String _tmpSideEffects;
            if (_cursor.isNull(_cursorIndexOfSideEffects)) {
              _tmpSideEffects = null;
            } else {
              _tmpSideEffects = _cursor.getString(_cursorIndexOfSideEffects);
            }
            _item.setSideEffects(_tmpSideEffects);
            final String _tmpPrescribedBy;
            if (_cursor.isNull(_cursorIndexOfPrescribedBy)) {
              _tmpPrescribedBy = null;
            } else {
              _tmpPrescribedBy = _cursor.getString(_cursorIndexOfPrescribedBy);
            }
            _item.setPrescribedBy(_tmpPrescribedBy);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            _item.setStartDate(_tmpStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            _item.setEndDate(_tmpEndDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<MedicationEntity> getMedicationById(final int medicationId) {
    final String _sql = "SELECT * FROM medications WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medications"}, false, new Callable<MedicationEntity>() {
      @Override
      @Nullable
      public MedicationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTiming = CursorUtil.getColumnIndexOrThrow(_cursor, "timing");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_weeks");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSideEffects = CursorUtil.getColumnIndexOrThrow(_cursor, "side_effects");
          final int _cursorIndexOfPrescribedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribed_by");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final MedicationEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new MedicationEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _result.setId(_tmpId);
            final int _tmpPatientId;
            _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
            _result.setPatientId(_tmpPatientId);
            final String _tmpMedicationName;
            if (_cursor.isNull(_cursorIndexOfMedicationName)) {
              _tmpMedicationName = null;
            } else {
              _tmpMedicationName = _cursor.getString(_cursorIndexOfMedicationName);
            }
            _result.setMedicationName(_tmpMedicationName);
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            _result.setDosage(_tmpDosage);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            _result.setFrequency(_tmpFrequency);
            final String _tmpTiming;
            if (_cursor.isNull(_cursorIndexOfTiming)) {
              _tmpTiming = null;
            } else {
              _tmpTiming = _cursor.getString(_cursorIndexOfTiming);
            }
            _result.setTiming(_tmpTiming);
            final int _tmpDurationWeeks;
            _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            _result.setDurationWeeks(_tmpDurationWeeks);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            _result.setInstructions(_tmpInstructions);
            final String _tmpSideEffects;
            if (_cursor.isNull(_cursorIndexOfSideEffects)) {
              _tmpSideEffects = null;
            } else {
              _tmpSideEffects = _cursor.getString(_cursorIndexOfSideEffects);
            }
            _result.setSideEffects(_tmpSideEffects);
            final String _tmpPrescribedBy;
            if (_cursor.isNull(_cursorIndexOfPrescribedBy)) {
              _tmpPrescribedBy = null;
            } else {
              _tmpPrescribedBy = _cursor.getString(_cursorIndexOfPrescribedBy);
            }
            _result.setPrescribedBy(_tmpPrescribedBy);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _result.setStatus(_tmpStatus);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            _result.setStartDate(_tmpStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            _result.setEndDate(_tmpEndDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getActiveMedicationCount(final int patientId) {
    final String _sql = "SELECT COUNT(*) FROM medications WHERE patient_id = ? AND status = 'active'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medications"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
