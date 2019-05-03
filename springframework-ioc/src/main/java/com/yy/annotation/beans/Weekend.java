package com.yy.annotation.beans;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 2019/5/3.
 */
@Component
public class Weekend {

    @Resource
    private TimeKiller gameTimeKiller;

    public TimeKiller getTimeKiller() {
        return gameTimeKiller;
    }

    public void setTimeKiller(TimeKiller timeKiller) {
        this.gameTimeKiller = timeKiller;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"timeKiller\":")
                .append(gameTimeKiller);
        sb.append('}');
        return sb.toString();
    }
}
