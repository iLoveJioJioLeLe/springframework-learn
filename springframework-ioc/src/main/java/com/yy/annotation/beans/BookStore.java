package com.yy.annotation.beans;

/**
 * Created by 2019/5/3.
 */
public class BookStore {

    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"book\":")
                .append(book);
        sb.append('}');
        return sb.toString();
    }
}
