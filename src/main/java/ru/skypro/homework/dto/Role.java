package ru.skypro.homework.dto;

public enum Role {
    USER, ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
