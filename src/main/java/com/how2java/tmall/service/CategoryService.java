package com.how2java.tmall.service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    @Cacheable(key = "'categories-all'")
    public List<Category> list(){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll(sort);
    }
    @Cacheable(key = "'categories-page-'+#p0+'-'+#p1")
    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=new PageRequest(start,size,sort);
        Page pageFromJPA=categoryDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @CacheEvict(allEntries = true)
    //其意义是删除 categories~keys 里的所有的keys.一旦增加了某个分类数据，
    // 那么就把缓存里所有分类相关的数据，都清除掉。 下一次再访问的时候，一看，缓存里没数据，
    // 那么就会从数据库中读出来，读出来之后，再放在缓存里。如此这般，牺牲了一点小小的性能，
    // 数据的一致性就得到了保障了。
    public void add(Category bean){
        categoryDAO.save(bean);
    }
    @CacheEvict(allEntries = true)
    public void delete(int id){
        categoryDAO.delete(id);
    }

    //它的作用是以 category-one-id 的方式增加到 Redis中去。
    // 这样做本身其实是没有问题的，而且在 get 的时候，还可以使用，但是最后还是放弃这种做法了，为什么呢？
    //因为，虽然这种方式可以在 redis 中增加一条数据，
    // 但是： 它并不能更新分页缓存 categories-page-0-5 里的数据， 这样就会出现数据不一致的问题了。
    // 即。在redis 中，有这一条单独数据的缓存，但是在分页数据里，却没有这一条，这样就矛盾了。
    @Cacheable(key = "'categories-one-'+#p0")
    public Category get(int id){
        Category c=categoryDAO.findOne(id);
        return c;
    }
    @CacheEvict(allEntries=true)
    public void update(Category bean){
        categoryDAO.save(bean);
    }
    public void removeCategoryFromProduct (List<Category> cs){
        for (Category category:cs){
            removeCategoryFromProduct(category);
        }
    }

    //这个方法的用处是删除Product对象上的 分类。
    // 为什么要删除呢？ 因为在对分类做序列还转换为 json 的时候，会遍历里面的 products,
    // 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了，就搞死个人了
    //而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系
    public void removeCategoryFromProduct(Category category){
        List<Product> products=category.getProducts();
        if (null!=products){
            for (Product product:products){
                product.setCategory(null);
            }
        }
        List<List<Product>> productsByRow=category.getProductsByRow();
        if (null!=productsByRow){
            for (List<Product> ps:productsByRow){
                for (Product p:ps){
                    p.setCategory(null);
                }
            }
        }
    }
}
