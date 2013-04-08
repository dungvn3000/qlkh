package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

import javax.persistence.Transient;

/**
 * The Class MaterialLimit.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 2:58 PM
 */
public class Material extends AbstractEntity {

    private String name;
    private String code;
    private String unit;
    private String note;

    @Transient
    private MaterialPrice currentPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MaterialPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(MaterialPrice currentPrice) {
        this.currentPrice = currentPrice;
    }
}
