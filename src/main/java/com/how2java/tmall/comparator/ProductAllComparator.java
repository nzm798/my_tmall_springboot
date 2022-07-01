package com.how2java.tmall.comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductAllComparator implements Comparator<Product> {
    //综合评价器，把销量*评价高的放前面
    @Override
    public int compare(Product p1,Product p2){
        return p2.getReviewCount()* p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
    }
}
