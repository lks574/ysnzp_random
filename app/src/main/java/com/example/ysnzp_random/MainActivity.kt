package com.example.ysnzp_random

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ysnzp_random.adapter.CleanAdapter
import com.example.ysnzp_random.db.DBHelper
import com.example.ysnzp_random.default_data.Defaults
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 상수
    private val popupActivityCode = 1


    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // name 지정하지 않으면 db 클로즈하면 지워진다.
        dbHelper = DBHelper(this, "people", null, 1)

        result_button.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }


        chipSetting()


        // 청소구역 리사이클뷰
        val clean = Defaults.cleanType
        recycler_people.layoutManager = LinearLayoutManager(this)
        recycler_people.adapter = CleanAdapter(clean, this)
    }

    // Custom Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_add_button, menu)
        return true
    }

    // Custom Action Bar item Action
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId){
            R.id.action_add_button -> {
                val intent = Intent(this, AddPopupActivity::class.java)
                startActivityForResult(intent, popupActivityCode)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == popupActivityCode){
            if (data != null){
                val result = data.getStringExtra("result")
                dbHelper.addPerson(result)
                chipSetting()
            }

        }
    }


    @SuppressLint("InflateParams")
    private fun chipSetting(){
        people_chip_group.removeAllViews()
        val personModel = dbHelper.findAllPerson()
        if (personModel.isEmpty()){
            Toast.makeText(this, "DB 없음", Toast.LENGTH_LONG).show()
        }else{
            personModel.mapIndexed { index, s ->
                val chip =  LayoutInflater.from(this).inflate(R.layout.item_people_chip, null, false) as Chip
                chip.tag = index
                chip.text = s
                chip.setOnCloseIconClickListener {
                    dbHelper.deletePerson(1)
                    people_chip_group.removeView(it)
                }
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    Toast.makeText(this@MainActivity, buttonView.tag.toString(), Toast.LENGTH_LONG).show()
                }
                people_chip_group.addView(chip)
            }
        }

    }

}
