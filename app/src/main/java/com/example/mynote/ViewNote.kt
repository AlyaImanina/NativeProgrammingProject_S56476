package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        val intent = this.getIntent()
        val topic = intent.getStringExtra("topic")

        supportActionBar?.setTitle(topic)

        //read database
        val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
        val sql = "SELECT topic, detail from note where topic='$topic'"
        val cursor = db.rawQuery(sql, null)

        //var topic = ""
        var detail = ""

        while (cursor.moveToNext()){
           // topic = cursor.getString(0)
            detail = cursor.getString(1)
        }
        cursor.close()

        var udet = findViewById<EditText>(R.id.inDetail)

        udet.setText(detail)

        val btnDel = findViewById<Button>(R.id.delBtn)
        var delDialog : AlertDialog? = null
        val delBuilder = AlertDialog.Builder(this)
        delBuilder.setTitle("Delete Process")
        delBuilder.setMessage("Are you sure to delete?")

        delBuilder.setNeutralButton("Cancel"){dialogInterface, which ->
            subToast("Delete Cancelled")
        }

        delBuilder.setPositiveButton("Yes"){dialogInterface, which ->
            val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
            val sql = "DELETE FROM note where topic = '$topic';"
            db.execSQL(sql)

            subToast("Note $topic deleted!")
            val intent = Intent(this, HomeActivity::class.java).apply {
            }
            startActivity(intent)
        }

        delDialog = delBuilder.create()
        btnDel.setOnClickListener(){
            delDialog.show()
        }

        //=======================================================
        val btnEdit = findViewById<Button>(R.id.editBtn)
        var editDialog : AlertDialog? = null
        val editBuilder = AlertDialog.Builder(this)
        editBuilder.setTitle("Update Process")
        editBuilder.setMessage("Are you sure to update the data?")

        editBuilder.setNeutralButton("Edit"){dialogInterface, which ->
            subToast("Update Cancelled")
        }

        editBuilder.setPositiveButton("Yes"){dialogInterface, which ->

            var udet = findViewById<EditText>(R.id.inDetail)

            val pa = udet.text.toString()

            val db = openOrCreateDatabase("mynative", MODE_PRIVATE, null)
            val sql = "UPDATE note SET detail = '$pa'WHERE topic = '$topic';"
            db.execSQL(sql)

            subToast("Note $topic updated!")
            val intent = Intent(this, HomeActivity::class.java).apply {
            }
            startActivity(intent)
        }

        editDialog = editBuilder.create()
        btnEdit.setOnClickListener(){
            editDialog.show()
        }
    }
    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}