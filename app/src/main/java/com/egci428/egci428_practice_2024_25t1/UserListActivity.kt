package com.egci428.egci428_practice_2024_25t1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.egci428.egci428_practice_2024_25t1.model.User
import com.egci428.egci428_practice_2024_25t1.model.UserAdapter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class UserListActivity : AppCompatActivity() {


    private val file = "users.txt"
    lateinit var userList: MutableList<User>
    lateinit var user: User
    lateinit var useradapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val listView = findViewById<ListView>(R.id.listView)

        userList = mutableListOf()
        useradapter = UserAdapter(this,R.layout.userlist, userList)
        listView.adapter = useradapter
        read()

        listView.setOnItemClickListener { parent, view, position, id ->

            val auser = userList!!.get(position)
            displayMap(auser)
        }
    }

        private fun displayMap(ausr: User) {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("selUser", ausr.username)
            intent.putExtra("selLat", ausr.latitude)
            intent.putExtra("selLon", ausr.longitude)
            startActivity(intent)
        }

    fun read(){
        try {

            var FileIn = openFileInput(file)
            val mFile = InputStreamReader(FileIn)

            val br = BufferedReader(mFile)
            while(true) {
                var line = br.readLine()
                if(line==null) break;

                val userline = line.split(";")
                user = User(userline[0],userline[1],userline[2].toDouble(),userline[3].toDouble())
                userList.add(user)

            }
            br.close()
            mFile.close()
            FileIn.close()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

}