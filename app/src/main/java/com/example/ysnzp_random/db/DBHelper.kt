package com.example.ysnzp_random.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    // Table
    private val peopleTable = "people"

    // Column
    private val personIndex = "personIndex"
    private val personName = "personName"

    override fun onCreate(db: SQLiteDatabase) {
        val createPeopleTable = "CREATE TABLE $peopleTable ($personIndex INTEGER PRIMARY KEY, $personName TEXT)"
        db.execSQL(createPeopleTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $peopleTable")
        onCreate(db)
    }

    fun addPerson(name: String){
        val values = ContentValues()
        values.putNull(personIndex)
        values.put(personName, name)
        val db = this.writableDatabase
        db.insert(peopleTable, null, values)
        db.close()
//        val check =  db.insert(peopleTable, null, values)
//        Log.e("Asdasd", check.toString())
    }

    fun findAllPerson(): ArrayList<String>{
        val query = "SELECT $personName FROM $peopleTable"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val personModel = ArrayList<String>()

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast) {
                val name = cursor.getString(0)
                personModel.add(name)
                cursor.moveToNext()
            }
            cursor.close()
        }
        db.close()
        return personModel
    }

    fun deletePerson(index: Int){
        val db = this.writableDatabase
        db.delete(peopleTable, personIndex, arrayOf(index.toString()))
    }
}


data class PersonDBModel(
    val index: Int,
    val name: String
)