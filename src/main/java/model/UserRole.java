package model;

/**
 * Created by computer on 13.11.2017.
 */
public enum UserRole {
    CLIENT(1), ADMIN(2);

    private final int roleId;

    UserRole(int roleId) {
        this.roleId = roleId;
    }
}
