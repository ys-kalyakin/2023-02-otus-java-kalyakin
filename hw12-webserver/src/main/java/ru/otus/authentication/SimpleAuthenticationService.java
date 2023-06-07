package ru.otus.authentication;

public class SimpleAuthenticationService implements AuthenticationService {

    @Override
    public boolean authenticate(String login, String password) {
        return "admin".equals(login) && "admin".equals(password);
    }
}
