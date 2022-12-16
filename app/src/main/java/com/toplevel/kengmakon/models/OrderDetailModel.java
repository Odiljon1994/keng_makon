package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private OrderDetailData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderDetailData getData() {
        return data;
    }

    public void setData(OrderDetailData data) {
        this.data = data;
    }

    public OrderDetailModel(int code, String message, OrderDetailData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class OrderDetailData {
        @SerializedName("store")
        private Store store;
        @SerializedName("seller")
        private Seller seller;
        @SerializedName("order")
        private OrdersModel.DataItemsOrder order;
        @SerializedName("furnitures")
        private List<DetailDataFurnitures> furnitures;
        @SerializedName("payment")
        private DetailDataPayment payment;

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }

        public Seller getSeller() {
            return seller;
        }

        public void setSeller(Seller seller) {
            this.seller = seller;
        }

        public OrdersModel.DataItemsOrder getOrder() {
            return order;
        }

        public void setOrder(OrdersModel.DataItemsOrder order) {
            this.order = order;
        }

        public List<DetailDataFurnitures> getFurnitures() {
            return furnitures;
        }

        public void setFurnitures(List<DetailDataFurnitures> furnitures) {
            this.furnitures = furnitures;
        }

        public DetailDataPayment getPayment() {
            return payment;
        }

        public void setPayment(DetailDataPayment payment) {
            this.payment = payment;
        }

        public OrderDetailData(Store store, Seller seller, OrdersModel.DataItemsOrder order, List<DetailDataFurnitures> furnitures, DetailDataPayment payment) {
            this.store = store;
            this.seller = seller;
            this.order = order;
            this.furnitures = furnitures;
            this.payment = payment;
        }
    }

    public class DetailDataPayment {
        @SerializedName("total_payment")
        private double total_payment;
        @SerializedName("total_cost")
        private double total_cost;
        @SerializedName("items")
        private DetailPaymentItems items;

        public double getTotal_payment() {
            return total_payment;
        }

        public void setTotal_payment(double total_payment) {
            this.total_payment = total_payment;
        }

        public double getTotal_cost() {
            return total_cost;
        }

        public void setTotal_cost(double total_cost) {
            this.total_cost = total_cost;
        }

        public DetailPaymentItems getItems() {
            return items;
        }

        public void setItems(DetailPaymentItems items) {
            this.items = items;
        }

        public DetailDataPayment(double total_payment, double total_cost, DetailPaymentItems items) {
            this.total_payment = total_payment;
            this.total_cost = total_cost;
            this.items = items;
        }
    }
    public class DetailPaymentItems {
        @SerializedName("amount")
        private double amount;
        @SerializedName("rate")
        private double rate;
        @SerializedName("is_prepayment")
        private int is_prepayment;
        @SerializedName("date")
        private String date;
        @SerializedName("description")
        private String description;
        @SerializedName("received")
        private int received;
        @SerializedName("payment_type")
        private String payment_type;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getIs_prepayment() {
            return is_prepayment;
        }

        public void setIs_prepayment(int is_prepayment) {
            this.is_prepayment = is_prepayment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getReceived() {
            return received;
        }

        public void setReceived(int received) {
            this.received = received;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public DetailPaymentItems(double amount, double rate, int is_prepayment, String date, String description, int received, String payment_type) {
            this.amount = amount;
            this.rate = rate;
            this.is_prepayment = is_prepayment;
            this.date = date;
            this.description = description;
            this.received = received;
            this.payment_type = payment_type;
        }
    }

    public class DetailDataColor {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DetailDataColor(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public class DetailDataCategory {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DetailDataCategory(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public class DetailDataInfo {
        @SerializedName("is_custom")
        private int is_custom;
        @SerializedName("description")
        private String description;
        @SerializedName("amount")
        private int amount;
        @SerializedName("cost")
        private double cost;
        @SerializedName("selling_cost")
        private double selling_cost;
        @SerializedName("has_glass")
        private int has_glass;
        @SerializedName("has_drawing")
        private int has_drawing;

        public int getIs_custom() {
            return is_custom;
        }

        public void setIs_custom(int is_custom) {
            this.is_custom = is_custom;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getSelling_cost() {
            return selling_cost;
        }

        public void setSelling_cost(double selling_cost) {
            this.selling_cost = selling_cost;
        }

        public int getHas_glass() {
            return has_glass;
        }

        public void setHas_glass(int has_glass) {
            this.has_glass = has_glass;
        }

        public int getHas_drawing() {
            return has_drawing;
        }

        public void setHas_drawing(int has_drawing) {
            this.has_drawing = has_drawing;
        }

        public DetailDataInfo(int is_custom, String description, int amount, double cost, double selling_cost, int has_glass, int has_drawing) {
            this.is_custom = is_custom;
            this.description = description;
            this.amount = amount;
            this.cost = cost;
            this.selling_cost = selling_cost;
            this.has_glass = has_glass;
            this.has_drawing = has_drawing;
        }
    }

    public class DetailDataFurnitures {
        @SerializedName("furniture")
        private DetailDataFurniture furniture;
        @SerializedName("category")
        private DetailDataCategory category;
        @SerializedName("color")
        private DetailDataColor color;
        @SerializedName("info")
        private DetailDataInfo info;

        public DetailDataFurniture getFurniture() {
            return furniture;
        }

        public void setFurniture(DetailDataFurniture furniture) {
            this.furniture = furniture;
        }

        public DetailDataCategory getCategory() {
            return category;
        }

        public void setCategory(DetailDataCategory category) {
            this.category = category;
        }

        public DetailDataColor getColor() {
            return color;
        }

        public void setColor(DetailDataColor color) {
            this.color = color;
        }

        public DetailDataInfo getInfo() {
            return info;
        }

        public void setInfo(DetailDataInfo info) {
            this.info = info;
        }

        public DetailDataFurnitures(DetailDataFurniture furniture, DetailDataCategory category, DetailDataColor color, DetailDataInfo info) {
            this.furniture = furniture;
            this.category = category;
            this.color = color;
            this.info = info;
        }
    }

    public class DetailDataFurniture {
        @SerializedName("id")
        private int id;
        @SerializedName("category_id")
        private int category_id;
        @SerializedName("name")
        private String name;
        @SerializedName("size")
        private String size;
        @SerializedName("description")
        private String description;
        @SerializedName("image_url")
        private String image_url;
        @SerializedName("image_url_preview")
        private String image_url_preview;
        @SerializedName("is_liked")
        private boolean is_liked;
        @SerializedName("selling_cost")
        private double selling_cost;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getImage_url_preview() {
            return image_url_preview;
        }

        public void setImage_url_preview(String image_url_preview) {
            this.image_url_preview = image_url_preview;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public double getSelling_cost() {
            return selling_cost;
        }

        public void setSelling_cost(double selling_cost) {
            this.selling_cost = selling_cost;
        }

        public DetailDataFurniture(int id, int category_id, String name, String size, String description, String image_url, String image_url_preview, boolean is_liked, double selling_cost) {
            this.id = id;
            this.category_id = category_id;
            this.name = name;
            this.size = size;
            this.description = description;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
            this.is_liked = is_liked;
            this.selling_cost = selling_cost;
        }
    }

    public class Seller {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("is_active")
        private int is_active;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public Seller(int id, String name, int is_active) {
            this.id = id;
            this.name = name;
            this.is_active = is_active;
        }
    }

    public class Store {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Store(int id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
    }
}
