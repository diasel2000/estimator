package com.estimator.exception;

/**
 * Custom exception class with different types of errors and additional context information.
 */
public class CustomException extends RuntimeException {
    private final int errorCode;
    private final String contextInfo;

    /**
     * Constructor to create a new CustomException with a specified message, error code, and context information.
     *
     * @param message      The detail message of the exception.
     * @param errorCode    The error code associated with this exception.
     * @param contextInfo  Additional context information related to the error.
     */
    protected CustomException(String message, int errorCode, String contextInfo) {
        super(message);
        this.errorCode = errorCode;
        this.contextInfo = contextInfo;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return The error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns additional context information related to the error.
     *
     * @return The context information.
     */
    public String getContextInfo() {
        return contextInfo;
    }

    /**
     * Exception thrown when a user with the specified username already exists.
     */
    public static class UserAlreadyExistsException extends CustomException {
        /**
         * Constructs a new UserAlreadyExistsException with the specified username.
         *
         * @param username The username that already exists.
         */
        public UserAlreadyExistsException(String username) {
            super("Username already exists: " + username, 1001, "Username: " + username);
        }
    }

    /**
     * Exception thrown when the default role ROLE_USER is not found.
     */
    public static class DefaultRoleNotFoundException extends CustomException {
        /**
         * Constructs a new DefaultRoleNotFoundException.
         */
        public DefaultRoleNotFoundException() {
            super("Default role ROLE_USER not found", 1002, "Default role not found in the database");
        }
    }

    /**
     * Exception thrown when the default subscription "Basic" is not found.
     */
    public static class DefaultSubscriptionNotFoundException extends CustomException {
        /**
         * Constructs a new DefaultSubscriptionNotFoundException.
         */
        public DefaultSubscriptionNotFoundException() {
            super("Default subscription Basic not found", 1003, "Default subscription not found in the database");
        }
    }

    /**
     * Exception thrown when a user with the specified email not founds.
     */
    public static class UserNotFoundException extends CustomException {
        /**
         * Constructs a new UserAlreadyExistsException with the specified email.
         *
         * @param email The email that not found.
         */
        public UserNotFoundException(String email) {
            super("User not found with email: " + email, 1001, "Username: " + email);
        }
    }
        }
    }
}
