package com.devflow.service;

import com.devflow.model.DashboardStats;
import com.devflow.model.User;

public interface DashboardService {
    DashboardStats getDashboardStats(User currentUser);
}
