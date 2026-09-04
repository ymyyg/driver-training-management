package com.fzx.drivertrainingmanagement.common;

import lombok.Data;

/**
 * Result<T>：
 * 统一返回结果类
 *
 * 字段说明：
 * - success：是否成功
 * - message：提示信息
 * - data：返回的数据
 */
@Data
public class Result<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * 成功（带数据）
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功（不带数据）
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 失败（不带数据）
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}