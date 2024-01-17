package com.kakao.techcampus.wekiki._core.utils.port;

import com.kakao.techcampus.wekiki.group.domain.Invitation;
import org.springframework.data.redis.core.HashOperations;

import java.time.Duration;
import java.util.Set;

public interface RedisUtils {
    Boolean lock(Long key);
    Boolean unlock(Long key);
    void setValues(String key, String value, long lifetime);
    void setInvitationValues(String key, Invitation invitation, Duration lifetime);
    void setGroupIdValues(String key, Long groupId, Duration lifetime);
    Object getValues(String key);
    Set<String> getKeys(String pattern);
    void deleteValues(String key);
    void saveKeyAndHashValue(String key, String hashKey, String value);
    String getHashValue(String key, String hashKey);
    void deleteHashValue(String key, String hashKey);
}
