package com.yy.annotation.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/2.
 */
@Component("gameTimeKiller")
//@Qualifier("main")
public class Game implements TimeKiller, KillerStore<Game> {
    public void print() {
        System.out.println("game");
    }
}
