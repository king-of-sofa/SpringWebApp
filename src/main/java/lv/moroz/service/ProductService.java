package lv.moroz.service;

import lv.moroz.ProductToProductFormFunction;
import lv.moroz.form.ProductForm;
import lv.moroz.model.Product;
import lv.moroz.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<ProductForm> findByID(Long id){
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(new ProductToProductFormFunction());
    }

    public Page<ProductForm> findAll(int pageNumber, int maxElements, String sortBy, String direction){
        PageRequest pageRequest = generatePageRequest(pageNumber, maxElements, sortBy, direction);
        Page<Product> result = productRepository.findAll(pageRequest);
        return result.map(new ProductToProductFormFunction());
    }

    private PageRequest generatePageRequest(int pageNumber, int maxElements, String sortBy, String direction){
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(pageNumber, maxElements, Sort.by(sortDirection, sortBy));
    }

    private ProductForm toForm(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setPrice(product.getPrice());
        return form;
    }

    private Product toEntity(ProductForm form, Product product){
        product.setId(form.getId());
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        return product;
    }

    public ProductForm saveItem(ProductForm form) {
        Product product = toEntity(form, new Product());
        Product productAfterSave = productRepository.save(product);
        return toForm(productAfterSave);
    }

    public void deleteItem(Long id){
        productRepository.deleteById(id);
    }

    public Long cloneItem(Long id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            throw new RuntimeException("Incorrect ID");
        }
        Product product = productOptional.get();
        Product newProduct = Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();
        Product productAfterSave = productRepository.save(newProduct);
        return productAfterSave.getId();
    }
}
