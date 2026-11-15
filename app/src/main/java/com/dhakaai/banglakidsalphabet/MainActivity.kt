package com.dhakaai.banglakidsalphabet

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*
import java.util.Locale

data class Letter(val ch:String,val word:String,val emoji:String)

class MainActivity:Activity(),TextToSpeech.OnInitListener{
 private val vowels=listOf(
  Letter("অ","অজগর","🐍"),Letter("আ","আম","🥭"),
  Letter("ই","ইলিশ","🐟"),Letter("ঈ","ঈগল","🦅"),
  Letter("উ","উট","🐪"),Letter("ঊ","ঊষা","🌅"),
  Letter("ঋ","ঋতু","🌦️"),Letter("এ","একতারা","🎸"),
  Letter("ঐ","ঐক্য","🤝"),Letter("ও","ওল","🥔"),
  Letter("ঔ","ঔষধ","💊")
 )
 private var tts:TextToSpeech?=null
 private val ink=Color.rgb(58,67,111)
 private val colors=intArrayOf(
  Color.rgb(255,224,177),Color.rgb(211,237,255),
  Color.rgb(255,218,231),Color.rgb(220,242,207),
  Color.rgb(232,222,255),Color.rgb(255,239,184)
 )
 private fun shape(c:Int,r:Float=20f)=GradientDrawable().apply{setColor(c);cornerRadius=r}
 private fun label(s:String,size:Float,color:Int,bold:Boolean=false)=TextView(this).apply{
  text=s;textSize=size;setTextColor(color);gravity=Gravity.CENTER
  if(bold)setTypeface(typeface,Typeface.BOLD)
 }
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  tts=TextToSpeech(this,this)
  val root=LinearLayout(this).apply{
   orientation=1;setPadding(14,14,14,14)
   setBackgroundColor(Color.rgb(255,249,235))
  }
  val scroll=ScrollView(this).apply{setFillViewport(true)}
  val page=LinearLayout(this).apply{orientation=1}
  val hero=LinearLayout(this).apply{
   orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL
   background=shape(Color.rgb(255,222,143),24f);setPadding(14,12,14,12)
  }
  val intro=LinearLayout(this).apply{
   orientation=1;layoutParams=LinearLayout.LayoutParams(0,-2,1f)
  }
  intro.addView(label("হ্যালো ছোট্ট বন্ধু! 👋",20f,Color.rgb(105,67,35),true))
  intro.addView(label("চলো স্বরবর্ণ শিখি",18f,Color.rgb(105,67,35),true))
  intro.addView(label("খেলতে খেলতে বাংলা শিখি ❤️",13f,Color.rgb(105,67,35)))
  hero.addView(intro)
  hero.addView(label("🐦",54f,ink),LinearLayout.LayoutParams(72,76))
  page.addView(hero,LinearLayout.LayoutParams(-1,-2).apply{bottomMargin=14})
  page.addView(label("বাংলা স্বরবর্ণ",25f,Color.rgb(213,105,62),true),
   LinearLayout.LayoutParams(-1,48))
  page.addView(label("প্রতিটি বর্ণে টোকা দাও, ছবি দেখো ও শোনো",14f,Color.GRAY),
   LinearLayout.LayoutParams(-1,34))
  addVowelGrid(page)
  val footer=TextView(this).apply{
   text="🔊  বর্ণে চাপ দিলে বড় করে দেখতে ও শুনতে পাবে!"
   textSize=14f;gravity=Gravity.CENTER;setTextColor(ink)
   background=shape(Color.WHITE,18f);setPadding(12,14,12,14)
  }
  page.addView(footer,LinearLayout.LayoutParams(-1,-2).apply{
   topMargin=14;bottomMargin=10
  })
  scroll.addView(page)
  root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
  setContentView(root)
 }
 private fun addVowelGrid(parent:LinearLayout){
  val grid=GridLayout(this).apply{columnCount=2;useDefaultMargins=false}
  vowels.forEachIndexed{i,item->
   val card=LinearLayout(this).apply{
    orientation=1;gravity=Gravity.CENTER
    background=shape(colors[i%colors.size],20f)
    setPadding(6,10,6,10)
    setOnClickListener{showLearning(item)}
   }
   card.addView(label(item.ch,48f,Color.rgb(65,75,145),true),
    LinearLayout.LayoutParams(-1,58))
   card.addView(label(item.emoji,36f,ink),LinearLayout.LayoutParams(-1,44))
   card.addView(label(item.word,18f,Color.rgb(66,87,76),true),
    LinearLayout.LayoutParams(-1,30))
   val params=GridLayout.LayoutParams().apply{
    width=0;height=142
    columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f)
    setMargins(5,5,5,5)
   }
   grid.addView(card,params)
  }
  parent.addView(grid,LinearLayout.LayoutParams(-1,-2))
 }
 private fun showLearning(item:Letter){
  val panel=LinearLayout(this).apply{
   orientation=1;gravity=Gravity.CENTER
   background=shape(Color.WHITE,24f);setPadding(24,22,24,22)
  }
  panel.addView(label(item.ch,88f,Color.rgb(103,91,205),true))
  panel.addView(label(item.emoji,76f,ink))
  panel.addView(label("${item.ch} — ${item.word}",30f,Color.rgb(30,145,112),true))
  panel.addView(label("চলো বন্ধুরা, আবার বলি!",18f,ink))
  AlertDialogBuilder.show(this,panel){speak(item)}
 }
 private fun speak(item:Letter){
  val engine=tts ?: return
  engine.stop()
  engine.speak(item.ch,TextToSpeech.QUEUE_FLUSH,null,"letter")
  engine.playSilentUtterance(450,TextToSpeech.QUEUE_ADD,"pause")
  engine.speak("${item.word}! চলো বন্ধুরা, আবার বলি—${item.ch}, ${item.word}!",
   TextToSpeech.QUEUE_ADD,null,"word")
  engine.playSilentUtterance(300,TextToSpeech.QUEUE_ADD,"pause2")
  engine.speak("বাহ! খুব সুন্দর!",TextToSpeech.QUEUE_ADD,null,"praise")
 }
 override fun onInit(status:Int){
  if(status!=TextToSpeech.SUCCESS)return
  val engine=tts ?: return
  val bnVoices=engine.voices?.filter{it.locale.language=="bn"} ?: emptyList<Voice>()
  if(bnVoices.isNotEmpty()){
   val preferred=bnVoices.firstOrNull{it.locale.country.equals("BD",true)} ?: bnVoices.first()
   engine.voice=preferred
   engine.language=preferred.locale
  }else{
   val result=engine.setLanguage(Locale("bn","BD"))
   if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
    Toast.makeText(this,"এই ফোনে বাংলা TTS ভয়েস নেই। Bengali voice ইনস্টল করতে হবে।",Toast.LENGTH_LONG).show()
   }
  }
  engine.setSpeechRate(0.76f)
  engine.setPitch(1.22f)
 }
 override fun onDestroy(){tts?.stop();tts?.shutdown();super.onDestroy()}
}
private object AlertDialogBuilder{
 fun show(activity:Activity,view:android.view.View,onSpeak:()->Unit){
  val box=LinearLayout(activity).apply{
   orientation=1;addView(view)
   addView(TextView(activity).apply{
    text="🔊 শুনি";textSize=19f;gravity=Gravity.CENTER
    setTextColor(Color.WHITE)
    background=GradientDrawable().apply{
     setColor(Color.rgb(255,139,68));cornerRadius=30f
    }
    setPadding(12,14,12,14);setOnClickListener{onSpeak()}
   },LinearLayout.LayoutParams(-1,56).apply{topMargin=16})
  }
  android.app.AlertDialog.Builder(activity).setView(box)
   .setPositiveButton("বন্ধ করো",null).show()
 }
}
