package com.alim.spring_demo.entity;

public enum RecipientType {
    REGISTERED,   // existing customer in system
    EMAIL_ONLY,   // send tracking code via email (no account)
    MANUAL        // business shares tracking code themselves
}