package digi.coders.shardaagroagency.Model;

import com.google.gson.annotations.SerializedName;

public class OrderDetails {

    @SerializedName("CartId") String CartId;
    @SerializedName("ProductName") String ProductName;
    @SerializedName("ProductPrice") String ProductPrice;
    @SerializedName("Quantity") String Quantity;
    @SerializedName("ProductImage") String ProductImage;
    @SerializedName("Category") String Category;
    @SerializedName("Subcategory") String Subcategory;

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubcategory() {
        return Subcategory;
    }

    public void setSubcategory(String subcategory) {
        Subcategory = subcategory;
    }
}
