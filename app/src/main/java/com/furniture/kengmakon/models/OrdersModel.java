package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private OrdersData data;

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

    public OrdersData getData() {
        return data;
    }

    public void setData(OrdersData data) {
        this.data = data;
    }

    public OrdersModel(int code, String message, OrdersData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class OrdersData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        List<OrdersDataItems> items;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<OrdersDataItems> getItems() {
            return items;
        }

        public void setItems(List<OrdersDataItems> items) {
            this.items = items;
        }

        public OrdersData(int currentPage, int totalPages, List<OrdersDataItems> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class OrdersDataItems {
        @SerializedName("order")
        private DataItemsOrder order;
        @SerializedName("furnitures")
        private List<OrderDataFurniture> furnitures;
        @SerializedName("payment")
        private OrderDataPayment payment;


        public DataItemsOrder getOrder() {
            return order;
        }

        public void setOrder(DataItemsOrder order) {
            this.order = order;
        }

        public List<OrderDataFurniture> getFurnitures() {
            return furnitures;
        }

        public void setFurnitures(List<OrderDataFurniture> furnitures) {
            this.furnitures = furnitures;
        }

        public OrderDataPayment getPayment() {
            return payment;
        }

        public void setPayment(OrderDataPayment payment) {
            this.payment = payment;
        }

        public OrdersDataItems(DataItemsOrder order, List<OrderDataFurniture> furnitures, OrderDataPayment payment) {
            this.order = order;
            this.furnitures = furnitures;
            this.payment = payment;
        }
    }

    public class OrderDataFurniture {
        @SerializedName("furniture")
        private OrdersDataFurniture furniture;

        public OrdersDataFurniture getFurniture() {
            return furniture;
        }

        public void setFurniture(OrdersDataFurniture furniture) {
            this.furniture = furniture;
        }

        public OrderDataFurniture(OrdersDataFurniture furniture) {
            this.furniture = furniture;
        }
    }

    public class OrderDataPayment {
        @SerializedName("total_payment")
        private double total_payment;
        @SerializedName("total_cost")
        private float total_cost;

        public double getTotal_payment() {
            return total_payment;
        }

        public void setTotal_payment(double total_payment) {
            this.total_payment = total_payment;
        }

        public float getTotal_cost() {
            return total_cost;
        }

        public void setTotal_cost(float total_cost) {
            this.total_cost = total_cost;
        }

        public OrderDataPayment(double total_payment, float total_cost) {
            this.total_payment = total_payment;
            this.total_cost = total_cost;
        }
    }

    public class OrdersDataFurniture {
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

        public OrdersDataFurniture(int id, int category_id, String name, String size, String description, String image_url, String image_url_preview, boolean is_liked, double selling_cost) {
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

    public class DataItemsOrder {
        @SerializedName("id")
        private int id;
        @SerializedName("time_from")
        private String time_from;
        @SerializedName("time_to")
        private String time_to;
        @SerializedName("date")
        private String date;
        @SerializedName("delivery_date")
        private String delivery_date;
        @SerializedName("doc_no")
        private String doc_no;
        @SerializedName("is_delivery")
        private int is_delivery;
        @SerializedName("is_assembly")
        private int is_assembly;
        @SerializedName("is_approved")
        private int is_approved;
        @SerializedName("address")
        private String address;
        @SerializedName("closed")
        private int closed;
        @SerializedName("region_id")
        private int region_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime_from() {
            return time_from;
        }

        public void setTime_from(String time_from) {
            this.time_from = time_from;
        }

        public String getTime_to() {
            return time_to;
        }

        public void setTime_to(String time_to) {
            this.time_to = time_to;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getDoc_no() {
            return doc_no;
        }

        public void setDoc_no(String doc_no) {
            this.doc_no = doc_no;
        }

        public int getIs_delivery() {
            return is_delivery;
        }

        public void setIs_delivery(int is_delivery) {
            this.is_delivery = is_delivery;
        }

        public int getIs_assembly() {
            return is_assembly;
        }

        public void setIs_assembly(int is_assembly) {
            this.is_assembly = is_assembly;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getClosed() {
            return closed;
        }

        public void setClosed(int closed) {
            this.closed = closed;
        }

        public int getRegion_id() {
            return region_id;
        }

        public void setRegion_id(int region_id) {
            this.region_id = region_id;
        }

        public DataItemsOrder(int id, String time_from, String time_to, String date, String delivery_date, String doc_no, int is_delivery, int is_assembly, int is_approved, String address, int closed, int region_id) {
            this.id = id;
            this.time_from = time_from;
            this.time_to = time_to;
            this.date = date;
            this.delivery_date = delivery_date;
            this.doc_no = doc_no;
            this.is_delivery = is_delivery;
            this.is_assembly = is_assembly;
            this.is_approved = is_approved;
            this.address = address;
            this.closed = closed;
            this.region_id = region_id;
        }
    }
}
