package com.nburuchara.novalauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {


    lateinit var mHomeScreenImage : ImageView
    lateinit var mNumRow : EditText
    lateinit var mNumColumn : EditText

    val REQUEST_CODE_IMAGE = 1
    val PREFS_NAME  = "NovaPrefs"

    lateinit var imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mHomeScreenImage = findViewById<ImageView>(R.id.homeScreenImage)
        val mHomeScreenButton = findViewById<Button>(R.id.homeScreenButton)
        val mGridSizeButton = findViewById<Button>(R.id.gridSizeButton)
        mNumRow = findViewById(R.id.numRow)
        mNumColumn = findViewById(R.id.numColumn)


        mGridSizeButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                saveData()
            }
        })

        mHomeScreenButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val i  = Intent(Intent.ACTION_PICK)
                i.setType("image/*")
                startActivityForResult(i, REQUEST_CODE_IMAGE)
            }
        })
        getData()
    }

    fun getData () {
        val sharedPreferences : SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("imageUri", null)
        val numRow = sharedPreferences.getInt("numRow", 7)
        val numColumn = sharedPreferences.getInt("numColumn", 4)

        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString)
            mHomeScreenImage.setImageURI(imageUri)
        }

        val numRowString : String = numRow.toString()
        val numColumnString : String  = numColumn.toString()

        mNumRow.setText(numRowString)
        mNumColumn.setText(numColumnString)

    }

    fun saveData () {
        val sharedPreferences : SharedPreferences.Editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
        if (imageUri != null) {
            sharedPreferences.putString("imageUri", imageUri.toString())
        }
        sharedPreferences.putInt("numRow", (mNumRow.text.toString()).toInt())
        sharedPreferences.putInt("numColumn", (mNumColumn.text.toString()).toInt())
        sharedPreferences.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            mHomeScreenImage.setImageURI(imageUri)
            saveData()
        }
    }
}