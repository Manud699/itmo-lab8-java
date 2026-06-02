package common.network;

import java.io.Serial;
import java.io.Serializable;

public record User(String name, String password) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public User{
//        if(name == null || name.isEmpty())
//            throw new IllegalArgumentException("Name cannot be empty. Must contain at least 2 characters");
//
//        if(name.length() < 2)
//            throw new IllegalArgumentException("Name must contain at least 2 characters");
//
//        if(password == null || password.isEmpty())
//            throw new IllegalArgumentException("Password cannot be empty.");
//
//        if(password.length() < 8)
//            throw new IllegalArgumentException("Password must contain at least 8 characters");
//
//        if(!password.matches(PASSWORD_REGEX))
//            throw new IllegalArgumentException("Password must be at least 8 characters long," +
//                    "include one uppercase letter, one lowercase letter, one number and one special character.");
//        name.trim();
    }
}