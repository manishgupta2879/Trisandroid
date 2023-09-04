package com.pcs.tim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataService {

    public static String IP = "1";
    public static String IP_NoPort = "";
    public static String AndroidVersion="";
    public static String AndroidPackageName="";

    public static final String VERIMYRC_URL = "verimyrc_url";
    public static final String VERIMYRC_API_URL = "verimyrc_api_url";
    public static final String VERIMYRC_ANDROID_VERSION = "verimyrc_android_version";
    public static final String VERIMYRC_IOS_VERSION = "verimyrc_ios_version";
    public static final String VERIMYRC_ANDROID_PACKAGE_NAME = "verimyrc_android_package_name";
    public static final String VERIMYRC_IOS_PACKAGE_NAME = "verimyrc_ios_package_name";

    public static  String GET_TOKEN = "/api/token/requesttoken";
    public static final String VIEW_DETAILS =  "/api/register/viewdetails";
    public static final String VIEW_VACCINE =  "/api/vaccinecheckstatus";
    public static final String VIEW_SEARCH_REFUGEE = "/api/register/search";
    public static final String FCM_TOKEN = "/api/Enforcement/SaveToken";
    public static final String POST_TRACK_LOG =  "/api/tracklog/addtracklog";
    public static final String ENFORCEMENT_LOGIN =  "/api/enforcement/login";
    public static final String ENFORCEMENT_CHANGE_PASSWORD =  "/api/enforcement/changepassword";
    public static final String ENFORCEMENT_NEW_REGISTER = "/api/enforcement/newregister";
    public static final String ENFORCEMENT_UPDATE =  "/api/enforcement/update";
    public static final String UPLOAD_PHOTO =  "/api/uploadphoto";
    public static final String GET_AGENCY = "/api/enforcementStation/agency";
    public static final String GET_DEPARTMENT =  "/api/enforcementDepartment/";
    public static final String GET_STATE_BY_AGENCY =  "/api/enforcementStation/";
    public static final String GET_AGENCY_BY_AGENCY_STATE =  "/api/enforcementStation/";
    public static final String GET_API_KEY = "/api/apikey/APP";
    public static final String GET_APP_VERSION =  "/api/appversion";
    public static final String GET_MYRC_TRACK_LOG =  "/api/tracklog/myrc/";
    public static final String GET_ENFORCEMENT_ID_TRACK_LOG =  "/api/tracklog/enforcement/";
    public static final String GET_PLKS =  "/api/PLKS/details/";
    public static  String accessToken ="";
    public static  String refreshToken ="";

    static DataService _instance;

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public static DataService instance(Context context) {
        if (_instance == null) {
            _instance = new DataService();
            _instance.configSessionUtils(context);
        }
        return _instance;
    }

    public static DataService instance() {
        return _instance;
    }

    public void configSessionUtils(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(Utilities.MY_RC_SHARE_PREF, Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
    }

    public void storeValueString(String key, String value) {
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public String fetchValueString(String key) {
        return sharedPref.getString(key, null);
    }


    public static Boolean RequestToken() throws IOException {
        Log.d("check_api_url1",VERIMYRC_API_URL);
        
       // DataService.instance().storeValueString(DataService.VERIMYRC_API_URL, "www.dhillonfarm.com");
       DataService.instance().storeValueString(DataService.VERIMYRC_API_URL, "211.24.73.117:9999");
        Log.d("check_api_url2",DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL));
        Log.d("check_api_url3",GET_TOKEN);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        RequestTokenModel rtm = new RequestTokenModel(100,"e0ef5ac197489cd777179443ed1a1b17");
        String jsonString = gson.toJson(rtm);
        RequestBody body = RequestBody.create(mediaType,jsonString );

        Log.d("resultss__1", "RequestToken: +http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL) +GET_TOKEN);

        Request request = new Request.Builder()
                .url( ("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_TOKEN)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
//            return response.body().string();
            String responseResult = response.body().string();
            Log.d("check_api_url",responseResult);
            try{
                Log.d("check_api_url",responseResult);
                if(!responseResult.isEmpty()){

                    JSONObject jsonObject = new JSONObject(responseResult);

                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        Gson gsonToken = new Gson();
                        RequestTokenResponse resp = gsonToken.fromJson(jsonObject.getString("data"), RequestTokenResponse.class);
                        accessToken = resp.getAccessToken();
                        refreshToken = resp.getRefreshToken();
                        return true;
                    }
                    else{
                        return false;
                    }
                }else{
                    return false;
                }

            }catch(Exception ex){
                Log.d("check_api_url",ex.getMessage());
                return false;
            }

        }else{
            Log.d("check_api_url","1");
            return false;
        }
    }

    public static String PostTrackLog(Long RegId, String TrackType, Long EnforcementId, String Location, Float Lat, Float Lng, String Remark ) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                TrackLogModel tlm = new TrackLogModel(RegId,TrackType,EnforcementId,Location,Lat,Lng,Remark);
                String jsonString = gson.toJson(tlm);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL))+POST_TRACK_LOG)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    return response.body().string();
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }


    public static String sendFcmToken(int policeId,String fcmToken) throws IOException {


        boolean requestToken = RequestToken();
        if (fcmToken != null && !fcmToken.isEmpty()) {
            if (requestToken) {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");
                Log.d("fcmToken___0", "sendFcmToken: "+fcmToken);

                String url = DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL) + "/api/Enforcement/SaveToken?Id=" + policeId + "&type=" + "employee" + "&fcmToken=" + fcmToken;

                Request request = new Request.Builder()
                        .url("http://"+url)
                        .method("POST", RequestBody.create(mediaType, ""))
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("fcmToken___1", "ViewRefugeeDetails: "+response.body().string());
                    return "Success";
                    //return response.body().string();
                } else {
                    Log.d("fcmToken___2", "ViewRefugeeDetails: "+response.body().string());
                    return "ERROR";
                }

            } else {
                Log.d("fcmToken___3", "ViewRefugeeDetails: ");
                return "ERROR";
            }
        } else {
            Log.d("fcmToken___4", "ViewRefugeeDetails: ");

            return "ERROR";
        }
    }



    public static String ViewRefugeeDetails(String searchOption, String data) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                ViewDetailsModel vdm = new ViewDetailsModel(searchOption,data);
                String jsonString = gson.toJson(vdm);

                RequestBody body = RequestBody.create(mediaType,jsonString );

                Log.d("body__", "ViewRefugeeDetails: body "+jsonString);
                Log.d("body__url", "ViewRefugeeDetails: body "+("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +VIEW_DETAILS);
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +VIEW_DETAILS)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){

                    return response.body().string();
                }
                else{

                    return "ERROR";
                }
            }else{

                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String ViewVaccineData(String myrc, String phone) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                ViewVaccineModel vvm = new ViewVaccineModel(myrc,phone);
                String jsonString = gson.toJson(vvm);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +VIEW_VACCINE)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String SearchRefugee(String MyrcOrName,String UnhcrId, String CountryOfOrigin, String State, String Category) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                ViewSearchRefugeeModel vsrm = new ViewSearchRefugeeModel(MyrcOrName,UnhcrId,CountryOfOrigin,State,Category);
                String jsonString = gson.toJson(vsrm);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +VIEW_SEARCH_REFUGEE)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetApiKey() throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_API_KEY)
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();
                    Log.d("GetApiKey",result);
                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetAppVersion() throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                try{
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("application/json");

                    Gson gson = new Gson();
                    AppVersionModel avm = new AppVersionModel("VeriMyRC","Android");
                    String jsonString = gson.toJson(avm);

                    RequestBody body = RequestBody.create(mediaType,jsonString );

                    Request request = new Request.Builder()
                            .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_APP_VERSION)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer "+accessToken)
                            .build();

                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String result =response.body().string();
                        Log.d("GetAppVersion",result);
                        return result;
                    }
                    else{
                        return "ERROR";
                    }
                }catch(Exception ex){
                    Log.d("GetAppVersion",ex.getMessage());
                    return  "ERROR";
                }

            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetAgency() throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_AGENCY)
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetDepartmentByAgency(String value) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_DEPARTMENT+value+"/department")
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetStateByAgency(String agency) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_STATE_BY_AGENCY+agency+"/state")
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();


                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetStationByAgencyState(String agency,String state) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_AGENCY_BY_AGENCY_STATE+agency+"/"+state+"/station")
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetPLKS(Integer regID) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_PLKS+regID)
                        .method("GET", null)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetMyRCTrackLog(String myrc) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){


            Log.d("accesss___", "GetMyRCTrackLog: accessToken "+accessToken+" myRc "+  myrc);
            Log.d("accesss___", "http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)+GET_MYRC_TRACK_LOG+myrc);

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_MYRC_TRACK_LOG+myrc)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String GetEnforcementTrackLog(String policeID) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){
            Log.d("GetEnforcementTrackLog","1");
            if(resultToken){
                Log.d("GetEnforcementTrackLog","2");
                Log.d("GetEnforcementTrackLog","http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)+GET_ENFORCEMENT_ID_TRACK_LOG+policeID);
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_ENFORCEMENT_ID_TRACK_LOG+policeID)
                        //.url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +GET_ENFORCEMENT_ID_TRACK_LOG+policeID)
                        // .url("http://n3nwvpweb005.shr.prod.ams3.secureserver.net:8443" +GET_ENFORCEMENT_ID_TRACK_LOG+policeID)
                      //  .url("http://www.dhillonfarm.com/api/tracklog/enforcement/48")
                        .method("POST", body)
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();
                    Log.d("GetEnforcementTrackLog",result);
                    return result;
                }
                else{
                    //Log.d("GetEnforcementTrackLog_result ",response.code()+"");
                    Log.d("GetEnforcementTrackLog","access  "+accessToken);
                    return "ERROR";
                }
            }else{
                Log.d("GetEnforcementTrackLog","4");
                return  "ERROR";
            }
        }else{
            Log.d("GetEnforcementTrackLog","5");
            return "ERROR";
        }
    }

    public static String EnforcementLogin(String  policeid, String password) throws IOException {
        Log.d("debugLogin___","11111");
        boolean resultToken = RequestToken();
        Log.d("debugLogin___123",resultToken+"");
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            Log.d("debugLogin","1");
            if(resultToken){
                Log.d("debugLogin","2");
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                EnforcementLoginModel elm = new EnforcementLoginModel(policeid,password);
                String jsonString = gson.toJson(elm);
                Log.d("debugLogin____","abc     "+("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +ENFORCEMENT_LOGIN);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Log.d("debugLogin____body","abc     "+jsonString);
                Log.d("debugLogin____access","abc     "+accessToken);
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +ENFORCEMENT_LOGIN)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    String result =response.body().string();
                    Log.d("resultss__",result);
                    return result;
                }
                else{
                    Log.d("debugLogin","3");
                    return "ERROR";
                }
            }else{
                Log.d("debugLogin","4");
                return  "ERROR";
            }
        }else{
            Log.d("debugLogin","0");
            return "ERROR";
        }
    }

    public static String EnforcementChangePassword(Long policeid, String password, String newpassword) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                ChangePasswordModel cpm = new ChangePasswordModel(policeid,password,newpassword);
                String jsonString = gson.toJson(cpm);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +ENFORCEMENT_CHANGE_PASSWORD)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String EnforcementUpdate(Long id,String policeid,String icnumber,String fullname,String mobileno,String policerank,String agency,String state,
                                           String station,String department,String password,String photo) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                EnforcementUpdateModel eum = new EnforcementUpdateModel(id, policeid, icnumber, fullname, mobileno, policerank, agency, state,
                         station, department, password, photo);
                String jsonString = gson.toJson(eum);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +ENFORCEMENT_UPDATE)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String NewRegister(String policeid,String icnumber,String fullname, String mobileno, String policerank, String agency,
                                     String state,String station,String department,String password,String photo) throws IOException {
        boolean resultToken = RequestToken();
        if(!accessToken.isEmpty() || !accessToken.equals("")){

            if(resultToken){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Gson gson = new Gson();
                NewRegisterModel nrm = new NewRegisterModel(policeid,icnumber,fullname,mobileno,policerank,agency,state,station,department,password,photo);
                String jsonString = gson.toJson(nrm);

                RequestBody body = RequestBody.create(mediaType,jsonString );
                Request request = new Request.Builder()
                        .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +ENFORCEMENT_NEW_REGISTER)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+accessToken)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String result =response.body().string();

                    return result;
                }
                else{
                    return "ERROR";
                }
            }else{
                return  "ERROR";
            }
        }else{
            return "ERROR";
        }
    }

    public static String UploadFileWithBase64String(String type,String imageFilePath){
        try{

            Log.d("imagePathEdit__1", "UploadFileWithBase64String: "+imageFilePath);
            File imgFile = new File(imageFilePath);

            Log.d("imagePathEdit__2", "UploadFileWithBase64String: length "+imgFile.length()+ "imgFile exist "+ imgFile.exists());

            if (imgFile.exists() && imgFile.length() > 0) {
                Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
                String base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);

                Log.d("imagePathEdit__",base64Image);
                if(!accessToken.isEmpty() || !accessToken.equals("")){
                    boolean resultToken = RequestToken();

                    if(resultToken){
                        Log.d("bxxx","1");
                        OkHttpClient client = new OkHttpClient()
                                .newBuilder()
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");

                        Gson gson = new Gson();
                        UploadPhoto up = new UploadPhoto(type,base64Image);
                        String jsonString = gson.toJson(up);

                        RequestBody body = RequestBody.create(mediaType,jsonString );
                        Request request = new Request.Builder()
                                .url(("http://"+DataService.instance().fetchValueString(DataService.VERIMYRC_API_URL)) +UPLOAD_PHOTO)
                                .method("POST", body)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer "+accessToken)
                                .build();
                        Response response = client.newCall(request).execute();
                        Log.d("response__", "UploadFileWithBase64String: "+response.code());
                        if(response.isSuccessful()){
                            String result =response.body().string();

                            Log.d("cxxx",result);
                            return result;
                        }
                        else{
                            return "ERROR";
                        }
                    }else{
                        return  "ERROR";
                    }
                }else{
                    return "ERROR";
                }
            }else{
                Log.d("bxxx","no img file");
                return "ERROR";
            }
        }catch(Exception ex){
            Log.d("bxxx",ex.getMessage());
            return "ERROR";
        }
    }
}
