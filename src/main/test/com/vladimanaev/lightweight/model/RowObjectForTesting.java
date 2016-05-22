package com.vladimanaev.lightweight.model;

import com.vladimanaev.lightweight.model.annotation.*;

/**
 * Created by Vladi
 * Date: 5/21/2016
 * Time: 7:58 PM
 * Copyright VMSR
 */
public class RowObjectForTesting {

    @ColumnDetails(name = "primitive_long")
    private long primitiveLong;

    @ColumnDetails(name = "obj_long")
    private Long objLong;

    @ColumnDetails(name = "primitive_int")
    private int primitiveInt;

    @ColumnDetails(name = "obj_int")
    private Integer objInt;

    @ColumnDetails(name = "primitive_double")
    private Double primitiveDouble;

    @ColumnDetails(name = "obj_double")
    private Double objDouble;

    @ColumnDetails(name = "primitive_boolean")
    private boolean primitiveBoolean;

    @ColumnDetails(name = "obj_boolean")
    private Boolean objBoolean;

    @ColumnDetails(name = "primitive_float")
    private float primitiveFloat;

    @ColumnDetails(name = "obj_float")
    private Float objFloat;

    @ColumnDetails(name = "obj_string")
    private String objString;

    @ColumnDetails(name = "enum_type")
    private EnumForTesting enumType;

    private String fieldWithoutAnnotations;

    public static String justStaticField; //allowed and not mapped

    public static final String justStaticFinalField = "dummy_str"; //allowed and not mapped

    public EnumForTesting getEnumType() {
        return enumType;
    }

    public void setEnumType(EnumForTesting enumType) {
        this.enumType = enumType;
    }

    public boolean getPrimitiveBoolean() {
        return primitiveBoolean;
    }

    public void setPrimitiveBoolean(boolean primitiveBoolean) {
        this.primitiveBoolean = primitiveBoolean;
    }

    public Boolean getObjBoolean() {
        return objBoolean;
    }

    public void setObjBoolean(Boolean objBoolean) {
        this.objBoolean = objBoolean;
    }

    public float getPrimitiveFloat() {
        return primitiveFloat;
    }

    public void setPrimitiveFloat(float primitiveFloat) {
        this.primitiveFloat = primitiveFloat;
    }

    public Float getObjFloat() {
        return objFloat;
    }

    public void setObjFloat(Float objFloat) {
        this.objFloat = objFloat;
    }

    public static enum EnumForTesting {
        TESTING
    }

    public Long getPrimitiveLong() {
        return primitiveLong;
    }

    public void setPrimitiveLong(Long primitiveLong) {
        this.primitiveLong = primitiveLong;
    }

    public String getObjString() {
        return objString;
    }

    public void setObjString(String objString) {
        this.objString = objString;
    }

    public String getFieldWithoutAnnotations() {
        return fieldWithoutAnnotations;
    }

    public void setFieldWithoutAnno(String fieldWithoutAnnotations) {
        this.fieldWithoutAnnotations = fieldWithoutAnnotations;
    }

    public Long getObjLong() {
        return objLong;
    }

    public void setObjLong(Long objLong) {
        this.objLong = objLong;
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    public Integer getObjInt() {
        return objInt;
    }

    public void setObjInt(Integer objInt) {
        this.objInt = objInt;
    }

    public Double getPrimitiveDouble() {
        return primitiveDouble;
    }

    public void setPrimitiveDouble(Double primitiveDouble) {
        this.primitiveDouble = primitiveDouble;
    }

    public Double getObjDouble() {
        return objDouble;
    }

    public void setObjDouble(Double objDouble) {
        this.objDouble = objDouble;
    }
}
