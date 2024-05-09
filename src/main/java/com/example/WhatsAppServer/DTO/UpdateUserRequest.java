package com.example.WhatsAppServer.DTO;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;

    private String profilePicture;

    public UpdateUserRequest()
    {

    }

    public UpdateUserRequest(String name, String profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
