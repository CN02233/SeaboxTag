package com.seabox.tagsys.usertags.msg;

import java.io.IOException;

/**
 * @author Changhua, Wu
 *         Created on: 5/18/16,11:10 AM
 */
public class MissingCampException extends IOException {

    public MissingCampException(String msg) {
        super( msg);
    }
}
