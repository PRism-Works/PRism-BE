package com.prismworks.prism.domain.project.exception;

import com.prismworks.prism.common.exception.ApplicationErrorCode;
import com.prismworks.prism.common.exception.ApplicationException;

public class ProjectException extends ApplicationException {

    public static final ProjectException PROJECT_NOT_FOUND = new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND);
    public static final ProjectException USER_NOT_FOUND = new ProjectException(ProjectErrorCode.USER_NOT_FOUND);
    public static final ProjectException NO_MEMBER = new ProjectException(ProjectErrorCode.NO_MEMBER);
    public static final ProjectException NO_PROJECT_NAME = new ProjectException(ProjectErrorCode.NO_PROJECT_NAME);
    public static final ProjectException NO_DATETIME = new ProjectException(ProjectErrorCode.NO_DATETIME);
    public static final ProjectException INVALID_DATE_FORMAT = new ProjectException(ProjectErrorCode.INVALID_DATE_FORMAT);

    public ProjectException(String message, ApplicationErrorCode errorCode) {
        super(message, errorCode);
    }

    public ProjectException(ApplicationErrorCode errorCode) {
        super(errorCode.getErrorInfo().getReason(), errorCode);
    }
}
