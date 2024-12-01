package com.egci428.egci428_practice_2024_25t1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.egci428.egci428_practice_2024_25t1.model.User
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {


    var uname: String? = null
    var pname: String? = null
    var userprofile: User? = null
    private val file = "users.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signInBtn = findViewById<Button>(R.id.signInBtn)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        val userText = findViewById<EditText>(R.id.userText)
        val passText = findViewById<EditText>(R.id.passText)


        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        signInBtn.setOnClickListener {
            uname = userText.text.toString()
            pname = passText.text.toString()
            if (!uname.isNullOrEmpty() && !pname.isNullOrEmpty()) {
                if(read()){
                    Toast.makeText(this,"Sign In Successfully",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, UserListActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Username Or password not matched",Toast.LENGTH_LONG).show()
                }
            }
        }
        cancelBtn.setOnClickListener {
            userText.text = null
            passText.text = null

        }
    }

    fun read(): Boolean{
        try {

            var FileIn = openFileInput(file)
            val mFile = InputStreamReader(FileIn)

            val br = BufferedReader(mFile)
            while(true) {
                var line = br.readLine()
                if(line==null) break
                var user = line.split(';')
                Log.d("Read Username", user[0])
                Log.d("Read Pass", user[1])

                if(uname.toString().equals(user[0]) && pname.equals(user[1])){
                    return true
                }

            }
            br.close()
            mFile.close()
            FileIn.close()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }



}