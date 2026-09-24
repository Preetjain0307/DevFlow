package com.devflow.dao;

import com.devflow.model.SystemSetting;
import java.util.List;
import java.util.Map;

public interface SystemSettingDAO {
    String get(String key, String defaultValue);
    boolean set(String key, String value, String description);
    List<SystemSetting> findAll();
    Map<String, String> getAllAsMap();
}
