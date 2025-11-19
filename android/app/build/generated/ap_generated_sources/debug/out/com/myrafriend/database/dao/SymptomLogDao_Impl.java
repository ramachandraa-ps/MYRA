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
import com.myrafriend.database.entities.SymptomLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SymptomLogDao_Impl implements SymptomLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SymptomLogEntity> __insertionAdapterOfSymptomLogEntity;

  private final EntityDeletionOrUpdateAdapter<SymptomLogEntity> __updateAdapterOfSymptomLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPatientId;

  public SymptomLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSymptomLogEntity = new EntityInsertionAdapter<SymptomLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `symptom_logs` (`id`,`patient_id`,`pain_level`,`joint_count`,`morning_stiffness`,`fatigue_level`,`swelling`,`warmth`,`functional_ability`,`notes`,`log_date`,`created_at`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SymptomLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPatientId());
        statement.bindLong(3, entity.getPainLevel());
        statement.bindLong(4, entity.getJointCount());
        if (entity.getMorningStiffness() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMorningStiffness());
        }
        statement.bindLong(6, entity.getFatigueLevel());
        final int _tmp = entity.isSwelling() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isWarmth() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getFunctionalAbility() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFunctionalAbility());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        if (entity.getLogDate() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLogDate());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCreatedAt());
        }
        final int _tmp_2 = entity.isSynced() ? 1 : 0;
        statement.bindLong(13, _tmp_2);
      }
    };
    this.__updateAdapterOfSymptomLogEntity = new EntityDeletionOrUpdateAdapter<SymptomLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `symptom_logs` SET `id` = ?,`patient_id` = ?,`pain_level` = ?,`joint_count` = ?,`morning_stiffness` = ?,`fatigue_level` = ?,`swelling` = ?,`warmth` = ?,`functional_ability` = ?,`notes` = ?,`log_date` = ?,`created_at` = ?,`synced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SymptomLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPatientId());
        statement.bindLong(3, entity.getPainLevel());
        statement.bindLong(4, entity.getJointCount());
        if (entity.getMorningStiffness() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMorningStiffness());
        }
        statement.bindLong(6, entity.getFatigueLevel());
        final int _tmp = entity.isSwelling() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isWarmth() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getFunctionalAbility() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFunctionalAbility());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        if (entity.getLogDate() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getLogDate());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCreatedAt());
        }
        final int _tmp_2 = entity.isSynced() ? 1 : 0;
        statement.bindLong(13, _tmp_2);
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE symptom_logs SET synced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByPatientId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM symptom_logs WHERE patient_id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final SymptomLogEntity symptomLog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfSymptomLogEntity.insertAndReturnId(symptomLog);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final SymptomLogEntity symptomLog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSymptomLogEntity.handle(symptomLog);
      __db.setTransactionSuccessful();
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
  public LiveData<List<SymptomLogEntity>> getSymptomLogsByPatientId(final int patientId) {
    final String _sql = "SELECT * FROM symptom_logs WHERE patient_id = ? ORDER BY log_date DESC, created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"symptom_logs"}, false, new Callable<List<SymptomLogEntity>>() {
      @Override
      @Nullable
      public List<SymptomLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfPainLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "pain_level");
          final int _cursorIndexOfJointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "joint_count");
          final int _cursorIndexOfMorningStiffness = CursorUtil.getColumnIndexOrThrow(_cursor, "morning_stiffness");
          final int _cursorIndexOfFatigueLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "fatigue_level");
          final int _cursorIndexOfSwelling = CursorUtil.getColumnIndexOrThrow(_cursor, "swelling");
          final int _cursorIndexOfWarmth = CursorUtil.getColumnIndexOrThrow(_cursor, "warmth");
          final int _cursorIndexOfFunctionalAbility = CursorUtil.getColumnIndexOrThrow(_cursor, "functional_ability");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<SymptomLogEntity> _result = new ArrayList<SymptomLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SymptomLogEntity _item;
            _item = new SymptomLogEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpPatientId;
            _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
            _item.setPatientId(_tmpPatientId);
            final int _tmpPainLevel;
            _tmpPainLevel = _cursor.getInt(_cursorIndexOfPainLevel);
            _item.setPainLevel(_tmpPainLevel);
            final int _tmpJointCount;
            _tmpJointCount = _cursor.getInt(_cursorIndexOfJointCount);
            _item.setJointCount(_tmpJointCount);
            final String _tmpMorningStiffness;
            if (_cursor.isNull(_cursorIndexOfMorningStiffness)) {
              _tmpMorningStiffness = null;
            } else {
              _tmpMorningStiffness = _cursor.getString(_cursorIndexOfMorningStiffness);
            }
            _item.setMorningStiffness(_tmpMorningStiffness);
            final int _tmpFatigueLevel;
            _tmpFatigueLevel = _cursor.getInt(_cursorIndexOfFatigueLevel);
            _item.setFatigueLevel(_tmpFatigueLevel);
            final boolean _tmpSwelling;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSwelling);
            _tmpSwelling = _tmp != 0;
            _item.setSwelling(_tmpSwelling);
            final boolean _tmpWarmth;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWarmth);
            _tmpWarmth = _tmp_1 != 0;
            _item.setWarmth(_tmpWarmth);
            final String _tmpFunctionalAbility;
            if (_cursor.isNull(_cursorIndexOfFunctionalAbility)) {
              _tmpFunctionalAbility = null;
            } else {
              _tmpFunctionalAbility = _cursor.getString(_cursorIndexOfFunctionalAbility);
            }
            _item.setFunctionalAbility(_tmpFunctionalAbility);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final String _tmpLogDate;
            if (_cursor.isNull(_cursorIndexOfLogDate)) {
              _tmpLogDate = null;
            } else {
              _tmpLogDate = _cursor.getString(_cursorIndexOfLogDate);
            }
            _item.setLogDate(_tmpLogDate);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item.setCreatedAt(_tmpCreatedAt);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
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
  public LiveData<List<SymptomLogEntity>> getSymptomLogsByDateRange(final int patientId,
      final String startDate, final String endDate) {
    final String _sql = "SELECT * FROM symptom_logs WHERE patient_id = ? AND log_date >= ? AND log_date <= ? ORDER BY log_date DESC";
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
    return __db.getInvalidationTracker().createLiveData(new String[] {"symptom_logs"}, false, new Callable<List<SymptomLogEntity>>() {
      @Override
      @Nullable
      public List<SymptomLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
          final int _cursorIndexOfPainLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "pain_level");
          final int _cursorIndexOfJointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "joint_count");
          final int _cursorIndexOfMorningStiffness = CursorUtil.getColumnIndexOrThrow(_cursor, "morning_stiffness");
          final int _cursorIndexOfFatigueLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "fatigue_level");
          final int _cursorIndexOfSwelling = CursorUtil.getColumnIndexOrThrow(_cursor, "swelling");
          final int _cursorIndexOfWarmth = CursorUtil.getColumnIndexOrThrow(_cursor, "warmth");
          final int _cursorIndexOfFunctionalAbility = CursorUtil.getColumnIndexOrThrow(_cursor, "functional_ability");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<SymptomLogEntity> _result = new ArrayList<SymptomLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SymptomLogEntity _item;
            _item = new SymptomLogEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpPatientId;
            _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
            _item.setPatientId(_tmpPatientId);
            final int _tmpPainLevel;
            _tmpPainLevel = _cursor.getInt(_cursorIndexOfPainLevel);
            _item.setPainLevel(_tmpPainLevel);
            final int _tmpJointCount;
            _tmpJointCount = _cursor.getInt(_cursorIndexOfJointCount);
            _item.setJointCount(_tmpJointCount);
            final String _tmpMorningStiffness;
            if (_cursor.isNull(_cursorIndexOfMorningStiffness)) {
              _tmpMorningStiffness = null;
            } else {
              _tmpMorningStiffness = _cursor.getString(_cursorIndexOfMorningStiffness);
            }
            _item.setMorningStiffness(_tmpMorningStiffness);
            final int _tmpFatigueLevel;
            _tmpFatigueLevel = _cursor.getInt(_cursorIndexOfFatigueLevel);
            _item.setFatigueLevel(_tmpFatigueLevel);
            final boolean _tmpSwelling;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSwelling);
            _tmpSwelling = _tmp != 0;
            _item.setSwelling(_tmpSwelling);
            final boolean _tmpWarmth;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWarmth);
            _tmpWarmth = _tmp_1 != 0;
            _item.setWarmth(_tmpWarmth);
            final String _tmpFunctionalAbility;
            if (_cursor.isNull(_cursorIndexOfFunctionalAbility)) {
              _tmpFunctionalAbility = null;
            } else {
              _tmpFunctionalAbility = _cursor.getString(_cursorIndexOfFunctionalAbility);
            }
            _item.setFunctionalAbility(_tmpFunctionalAbility);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final String _tmpLogDate;
            if (_cursor.isNull(_cursorIndexOfLogDate)) {
              _tmpLogDate = null;
            } else {
              _tmpLogDate = _cursor.getString(_cursorIndexOfLogDate);
            }
            _item.setLogDate(_tmpLogDate);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item.setCreatedAt(_tmpCreatedAt);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
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
  public List<SymptomLogEntity> getUnsyncedSymptomLogs() {
    final String _sql = "SELECT * FROM symptom_logs WHERE synced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
      final int _cursorIndexOfPainLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "pain_level");
      final int _cursorIndexOfJointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "joint_count");
      final int _cursorIndexOfMorningStiffness = CursorUtil.getColumnIndexOrThrow(_cursor, "morning_stiffness");
      final int _cursorIndexOfFatigueLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "fatigue_level");
      final int _cursorIndexOfSwelling = CursorUtil.getColumnIndexOrThrow(_cursor, "swelling");
      final int _cursorIndexOfWarmth = CursorUtil.getColumnIndexOrThrow(_cursor, "warmth");
      final int _cursorIndexOfFunctionalAbility = CursorUtil.getColumnIndexOrThrow(_cursor, "functional_ability");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<SymptomLogEntity> _result = new ArrayList<SymptomLogEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SymptomLogEntity _item;
        _item = new SymptomLogEntity();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpPatientId;
        _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
        _item.setPatientId(_tmpPatientId);
        final int _tmpPainLevel;
        _tmpPainLevel = _cursor.getInt(_cursorIndexOfPainLevel);
        _item.setPainLevel(_tmpPainLevel);
        final int _tmpJointCount;
        _tmpJointCount = _cursor.getInt(_cursorIndexOfJointCount);
        _item.setJointCount(_tmpJointCount);
        final String _tmpMorningStiffness;
        if (_cursor.isNull(_cursorIndexOfMorningStiffness)) {
          _tmpMorningStiffness = null;
        } else {
          _tmpMorningStiffness = _cursor.getString(_cursorIndexOfMorningStiffness);
        }
        _item.setMorningStiffness(_tmpMorningStiffness);
        final int _tmpFatigueLevel;
        _tmpFatigueLevel = _cursor.getInt(_cursorIndexOfFatigueLevel);
        _item.setFatigueLevel(_tmpFatigueLevel);
        final boolean _tmpSwelling;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSwelling);
        _tmpSwelling = _tmp != 0;
        _item.setSwelling(_tmpSwelling);
        final boolean _tmpWarmth;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfWarmth);
        _tmpWarmth = _tmp_1 != 0;
        _item.setWarmth(_tmpWarmth);
        final String _tmpFunctionalAbility;
        if (_cursor.isNull(_cursorIndexOfFunctionalAbility)) {
          _tmpFunctionalAbility = null;
        } else {
          _tmpFunctionalAbility = _cursor.getString(_cursorIndexOfFunctionalAbility);
        }
        _item.setFunctionalAbility(_tmpFunctionalAbility);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpLogDate;
        if (_cursor.isNull(_cursorIndexOfLogDate)) {
          _tmpLogDate = null;
        } else {
          _tmpLogDate = _cursor.getString(_cursorIndexOfLogDate);
        }
        _item.setLogDate(_tmpLogDate);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _item.setCreatedAt(_tmpCreatedAt);
        final boolean _tmpSynced;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
        _tmpSynced = _tmp_2 != 0;
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
  public SymptomLogEntity getLatestSymptomLog(final int patientId) {
    final String _sql = "SELECT * FROM symptom_logs WHERE patient_id = ? ORDER BY log_date DESC, created_at DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patient_id");
      final int _cursorIndexOfPainLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "pain_level");
      final int _cursorIndexOfJointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "joint_count");
      final int _cursorIndexOfMorningStiffness = CursorUtil.getColumnIndexOrThrow(_cursor, "morning_stiffness");
      final int _cursorIndexOfFatigueLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "fatigue_level");
      final int _cursorIndexOfSwelling = CursorUtil.getColumnIndexOrThrow(_cursor, "swelling");
      final int _cursorIndexOfWarmth = CursorUtil.getColumnIndexOrThrow(_cursor, "warmth");
      final int _cursorIndexOfFunctionalAbility = CursorUtil.getColumnIndexOrThrow(_cursor, "functional_ability");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final SymptomLogEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new SymptomLogEntity();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpPatientId;
        _tmpPatientId = _cursor.getInt(_cursorIndexOfPatientId);
        _result.setPatientId(_tmpPatientId);
        final int _tmpPainLevel;
        _tmpPainLevel = _cursor.getInt(_cursorIndexOfPainLevel);
        _result.setPainLevel(_tmpPainLevel);
        final int _tmpJointCount;
        _tmpJointCount = _cursor.getInt(_cursorIndexOfJointCount);
        _result.setJointCount(_tmpJointCount);
        final String _tmpMorningStiffness;
        if (_cursor.isNull(_cursorIndexOfMorningStiffness)) {
          _tmpMorningStiffness = null;
        } else {
          _tmpMorningStiffness = _cursor.getString(_cursorIndexOfMorningStiffness);
        }
        _result.setMorningStiffness(_tmpMorningStiffness);
        final int _tmpFatigueLevel;
        _tmpFatigueLevel = _cursor.getInt(_cursorIndexOfFatigueLevel);
        _result.setFatigueLevel(_tmpFatigueLevel);
        final boolean _tmpSwelling;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSwelling);
        _tmpSwelling = _tmp != 0;
        _result.setSwelling(_tmpSwelling);
        final boolean _tmpWarmth;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfWarmth);
        _tmpWarmth = _tmp_1 != 0;
        _result.setWarmth(_tmpWarmth);
        final String _tmpFunctionalAbility;
        if (_cursor.isNull(_cursorIndexOfFunctionalAbility)) {
          _tmpFunctionalAbility = null;
        } else {
          _tmpFunctionalAbility = _cursor.getString(_cursorIndexOfFunctionalAbility);
        }
        _result.setFunctionalAbility(_tmpFunctionalAbility);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final String _tmpLogDate;
        if (_cursor.isNull(_cursorIndexOfLogDate)) {
          _tmpLogDate = null;
        } else {
          _tmpLogDate = _cursor.getString(_cursorIndexOfLogDate);
        }
        _result.setLogDate(_tmpLogDate);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _result.setCreatedAt(_tmpCreatedAt);
        final boolean _tmpSynced;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
        _tmpSynced = _tmp_2 != 0;
        _result.setSynced(_tmpSynced);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<Float> getAveragePainLevel(final int patientId, final String startDate) {
    final String _sql = "SELECT AVG(pain_level) FROM symptom_logs WHERE patient_id = ? AND log_date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"symptom_logs"}, false, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
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
  public LiveData<Integer> getSymptomLogCount(final int patientId, final String startDate) {
    final String _sql = "SELECT COUNT(*) FROM symptom_logs WHERE patient_id = ? AND log_date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, patientId);
    _argIndex = 2;
    if (startDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startDate);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"symptom_logs"}, false, new Callable<Integer>() {
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
