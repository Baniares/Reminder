package com.nearsoft.reminder.main.objects;

/**
 * Created by Baniares on 4/3/14.
 */
public class User {
    private int id;
    private String idFacebook;
    private String name;
    private String bio;
    private String email;
    private String password;

    public User() {
        this.id=0;
    }

    public User(int id, String idFacebook, String name, String bio, String email, String password) {
        this.id = id;
        this.idFacebook = idFacebook;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
