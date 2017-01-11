package com.seabox.tagsys.usertags.service.impl;

/**
 * @author Changhua, Wu
 *         Created on: 5/6/16,11:35 AM
 */
public abstract class FileImportException extends Exception{

    public FileImportException() {

    }

    public FileImportException(Throwable e) {
        super( e );
    }

    public static class FileAlreadyExistsException extends FileImportException{

    }


}
