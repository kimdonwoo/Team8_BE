package com.kakao.techcampus.wekiki.mock;

import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki.group.domain.Invitation;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FakeRedisUtils implements RedisUtils {

    private final HashMap<String, Object> data = new HashMap<>();

    @Override
    public Boolean lock(Long key) {
        return null;
    }

    @Override
    public Boolean unlock(Long key) {
        return null;
    }

    @Override
    public void setValues(String key, String value, long lifetime) {
        data.put(key,value);
    }

    @Override
    public void setInvitationValues(String key, Invitation invitation, Duration lifetime) {
        data.put(key,invitation);
    }

    @Override
    public void setGroupIdValues(String key, Long groupId, Duration lifetime) {
        data.put(key,groupId);
    }

    @Override
    public Object getValues(String key) {
        return data.get(key);
    }

    @Override
    public Set<String> getKeys(String pattern) {
        return data.keySet().stream().filter(item->item.matches(pattern)).collect(Collectors.toSet());
    }

    @Override
    public void deleteValues(String key) {
        data.remove(key);
    }

    @Override
    public void saveKeyAndHashValue(String key, String hashKey, String value) {
        if(this.data.containsKey(key)){
            // 이게 만약 hashMap이라면 ?
            if(data.get(key) instanceof HashMap<?,?>){
                ((HashMap<String, String>) data.get(key)).put(hashKey,value);
            }else{
                throw new Exception404("에러입니다.");
            }
        }else{
            HashMap hm = new HashMap<String,String>();
            hm.put(hashKey,value);
            data.put(key,hm);
        }
    }

    @Override
    public String getHashValue(String key, String hashKey) {
        if(this.data.containsKey(key)){
            return ((HashMap<String, String>) data.get(key)).get(hashKey);
        }else{
            return null;
        }
    }

    @Override
    public void deleteHashValue(String key, String hashKey) {
        if(this.data.containsKey(key)){
            data.remove(key);
        }
    }
}
