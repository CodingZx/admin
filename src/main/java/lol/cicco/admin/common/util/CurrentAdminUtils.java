package lol.cicco.admin.common.util;

import lol.cicco.admin.common.model.Token;

public class CurrentAdminUtils {

    private CurrentAdminUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private static final ThreadLocal<Token> THREAD_LOCAL = new ThreadLocal<>();

    public static void setToken(Token token) {
        THREAD_LOCAL.set(token);
    }

    public static Token getToken() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
