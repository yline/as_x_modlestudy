package com.system.app.contacter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.system.app.activity.AppConstant;
import com.yline.log.LogFileUtil;

/**
 * android.permission.READ_CONTACTS
 * android.permission.WRITE_CONTACTS
 *
 * @author YLine
 * <p/>
 * 2016年7月15日 下午10:49:17
 */
public class ContacterHelper {
    private static final String RAW_CONTACTS_URI = "content://com.android.contacts/raw_contacts";

    private static final String DATA_URI = "content://com.android.contacts/data";

    private static final String NAME_MIMETYPE = "vnd.android.cursor.item/name";

    private static final String PHONE_MIMETYPE = "vnd.android.cursor.item/phone_v2";

    private static final String EMAIL_MIMETYPE = "vnd.android.cursor.item/email_v2";

    /**
     * 查询联系人部分信息
     *
     * @param context
     */
    public void queryContacterInfo(Context context) {
        final int contact_name = 0;
        final int contact_number = 1;
        final int contact_photo = 2;
        final int contact_id = 3;

        //	Phone.CONTENT_URI = "content://" + "com.android.contacts" + "data" + "phones";
        final String[] PHONES_PROJECTION = new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID, Phone.CONTACT_ID};

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactNumber = cursor.getString(contact_number);
                if (TextUtils.isEmpty(contactNumber)) {
                    continue;
                }
                String contactName = cursor.getString(contact_name);
                Long contactid = cursor.getLong(contact_id);
                Long photoid = cursor.getLong(contact_photo);
                Long contactPhoneID = null;
                if (photoid > 0) {
                    contactPhoneID = contactid;
                }

                LogFileUtil.v(AppConstant.TAG_CONTACTER, "contactName = " + contactName + ",contactid = " + contactid + ",contactPhoneID = " + contactPhoneID + ",contactNumber = " + contactNumber);
            }
            cursor.close();
        }
    }

    /**
     * 查询所有联系人
     *
     * @param context
     */
    public void queryContacter(Context context) {
        Uri raw_contactsUri = Uri.parse(RAW_CONTACTS_URI);
        Uri dataUri = Uri.parse(DATA_URI);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(raw_contactsUri, null, null, null, null);

        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            /**
             * 联系人删除操作：使contact_id = null 数据并不删除
             * 目的：有一个功能，服务器同步;为了在网络不好时，删除而本地删除，网络没有删除;
             * 联网后，检查是否为空即可而不需要全部遍历
             */
            if (contact_id != null) {
                Cursor cursorId = resolver.query(dataUri, null, "contact_id=?", new String[]{contact_id}, null);
                while (cursorId.moveToNext()) {
                    String data1 = cursorId.getString(cursorId.getColumnIndex("data1"));
                    String mimetype = cursorId.getString(cursorId.getColumnIndex("mimetype"));

                    LogFileUtil.i(AppConstant.TAG_CONTACTER, "contact_id = " + contact_id + ",display_name = " + display_name + ",data1 = " + data1 + ",mimetype = " + mimetype);
                }

                cursorId.close();
            }
        }
        cursor.close();
    }

    /**
     * 添加联系人
     *
     * @param context
     */
    public void insertContacter(Context context) {
        LogFileUtil.v(AppConstant.TAG_CONTACTER, "insertContacter -> start");

        Uri raw_contacturi = Uri.parse(RAW_CONTACTS_URI);
        Uri datauri = Uri.parse(DATA_URI);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursorId = resolver.query(raw_contacturi, new String[]{"_id"}, null, null, null);

        //得到 插入新的id的值
        cursorId.moveToLast();
        int oldId = cursorId.getInt(cursorId.getColumnIndex("_id"));
        int newId = oldId + 1;

        insertContacterName(resolver, datauri, newId, "yline");
        insertContacterPhone(resolver, datauri, newId, "15958148487");
        insertContacterEmail(resolver, datauri, newId, "957339173@qq.com");

        LogFileUtil.i(AppConstant.TAG_CONTACTER, "insertContacter -> name = yline" + ",phone = 15958148487" + ",email = 957339173@qq.com");
        LogFileUtil.v(AppConstant.TAG_CONTACTER, "insertContacter -> end");
    }

    /**
     * 向data表中插入姓名
     *
     * @param resolver
     * @param datauri
     * @param newId
     * @param name
     */
    private void insertContacterName(ContentResolver resolver, Uri datauri, int newId, String name) {
        ContentValues namevalues = new ContentValues();
        namevalues.put("data1", "yline");
        namevalues.put("mimetype", NAME_MIMETYPE);
        namevalues.put("raw_contact_id", newId);
        resolver.insert(datauri, namevalues);
    }

    /**
     * 向data表中插入电话
     *
     * @param resolver
     * @param datauri
     * @param newId
     * @param phone
     */
    private void insertContacterPhone(ContentResolver resolver, Uri datauri, int newId, String phone) {
        ContentValues phonevalues = new ContentValues();
        phonevalues.put("data1", "15958148487");
        phonevalues.put("mimetype", PHONE_MIMETYPE);
        phonevalues.put("raw_contact_id", newId);
        resolver.insert(datauri, phonevalues);
    }

    private void insertContacterEmail(ContentResolver resolver, Uri datauri, int newId, String email) {
        ContentValues emailvalues = new ContentValues();
        emailvalues.put("data1", "957339173@qq.com");
        emailvalues.put("mimetype", EMAIL_MIMETYPE);
        emailvalues.put("raw_contact_id", newId);
        resolver.insert(datauri, emailvalues);
    }
}
