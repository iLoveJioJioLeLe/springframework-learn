package com.yy.scope;

/**
 * Created by 2019/4/20.
 */
public class Foo {

    private Bar bar;

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        System.out.println(super.toString());
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"bar\":")
                .append(bar);
        sb.append('}');
        return sb.toString();
    }
}
