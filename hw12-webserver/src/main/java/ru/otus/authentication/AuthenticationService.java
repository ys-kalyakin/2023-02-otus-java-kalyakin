package ru.otus.authentication;

/**
 * Интерфейс сервиса аутентификации
 */
public interface AuthenticationService {
    /**
     * выполнить аутентификацию пользователя
     *
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return результат аутентификации
     */
    boolean authenticate(String login, String password);
}
