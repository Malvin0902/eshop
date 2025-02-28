package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository implements IProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        if (product.getProductId() == null) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String productId) {
        for (Product product : productData) {
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Product update(Product updated) {
        for (int i = 0; i < productData.size(); i++) {
            Product existing = productData.get(i);
            if (existing.getProductId() != null && existing.getProductId().equals(updated.getProductId())) {
                productData.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    public void delete(String productId) {
        for (Iterator<Product> it = productData.iterator(); it.hasNext();) {
            Product product = it.next();
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                it.remove();
                break;
            }
        }
    }
}