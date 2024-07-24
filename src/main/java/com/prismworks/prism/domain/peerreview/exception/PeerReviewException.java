package com.prismworks.prism.domain.peerreview.exception;

import com.prismworks.prism.common.exception.ApplicationErrorCode;
import com.prismworks.prism.common.exception.ApplicationException;

public class PeerReviewException extends ApplicationException {

    public static final PeerReviewException ALREADY_FINISH_REVIEW =
            new PeerReviewException(PeerReviewErrorCode.ALREADY_FINISH_REVIEW);

    public static final PeerReviewException REVIEWEE_NOT_EXIST =
            new PeerReviewException(PeerReviewErrorCode.REVIEWEE_NOT_EXIST);

    public static final PeerReviewException LINK_CODE_NOT_FOUND =
            new PeerReviewException(PeerReviewErrorCode.LINK_CODE_NOT_FOUND);

    public PeerReviewException(String message, ApplicationErrorCode errorCode) {
        super(message, errorCode);
    }

    public PeerReviewException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
