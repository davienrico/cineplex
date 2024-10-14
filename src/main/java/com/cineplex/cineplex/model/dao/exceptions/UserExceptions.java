package com.cineplex.cineplex.model.dao.exceptions;

public class UserExceptions {

    public static class DuplicateUsernameException extends Exception {
        public DuplicateUsernameException(String message) {
            super(message);
        }
    }

    public static class DuplicateEmailException extends Exception {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }
}