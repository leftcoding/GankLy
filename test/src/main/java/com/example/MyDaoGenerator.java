package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Create by LingYan on 2016-04-11
 */
public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.gank.gankly");

        addNote(schema);
//        addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "E:/GankLy/app/src/main/java");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("UrlCollect");//类名
        note.addIdProperty();
        note.addStringProperty("url").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
        note.addStringProperty("g_type");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }
}
