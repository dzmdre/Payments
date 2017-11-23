package model;

/**
 * Created by computer on 13.11.2017.
 */
public enum UserRole {
    USER("USER"), ADMIN("ADMIN");

    private final String roleId;

    UserRole(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }
}
