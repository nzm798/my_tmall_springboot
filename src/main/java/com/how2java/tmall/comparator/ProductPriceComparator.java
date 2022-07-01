package com.how2java.tmall.comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {
    //价格低的放前面
    @Override
    public int compare(Product p1,Product p2){
        return (int) (p1.getPromotePrice()- p2.getPromotePrice());
    }
}
