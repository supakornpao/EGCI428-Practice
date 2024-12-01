package com.egci428.egci428_practice_2024_25t1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.egci428.egci428_practice_2024_25t1.model.User
import java.util.*


class SignUpActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0

    private val file = "users.txt"
    lateinit var latText: EditText
    lateinit var lonText: EditText
    lateinit var userSignText: EditText
    lateinit var passSignText: EditText
    private var getValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val randomBtn = findViewById<Button>(R.id.randomBtn)
        val addBtn = findViewById<Button>(R.id.addBtn)

        latText = findViewById(R.id.latText)
        lonText = findViewById(R.id.lonText)
        userSignText = findViewById(R.id.userSignText)
        passSignText = findViewById(R.id.passSignText)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        lastUpdate = System.currentTimeMillis()

        randomBtn.setOnClickListener {
            latText.setText(randomLocation(true).toString())
            lonText.setText(randomLocation(false).toString())
        }
        addBtn.setOnClickListener {
            saveData()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun randomLocation(type: Boolean): Double {
        val r = Random()
        var result: Double
        //true: latitude, false: longitude
        if (type) {
            result = (r.nextInt(170) - 85).toDouble()
        } else {
            result = (r.nextInt(360) - 180).toDouble()
        }
        return result
    }

    private fun saveData() {
        val usr = userSignText.text.toString()
        val pwd = passSignText.text.toString()
        val lat = latText.text.toString().toDouble()
        val lon = lonText.text.toString().toDouble()
        Log.d("Debug", usr)
        if (usr.isEmpty()) {
            userSignText.error = "Please enter a username"
            return
        }

        val userId = usr
        val userData = User(usr, pwd, lat, lon)
        val line = usr+";"+pwd+";"+lat+";"+lon+"\n"

        try {
            val FileOut = openFileOutput(file, Context.MODE_APPEND)

            FileOut.write(line.toByteArray())
            FileOut.close()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val textView = findViewById<TextView>(R.id.textView)
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x*x + y*y + z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()

        if(accel >= 2){

            if(actualTime-lastUpdate < 200 || getValue==true){
                return // Exit from function
            }
            lastUpdate = actualTime

            //val randomNum = kotlin.random.Random.nextInt(0,11)

            latText.setText(randomLocation(true).toString())
            lonText.setText(randomLocation(false).toString())

            getValue=true

            //Toast.makeText(this,"Device was shuffled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }


}