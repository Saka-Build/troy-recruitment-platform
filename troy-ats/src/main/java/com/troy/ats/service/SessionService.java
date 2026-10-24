package com.troy.ats.service;

import com.troy.ats.entity.Employee;

public interface SessionService {

    /**
     * current session user
     * @return
     */
    Employee getCurrentUser();
}
