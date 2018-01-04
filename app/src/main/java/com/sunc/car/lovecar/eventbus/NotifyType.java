package com.sunc.car.lovecar.eventbus;

public class NotifyType {
    public static final int EVENT_CAR_CHANGE = 100;
    public static final int EVENT_OIL_ADDED = 101;
    public static final int EVENT_BILL_ADDED = 102;


    private int mType;
    private Object mExtra;

    public NotifyType(int type) {
        this(type, null);
    }

    public NotifyType(int type, Object extra) {
        this.mType = type;
        this.mExtra = extra;
    }

    public int getType() {
        return mType;
    }

    public Object getExtra() {
        return mExtra;
    }

    public interface INotify {
        void onNotify(NotifyType type);
    }
}
