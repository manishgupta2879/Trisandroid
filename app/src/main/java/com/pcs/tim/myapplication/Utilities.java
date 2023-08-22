package com.pcs.tim.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Tim on 6/22/2017.
 */

public class Utilities {
    //for location
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.pcs.tim.myapplication";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    //IP & URL
    //public static final String IP = "58.71.201.41";
    public static final String IP = "211.24.73.117:8102";
    public static final String IP_NoPort = "211.24.73.117";
//    public static final String IP = "192.168.0.147:8088";
    public static final String GET_VERSION = "http://" + IP + "/api/AndroidApp";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.pcs.tim.myapplication";
    public static final String GET_TOKEN = "http://" + IP + "/token";
    public static final String LOGIN = "http://" + IP + "/api/enforcementlogin";
    public static final String API_KEY = "http://" + IP + "/api/apikey";
    public static final String ENFORCEMENT = "http://" + IP + "/api/enforcement";
    public static final String DISTRICT_STATION = "http://" + IP + "/api/districtstation";
    public static final String DEPARTMENT = "http://" + IP + "/api/department";
    public static final String REFUGEE = "http://" + IP + "/api/refugee";
    public static final String PLKS = "http://" + IP + "/api/PLKS";
    public static final String LOG = "http://" + IP + "/api/log";
    public static final String ENFORMENT_PHOTO_IP = "http://" + IP_NoPort + ":8101/";

    //Refugee Data
    public static final String NFC = "nfc_id";
    public static final String ERROR = "Error, please try again later.";
    public static final String CATEGORY = "category";
    public static final String REG_ID = "regID";
    public static final String MY_RC = "myrc";
    public static final String UNHCRID = "unhcrID";
    public static final String FULL_NAME = "fullName";
    public static final String DOB = "dob";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String COUNTRY = "countryOfOrigin";
    public static final String RELIGION = "religion";
    public static final String ETHNIC_GROUP = "ethincGroup";
    public static final String ISSUE_DATE = "issueDate";
    public static final String EXPIRY_DATE = "expiredDate";
    public static final String PHOTO = "photo";

    //Police Data
    public static final String POLICE_ID = "police_id";
    public static final String IC_NO = "ic_number";
    public static final String POLICE_NAME = "fullname";
    public static final String POLICE_RANK = "police_rank";
    public static final String POLICE_MOBILE = "mobile_no";
    public static final String POLICE_AGENCY = "agency";
    public static final String POLICE_DEPARTMENT = "department";
    public static final String POLICE_STATION = "station";
    public static final String POLICE_STATE = "state";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";

    //shared preferences
    public static final String LOGIN_POLICE_PHOTO = "logged_in_police_photo";
    public static final String LOGIN_POLICE_IC = "logged_in_police_ic";
    public static final String LOGIN_POLICE_NAME = "logged_in_police_name";
    public static final String LOGIN_POLICE_MOBILE = "logged_in_police_mobile";
    public static final String LOGIN_POLICE_RANK = "logged_in_police_rank";
    public static final String LOGIN_POLICE_AGENCY = "logged_in_police_agency";
    public static final String LOGIN_POLICE_STATE = "logged_in_police_state";
    public static final String LOGIN_POLICE_DEPT = "logged_in_police_dept";
    public static final String LOGIN_POLICE_STATION = "logged_in_police_station";
    public static final String LOGIN_POLICE_PWD = "logged_in_police_pwd";
    public static final String LOGIN_POLICE_STATUS = "logged_in_police_status";

    public static final String MY_RC_SHARE_PREF = "MyRC Verification Shared Preferences";
    public static final String LOGIN_POLICE_ID = "logged_in_police_id";
    public static final String LOGIN_ID = "logged_in_id";
    public static final String LOGGED_IN = "logged_in";

    //Log Data
    public static final String LOG_POLICE_ID = "police_id";
    public static final String LOG_REMARK = "remark";
    public static final String LOCATION_ADDRESS = "location_address";
    public static final String LOCATION = "location";
    public static final String CHECK_TIME = "check_time";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";

    public Utilities(){}

    public static String getHexString(byte[] a){
        String hexString = "";
        String thisByte;
        for(int i = 0; i < a.length; i++){
            thisByte = "".format("%02x", a[i]);
            hexString += thisByte;
        }

        return hexString;
    }

