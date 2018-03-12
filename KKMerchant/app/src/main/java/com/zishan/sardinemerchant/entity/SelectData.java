package com.zishan.sardinemerchant.entity;

/**
 * 预约 筛选的数据
 * Created by wislie on 2017/10/24.
 */

public class SelectData {

    private String dateContent;
    private String seatContent;
    private String numContent;

    private boolean isSelected;

    private Long start_time;//开始时间(预约时间)
    private Long end_time;//结束时间(预约时间)
    private Integer need_room;//桌台类型(0:卡座,1:包厢)
    private Integer min_dinner_num;//人数下界
    private Integer max_dinner_num;//人数上界

    public SelectData(){}

    public SelectData(String dateContent){
        this.dateContent = dateContent;
    }

    public SelectData(String dateContent, Long start_time, Long end_time){
        this.dateContent = dateContent;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public SelectData(String seatContent, Integer need_room){
        this.seatContent = seatContent;
        this.need_room = need_room;
    }

    public SelectData(String numContent, Integer min_dinner_num, Integer max_dinner_num){
        this.numContent = numContent;
        this.min_dinner_num = min_dinner_num;
        this.max_dinner_num = max_dinner_num;
    }

    public SelectData(String dateContent, String seatContent, String numContent, Long start_time, Long end_time,
                      Integer need_room, Integer min_dinner_num, Integer max_dinner_num){
        this.dateContent = dateContent;
        this.seatContent = seatContent;
        this.numContent = numContent;
        this.start_time = start_time;
        this.end_time = end_time;
        this.need_room = need_room;
        this.min_dinner_num = min_dinner_num;
        this.max_dinner_num = max_dinner_num;
    }

    public String getDateContent() {
        return dateContent;
    }

    public void setDateContent(String dateContent) {
        this.dateContent = dateContent;
    }

    public String getSeatContent() {
        return seatContent;
    }

    public void setSeatContent(String seatContent) {
        this.seatContent = seatContent;
    }

    public String getNumContent() {
        return numContent;
    }

    public void setNumContent(String numContent) {
        this.numContent = numContent;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Integer getNeed_room() {
        return need_room;
    }

    public void setNeed_room(Integer need_room) {
        this.need_room = need_room;
    }

    public Integer getMin_dinner_num() {
        return min_dinner_num;
    }

    public void setMin_dinner_num(Integer min_dinner_num) {
        this.min_dinner_num = min_dinner_num;
    }

    public Integer getMax_dinner_num() {
        return max_dinner_num;
    }

    public void setMax_dinner_num(Integer max_dinner_num) {
        this.max_dinner_num = max_dinner_num;
    }
}
