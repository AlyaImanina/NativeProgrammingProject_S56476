package com.example.mynote

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var topics = ArrayList<String>()

        if(!dbExists(this, "mynative")){
            createDB();

        }
        val db: SQLiteDatabase = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
        val sql = "SELECT topic from note"
        var c: Cursor = db.rawQuery(sql,null)
        while (c.moveToNext()){
            val topic = c.getString(0)
            topics.add(topic)
        }
        c.close()

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics)
        val lv = findViewById<ListView>(R.id.lv)
        lv.setAdapter(myAdapter)
        lv.onItemClickListener = AdapterView.OnItemClickListener{ adapter, v, position, arg3 ->
            val value = adapter.getItemAtPosition(position).toString()
            val intent = Intent(this, ViewNote::class.java).apply {
                putExtra("topic", value.toString())
            }
            startActivity(intent)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab1)
        fab.setOnClickListener(){
            val intent = Intent(this, AddNote::class.java).apply {
            }
            startActivity(intent)
        }
    }

    private fun dbExists(c: Context, dbName: String):Boolean{
        val dbFile: File = c.getDatabasePath(dbName)
        return dbFile.exists()
    }

    private fun createDB(){
        val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
        subToast("Database mynative created")
        val sqlText = "CREATE TABLE IF NOT EXISTS note" +
                "(topic VARCHAR(30) PRIMARY KEY, " +
                "detail VARCHAR(300) NOT NULL" +
                // "password VARCHAR(30) NOT NULL " +
                ");"
        subToast("Table mynative created")
        db.execSQL(sqlText)
        var nextSQL = "INSERT INTO note(topic, detail) VALUES ('Instagram', 'alyaaaa');"
        db.execSQL(nextSQL)
        nextSQL = "INSERT INTO note(topic, detail) VALUES ('Facebook', 'wawwsq');"
        db.execSQL(nextSQL)
        subToast("2 sample note added!")
    }
    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}