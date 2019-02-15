package com.example.ysnzp_random

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ysnzp_random.adapter.CleanAdapter
import com.example.ysnzp_random.db.DBHelper
import com.example.ysnzp_random.default_data.Defaults
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_people.view.*

class MainActivity : AppCompatActivity() {

    // 상수
    private val popupActivityCode = 1

    // Model
    private val cleanPeopleList = ArrayList<String>()
    private val cleanModel = Defaults.cleanType


    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: CleanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // name 지정하지 않으면 db 클로즈하면 지워진다.
        dbHelper = DBHelper(this, "people", null, 1)


        total_people_text.text = cleanPeopleList.size.toString()
        clean_number.text = cleanModel.sumBy { it.number }.toString()

        // 인원 Chip 세팅
        peopleChipSetting()

        result_button.setOnClickListener {
            dialogResult()
        }


        // 청소구역 리사이클뷰
        adapter = CleanAdapter(cleanModel, this)

        recycler_people.layoutManager = LinearLayoutManager(this)
        recycler_people.adapter = adapter

        recycler_people.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action){
                    MotionEvent.ACTION_UP -> {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        if (child != null) {
                            val tv = rv.getChildViewHolder(child).itemView.recycler_clean_text
                            cleanModel[tv.tag as Int].number += 1
                            adapter.notifyItemChanged(tv.tag as Int)

                            clean_number.text = cleanModel.sumBy { it.number }.toString()
                        }
                    }
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        cleanModel.map{
            it.number = 0
        }
        clean_number.text = 0.toString()
        adapter.notifyDataSetChanged()
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
                peopleChipSetting()
            }

        }
    }


    @SuppressLint("InflateParams")
    private fun peopleChipSetting(){

        people_chip_group.removeAllViews()
        val personModel = dbHelper.findAllPerson()
        if (personModel.isEmpty()){
            Toast.makeText(this, "DB 없음", Toast.LENGTH_LONG).show()
        }else{
            personModel.mapIndexed { _, s ->
                val chip =  LayoutInflater.from(this).inflate(R.layout.item_people_chip, null, false) as Chip
                chip.tag = s.index
                chip.text = s.name
                chip.isCheckable = true
                chip.setOnCloseIconClickListener {
                    dialogDelete(it)
                }
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        cleanPeopleList.add(buttonView.text.toString())
                    }else{
                        cleanPeopleList.remove(buttonView.text.toString())
                    }
                    total_people_text.text = cleanPeopleList.size.toString()
                }
                people_chip_group.addView(chip)
            }
        }
    }


    // 삭제시 Dialog 띄우기
    private fun dialogDelete(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("삭제")
            .setMessage("인원삭제")
            .setPositiveButton("확인") { dialog, which ->
                dbHelper.deletePerson(view.tag as Int)
                people_chip_group.removeView(view)
                Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("취소"){ dialog, which ->
                Toast.makeText(this, "삭제취소", Toast.LENGTH_LONG).show()
            }
            .show()
    }


    // Dialog 결과
    private fun dialogResult(){
        val peopleNumber = cleanPeopleList.size
        val cleanNumber = cleanModel.sumBy { it.number}
        if ((peopleNumber-cleanNumber) < 0){
            Toast.makeText(this, "청소 인원이 부족합니다.", Toast.LENGTH_LONG).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("시작")
            .setMessage("청소인원 $peopleNumber, 청소구역 $cleanNumber 쉬는사람 ${peopleNumber-cleanNumber}")
            .setPositiveButton("확인") { dialog, which ->
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("취소"){ dialog, which ->
                Toast.makeText(this, "삭제취소", Toast.LENGTH_LONG).show()
            }
            .show()
    }

}
