package com.myrafriend.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.myrafriend.database.entities.MedicationIntakeEntity;
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
public final class MedicationIntakeDao_Impl implements MedicationIntakeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MedicationIntakeEntity> __insertionAdapterOfMedicationIntakeEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPatientId;

  public MedicationIntakeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicationIntakeEntity = new EntityInsertionAdapter<MedicationIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medication_intake_logs` (`id`,`assigned_medication_id`,`patient_id`,`medication_name`,`status`,`intake_date`,`intake_time`,`notes`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MedicationIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAssignedMedicationId());
        statement.bindLong(3, entity.getPatientId());
        if (entity.getMedicationName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMedicationName());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getIntakeDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getIntakeDate());
        }
        if (entity.getIntakeTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getIntakeTime());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE medication_intake_logs SET synced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByPatientId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medication_intake_logs WHERE patient_id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final MedicationIntakeEntity intakeLog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfMedicationIntakeEntity.insertAndReturnId(intakeLog);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void markAsSynced(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfMarkAsSynced.release(_stmt);
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
  public LiveData<List<MedicationIntakeEntity>> getIntakeLogsByPatientId(final int patientId) {
    final String _sql = "SELECT * FROM medication_intake_logs WHERE patient_id = ? ORDER BY intake_date DESC, intake_time DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<List<MedicationIntakeEntity>>() {
      @Override
      @Nullable
      public List<MedicationIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAssignedMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigned_medication_id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIntakeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_date");
          final int _cursorIndexOfIntakeTime = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_time");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<MedicationIntakeEntity> _result = new ArrayList<MedicationIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationIntakeEntity _item;
            _item = new MedicationIntakeEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpAssignedMedicationId;
            _tmpAssignedMedicationId = _cursor.getInt(_cursorIndexOfAssignedMedicationId);
            _item.setAssignedMedicationId(_tmpAssignedMedicationId);
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
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpIntakeDate;
            if (_cursor.isNull(_cursorIndexOfIntakeDate)) {
              _tmpIntakeDate = null;
            } else {
              _tmpIntakeDate = _cursor.getString(_cursorIndexOfIntakeDate);
            }
            _item.setIntakeDate(_tmpIntakeDate);
            final String _tmpIntakeTime;
            if (_cursor.isNull(_cursorIndexOfIntakeTime)) {
              _tmpIntakeTime = null;
            } else {
              _tmpIntakeTime = _cursor.getString(_cursorIndexOfIntakeTime);
            }
            _item.setIntakeTime(_tmpIntakeTime);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item.setSynced(_tmpSynced);
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
  public LiveData<List<MedicationIntakeEntity>> getIntakeLogsByMedicationId(
      final int medicationId) {
    final String _sql = "SELECT * FROM medication_intake_logs WHERE assigned_medication_id = ? ORDER BY intake_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<List<MedicationIntakeEntity>>() {
      @Override
      @Nullable
      public List<MedicationIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAssignedMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigned_medication_id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIntakeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_date");
          final int _cursorIndexOfIntakeTime = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_time");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<MedicationIntakeEntity> _result = new ArrayList<MedicationIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationIntakeEntity _item;
            _item = new MedicationIntakeEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpAssignedMedicationId;
            _tmpAssignedMedicationId = _cursor.getInt(_cursorIndexOfAssignedMedicationId);
            _item.setAssignedMedicationId(_tmpAssignedMedicationId);
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
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpIntakeDate;
            if (_cursor.isNull(_cursorIndexOfIntakeDate)) {
              _tmpIntakeDate = null;
            } else {
              _tmpIntakeDate = _cursor.getString(_cursorIndexOfIntakeDate);
            }
            _item.setIntakeDate(_tmpIntakeDate);
            final String _tmpIntakeTime;
            if (_cursor.isNull(_cursorIndexOfIntakeTime)) {
              _tmpIntakeTime = null;
            } else {
              _tmpIntakeTime = _cursor.getString(_cursorIndexOfIntakeTime);
            }
            _item.setIntakeTime(_tmpIntakeTime);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item.setSynced(_tmpSynced);
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
  public LiveData<List<MedicationIntakeEntity>> getIntakeLogsByDateRange(final int patientId,
      final String startDate, final String endDate) {
    final String _sql = "SELECT * FROM medication_intake_logs WHERE patient_id = ? AND intake_date >= ? AND intake_date <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    _argIndex = 3;
    if (endDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endDate);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<List<MedicationIntakeEntity>>() {
      @Override
      @Nullable
      public List<MedicationIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAssignedMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigned_medication_id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIntakeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_date");
          final int _cursorIndexOfIntakeTime = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_time");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<MedicationIntakeEntity> _result = new ArrayList<MedicationIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationIntakeEntity _item;
            _item = new MedicationIntakeEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpAssignedMedicationId;
            _tmpAssignedMedicationId = _cursor.getInt(_cursorIndexOfAssignedMedicationId);
            _item.setAssignedMedicationId(_tmpAssignedMedicationId);
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
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpIntakeDate;
            if (_cursor.isNull(_cursorIndexOfIntakeDate)) {
              _tmpIntakeDate = null;
            } else {
              _tmpIntakeDate = _cursor.getString(_cursorIndexOfIntakeDate);
            }
            _item.setIntakeDate(_tmpIntakeDate);
            final String _tmpIntakeTime;
            if (_cursor.isNull(_cursorIndexOfIntakeTime)) {
              _tmpIntakeTime = null;
            } else {
              _tmpIntakeTime = _cursor.getString(_cursorIndexOfIntakeTime);
            }
            _item.setIntakeTime(_tmpIntakeTime);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item.setSynced(_tmpSynced);
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
  public List<MedicationIntakeEntity> getUnsyncedIntakeLogs() {
    final String _sql = "SELECT * FROM medication_intake_logs WHERE synced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAssignedMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigned_medication_id");
      final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
      final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfIntakeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_date");
      final int _cursorIndexOfIntakeTime = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_time");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<MedicationIntakeEntity> _result = new ArrayList<MedicationIntakeEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final MedicationIntakeEntity _item;
        _item = new MedicationIntakeEntity();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpAssignedMedicationId;
        _tmpAssignedMedicationId = _cursor.getInt(_cursorIndexOfAssignedMedicationId);
        _item.setAssignedMedicationId(_tmpAssignedMedicationId);
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
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpIntakeDate;
        if (_cursor.isNull(_cursorIndexOfIntakeDate)) {
          _tmpIntakeDate = null;
        } else {
          _tmpIntakeDate = _cursor.getString(_cursorIndexOfIntakeDate);
        }
        _item.setIntakeDate(_tmpIntakeDate);
        final String _tmpIntakeTime;
        if (_cursor.isNull(_cursorIndexOfIntakeTime)) {
          _tmpIntakeTime = null;
        } else {
          _tmpIntakeTime = _cursor.getString(_cursorIndexOfIntakeTime);
        }
        _item.setIntakeTime(_tmpIntakeTime);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final boolean _tmpSynced;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _tmpSynced = _tmp != 0;
        _item.setSynced(_tmpSynced);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<Integer> getTakenCount(final int medicationId, final String startDate) {
    final String _sql = "SELECT COUNT(*) FROM medication_intake_logs WHERE assigned_medication_id = ? AND status = 'taken' AND intake_date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<Integer>() {
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

  @Override
  public LiveData<Integer> getTotalDoses(final int medicationId, final String startDate) {
    final String _sql = "SELECT COUNT(*) FROM medication_intake_logs WHERE assigned_medication_id = ? AND intake_date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<Integer>() {
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

  @Override
  public LiveData<List<MedicationIntakeEntity>> getTodayIntakeLogs(final int patientId,
      final String date) {
    final String _sql = "SELECT * FROM medication_intake_logs WHERE patient_id = ? AND intake_date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    _argIndex = 2;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"medication_intake_logs"}, false, new Callable<List<MedicationIntakeEntity>>() {
      @Override
      @Nullable
      public List<MedicationIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAssignedMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigned_medication_id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfMedicationName = CursorUtil.getColumnIndexOrThrow(_cursor, "medication_name");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIntakeDate = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_date");
          final int _cursorIndexOfIntakeTime = CursorUtil.getColumnIndexOrThrow(_cursor, "intake_time");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<MedicationIntakeEntity> _result = new ArrayList<MedicationIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationIntakeEntity _item;
            _item = new MedicationIntakeEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpAssignedMedicationId;
            _tmpAssignedMedicationId = _cursor.getInt(_cursorIndexOfAssignedMedicationId);
            _item.setAssignedMedicationId(_tmpAssignedMedicationId);
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
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpIntakeDate;
            if (_cursor.isNull(_cursorIndexOfIntakeDate)) {
              _tmpIntakeDate = null;
            } else {
              _tmpIntakeDate = _cursor.getString(_cursorIndexOfIntakeDate);
            }
            _item.setIntakeDate(_tmpIntakeDate);
            final String _tmpIntakeTime;
            if (_cursor.isNull(_cursorIndexOfIntakeTime)) {
              _tmpIntakeTime = null;
            } else {
              _tmpIntakeTime = _cursor.getString(_cursorIndexOfIntakeTime);
            }
            _item.setIntakeTime(_tmpIntakeTime);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item.setSynced(_tmpSynced);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
