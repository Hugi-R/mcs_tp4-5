package com.example.aliceprobst.mcs;

public class Recording {

    String command_name;
    String URI;

    public Recording(String command_name, String URI) {
        this.command_name = command_name;
        this.URI = URI;
    }

    public String getCommand_name() {
        return command_name;
    }

    public String getURI() {
        return URI;
    }
}
