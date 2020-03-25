package com.yline.webview.study.jsbridge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * data of bridge
 *
 * @author haoqing
 */
public class Message {
    private String callbackId; //callbackId
    private String responseId; //responseId
    private String responseData; //responseData
    private String data; //data of message
    private String handlerName; //name of handler

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }


    public static String toJson(Message message) {
        return message2String(message);
    }

    public static Message toMessage(String jsonStr) {
        try {
            return string2Message(new JSONObject(jsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Message> toMessageList(String jsonStr) {
        try {
            List<Message> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(string2Message(jsonObject));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String message2String(Message message) {
        if (null == message) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("callbackId", message.callbackId);
            jsonObject.put("data", message.data);
            jsonObject.put("handlerName", message.handlerName);
            jsonObject.put("responseId", message.responseId);
            jsonObject.put("responseData", message.responseData);
            return jsonObject.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Message string2Message(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        try {
            Message message = new Message();
            message.callbackId = jsonObject.has("callbackId") ? jsonObject.getString("callbackId") : null;
            message.data = jsonObject.has("data") ? jsonObject.getString("data") : null;
            message.handlerName = jsonObject.has("handlerName") ? jsonObject.getString("handlerName") : null;
            message.responseId = jsonObject.has("responseId") ? jsonObject.getString("responseId") : null;
            message.responseData = jsonObject.has("responseData") ? jsonObject.getString("responseData") : null;
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
