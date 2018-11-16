package be.yildizgames.engine.feature.mission.protocol.mapper;

import be.yildizgames.common.exception.business.BusinessException;

class MappingException extends BusinessException {
    MappingException(String message) {
        super(message);
    }

    MappingException(Exception cause) {
        super(cause);
    }

    MappingException(String message, Exception cause) {
        super(message, cause);
    }
}
