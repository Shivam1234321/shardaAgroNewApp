package digi.coders.shardaagroagency.Model;

public class ShopProductModel {

    String ProductId,ProductName,ProductTitle,ProductCategory,ProductSubCategory,ProductBrand,SinglePrice,ProductOfferPrice,ProductPrice,
            ProductDiscount,ProductImage1,ProductImage2,ProductImage3,ProductDescription,CartQuantity,Stock,NotifyStatus,unit,altunit;

    public ShopProductModel(String productId, String productName, String productTitle, String productCategory, String productSubCategory, String productBrand,
                            String singlePrice, String productOfferPrice, String productPrice, String productDiscount, String productImage1, String productImage2,
                            String productImage3, String productDescription, String cartQuantity,String stock,String notifyStatus,String Unit,String Altunit) {
        ProductId = productId;
        ProductName = productName;
        ProductTitle = productTitle;
        ProductCategory = productCategory;
        ProductSubCategory = productSubCategory;
        ProductBrand = productBrand;
        SinglePrice = singlePrice;
        ProductOfferPrice = productOfferPrice;
        ProductPrice = productPrice;
        ProductDiscount = productDiscount;
        ProductImage1 = productImage1;
        ProductImage2 = productImage2;
        ProductImage3 = productImage3;
        ProductDescription = productDescription;
        CartQuantity = cartQuantity;
        Stock = stock;
        NotifyStatus = notifyStatus;
        unit = Unit;
        altunit = Altunit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAltunit() {
        return altunit;
    }

    public void setAltunit(String altunit) {
        this.altunit = altunit;
    }

    public String getNotifyStatus() {
        return NotifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        NotifyStatus = notifyStatus;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductSubCategory() {
        return ProductSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        ProductSubCategory = productSubCategory;
    }

    public String getProductBrand() {
        return ProductBrand;
    }

    public void setProductBrand(String productBrand) {
        ProductBrand = productBrand;
    }

    public String getSinglePrice() {
        return SinglePrice;
    }

    public void setSinglePrice(String singlePrice) {
        SinglePrice = singlePrice;
    }

    public String getProductOfferPrice() {
        return ProductOfferPrice;
    }

    public void setProductOfferPrice(String productOfferPrice) {
        ProductOfferPrice = productOfferPrice;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductDiscount() {
        return ProductDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        ProductDiscount = productDiscount;
    }

    public String getProductImage1() {
        return ProductImage1;
    }

    public void setProductImage1(String productImage1) {
        ProductImage1 = productImage1;
    }

    public String getProductImage2() {
        return ProductImage2;
    }

    public void setProductImage2(String productImage2) {
        ProductImage2 = productImage2;
    }

    public String getProductImage3() {
        return ProductImage3;
    }

    public void setProductImage3(String productImage3) {
        ProductImage3 = productImage3;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getCartQuantity() {
        return CartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        CartQuantity = cartQuantity;
    }
}
