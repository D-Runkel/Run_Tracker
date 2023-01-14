package com.main.database.database;

import java.util.Vector;

public class User {

    final private long id;
    private String email;
    private String userName;
    private String password;
    private Vector<Run> runs;
    private Long goal;

    public long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {return password;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password){this.password = password;}

    public Vector<Run> getRuns(){
        return this.runs;
    }

    public void addRun(Run run){
        runs.add(run);
    }

    public User(long id, String email, String userName, String password){
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public User(){
        this.id = -1;
        this.userName = null;
        this.email = null;
        this.password = null;
    }


}
