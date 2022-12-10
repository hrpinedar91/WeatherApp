package com.example.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat = 5.0689
        var long = -75.5174

        window.statusBarColor = Color.parseColor("#1383C3")
        getJsonData(lat, long)

        // get reference to button
        val bogota = findViewById(R.id.bogota) as Button
        val medellin = findViewById(R.id.medellin) as Button
        val pereira = findViewById(R.id.pereira) as Button
        bogota.setOnClickListener {
            clickButton(name = "bogota")
        }
        medellin.setOnClickListener {
            clickButton(name = "medellin")
        }
        pereira.setOnClickListener {
            clickButton(name = "pereira")
        }


    }

    private fun clickButton(name: String) {
        Toast.makeText(this@MainActivity, "Cambiando a ${name}", Toast.LENGTH_SHORT).show()
        getJsonDataName(name = name)
    }


    private fun getJsonData(lat: Double, long: Double) {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${Companion.API_KEY}&lang=sp"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })


        queue.add(jsonRequest)
    }

    private fun getJsonDataName(name: String) {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=${name}&appid=${Companion.API_KEY}&lang=sp"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })


        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject) {
        city.text = response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        coordinates.text = "${lat} , ${long}"
        weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr = response.getJSONObject("main").getString("temp")
        tempr = ((((tempr).toFloat() - 273.15)).toInt()).toString()
        temp.text = "${tempr}°C"


        var mintemp = response.getJSONObject("main").getString("temp_min")
        mintemp = ((((mintemp).toFloat() - 273.15)).toInt()).toString()
        min_temp.text = mintemp + "°C"
        var maxtemp = response.getJSONObject("main").getString("temp_max")
        maxtemp = ((ceil((maxtemp).toFloat() - 273.15)).toInt()).toString()
        max_temp.text = maxtemp + "°C"

        pressure.text = response.getJSONObject("main").getString("pressure")
        humidity.text = response.getJSONObject("main").getString("humidity") + "%"
        wind.text = response.getJSONObject("wind").getString("speed")
        degree.text = "Degree : " + response.getJSONObject("wind").getString("deg") + "°"
//        gust.text="Gust : "+response.getJSONObject("wind").getString("gust")
    }

    companion object {
        private const val API_KEY = "8ca87bd29f714555bf7a398b45c1d06f"
    }
}

