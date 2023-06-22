package com.daniel.ltc20.domain;
import java.util.Date;

public class InscriptionMintInfo {
    private int id;
    private String address;
    private String inscriptionName;
    private Long total;
    private Long transferable;
    private Long available;
    private String mintTime;
    private Date createTime;

    public InscriptionMintInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInscriptionName() {
        return inscriptionName;
    }

    public void setInscriptionName(String inscriptionName) {
        this.inscriptionName = inscriptionName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTransferable() {
        return transferable;
    }

    public void setTransferable(Long transferable) {
        this.transferable = transferable;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public String getMintTime() {
        return mintTime;
    }

    public void setMintTime(String mintTime) {
        this.mintTime = mintTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
