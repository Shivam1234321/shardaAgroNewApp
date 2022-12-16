package digi.coders.shardaagroagency.Helper;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetData {

    @POST("login.php")
    @FormUrlEncoded
    Call<JsonArray> userLogin(@Field("Email") String mobile,
                              @Field("Password") String password,
                              @Field("Flag") String flag);

    @POST("register.php")
    @FormUrlEncoded
    Call<JsonArray> userRegistration(
            @Field("name") String name,
            @Field("shopname") String shopname,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("password") String password,
            @Field("Flag") String flag,
            @Field("gst") String gst,
            @Field("state") String state,
            @Field("area") String area,
            @Field("city") String city,
            @Field("pincode") String pincode
    );

    @POST("reset_password.php")
    @FormUrlEncoded
    Call<JsonArray> sendMobile(@Field("Mobile") String mobile,
                               @Field("Flag") String flag);

    @POST("register.php")
    @FormUrlEncoded
    Call<JsonArray> verifyOTP(@Field("Mobile") String mobile,
                              @Field("OTP") String otp,
                              @Field("Flag") String flag);

    @POST("register.php")
    @FormUrlEncoded
    Call<JsonArray> resendotp(@Field("Mobile") String mobile,
                              @Field("Flag") String flag);

    @POST("login.php")
    @FormUrlEncoded
    Call<JsonArray> checkStatus(@Field("UserId") String mobile,
                                @Field("Flag") String flag);

    @POST("version.php")
    @FormUrlEncoded
    Call<JsonArray> checkVersion(
            @Field("Flag") String flag);

    @POST("version.php")
    @FormUrlEncoded
    Call<JsonArray> getSlider(
            @Field("Flag") String flag);

    @POST("version.php")
    @FormUrlEncoded
    Call<JsonArray> homeProduct(
            @Field("Flag") String flag);

    @POST("version.php")
    @FormUrlEncoded
    Call<JsonArray> getCityList(
            @Field("Flag") String flag);

    @POST("version.php")
    @FormUrlEncoded
    Call<JsonArray> notify_product(
            @Field("Flag") String flag,
            @Field("UserId") String UserId,
            @Field("ProductId") String ProductId
    );

    @POST("reset_password.php")
    @FormUrlEncoded
    Call<JsonArray> resendOTP(@Field("Mobile") String mobile,
                              @Field("Flag") String flag);

    @POST("reset_password.php")
    @FormUrlEncoded
    Call<JsonArray> sendOTP(@Field("Mobile") String mobile,
                            @Field("OTP") String password,
                            @Field("Flag") String flag);

    @POST("reset_password.php")
    @FormUrlEncoded
    Call<JsonArray> sendPass(@Field("Mobile") String mobile,
                             @Field("NewPassword") String password,
                             @Field("Flag") String flag);


    @POST("notification.php")
    @FormUrlEncoded
    Call<JsonArray> getNotification(@Field("UserId") String mobile,
                                    @Field("Flag") String flag);


    @POST("profile.php")
    @FormUrlEncoded
    Call<JsonArray> getProfile(@Field("Flag") String mobile,
                               @Field("UserId") String userid);

    @POST("chat.php")
    @FormUrlEncoded
    Call<JsonArray> addProducts(@Field("Flag") String mobile,
                                @Field("UserId") String userid,
                                @Field("Quantity") String qty,
                                @Field("ProductId") String productID);

    @POST("contact.php")
    @FormUrlEncoded
    Call<JsonArray> getContact(@Field("Flag") String mobile,
                               @Field("UserId") String userid,
                               @Field("Name") String qty,
                               @Field("Email") String type,
                               @Field("Mobile") String txnid,
                               @Field("Message") String msg,
                               @Field("Subject") String productID);

    @POST("payment.php")
    @FormUrlEncoded
    Call<JsonArray> payAmount(@Field("Flag") String Flag,
                              @Field("UserId") String userid,
                              @Field("Amount") String Amount,
                              @Field("Type") String type,
                              @Field("txnid") String txnid,
                              @Field("Remark") String Remark,
                              @Field("paytype") String paytype);

    @POST("change_password.php")
    @FormUrlEncoded
    Call<JsonArray> changePassword(@Field("Flag") String flag,
                                   @Field("UserId") String oldpass,
                                   @Field("OldPassword") String newpass,
                                   @Field("NewPassword") String mobile);


    @POST("chat.php")
    @FormUrlEncoded
    Call<JsonArray> getChats(@Field("Flag") String mobile,
                             @Field("UserId") String userid);


    @POST("chat.php")
    @FormUrlEncoded
    Call<JsonArray> user_chat(@Field("Flag") String Flag,
                              @Field("UserId") String UserId,
                              @Field("Message") String Message
    );

    @POST("chat.php")
    @FormUrlEncoded
    Call<JsonArray> user_chat_show(@Field("Flag") String Flag,
                                   @Field("UserId") String UserId
    );

    @POST("chat.php")
    @FormUrlEncoded
    Call<JsonArray> sendQuery(@Field("Flag") String mobile,
                              @Field("UserId") String userid,
                              @Field("ProductId") String text);

    @POST("stock.php")
    Call<JsonArray> getProducts();

    @POST("requirement.php")
    @FormUrlEncoded
    Call<JsonArray> getSellerProducts(@Field("Flag") String Flag);

    @POST("requirement.php")
    @FormUrlEncoded
    Call<JsonArray> quoteRequirement(@Field("Flag") String mobile,
                                     @Field("UserId") String UserId,
                                     @Field("ProductId") String ProductId,
                                     @Field("Rate") String Rate,
                                     @Field("Availability") String Availability,
                                     @Field("Remark") String Remark,
                                     @Field("StockType") String StockType
    );

    @POST("requirement.php")
    @FormUrlEncoded
    Call<JsonArray> getRequirements(
            @Field("Flag") String flag,
            @Field("UserId") String userid
    );

    @POST("requirement.php")
    @FormUrlEncoded
    Call<JsonArray> getRequirementsPro(
            @Field("Flag") String flag,
            @Field("Id") String id
    );

    @POST("stocklive.php")
    @FormUrlEncoded
    Call<JsonArray> getPDF(
            @Field("Flag") String userid
    );


    @POST("cart.php")
    @FormUrlEncoded
    Call<JsonArray> getCartItems(
            @Field("Flag") String flag,
            @Field("UserId") String userid
    );

    @POST("checkout.php")
    @FormUrlEncoded
    Call<JsonArray> getAddress(
            @Field("Flag") String flag,
            @Field("UserId") String userid
    );

    @POST("checkout.php")
    @FormUrlEncoded
    Call<JsonArray> getOrders(
            @Field("Flag") String flag,
            @Field("UserId") String userid
    );

    @POST("checkout.php")
    @FormUrlEncoded
    Call<JsonArray> getOrderDetails(
            @Field("Flag") String flag,
            @Field("OrderId") String userid
    );

    @POST("faq.php")
    @FormUrlEncoded
    Call<JsonArray> getFaq(
            @Field("Flag") String flag
    );

    @POST("offer.php")
    @FormUrlEncoded
    Call<JsonArray> getOffers(
            @Field("Flag") String flag
    );

    @POST("cart.php")
    @FormUrlEncoded
    Call<JsonArray> addItems(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("ProductId") String p_id,
            @Field("Quantity") String qty
    );

    @POST("profile.php")
    @FormUrlEncoded
    Call<JsonArray> updateProfile(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("Profile") String p_id
    );

    @POST("profile.php")
    @FormUrlEncoded
    Call<JsonArray> updateProfileDetail(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("Name") String p_id,
            @Field("ShopName") String ShopName,
            @Field("Email") String email,
            @Field("Mobile") String mobile,
            @Field("Address") String address,
            @Field("GSTNumber") String GSTNumber,
            @Field("LicenceNumber") String LicenceNumber,
            @Field("FirmName") String FirmName,
            @Field("AccountNumber") String AccountNumber,
            @Field("IFSC") String IFSC,
            @Field("GooglePay") String GooglePay,
            @Field("PanNumber") String PanNumber,
            @Field("licence_no_1") String licence_no_1,
            @Field("licence_no_2") String licence_no_2,
            @Field("gst_file") String gst_file,
            @Field("State") String State,
            @Field("City") String City,
            @Field("Area") String Area,
            @Field("pincode") String pincode
    );

    @POST("checkout.php")
    @FormUrlEncoded
    Call<JsonArray> checkout(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("GSTNO") String gst,
            @Field("Address") String p_id,
            @Field("City") String city,
            @Field("Pincode") String pincode,
            @Field("State") String state,
            @Field("Paymode") String mode,
            @Field("TransportName") String TransportName,
            @Field("Amount") String amount,
            @Field("DiscountAmount") String Discountamount
    );

    @POST("checkout.php")
    @FormUrlEncoded
    Call<JsonArray> addStatus(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("OrderId") String p_id,
            @Field("txnid") String txn,
            @Field("txndata") String txndata
    );

    @POST("cart.php")
    @FormUrlEncoded
    Call<JsonArray> deleteCart(
            @Field("Flag") String flag,
            @Field("CartId") String userid
    );

    @POST("payment.php")
    @FormUrlEncoded
    Call<JsonArray> paymentHistory(
            @Field("Flag") String flag,
            @Field("UserId") String UserId
    );

    @POST("searchproduct.php")
    @FormUrlEncoded
    Call<JsonArray> search(
            @Field("Flag") String flag,
            @Field("UserId") String userid,
            @Field("Search") String search
    );


    @POST("product.php ")
    @FormUrlEncoded
    Call<JsonArray> getShopProducts(
            @Field("MainCategory") String MainCategory

    );

    @POST("productPagination.php")
    @FormUrlEncoded
    Call<JsonArray> getShopProductsPagination(
            @Query("page") int pageNo,
            @Field("MainCategory") String MainCategory,
            @Field("searchText") String searchText

    );

    @POST("token.php")
    @FormUrlEncoded
    Call<JsonArray> sendTokens(
            @Field("Token") String id,
            @Field("UserId") String mobile
    );
}
