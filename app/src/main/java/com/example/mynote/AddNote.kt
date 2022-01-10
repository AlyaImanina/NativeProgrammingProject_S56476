package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.mynote.MainActivity
import com.example.mynote.R

class AddNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener(){
            val topic = findViewById<EditText>(R.id.inTopic)
            val detail = findViewById<EditText>(R.id.inDetail)

            val emptyLevel = emptiness(topic, detail)
            if(emptyLevel>0){
                //which field is empty
                when(emptyLevel){
                    5 -> subToast("Please fill Topic")
                    6 -> subToast("Please fill Detail")
                    11 -> subToast("Topic and Detail must no empty!")
                }
            }else{
                //check for exist topic
                val status = checkKey(topic.text.toString())
                val ent = topic.text.toString()
                val uname = detail.text.toString()
                if(!status){
                    val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
                    val sql = "INSERT INTO note (topic,detail) VALUES ('$ent','$uname');"
                    db.execSQL(sql)
                    subToast("New Note $ent added!")
                    val intent = Intent(this, HomeActivity::class.java).apply {
                    }
                    startActivity(intent)

                }else{
                    subToast("Note already exists inside Database!")
                }
            }
        }
    }

    private fun checkKey(topic: String):Boolean {
        val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
        val sql = "SELECT * FROM note where topic='$topic'"
        val cursor = db.rawQuery(sql, null)
        var out =false
        if(cursor.count>0)
            out=true
        return out
    }

    private fun emptiness(topic:EditText,detail:EditText):Int{
        var empty = 0

        if(topic.text.isEmpty())
            empty +=5

        if(detail.text.isEmpty())
            empty +=6

        return empty

    }
    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}