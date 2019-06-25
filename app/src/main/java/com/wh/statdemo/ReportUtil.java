package com.wh.statdemo;

import android.view.View;

/**
 * @author KwunYamShan.
 * @time 2019/6/25.
 * @explain
 */
public class ReportUtil {

    private static class HelperHolder {
        public static final ReportUtil util = new ReportUtil();
    }

    public static ReportUtil getInstance() {
        return HelperHolder.util;
    }

    public int getMarkTag() {
        return R.id.mark;
    }

    public void setMarkTag(View view) {
        view.setTag(getMarkTag(), "msg");
    }

    public int getPriceTag() {
        return R.id.price;
    }

    public void setPriceReport(View view, String msg) {
        setMarkTag(view);
        view.setTag(getPriceTag(), msg);
    }

    public int getBlockNameTag() {
        return R.id.blockname;
    }

    public void setBlockNameTag(View view, String msg) {
        setMarkTag(view);
        view.setTag(getBlockNameTag(), msg);
    }

    public int getItemNameTag() {
        return R.id.itemname;
    }

    public void setItemNameTag(View view, String msg) {
        setMarkTag(view);
        view.setTag(getItemNameTag(), msg);
    }

    public void setReportData(View view, String price, String blockname, String itemName) {
        setMarkTag(view);
        view.setTag(getPriceTag(), price);
        view.setTag(getBlockNameTag(), blockname);
        view.setTag(getItemNameTag(), itemName);
    }

}