package inspire.example.com.siri;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import android.view.View.OnTouchListener;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class SampleService extends AccessibilityService{

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(getApplicationContext(), "Wakeup", Toast.LENGTH_SHORT).show();
    }

    public CharSequence recycle(AccessibilityNodeInfo info) {
        String text = "";
        if (info.getChildCount() == 0) {
            Toast.makeText(getApplicationContext(),info.getText() , Toast.LENGTH_SHORT).show();
            text += info.getText();
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
        return text;
    }

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                AccessibilityNodeInfo rowNode = getRootInActiveWindow();
                if (rowNode == null) {
                    Toast.makeText(getApplicationContext(),"noteInfo is　null" , Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "noteInfo is　null");
                    return;
                } else{
                    recycle(rowNode);
                    //Toast.makeText(getApplicationContext(),recycle(rowNode) , Toast.LENGTH_SHORT).show();
                }



                /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }*/

                //printAnswer("how many");

                //Intent intent = new Intent(getBaseContext(),MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                disableSelf();
        }
    }

    private void printAnswer(String question) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://allgood.cs.washington.edu:1995/";
        Log.e("In Request!:", "begin");
        JSONObject req = new JSONObject();
        try {
            req.put("paragragh", R.string.paragraph);
            req.put("question", question);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("In answer:", "!!!!!!!json");
        }
        Log.e(req.toString(), req.getClass().getName());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, req, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        txtAnswer.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("In Request!:", error.getClass().getName());
//                        txtAnswer.setText(error.toString());
//                        error.printStackTrace();
//                    }
//                });
//        queue.add(jsonObjectRequest);
        // Request a string response from the provided URL.
        String paragraph = getResources().getString(R.string.paragraph);
        url += "submit?paragraph="+paragraph+"&question="+question;
        Log.e("In Response:", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        JSONObject answer = null;

                        String[] res = response.split("\"");

                        Log.e("In Response:", response);
                        Toast.makeText(getApplicationContext(), "Response: "+ response.split("\"")[3], Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "That didn't work", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    @Override
    protected boolean onGesture (int gestureId){

        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
        return true;
    }
    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {

    }
}
