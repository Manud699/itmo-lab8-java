package common.network;

import java.io.Serial;
import java.io.Serializable;

public record User(String name, String password) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public User {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("error.user.name.empty");

        if (name.trim().length() < 2)
            throw new IllegalArgumentException("error.user.name.short");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("error.user.password.empty");

        if (password.length() < 8)
            throw new IllegalArgumentException("error.user.password.short");

        if (!password.matches(PASSWORD_REGEX))
            throw new IllegalArgumentException("error.user.password.regex");
        name = name.trim();
    }
}