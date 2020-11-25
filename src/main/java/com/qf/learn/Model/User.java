package com.qf.learn.Model;

import java.util.Objects;

import com.google.gson.JsonObject;

public class User {
    private String username;
    private String role;

    public User() {
    }

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User role(String role) {
        this.role = role;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("name", getUsername());
        json.addProperty("role", getRole());
        return json.toString();
    }
}
