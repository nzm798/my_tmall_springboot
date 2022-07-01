package com.how2java.tmall.comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductSaleCountComparator implements Comparator<Product> {
    //把销量高的放前面
    @Override
    public int compare(Product p1,Product p2){
        return p2.getSaleCount()-p1.getSaleCount();
    }
}
