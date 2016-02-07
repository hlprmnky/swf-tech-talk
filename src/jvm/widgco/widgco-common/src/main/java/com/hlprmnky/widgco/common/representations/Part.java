package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Part {

    private int partNo;
    private String name;
    private int supplierNo;
    private int onHandQty;

    public Part() {}
    public Part(int partNo, String name, int supplierNo, int onHandQty) {
        this.partNo = partNo;
        this.name = name;
        this.supplierNo = supplierNo;
        this.onHandQty = onHandQty;
    }

    @JsonProperty
    public int getPartNo() {
        return partNo;
    }

    @JsonProperty
    public void setPartNo(int partNo) {
        this.partNo = partNo;
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
    public int getSupplierNo() {
        return supplierNo;
    }

    @JsonProperty
    public void setSupplierNo(int supplierNo) {
        this.supplierNo = supplierNo;
    }

    @JsonProperty
    public int getOnHandQty() {
        return onHandQty;
    }

    @JsonProperty
    public void setOnHandQty(int onHandQty) {
        this.onHandQty = onHandQty;
    }
}
