package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Supplier {

    private int supplierNo;
    private String name;
    private String address;

    public Supplier() {}
    public Supplier(int supplierNo, String name, String address) {
        this.supplierNo = supplierNo;
        this.name = name;
        this.address = address;
    }

    @JsonProperty
    public int getSupplierNo() {
        return supplierNo;
    }

    @JsonProperty
    public void setSupplierNo(int supplierNo) {
        this.supplierNo = supplierNo;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getAddress() {
        return address;
    }

    @JsonProperty
    public void setAddress(String address) {
        this.address = address;
    }
}
