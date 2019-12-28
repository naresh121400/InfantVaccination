package com.example.admin.infantvaccination;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Admin on 21-Feb-18.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //DATABASE NAME
    public static final String DATABASE_NAME = "CHILDVACCINE.db";

    //USER TABLE

    public static final String TABLE_USER = "USER";

    public static final String USER_ID = "ID";
    public static final String USER_NAME = "NAME";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "PASSWORD";
    public static final String USER_FBID="FBID";

    //NOTIFICATION TABLE

    public static final String TABLE_NOTIFICATION = "NOTIFICATION";

    public static final String NOTIFICATION_CID = "NCID";
    public static final String NOTIFICATION_ID = "NID";
    public static final String NOTIFICATION_DATE = "NDATE";




    //NOTIFICATION TABLE

    public static final String TABLE_CALENDAR_STATUS = "CALENDAR";

    public static final String CALENDAR_CID = "CCID";
    public static final String CALENDAR_ID = "CALID";




    //CHILD TABLE

    public static final String TABLE_CHILD = "CHILD";
    public static final String CHILD_CID = "CID";
    public static final String CHILD_CNAME = "CNAME";
    public static final String CHILD_CDOB = "CDOB";
    public static final String CHILD_CEMAIL = "CEMAIL";
    public static final String CHILD_CPHONE = "CPHONE";
    public static final String CHILD_CREMINDER = "CREMINDER";
    public static final String CHILD_IMAGE = "CIMAGE";
    public static final String CHILD_CUID = "CUID";
    public static final String CHILD_CESTATUS = "CESTATUS";


    //VACCINE TABLE

    public static final String TABLE_VACCINE = "VACCINE";

    public static final String VACCINE_VID = "VID";
    public static final String VACCINE_VNAME = "VNAME";
    public static final String VACCINE_VDAYS = "VDAYS";
    public static final String VACCINE_VDESC = "VDESC";


    //LIST TABLE

    public static final String TABLE_LIST = "LIST";

    public static final String LIST_LID = "LID";
    public static final String LIST_LCID = "LCID";
    public static final String LIST_LVID = "LVID";
    public static final String LIST_LVNAME = "LVNAME";
    public static final String LIST_LSCHEDULE = "LSCHEDULE";
    public static final String LIST_LGIVEN = "LGIVEN";
    public static final String LIST_LSTATUS = "LSTATUS";
    public static final String LIST_LHOSPITAL = "LHOSPITAL";


    //Database for all
    private SQLiteDatabase database;


    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        //USER TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + "," + USER_NAME + " VARCHAR NOT NULL" + "," + USER_EMAIL + " VARCHAR NOT NULL UNIQUE" + "," + USER_PASSWORD + " VARCHAR NOT NULL" + "," + USER_FBID + " VARCHAR" +")");


        //CHILD TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHILD + "(" + CHILD_CID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + "," + CHILD_CNAME + " VARCHAR NOT NULL" + "," + CHILD_CDOB + " VARCHAR NOT NULL" + "," + CHILD_CEMAIL + " VARCHAR NOT NULL UNIQUE" + "," + CHILD_CPHONE + " VARCHAR NOT NULL UNIQUE" + "," + CHILD_CREMINDER + " VARCHAR NOT NULL " + "," + CHILD_IMAGE + " BLOB" + "," + CHILD_CUID + " INTEGER NOT NULL"+ "," + CHILD_CESTATUS + " VARCHAR NOT NULL" + ")");

        //VACCINE TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VACCINE + "(" + VACCINE_VID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + "," + VACCINE_VNAME + " VARCHAR NOT NULL" + "," + VACCINE_VDAYS + " VARCHAR NOT NULL" + "," + VACCINE_VDESC + " VARCHAR " + ")");

        //LIST TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LIST + "(" + LIST_LID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + "," + LIST_LCID + " INTEGER NOT NULL" + "," + LIST_LVID + " INTEGER NOT NULL" + "," + LIST_LVNAME + " VARCHAR NOT NULL" + "," + LIST_LSCHEDULE + " VARCHAR NOT NULL" + "," + LIST_LGIVEN + " VARCHAR " + "," + LIST_LSTATUS + " VARCHAR " + "," + LIST_LHOSPITAL + " VARCHAR" + "," + "FOREIGN KEY (" + LIST_LCID + ")" + " REFERENCES " + TABLE_CHILD + " (" + CHILD_CID + ")" + " ON DELETE CASCADE " + ")");


        //INSERTING VACCINE DATA


        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_VACCINE + "(" + VACCINE_VNAME + "," + VACCINE_VDAYS + "," + VACCINE_VDESC + ")" + " VALUES(" + "\"BCG\"" + "," + "\"0\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-0\"" + "," + "\"0\"" + "," + "\"NULL\"" + ")" + ",(" + "\"DTP-1\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"IPV-1\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-1\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HB-1\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"Conjugate Pneumococcal\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"ROTARIX-1\"" + "," + "\"42\"" + "," + "\"NULL\"" + ")" + ",(" + "\"DTP-2\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"IPV-2\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-2\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HB-2\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"Conjugate Pneumococcal\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"ROTARIX-2\"" + "," + "\"72\"" + "," + "\"NULL\"" + ")" + ",(" + "\"DTP-3\"" + "," + "\"103\"" + "," + "\"NULL\"" + ")" + ",(" + "\"IPV-3\"" + "," + "\"103\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-3\"" + "," + "\"103\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HIB-3\"" + "," + "\"103\"" + "," + "\"NULL\"" + ")" + ",(" + "\"INFLUENZA-1\"" + "," + "\"181\"" + "," + "\"NULL\"" + ")" + ",(" + "\"INFLUENZA-2\"" + "," + "\"212\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HB-3\"" + "," + "\"242\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-4\"" + "," + "\"242\"" + "," + "\"NULL\"" + ")" + ",(" + "\"MEASLES\"" + "," + "\"273\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HEPATITIS-A1\"" + "," + "\"365\"" + "," + "\"NULL\"" + ")" + ",(" + "\"CHICKEN POX\"" + "," + "\"365\"" + "," + "\"NULL\"" + ")" + ",(" + "\"MMR+VIT-A\"" + "," + "\"454\"" + "," + "\"NULL\"" + ")" + ",(" + "\"DTP-BOOSTER-1\"" + "," + "\"546\"" + "," + "\"NULL\"" + ")" + ",(" + "\"IPV-BOOSTER\"" + "," + "\"546\"" + "," + "\"NULL\"" + ")" + ",(" + "\"OPV-BOOSTER-1\"" + "," + "\"546\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HIB-B1\"" + "," + "\"546\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HEPATITIS A2\"" + "," + "\"546\"" + "," + "\"NULL\"" + ")" + ",(" + "\"Pneumo\"" + "," + "\"730\"" + "," + "\"NULL\"" + ")" + ",(" + "\"TYPHOID\"" + "," + "\"730\"" + "," + "\"NULL\"" + ")" + ",(" + "\"DTP-BOOSTER 2\"" + "," + "\"1813\"" + "," + "\"NULL\"" + ")" + ",(" + "\"POV-BOOSTER 2\"" + "," + "\"1826\"" + "," + "\"NULL\"" + ")" + ",(" + "\"HB-BOOSTER\"" + "," + "\"1826\"" + "," + "\"NULL\"" + ")" + ",(" + "\"MMR+VIT-A\"" + "," + "\"1826\"" + "," + "\"NULL\"" + ")");


        //NOTIFICATION TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "(" + NOTIFICATION_CID + " INTEGER NOT NULL" + "," + NOTIFICATION_ID + " INTEGER NOT NULL" + "," + NOTIFICATION_DATE + " VARCHAR NOT NULL" + ")");

        //CALENDAR TABLE CREATION

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CALENDAR_STATUS + "(" + CALENDAR_CID + " INTEGER NOT NULL" + "," + CALENDAR_ID + " VARCHAR NOT NULL"+ ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VACCINE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR_STATUS);
        onCreate(sqLiteDatabase);

    }


    public ArrayList<GettingVaccineData> getVaccineDetails() {

        database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_VACCINE, null, null, null, null, null, null);

        ArrayList<GettingVaccineData> list = new ArrayList<GettingVaccineData>();

        GettingVaccineData gd;

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                gd = new GettingVaccineData();
                gd.setID(cursor.getString(0));
                gd.setNAME(cursor.getString(1));
                gd.setDAYS(cursor.getString(2));
                gd.setDESC(cursor.getString(3));

                list.add(gd);
            }
        }

        cursor.close();
        database.close();

        return list;

    }

    public int addNewChild(AddChild addChild) {

        int k;
        String phone = addChild.getPhone().toString();
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHILD_CNAME, addChild.getName());
        contentValues.put(CHILD_CDOB, addChild.getDob());
        contentValues.put(CHILD_CEMAIL, addChild.getEmail());
        contentValues.put(CHILD_CPHONE, addChild.getPhone());
        contentValues.put(CHILD_CREMINDER, addChild.getReminder());
        contentValues.put(CHILD_CUID, addChild.getUid());
        contentValues.put(CHILD_CESTATUS,addChild.getEstatus());
        k = (int) database.insert(TABLE_CHILD, null, contentValues);

        String[] selectionArgs = new String[]{phone};
        String entryGirisSQL = "SELECT " + CHILD_CID + " FROM CHILD WHERE " + CHILD_CPHONE + "= ?";
        Cursor cursor = database.rawQuery(entryGirisSQL, selectionArgs);
        //Cursor c = database.rawQuery("SELECT "+CHILD_CID+" FROM " +TABLE_CHILD+ " WHERE "+CHILD_CPHONE+" = '" +phone+ "'" , null);

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(CHILD_CID));
            if (index >= 0)
                k = index;


        } else
            k = 0;

        database.close();

        return k;


    }

    public int insertList(ArrayList<ListModelClass> arrayList) {


        int k = 0;
        database = this.getReadableDatabase();
        ContentValues contentValues;
        ListModelClass listModelClass;

        for (int i = 0; i < arrayList.size(); i++) {
            contentValues = new ContentValues();
            listModelClass = arrayList.get(i);
            contentValues.put(LIST_LCID, listModelClass.getLcid());
            contentValues.put(LIST_LVID, listModelClass.getLvid());
            contentValues.put(LIST_LVNAME, listModelClass.getLvname());
            contentValues.put(LIST_LSCHEDULE, listModelClass.getLschedule());
            contentValues.put(LIST_LGIVEN, listModelClass.getLgiven());
            contentValues.put(LIST_LSTATUS, listModelClass.getLstatus());
            contentValues.put(LIST_LHOSPITAL, listModelClass.getLhospital());
            k = (int) database.insert(TABLE_LIST, null, contentValues);

        }

        database.close();
        return k;

    }


    public ArrayList<AddChild> fetchChildList(String uid) {


        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{uid};
        String sql = "SELECT * FROM " + TABLE_CHILD + " WHERE CUID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        ArrayList<AddChild> childArrayList = new ArrayList<AddChild>();
        AddChild addChild;

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                addChild = new AddChild();
                addChild.setId(cursor.getString(0));
                addChild.setName(cursor.getString(1));
                addChild.setDob(cursor.getString(2));
                addChild.setEmail(cursor.getString(3));
                addChild.setPhone(cursor.getString(4));
                addChild.setReminder(cursor.getString(5));
                addChild.setImage(cursor.getBlob(6));

                childArrayList.add(addChild);
            }

        }

        database.close();
        return childArrayList;

    }

    public ArrayList<ListModelClass> gettingChildVaccineData(String cid,String option) {

        database = this.getReadableDatabase();
        String sql="";
        String[] selectionArgs = new String[]{cid};
        if(option.equals("1")){
            sql = sql+"SELECT * FROM " + TABLE_LIST + " WHERE LCID = ?";
        }
        else if(option.equals("2")){
            sql = sql+"SELECT * FROM " + TABLE_LIST + " WHERE LCID = ? AND LSTATUS ="+"\"GIVEN\""+";";
        }
        else if(option.equals("3")){
            sql = sql+"SELECT * FROM " + TABLE_LIST + " WHERE LCID = ? AND LSTATUS ="+"\"NOT GIVEN\""+";";
        }

        Cursor cursor = database.rawQuery(sql, selectionArgs);
        ArrayList<ListModelClass> arrayList = new ArrayList<ListModelClass>();
        ListModelClass listModelClass;

        if (cursor.getCount() > 0) {

            String s = "";
            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToNext();


                listModelClass = new ListModelClass();
                listModelClass.setLid(cursor.getString(0));
                listModelClass.setLcid(cursor.getString(1));
                listModelClass.setLvid(cursor.getString(2));
                listModelClass.setLvname(cursor.getString(3));
                listModelClass.setLschedule(cursor.getString(4));
                s = cursor.getString(5).toString();
                if (s.equals("NULL")) {
                    listModelClass.setLgiven("00/00/0000");
                } else {
                    listModelClass.setLgiven(cursor.getString(5));
                }
                listModelClass.setLstatus(cursor.getString(6));
                listModelClass.setLhospital(cursor.getString(7));

                arrayList.add(listModelClass);

            }

        }

        database.close();

        return arrayList;
    }

    public int updateVaccineData(ListModelClass listModelClass) {


        database = this.getReadableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(LIST_LGIVEN, listModelClass.getLgiven());
        contentValues.put(LIST_LSTATUS, listModelClass.getLstatus());
        contentValues.put(LIST_LHOSPITAL, listModelClass.getLhospital());

        int k = (int) database.update(TABLE_LIST, contentValues, LIST_LID + " = ?", new String[]{listModelClass.getLid()});

        database.close();

        return k;

    }

    public AddChild getSingleChild(String id) {

        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{id};
        String sql = "SELECT * FROM " + TABLE_CHILD + " WHERE CID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        AddChild addChild = new AddChild();

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToNext();

                addChild.setId(cursor.getString(0));
                addChild.setName(cursor.getString(1));
                addChild.setDob(cursor.getString(2));
                addChild.setEmail(cursor.getString(3));
                addChild.setPhone(cursor.getString(4));
                addChild.setReminder(cursor.getString(5));
            }

        }

        database.close();
        return addChild;
    }

    public int deleteSingleVaccine(String cid, String vid) {

        int res;
        database = this.getReadableDatabase();
        String ids[] = {cid, vid};
        String sql = "LCID = ? AND LVID = ? ";
        res = database.delete(TABLE_LIST, LIST_LCID + "=? AND " + LIST_LVID + "=?", new String[]{cid, vid});
        database.close();
        return res;
    }

    public String getLvidForDelete(String lid) {

        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{lid};
        String sql = "SELECT LVID FROM " + TABLE_LIST + " WHERE LID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        cursor.moveToNext();
        String lvid = cursor.getString(0);

        return lvid;
    }

    public ListModelClass gettingChildVaccineDataWithLid(String lid) {

        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{lid};
        String sql = "SELECT * FROM " + TABLE_LIST + " WHERE LID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        ListModelClass listModelClass = new ListModelClass();

        String s = "";
        if (cursor.getCount() > 0) {


            cursor.moveToNext();


            listModelClass.setLid(cursor.getString(0));
            listModelClass.setLcid(cursor.getString(1));
            listModelClass.setLvid(cursor.getString(2));
            listModelClass.setLvname(cursor.getString(3));
            listModelClass.setLschedule(cursor.getString(4));
            s = cursor.getString(5).toString();
            if (s.equals("NULL")) {
                listModelClass.setLgiven("00/00/0000");
            } else {
                listModelClass.setLgiven(cursor.getString(5));
            }
            listModelClass.setLstatus(cursor.getString(6));
            listModelClass.setLhospital(cursor.getString(7));


        }

        database.close();

        return listModelClass;
    }

    public int changeImage(AddChild ac) {


        database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(CHILD_IMAGE, ac.getImage());

        int k = (int) database.update(TABLE_CHILD, contentValues, CHILD_CID + " = ?", new String[]{ac.getId()});

        database.close();

        return k;

    }

    public ArrayList<String> getScheduleDays(String cid) {


        ArrayList<String> list = new ArrayList<String>();
        String date = "";
        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{cid};
        String entryGirisSQL = "SELECT " + LIST_LSCHEDULE + " FROM LIST WHERE " + LIST_LCID + "= ?";
        Cursor cursor = database.rawQuery(entryGirisSQL, selectionArgs);
        //Cursor c = database.rawQuery("SELECT "+CHILD_CID+" FROM " +TABLE_CHILD+ " WHERE "+CHILD_CPHONE+" = '" +phone+ "'" , null);

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToNext();
                date = cursor.getString(0);
                list.add(date);

            }

        }
        database.close();

        return list;
    }

    public String insertUserDet(UserModelClass umc,String type) {

        String res = "";
        database = this.getReadableDatabase();
        if(type.equals("gmail") || type.equals("normal")){

            String[] selectionArgs = new String[]{umc.getEMAIL().toString()};
            String sql = "SELECT ID FROM " + TABLE_USER + " WHERE EMAIL = ?";
            Cursor cursor = database.rawQuery(sql, selectionArgs);

            if (cursor.getCount() > 0) {
                cursor.moveToPosition(cursor.getCount() - 1);
                int index = cursor.getInt(cursor.getColumnIndex(USER_ID));
                if (index >= 0)
                    res = res + "euser";


            } else {

                ContentValues contentValues = new ContentValues();
                contentValues.put(USER_NAME, umc.getNAME());
                contentValues.put(USER_EMAIL, umc.getEMAIL());
                contentValues.put(USER_PASSWORD, umc.getPASSWORD());

                int k = (int) database.insert(TABLE_USER, null, contentValues);
                if (k > 0) {
                    res = res + "insert";
                } else {
                    res = res + "not insert";
                }

            }


        }
        else if(type.equals("facebook")){

            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_NAME, umc.getNAME());
            contentValues.put(USER_EMAIL, umc.getEMAIL());
            contentValues.put(USER_PASSWORD, umc.getPASSWORD());
            contentValues.put(USER_FBID,umc.getFBID());

            int k = (int) database.insert(TABLE_USER, null, contentValues);
            if (k > 0) {
                res = res + "insert";
            } else {
                res = res + "not insert";
            }


        }


        database.close();
        return res;


    }

    public String userLogin(String email, String password) {

        String res = "";
        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{email};
        String sql = "SELECT ID FROM " + TABLE_USER + " WHERE EMAIL = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);


        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(USER_ID));
            if (index >= 0) {

                String[] selectionArgs1 = new String[]{email, password};

                String sql1 = "SELECT * FROM " + TABLE_USER + " WHERE EMAIL = ? AND PASSWORD = ?";
                Cursor cursor1 = database.rawQuery(sql1, selectionArgs1);
                if (cursor1.getCount() > 0) {

                    res = res + index;
                } else {
                    res = res + "wrong credentials";
                }

            }


        } else {

            res = res + "not register";
        }


        database.close();
        return res;
    }

    public String checkExist(String email) {

        database = this.getReadableDatabase();
        String res = "";
        String[] selectionArgs = new String[]{email};
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE EMAIL = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        if (cursor.getCount() > 0) {
            res = res + "exist";
        } else {
            res = res + "not exist";
        }
        database.close();
        return res;

    }
    public String checkExistWithFbID(String fbid) {

        String res = "";
        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{fbid};
        String sql = "SELECT ID FROM " + TABLE_USER + " WHERE FBID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);


        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(USER_ID));
            res=res+index;


        } else {

            res = res + "not register";
        }
        return res;

    }

    public int changePassword(String email, String password) {

        database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_PASSWORD, password);

        int k = (int) database.update(TABLE_USER, contentValues, USER_EMAIL + " = ?", new String[]{email});

        return k;

    }

    public String deleteChild(String cid) {

        String result = "";
        database = this.getReadableDatabase();

        int res;
        res = database.delete(TABLE_CHILD, CHILD_CID + "=?", new String[]{cid});

        if (res > 0) {


            int res1;
            res1 = database.delete(TABLE_LIST, LIST_LCID + "=?", new String[]{cid});
            if (res1 > 0) {

                int res2;
                res2 = database.delete(TABLE_NOTIFICATION, NOTIFICATION_CID + "=?", new String[]{cid});
                if(res2>0){
                    result = result + "success";
                }
                else{
                    result = result + "failure";
                }


            }
            else {
                result = result + "failure";
            }


        } else {
            result = result + "failure";
        }
        database.close();

        return result;

    }

    public String userMail(String uid) {


        String res = "";
        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{uid};
        String sql = "SELECT EMAIL FROM " + TABLE_USER + " WHERE ID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);


        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(USER_EMAIL));
            if (index >= 0) {

                String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                res = res + email;

            } else {
                res = res + "fail";
            }

        } else {
            res = res + "fail";
        }

        return res;

    }

    public int storeNotificationData(ArrayList<NotificationsModelClass> arrayList) {


        int k = 0;
        database = this.getReadableDatabase();
        ContentValues contentValues;
        NotificationsModelClass nmc;

        for (int i = 0; i < arrayList.size(); i++) {
            contentValues = new ContentValues();
            nmc = arrayList.get(i);
            contentValues.put(NOTIFICATION_CID, nmc.getCid());
            contentValues.put(NOTIFICATION_ID, nmc.getNotificationId());
            contentValues.put(NOTIFICATION_DATE, nmc.getNotificationDate());
            k = (int) database.insert(TABLE_NOTIFICATION, null, contentValues);

        }

        database.close();

        return k;

    }

    public ArrayList<NotificationsModelClass> getNotificationData(String cid) {


        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{cid};
        String sql = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE NCID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        ArrayList<NotificationsModelClass> notificationArrayList = new ArrayList<NotificationsModelClass>();
        NotificationsModelClass nmc;

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                nmc = new NotificationsModelClass();
                nmc.setCid(cursor.getString(0));
                nmc.setNotificationId(cursor.getString(1));
                nmc.setNotificationDate(cursor.getString(2));

                notificationArrayList.add(nmc);
            }

        }

        database.close();
        return notificationArrayList;


    }

    public String getNotificationStatus(String cid) {

        String res = "";
        database = this.getReadableDatabase();


        String[] selectionArgs = new String[]{cid};
        String sql = "SELECT CREMINDER FROM " + TABLE_CHILD + " WHERE CID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(CHILD_CREMINDER));
            if (index >= 0) {

                String reminder = cursor.getString(cursor.getColumnIndex(CHILD_CREMINDER));
                res = res + reminder;

            } else {
                res = res + "fail";
            }

        }

        database.close();

        return res;
    }
    public int setNotificationStatus(String cid,String status){


        database = this.getReadableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(CHILD_CREMINDER, status);

        int k = (int) database.update(TABLE_CHILD, contentValues, CHILD_CID + " = ?", new String[]{cid});

        database.close();

        return k;
    }
    public String getEstatus(String cid){

        String res = "";
        database = this.getReadableDatabase();


        String[] selectionArgs = new String[]{cid};
        String sql = "SELECT CESTATUS FROM " + TABLE_CHILD + " WHERE CID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(cursor.getCount() - 1);
            int index = cursor.getInt(cursor.getColumnIndex(CHILD_CESTATUS));
            if (index >= 0) {

                String reminder = cursor.getString(cursor.getColumnIndex(CHILD_CESTATUS));
                res = res + reminder;

            } else {
                res = res + "fail";
            }

        }

        database.close();

        return res;

    }
    public int setEstatus(String cid,String status){


        database = this.getReadableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(CHILD_CESTATUS, status);

        int k = (int) database.update(TABLE_CHILD, contentValues, CHILD_CID + " = ?", new String[]{cid});

        database.close();

        return k;

    }
    public int storeCalendarData(ArrayList<CalendarModelClass> arrayList) {


        int k = 0;
        database = this.getReadableDatabase();
        ContentValues contentValues;
        CalendarModelClass cmc;

        for (int i = 0; i < arrayList.size(); i++) {
            contentValues = new ContentValues();
            cmc = arrayList.get(i);
            contentValues.put(CALENDAR_CID, cmc.getCid());
            contentValues.put(CALENDAR_ID, cmc.getId());
            k = (int) database.insert(TABLE_CALENDAR_STATUS, null, contentValues);

        }

        database.close();

        return k;

    }
    public ArrayList<CalendarModelClass> getCalendarData(String cid) {


        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{cid};
        String sql = "SELECT * FROM " + TABLE_CALENDAR_STATUS + " WHERE CCID = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);

        ArrayList<CalendarModelClass> notificationArrayList = new ArrayList<CalendarModelClass>();
        CalendarModelClass cmc;

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                cmc = new CalendarModelClass();
                cmc.setCid(cursor.getString(0));
                cmc.setId(cursor.getString(1));

                notificationArrayList.add(cmc);
            }

        }

        database.close();
        return notificationArrayList;


    }
    public int deleteCalendarEvents(String cid) {

        int res;
        database = this.getReadableDatabase();
        String ids[] = {cid};
        String sql = "CCID = ?";
        res = database.delete(TABLE_CALENDAR_STATUS, CALENDAR_CID + "=?", new String[]{cid});
        database.close();
        return res;
    }
    public String getUidFromUser(String email) {

        database = this.getReadableDatabase();

        String[] selectionArgs = new String[]{email};
        String sql = "SELECT ID FROM " + TABLE_USER + " WHERE EMAIL = ?";
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        cursor.moveToNext();
        String lvid = cursor.getString(0);

        return lvid;
    }
    public int updateChildName(String cid,String name){

        database = this.getReadableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(CHILD_CNAME,name);

        int k = (int) database.update(TABLE_CHILD, contentValues, CHILD_CID + " = ?", new String[]{cid});

        database.close();

        return k;

    }
}
