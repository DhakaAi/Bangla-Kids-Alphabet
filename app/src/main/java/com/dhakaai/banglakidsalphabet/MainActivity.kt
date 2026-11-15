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

data class Letter(val ch:String,val word:String,val emoji:String)

class MainActivity:Activity(),TextToSpeech.OnInitListener{
 private val letters=listOf(
 Letter("অ","অজগর","🐍"),Letter("আ","আম","🥭"),Letter("ই","ইলিশ","🐟"),Letter("ঈ","ঈগল","🦅"),Letter("উ","উট","🐪"),Letter("ঊ","ঊষা","🌅"),Letter("ঋ","ঋতু","🌦️"),Letter("এ","একতারা","🎸"),Letter("ঐ","ঐক্য","🤝"),Letter("ও","ওল","🥔"),Letter("ঔ","ঔষধ","💊"),
 Letter("ক","কলা","🍌"),Letter("খ","খরগোশ","🐰"),Letter("গ","গরু","🐮"),Letter("ঘ","ঘড়ি","⏰"),Letter("ঙ","ব্যাঙ","🐸"),Letter("চ","চাঁদ","🌙"),Letter("ছ","ছাতা","☂️"),Letter("জ","জাহাজ","🚢"),Letter("ঝ","ঝুড়ি","🧺"),Letter("ঞ","জ্ঞান","📚"),Letter("ট","টমেটো","🍅"),Letter("ঠ","ঠেলাগাড়ি","🛒"),Letter("ড","ডাব","🥥"),Letter("ঢ","ঢোল","🥁"),Letter("ণ","হরিণ","🦌"),Letter("ত","তরমুজ","🍉"),Letter("থ","থালা","🍽️"),Letter("দ","দোয়েল","🐦"),Letter("ধ","ধান","🌾"),Letter("ন","নৌকা","⛵"),Letter("প","প্রজাপতি","🦋"),Letter("ফ","ফুল","🌸"),Letter("ব","বল","⚽"),Letter("ভ","ভালুক","🐻"),Letter("ম","মাছ","🐠"),Letter("য","যমুনা","🏞️"),Letter("র","রংধনু","🌈"),Letter("ল","লাটিম","🌀"),Letter("শ","শাপলা","🪷"),Letter("ষ","ষাঁড়","🐂"),Letter("স","সূর্য","☀️"),Letter("হ","হাতি","🐘"),Letter("ড়","আষাঢ়","🌧️"),Letter("ঢ়","গাঢ়","🎨"),Letter("য়","ময়ূর","🦚"))
 private var index=0
 private var tts:TextToSpeech?=null
 private lateinit var letterView:TextView
 private lateinit var wordView:TextView
 private lateinit var emojiView:TextView
 private lateinit var countView:TextView
 private fun shape(c:Int,r:Float=22f)=GradientDrawable().apply{setColor(c);cornerRadius=r}
 private fun txt(s:String,z:Float,c:Int,b:Boolean=false)=TextView(this).apply{text=s;textSize=z;setTextColor(c);gravity=Gravity.CENTER;if(b)setTypeface(typeface,Typeface.BOLD)}
 private fun button(s:String,c:Int,action:()->Unit)=TextView(this).apply{text=s;textSize=18f;gravity=Gravity.CENTER;setTextColor(Color.WHITE);typeface=Typeface.DEFAULT_BOLD;background=shape(c,25f);setOnClickListener{action()}}
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState);tts=TextToSpeech(this,this)
  val root=LinearLayout(this).apply{orientation=1;setPadding(16,12,16,12);background=GradientDrawable(GradientDrawable.Orientation.TL_BR,intArrayOf(Color.rgb(255,246,224),Color.rgb(232,244,255)))}
  val header=LinearLayout(this).apply{orientation=1;gravity=Gravity.CENTER;background=shape(Color.WHITE,26f);setPadding(8,5,8,5)}
  header.addView(txt("🌈 ছোট্ট বর্ণমালা",27f,Color.rgb(75,83,170),true),LinearLayout.LayoutParams(-1,46))
  header.addView(txt("খেলতে খেলতে বাংলা শিখি ❤️",15f,Color.rgb(90,105,130)),LinearLayout.LayoutParams(-1,26))
  root.addView(header,LinearLayout.LayoutParams(-1,80))
  countView=txt("",14f,Color.GRAY,true);root.addView(countView,LinearLayout.LayoutParams(-1,30))
  val card=LinearLayout(this).apply{orientation=1;gravity=Gravity.CENTER;background=GradientDrawable(GradientDrawable.Orientation.TL_BR,intArrayOf(Color.WHITE,Color.rgb(239,248,255))).apply{cornerRadius=34f;setStroke(2,Color.rgb(218,229,255))};elevation=7f}
  letterView=txt("",78f,Color.rgb(103,91,205),true);card.addView(letterView,LinearLayout.LayoutParams(-1,98))
  emojiView=txt("",64f,Color.BLACK);card.addView(emojiView,LinearLayout.LayoutParams(-1,82))
  wordView=txt("",27f,Color.rgb(30,145,112),true);card.addView(wordView,LinearLayout.LayoutParams(-1,50))
  root.addView(card,LinearLayout.LayoutParams(-1,240).apply{setMargins(0,0,0,12)})
  root.addView(button("🔊  শুনি ও শিখি",Color.rgb(255,139,68)){speak()},LinearLayout.LayoutParams(-1,54).apply{setMargins(0,0,0,8)})
  val nav=LinearLayout(this).apply{gravity=Gravity.CENTER}
  nav.addView(button("‹  আগের",Color.rgb(91,145,220)){index=(index-1+letters.size)%letters.size;show()},LinearLayout.LayoutParams(0,48,1f).apply{setMargins(0,0,5,0)})
  nav.addView(button("পরের  ›",Color.rgb(91,145,220)){index=(index+1)%letters.size;show()},LinearLayout.LayoutParams(0,48,1f).apply{setMargins(5,0,0,0)})
  root.addView(nav)
  root.addView(txt("✨ তোমার পছন্দের বর্ণে টোকা দাও",15f,Color.rgb(77,91,130),true),LinearLayout.LayoutParams(-1,38))
  val grid=GridLayout(this).apply{columnCount=5}
  letters.forEachIndexed{i,item->val tile=TextView(this).apply{text=item.ch;textSize=22f;gravity=Gravity.CENTER;setTextColor(Color.rgb(74,82,155));background=shape(if(i==index)Color.rgb(255,224,154) else Color.WHITE,16f);setOnClickListener{index=i;show();speak()}}
   grid.addView(tile,GridLayout.LayoutParams().apply{width=0;height=48;columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);setMargins(3,3,3,3)})}
  val scroll=ScrollView(this);scroll.addView(grid);root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f));setContentView(root);show()
 }
 private fun show(){val a=letters[index];letterView.text=a.ch;emojiView.text=a.emoji;wordView.text="${a.ch} — ${a.word}";countView.text="অক্ষর ${index+1} / ${letters.size}"}
 private fun speak(){val a=letters[index];tts?.speak(a.ch,TextToSpeech.QUEUE_FLUSH,null,"letter");tts?.playSilentUtterance(350,TextToSpeech.QUEUE_ADD,"pause");tts?.speak(a.word,TextToSpeech.QUEUE_ADD,null,"word");tts?.playSilentUtterance(250,TextToSpeech.QUEUE_ADD,"pause2");tts?.speak("বাহ! খুব সুন্দর!",TextToSpeech.QUEUE_ADD,null,"praise")}
 override fun onInit(status:Int){if(status==TextToSpeech.SUCCESS){val r=tts?.setLanguage(Locale("bn","BD"));tts?.setSpeechRate(0.82f);tts?.setPitch(1.18f);if(r==TextToSpeech.LANG_MISSING_DATA||r==TextToSpeech.LANG_NOT_SUPPORTED)Toast.makeText(this,"বাংলা ভয়েসের জন্য ফোনের Text-to-Speech সেটিংসে Bengali voice ইনস্টল করুন।",Toast.LENGTH_LONG).show()}}
 override fun onDestroy(){tts?.stop();tts?.shutdown();super.onDestroy()}
}