package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    Button backProgramarCita;
    String PUBLISH_KEY = "pk_test_51MHW6WBm2QshfdlXsgtRUTMJdz4Uauoy3AeLvsjffiixYPxgMXq6EG6t5J4O4CVDgwi0HaVVFMJVd5pxrEaMI3g300QljgyAAV";
    String SECRET_KEY = "sk_test_51MHW6WBm2QshfdlXM3F2ODOYJMtzIgbgvPPB84EdqB65nNBL1X7IxYTUGPlkdSf3y6PK3njcNT75wmDXm880CqL8009LKxEMaY";
    PaymentSheet paymentSheet;

    String customerID;
    String ephericalKey;
    String clientSecret;

    String idMedico;
    String token;
    String espF;
    String medF;
    String horF;
    String costoT;

    String correo;

    TextView textEsp;
    TextView textMed;
    TextView textHor;

    Boolean estadoPermitirPago = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cita);

        idMedico = getIntent().getExtras().getString("idMedico");
        token = getIntent().getExtras().getString("Token");
        espF = getIntent().getExtras().getString("espF");
        medF = getIntent().getExtras().getString("medF");
        horF = getIntent().getExtras().getString("horF");
        costoT = getIntent().getExtras().getString("costo");

        pagarH = findViewById(R.id.confirmar_pago);
        backProgramarCita = findViewById(R.id.back_programar);
        textEsp = findViewById(R.id.esp_text);
        textMed = findViewById(R.id.med_text);
        textHor = findViewById(R.id.hor_text);

        textEsp.setText(espF);
        textMed.setText(medF);
        textHor.setText(horF);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        pagarH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estadoPermitirPago) {
                    PaymentFlow();
                } else {
                    Toast.makeText(PagoHistorial.this, "Espere mientras se generan sus credenciales de pago", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backProgramarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWindow = new Intent(getApplicationContext(), ProgramarCitaActivity.class);
                newWindow.putExtra("Token", token);
                startActivity(newWindow);
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
                            getClientSecret(ephericalKey, String.valueOf((int)((Double.valueOf(costoT) * 0.29) + (1.17) + Double.valueOf(costoT) * 100)));

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
                            estadoPermitirPago=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
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
                clientSecret, new PaymentSheet.Configuration("Creación de cita"
                        , new PaymentSheet.CustomerConfiguration(
                        customerID,
                        ephericalKey
                ))
        );
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult){
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            JSONObject body = new JSONObject();

            try {
                body.put("idmedico", idMedico);
                body.put("estado", "N");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "https://apipostahuaral.azurewebsites.net/createCita",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String message = object.getString("Message");
                                Toast.makeText(PagoHistorial.this, "Cita reservada exitosamente", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PagoHistorial.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }) {

                @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Auth", token);
                    return headers;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(PagoHistorial.this);
            requestQueue.add(stringRequest);

            Intent newWindow = new Intent(getApplicationContext(), MenuActivity.class);
            newWindow.putExtra("Token", token);
            Toast.makeText(this, "Pago exitoso", Toast.LENGTH_SHORT).show();
            startActivity(newWindow);
        } else if (paymentSheetResult instanceof  PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Pago cancelado", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Pago fallido", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("PAGO ???");
        }
    }

    /*
    private void enviarCorreo() {
        JSONObject body = new JSONObject();
        try {
            body.put("email", correo);
            body.put("esp", espF);
            body.put("medico", medF);
            body.put("dia", horF);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "https://apipostahuaral.azurewebsites.net/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String token = object.getString("Token");
                            Object Usuario = object.getJSONObject("Usuario");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PagoHistorial.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PagoHistorial.this);
        requestQueue.add(stringRequest);
    }

     */

}
