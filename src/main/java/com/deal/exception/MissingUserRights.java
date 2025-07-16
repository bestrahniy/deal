package com.deal.exception;

public class MissingUserRights extends RuntimeException {

    public MissingUserRights(String login) {
        super("User" + login + "not have all rights for this action");
    }

}
