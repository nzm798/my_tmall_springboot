package com.how2java.tmall.comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductDateComparator implements Comparator<Product> {
    //创建日期晚的放前面
    @Override
    public int compare(Product p1,Product p2){
        return p1.getCreateDate().compareTo(p2.getCreateDate());//专门用来比较时间
    }
}
