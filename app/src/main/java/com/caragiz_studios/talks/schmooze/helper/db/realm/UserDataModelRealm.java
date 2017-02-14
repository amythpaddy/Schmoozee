package com.caragiz_studios.talks.schmooze.helper.db.realm;

import io.realm.RealmObject;

/**
 * Created by caragiz on 13/2/17.
 */

public class UserDataModelRealm extends RealmObject {
    String userId;
    String partnerId;
    String roomId;

    public UserDataModelRealm() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}
