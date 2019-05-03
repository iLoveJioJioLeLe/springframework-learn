package com.yy.annotation.beans;

import com.yy.annotation.annotations.CustomQualifier;
import com.yy.annotation.annotations.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 2019/5/2.
 */
//@Component
public class Holiday {

    @Autowired(required = false)
//    @Qualifier("second")
//    @Resource(name = "bookTimeKiller")
//    @com.yy.annotation.annotations.TimeKiller("main")
//    @Good
    @CustomQualifier("second")
    private TimeKiller timeKiller;

    @Autowired
    private KillerStore<Book> killerStore;


    public TimeKiller getTimeKiller() {
        return timeKiller;
    }

    public void setTimeKiller(TimeKiller timeKiller) {
        this.timeKiller = timeKiller;
    }

    public KillerStore<Book> getKillerStore() {
        return killerStore;
    }

    public void setKillerStore(KillerStore<Book> killerStore) {
        this.killerStore = killerStore;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"timeKiller\":")
                .append(timeKiller);
        sb.append(",\"killerStore\":")
                .append(killerStore);
        sb.append('}');
        return sb.toString();
    }
}
