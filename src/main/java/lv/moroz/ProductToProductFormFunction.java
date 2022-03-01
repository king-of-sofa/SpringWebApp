package lv.moroz;

import lv.moroz.form.ProductForm;
import lv.moroz.model.Product;

import java.util.function.Function;

public class ProductToProductFormFunction implements Function<Product, ProductForm> {
    @Override
    public ProductForm apply(Product product) {
        ProductForm productForm = new ProductForm();
        productForm.setId(product.getId());
        productForm.setName(product.getName());
        productForm.setPrice(product.getPrice());
        return productForm;
    }
}
