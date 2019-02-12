package com.example.ysnzp_random

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import kotlinx.android.synthetic.main.activity_add_popup.*

class AddPopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단 타이틀바 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_popup)



        // 추가 버튼 엑션 처리
        add_button.setOnClickListener {
            intent.putExtra("result", name_edit_text.text.toString())
            setResult(Activity.RESULT_OK, intent)

            finish()
        }

        // 닫기 버튼 엑션 처리
        close_button.setOnClickListener {
            finish()
        }
    }


    // 바깥 레이어 클릭시 안닫히게
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }


    // 안드로이드 백버튼 막기
    override fun onBackPressed() {
        return
    }

}
