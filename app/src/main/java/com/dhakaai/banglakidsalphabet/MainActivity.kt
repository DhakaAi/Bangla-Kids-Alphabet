package com.dhakaai.banglakidsalphabet

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*
import java.util.Locale

data class Letter(val ch:String, val word:String, val emoji:String)

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private val letters = listOf(
        Letter("অ","অজগর","🐍"), Letter("আ","আম","🥭"), Letter("ই","ইলিশ","🐟"), Letter("ঈ","ঈগল","🦅"),
        Letter("উ","উট","🐪"), Letter("ঊ","ঊষা","🌅"), Letter("ঋ","ঋতু","🌦️"), Letter("এ","একতারা","🎸"),
        Letter("ঐ","ঐক্য","🤝"), Letter("ও","ওল","🥔"), Letter("ঔ","ঔষধ","💊"), Letter("ক","কলা","🍌"),
        Letter("খ","খরগোশ","🐰"), Letter("গ","গরু","🐮"), Letter("ঘ","ঘড়ি","⏰"), Letter("ঙ","ব্যাঙ","🐸"),
        Letter("চ","চাঁদ","🌙"), Letter("ছ","ছাতা","☂️"), Letter("জ","জাহাজ","🚢"), Letter("ঝ","ঝুড়ি","🧺"),
        Letter("ঞ","জ্ঞান","📚"), Letter("ট","টমেটো","🍅"), Letter("ঠ","ঠেলাগাড়ি","🛒"), Letter("ড","ডাব","🥥"),
        Letter("ঢ","ঢোল","🥁"), Letter("ণ","হরিণ","🦌"), Letter("ত","তরমুজ","🍉"), Letter("থ","থালা","🍽️"),
        Letter("দ","দোয়েল","🐦"), Letter("ধ","ধান","🌾"), Letter("ন","নৌকা","⛵"), Letter("প","প্রজাপতি","🦋"),
        Letter("ফ","ফুল","🌸"), Letter("ব","বল","⚽"), Letter("ভ","ভালুক","🐻"), Letter("ম","মাছ","🐠"),
        Letter("য","যমুনা","🏞️"), Letter("র","রংধনু","🌈"), Letter("ল","লাটিম","🌀"), Letter("শ","শাপলা","🪷"),
        Letter("ষ","ষাঁড়","🐂"), Letter("স","সূর্য","☀️"), Letter("হ","হাতি","🐘"), Letter("ড়","আষাঢ়","🌧️"),
        Letter("ঢ়","গাঢ়","🎨"), Letter("য়","ময়ূর","🦚")
    )
    private var index=0
    private var tts:TextToSpeech?=null
    private lateinit var letterView:TextView
    private lateinit var wordView:TextView
    private lateinit var emojiView:TextView
    private lateinit var countView:TextView

    private fun shape(color:Int, radius:Float=22f)=GradientDrawable().apply { setColor(color); cornerRadius=radius }
    private fun label(s:String, size:Float, color:Int, bold:Boolean=false)=TextView(this).apply {
        text=s; textSize=size; setTextColor(color); gravity=Gravity.CENTER
        if(bold) setTypeface(typeface,Typeface.BOLD)
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        tts=TextToSpeech(this,this)
        val root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(16,18,16,12); setBackgroundColor(Color.rgb(247,249,255)) }
        root.addView(label("🌈 ছোট্ট বর্ণমালা",27f,Color.rgb(65,78,165),true),LinearLayout.LayoutParams(-1,55))
        root.addView(label("খেলতে খেলতে বাংলা শিখি ❤️",16f,Color.DKGRAY),LinearLayout.LayoutParams(-1,32))
        countView=label("",14f,Color.GRAY,true); root.addView(countView,LinearLayout.LayoutParams(-1,30))
        val card=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; gravity=Gravity.CENTER; background=shape(Color.WHITE,28f); elevation=5f; setPadding(8,8,8,8) }
        letterView=label("",82f,Color.rgb(93,94,210),true); card.addView(letterView,LinearLayout.LayoutParams(-1,112))
        emojiView=label("",66f,Color.BLACK); card.addView(emojiView,LinearLayout.LayoutParams(-1,92))
        wordView=label("",29f,Color.rgb(38,145,111),true); card.addView(wordView,LinearLayout.LayoutParams(-1,55))
        root.addView(card,LinearLayout.LayoutParams(-1,270).apply { setMargins(0,10,0,12) })
        val speak=Button(this).apply { text="🔊  শুনি ও শিখি"; textSize=18f; setOnClickListener { speakCurrent() } }
        root.addView(speak,LinearLayout.LayoutParams(-1,54))
        val nav=LinearLayout(this).apply { gravity=Gravity.CENTER }
        nav.addView(Button(this).apply { text="← আগের"; setOnClickListener { index=(index-1+letters.size)%letters.size; showLetter() } },LinearLayout.LayoutParams(0,52,1f))
        nav.addView(Button(this).apply { text="পরের →"; setOnClickListener { index=(index+1)%letters.size; showLetter() } },LinearLayout.LayoutParams(0,52,1f))
        root.addView(nav)
        root.addView(label("অক্ষর বেছে নাও",16f,Color.DKGRAY,true),LinearLayout.LayoutParams(-1,38))
        val grid=GridLayout(this).apply { columnCount=6 }
        letters.forEachIndexed { i, item ->
            val tile=TextView(this).apply {
                text=item.ch; textSize=22f; gravity=Gravity.CENTER; setTextColor(Color.rgb(65,76,145)); background=shape(Color.rgb(231,237,255),14f)
                setOnClickListener { index=i; showLetter(); speakCurrent() }
            }
            grid.addView(tile,GridLayout.LayoutParams().apply { width=0; height=47; columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f); setMargins(3,3,3,3) })
        }
        val scroll=ScrollView(this); scroll.addView(grid); root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root); showLetter()
    }

    private fun showLetter() {
        val item=letters[index]
        letterView.text=item.ch; emojiView.text=item.emoji; wordView.text="${item.ch} — ${item.word}"
        countView.text="অক্ষর ${index+1} / ${letters.size}"
    }
    private fun speakCurrent() {
        val item=letters[index]
        tts?.speak(item.ch,TextToSpeech.QUEUE_FLUSH,null,"letter")
        tts?.playSilentUtterance(250,TextToSpeech.QUEUE_ADD,"pause")
        tts?.speak(item.word,TextToSpeech.QUEUE_ADD,null,"word")
    }
    override fun onInit(status:Int) {
        if(status==TextToSpeech.SUCCESS) {
            val result=tts?.setLanguage(Locale("bn","BD"))
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
                Toast.makeText(this,"বাংলা ভয়েসের জন্য ফোনের Text-to-Speech সেটিংসে Bengali voice ইনস্টল করুন।",Toast.LENGTH_LONG).show()
        }
    }
    override fun onDestroy() { tts?.stop(); tts?.shutdown(); super.onDestroy() }
}
