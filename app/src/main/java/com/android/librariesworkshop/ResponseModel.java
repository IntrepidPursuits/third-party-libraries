package com.android.librariesworkshop;

public class ResponseModel {

    private boolean success;
    private String message;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public ResponseModel() {
    }

    public class User {
        private int id;

        private String firstName;
        private String lastName;
        private int age;
        private String avatarUrl;

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }
    }

}
