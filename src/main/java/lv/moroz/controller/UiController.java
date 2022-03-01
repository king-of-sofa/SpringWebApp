package lv.moroz.controller;
import lombok.extern.slf4j.Slf4j;
import lv.moroz.enums.SortBy;
import lv.moroz.form.ProductForm;
import lv.moroz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@Slf4j // Lombok - при добавлении Lombok проверить, что установлен плагин Lombok, включить галочку annotation processing в IntelliJ

public class UiController {
    @Autowired
    ProductService productService;

    @GetMapping("/preview/{id}")
    public String getPreview(Model model, @PathVariable Long id){
        log.info("Preview page requested with ID: " + id);
        Optional<ProductForm> productFormOptional = productService.findByID(id);
        model.addAttribute("id", id);
        model.addAttribute("productForm", productFormOptional.orElse(new ProductForm()));
        return "product-html/preview";
    }

    @GetMapping(path = {"/list", "/"})
    public String getList(Model model,
                          @RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "id") SortBy sortBy,
                          @RequestParam(required = false, defaultValue = "ASC") String direction
                          ){
        log.info("List page requested");
        Page<ProductForm> productFormPage = productService.findAll(page, 10, sortBy.toString(), direction);
        model.addAttribute("isFirstPage", productFormPage.isFirst());
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("isLastPage", productFormPage.isLast());
        model.addAttribute("totalPages", productFormPage.getTotalPages());
        model.addAttribute("productFormList", productFormPage.getContent());
        return "product-html/list";
    }

    @GetMapping("/edit/")
    public String getEdit(Model model) {
        log.info("Edit page requested without product ID");
        model.addAttribute("productForm", new ProductForm());
        return "product-html/edit";
    }

    @GetMapping("/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        log.info("Edit page requested with ID: " + id);
        Optional<ProductForm> productFormOptional = productService.findByID(id);
        ProductForm productForm = productFormOptional.get();
        model.addAttribute("productForm", productForm);
        return "product-html/edit";
    }

    @PostMapping(path = {"/edit/{id}", "/edit/"})
    public String postEdit(Model model, @PathVariable(required = false) Long id, @ModelAttribute ProductForm productForm) {
        if (productForm.getPrice() <= 0.0) {
            model.addAttribute("errorText", "Price must be grater than 0");
            return "product-html/edit";
        }
        productService.saveItem(productForm);
        log.info("Saved product: " + productForm.getName());
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct( @PathVariable Long id) {
        log.info("Delete page requested with ID: " + id);
        String deletedProductName = productService.findByID(id).get().getName();
        productService.deleteItem(id);
        log.info("Product deleted: " + deletedProductName);
        return "redirect:/list";
    }


    @GetMapping("/clone/{id}")
    public String cloneProduct( @PathVariable Long id) {
        log.info("Clone page requested with ID: " + id);
        Long newId = productService.cloneItem(id);
        log.info("Product cloned. New product ID: " + newId);
        return "redirect:/list";
    }
}