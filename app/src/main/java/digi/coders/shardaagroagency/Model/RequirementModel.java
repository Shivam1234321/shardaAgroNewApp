package digi.coders.shardaagroagency.Model;

public class RequirementModel {

    String RequirementId, ProductName, ProductQuantity, ProductRequirementType;

    public RequirementModel(String requirementId, String productName, String productQuantity, String productRequirementType) {
        RequirementId = requirementId;
        ProductName = productName;
        ProductQuantity = productQuantity;
        ProductRequirementType = productRequirementType;
    }

    public String getRequirementId() {
        return RequirementId;
    }

    public void setRequirementId(String requirementId) {
        RequirementId = requirementId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductRequirementType() {
        return ProductRequirementType;
    }

    public void setProductRequirementType(String productRequirementType) {
        ProductRequirementType = productRequirementType;
    }
}