    public static String Get_Version() {

        URL url;
        String response = "";
        try {
            String stringURL = GET_VERSION;
            url = new URL(stringURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(60000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            int responseCode=conn.getResponseCode();
            String line;
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine() )!= null) {
                    response += line;
                }
            }
            else {
                response="ERROR";
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = ERROR;
        }

        return response;
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static String getPath(Context context, Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Connection getMySqlConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        Connection connection = DriverManager.getConnection("jdbc:mysql://" + IP + ":3306/tris_db", "root", "tris159753");

        //Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.100:3306/tris_db", "root", "tris159753");
        return connection;
    }

    public static void startIntentService(Context context, ResultReceiver mResultReceiver, Location mLastLocation) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Utilities.RECEIVER, mResultReceiver);
        intent.putExtra(Utilities.LOCATION_DATA_EXTRA, mLastLocation);
        context.startService(intent);
    }

    public static void displayAddressOutput(EditText editTextLocation, String mAddressOutput){
        editTextLocation.setVisibility(View.VISIBLE);
        editTextLocation.setText(mAddressOutput);
    }

    public static String getMD5Hash(String data) {

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return Utilities.getHexString(hash); // make it printable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static FTPClient connnectingwithFTP(String ip, String userName, String pass) {
        boolean status = false;
        try {
            FTPClient mFtpClient = new FTPClient();
            mFtpClient.setConnectTimeout(10 * 1000);
            mFtpClient.connect(InetAddress.getByName(ip));
            status = mFtpClient.login(userName, pass);
            Log.e("isFTPConnected", String.valueOf(status));
            if (FTPReply.isPositiveCompletion(mFtpClient.getReplyCode())) {
                mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFtpClient.enterLocalPassiveMode();
                FTPFile[] mFileArray = mFtpClient.listFiles();
                Log.e("Size", String.valueOf(mFileArray.length) + mFileArray.toString());
            }
            return mFtpClient;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean uploadFile(FTPClient ftpClient, File downloadFile, String serverfilePath) {
        try {
            FileInputStream srcFileStream = new FileInputStream(downloadFile);
            boolean status = ftpClient.storeFile(serverfilePath,
                    srcFileStream);
            Log.e("Status", String.valueOf(status) + " " + ftpClient.getReplyString() + " " + serverfilePath);
            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams, String token, String tokenType) {

        URL url;
        HttpURLConnection conn = null;
        String response = "";
        try {
            url = new URL(requestURL);
            if(token!=null && tokenType!=null) {
                String autorize = tokenType + " " + token;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", autorize);
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }
            else{
                conn = (HttpURLConnection) url.openConnection();
                //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }
            //Log.i("HttpURLConnection", conn.getRequestProperties().toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int responseCode = conn.getResponseCode();//unable to get response -- timeout

            Log.d("photo__1", "sendPostRequest: "+conn.getResponseMessage());
            String line;

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine() )!= null) {
                    //Log.i("Readline", line);
                    response += line;
                }
            } else {
                response = "Error Registering";
            }
        } catch (IOException e) {
            if (conn != null) {
                conn.disconnect();
            }            e.printStackTrace();
            response = "";
            sendPostRequest(requestURL, postDataParams, token , tokenType);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        //Log.i("Post String", result.toString());
        return result.toString();
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {

        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

// Split at colon, use second item in the array
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];

                String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    public static Bitmap compressImage( String filePath) {
        // String filePath = filepath;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        int actualHeight = 480;
        int actualWidth = 330;
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        if(options.outHeight > 0 && options.outWidth > 0) {
            actualHeight = options.outHeight;
            actualWidth = options.outWidth;
        }


//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 480;
        float maxWidth = 330;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path

            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX;
        float ratioY;
        float middleX;
        float middleY;


        ratioX = actualWidth / (float) options.outWidth;
        ratioY = actualHeight / (float) options.outHeight;
        middleX = actualWidth / 2.0f;
        middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly

        ExifInterface exif;

        try {

            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e){
            e.printStackTrace();
        }


        return scaledBitmap;

    }

    public static Bitmap getImage( ContentResolver cr, Uri photoURI, String filePath) {
        Bitmap scaledBitmap = null;

        try {
            Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, photoURI);

            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

        }catch(Exception ex){}
//      check the rotation of the image and display it properly

        ExifInterface exif;

        try {

            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e){
            e.printStackTrace();
        }


        return scaledBitmap;

    }
   /* public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("Internet", "Error checking internet connection", e);
                Toast.makeText(context, "Please check your internet connection.",Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("Internet", "No network available!");
            Toast.makeText(context, "No network available.",Toast.LENGTH_LONG).show();
        }
        return false;
    }*/

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public static String SendTokenPostRequest() {

        URL url;
        String response = "";
        try {
            url = new URL(GET_TOKEN);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(60000);
            //conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("grant_type=password&username=admin&password=123456");

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();
            String line;

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine() )!= null) {
                    response += line;
                }
            }
            else {
                response="ERROR";
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = ERROR;
        }

        return response;
    }

    public static String SendGetRequest(String URL, String token, String tokenType) {

        URL url;
        String response = "";
        try {
            String stringURL = URL;
            url = new URL(stringURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(60000);
            conn.setConnectTimeout(30000);
            String autorize = tokenType + " " + token;
            //conn.setDoOutput(false);
            //conn.setDoInput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", autorize);

            int responseCode=conn.getResponseCode();
            String line;
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine() )!= null) {
                    response += line;
                }
            }
            else {
                response="ERROR";
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = ERROR;
        }

        return response;
    }

}
