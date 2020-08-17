package org.itstep;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public int compareTo(User o) {
        return login.compareTo(o.login);
    }
}
