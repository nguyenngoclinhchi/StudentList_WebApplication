package com.student.backend;

import java.io.Serializable;
import java.util.Objects;

public class Subject implements Serializable {
    private Long id;
    private String name = "";

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(Subject other) {
        Objects.requireNonNull(other);
        this.name = other.getName();
        this.id = other.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        if(getId() == null) {
            return super.hashCode();
        }
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) obj;
        if (getId() == null) {
            return other.getId() == null;
        } else return getId().equals(other.getId());
    }

    @Override
    public String toString() {
        return "Subject{" + getId() + ":" + getName() + "}";
    }
}
