package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PagoHistorial extends AppCompatActivity {

    Button pagarH;
    String PUBLISH_KEY = "pk_test_51MHW6WBm2QshfdlXsgtRUTMJdz4Uauoy3AeLvsjffiixYPxgMXq6EG6t5J4O4CVDgwi0HaVVFMJVd5pxrEaMI3g300QljgyAAV";
    String SECRET_KEY = "sk_test_51MHW6WBm2QshfdlXM3F2ODOYJMtzIgbgvPPB84EdqB65nNBL1X7IxYTUGPlkdSf3y6PK3njcNT75wmDXm880CqL8009LKxEMaY";
    PaymentSheet paymentSheet;

    String customerID;
    String ephericalKey;
    String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_historial);


        pagarH = findViewById(R.id.pagarHistorial);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        pagarH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            System.out.println(customerID);
                            getEphericalKey(customerID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PagoHistorial.this);
        requestQueue.add(stringRequest);


        /*
        correo = findViewById(R.id.correoLogin);
        pass = findViewById(R.id.passLogin);
        iniciar_sesion = findViewById(R.id.iniciarsesion);
        registrarse = findViewById(R.id.registrarse);

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */


        /*
        autoCompleteTextView = findViewById(R.id.auto_complete_esp);
        adapterString = new ArrayAdapter<String>(this, R.layout.list_items, items);

        autoCompleteTextView.setAdapter(adapterString);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    */
    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ephericalKey = object.getString("id");
                            System.out.println(ephericalKey);

                            getClientSecret(ephericalKey, "1650");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                headers.put("Stripe-version", "2022-11-15");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PagoHistorial.this);
        requestQueue.add(stringRequest);


    }

    private void getClientSecret(String ephericalKey, String amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            clientSecret = object.getString("client_secret");
                            System.out.println(clientSecret);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", amount);
                params.put("currency", "pen");
                params.put("automatic_payment_methods[enabled]", "false");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PagoHistorial.this);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                clientSecret, new PaymentSheet.Configuration("Creación de historial"
                        , new PaymentSheet.CustomerConfiguration(
                        customerID,
                        ephericalKey
                ))
        );
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult){
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            System.out.println("PAGO EXITOSO");
        } else if (paymentSheetResult instanceof  PaymentSheetResult.Canceled) {
            System.out.println("PAGO CANCELADO");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            System.out.println("PAGO FALLIDO");
        } else {
            System.out.println("PAGO ???");
        }
    }

}
