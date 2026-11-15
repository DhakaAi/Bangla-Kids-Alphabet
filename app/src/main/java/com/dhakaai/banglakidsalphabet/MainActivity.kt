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
 private val vowels=listOf(Letter("অ","অজগর","🐍"),Letter("আ","আম","🥭"),Letter("ই","ইলিশ","🐟"),Letter("ঈ","ঈগল","🦅"),Letter("উ","উট","🐪"),Letter("ঊ","ঊষা","🌅"),Letter("ঋ","ঋতু","🌦️"),Letter("এ","একতারা","🎸"),Letter("ঐ","ঐক্য","🤝"),Letter("ও","ওল","🥔"),Letter("ঔ","ঔষধ","💊"))
 private val consonants=listOf(Letter("ক","কলা","🍌"),Letter("খ","খরগোশ","🐰"),Letter("গ","গরু","🐮"),Letter("ঘ","ঘড়ি","⏰"),Letter("ঙ","ব্যাঙ","🐸"),Letter("চ","চাঁদ","🌙"),Letter("ছ","ছাতা","☂️"),Letter("জ","জাহাজ","🚢"),Letter("ঝ","ঝুড়ি","🧺"),Letter("ঞ","জ্ঞান","📚"),Letter("ট","টমেটো","🍅"),Letter("ঠ","ঠেলাগাড়ি","🛒"),Letter("ড","ডাব","🥥"),Letter("ঢ","ঢোল","🥁"),Letter("ণ","হরিণ","🦌"),Letter("ত","তরমুজ","🍉"),Letter("থ","থালা","🍽️"),Letter("দ","দোয়েল","🐦"),Letter("ধ","ধান","🌾"),Letter("ন","নৌকা","⛵"),Letter("প","প্রজাপতি","🦋"),Letter("ফ","ফুল","🌸"),Letter("ব","বল","⚽"),Letter("ভ","ভালুক","🐻"),Letter("ম","মাছ","🐠"),Letter("য","যমুনা","🏞️"),Letter("র","রংধনু","🌈"),Letter("ল","লাটিম","🌀"),Letter("শ","শাপলা","🪷"),Letter("ষ","ষাঁড়","🐂"),Letter("স","সূর্য","☀️"),Letter("হ","হাতি","🐘"),Letter("ড়","আষাঢ়","🌧️"),Letter("ঢ়","গাঢ়","🎨"),Letter("য়","ময়ূর","🦚"))
 private var tts:TextToSpeech?=null
 private val ink=Color.rgb(58,67,111)
 private val colors=intArrayOf(Color.rgb(255,224,177),Color.rgb(211,237,255),Color.rgb(255,218,231),Color.rgb(220,242,207),Color.rgb(232,222,255),Color.rgb(255,239,184))
 private fun shape(c:Int,r:Float=20f)=GradientDrawable().apply{setColor(c);cornerRadius=r}
 private fun label(s:String,size:Float,color:Int,bold:Boolean=false)=TextView(this).apply{text=s;textSize=size;setTextColor(color);gravity=Gravity.CENTER;if(bold)setTypeface(typeface,Typeface.BOLD)}
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState);tts=TextToSpeech(this,this)
  val root=LinearLayout(this).apply{orientation=1;setPadding(14,12,14,14);background=Color.rgb(255,249,235)}
  val scroll=ScrollView(this)
  val page=LinearLayout(this).apply{orientation=1}
  val hero=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;background=shape(Color.rgb(255,222,143),26f);setPadding(14,12,14,12)}
  val intro=LinearLayout(this).apply{orientation=1;layoutParams=LinearLayout.LayoutParams(0,-2,1f)}
  intro.addView(label("হ্যালো ছোট্ট বন্ধু! 👋",20f,Color.rgb(105,67,35),true))
  intro.addView(label("চলো বাংলা বর্ণমালা শিখি",15f,Color.rgb(105,67,35)))
  intro.addView(label("খেলতে খেলতে বাংলা শিখি ❤️",13f,Color.rgb(105,67,35)))
  hero.addView(intro);hero.addView(label("🐦",58f,ink),LinearLayout.LayoutParams(86,90))
  page.addView(hero,LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,0,0,12)})
  page.addView(label("স্বরবর্ণ",22f,Color.rgb(213,105,62),true),LinearLayout.LayoutParams(-1,42))
  page.addView(label("বর্ণে টোকা দাও, শুনে শুনে শেখো",13f,Color.GRAY),LinearLayout.LayoutParams(-1,25))
  addAlphabetGrid(page,vowels,3)
  page.addView(label("ব্যঞ্জনবর্ণ",22f,Color.rgb(66,135,177),true),LinearLayout.LayoutParams(-1,46))
  addAlphabetGrid(page,consonants,3)
  val footer=TextView(this).apply{text="🔊  যেকোনো বর্ণে চাপ দিলে তার নাম, শব্দ ও মজার উৎসাহ শুনতে পাবে!";textSize=14f;gravity=Gravity.CENTER;setTextColor(ink);background=shape(Color.WHITE,20f);setPadding(12,14,12,14)}
  page.addView(footer,LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,14,0,12)})
  scroll.addView(page);root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f));setContentView(root)
 }
 private fun addAlphabetGrid(parent:LinearLayout,items:List<Letter>,columns:Int){
  val grid=GridLayout(this).apply{columnCount=columns}
  items.forEachIndexed{i,item->
   val card=LinearLayout(this).apply{orientation=1;gravity=Gravity.CENTER;background=shape(colors[i%colors.size],18f);setPadding(4,7,4,7);setOnClickListener{showLearning(item)}}
   card.addView(label(item.ch,34f,Color.rgb(65,75,145),true),LinearLayout.LayoutParams(-1,42))
   card.addView(label(item.emoji,30f,ink),LinearLayout.LayoutParams(-1,40))
   card.addView(label(item.word,14f,Color.rgb(66,87,76),true),LinearLayout.LayoutParams(-1,25))
   grid.addView(card,GridLayout.LayoutParams().apply{width=0;height=112;columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);setMargins(4,4,4,4)})
  }
  parent.addView(grid,LinearLayout.LayoutParams(-1,-2))
 }
 private fun showLearning(item:Letter){
  val panel=LinearLayout(this).apply{orientation=1;gravity=Gravity.CENTER;background=shape(Color.WHITE,28f);setPadding(24,24,24,24)}
  panel.addView(label(item.ch,76f,Color.rgb(103,91,205),true))
  panel.addView(label(item.emoji,72f,ink))
  panel.addView(label("${item.ch} — ${item.word}",30f,Color.rgb(30,145,112),true))
  panel.addView(label("চলো বন্ধুরা, আবার বলি!",18f,ink))
  AlertDialogBuilder.show(this,panel){speak(item)}
 }
 private fun speak(item:Letter){
  tts?.speak(item.ch,TextToSpeech.QUEUE_FLUSH,null,"letter")
  tts?.playSilentUtterance(350,TextToSpeech.QUEUE_ADD,"pause")
  tts?.speak("${item.word}! চলো বন্ধুরা, আবার বলি—${item.ch}, ${item.word}!",TextToSpeech.QUEUE_ADD,null,"word")
  tts?.playSilentUtterance(250,TextToSpeech.QUEUE_ADD,"pause2")
  tts?.speak("বাহ! খুব সুন্দর!",TextToSpeech.QUEUE_ADD,null,"praise")
 }
 override fun onInit(status:Int){if(status==TextToSpeech.SUCCESS){val r=tts?.setLanguage(Locale("bn","BD"));tts?.setSpeechRate(0.82f);tts?.setPitch(1.18f);if(r==TextToSpeech.LANG_MISSING_DATA||r==TextToSpeech.LANG_NOT_SUPPORTED)Toast.makeText(this,"বাংলা ভয়েসের জন্য ফোনের Text-to-Speech সেটিংসে Bengali voice ইনস্টল করুন।",Toast.LENGTH_LONG).show()}}
 override fun onDestroy(){tts?.stop();tts?.shutdown();super.onDestroy()}
}
private object AlertDialogBuilder{
 fun show(activity:Activity,view:android.view.View,onSpeak:()->Unit){
  val box=LinearLayout(activity).apply{orientation=1;addView(view);addView(TextView(activity).apply{text="🔊 শুনি";textSize=18f;gravity=Gravity.CENTER;setTextColor(Color.WHITE);background=GradientDrawable().apply{setColor(Color.rgb(255,139,68));cornerRadius=30f};setPadding(12,14,12,14);setOnClickListener{onSpeak()}},LinearLayout.LayoutParams(-1,54).apply{topMargin=16})}
  android.app.AlertDialog.Builder(activity).setView(box).setPositiveButton("বন্ধ করো",null).show()
 }
}