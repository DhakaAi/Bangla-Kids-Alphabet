package com.dhakaai.banglakidsalphabet

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.UtteranceProgressListener
import kotlin.math.PI
import kotlin.math.sin
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
 @Volatile private var musicRunning=false
 @Volatile private var musicPausedBySpeech=false
 private var musicTrack:AudioTrack?=null
 private var musicThread:Thread?=null
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
   setBackgroundColor(Color.rgb(247,250,255))
  }
  val scroll=ScrollView(this).apply{setFillViewport(true)}
  val page=LinearLayout(this).apply{orientation=1}
  val hero=LinearLayout(this).apply{
   orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL
   background=shape(Color.rgb(255,231,166),26f);setPadding(16,16,16,16)
  }
  val intro=LinearLayout(this).apply{
   orientation=1;layoutParams=LinearLayout.LayoutParams(0,-2,1f)
  }
  intro.addView(label("হ্যালো ছোট্ট বন্ধু! 👋",21f,Color.rgb(105,67,35),true))
  intro.addView(label("আজ শিখব বাংলা স্বরবর্ণ",18f,Color.rgb(105,67,35),true))
  intro.addView(label("ছবি দেখো, বর্ণ চেনো, মজা করে শেখো!",13f,Color.rgb(105,67,35)))
  hero.addView(intro)
  hero.addView(label("🐦",54f,ink),LinearLayout.LayoutParams(72,76))
  page.addView(hero,LinearLayout.LayoutParams(-1,-2).apply{bottomMargin=14})
  page.addView(label("চলো বর্ণের দেশে যাই! 🌈",25f,Color.rgb(75,93,165),true),
   LinearLayout.LayoutParams(-1,48))
  page.addView(label("একটি বর্ণে টোকা দিলেই বড় করে দেখতে ও শুনতে পাবে",13f,Color.GRAY),
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
    background=shape(Color.WHITE,24f)
    elevation=5f
    setPadding(8,12,8,12)
    setOnClickListener{showLearning(item)}
   }
   val badge=TextView(this).apply{
    text=item.ch;textSize=43f;setTextColor(Color.rgb(64,82,159))
    gravity=Gravity.CENTER;setTypeface(typeface,Typeface.BOLD)
    background=shape(colors[i%colors.size],60f)
   }
   card.addView(badge,LinearLayout.LayoutParams(76,76).apply{bottomMargin=7})
   card.addView(label(item.emoji,34f,ink),LinearLayout.LayoutParams(-1,42))
   card.addView(label(item.word,18f,Color.rgb(55,91,76),true),
    LinearLayout.LayoutParams(-1,29))
   card.addView(label("ট্যাপ করে শোনো",11f,Color.rgb(135,139,155)),
    LinearLayout.LayoutParams(-1,20))
   val params=GridLayout.LayoutParams().apply{
    width=0;height=190
    columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f)
    setMargins(6,6,6,6)
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
  engine.setOnUtteranceProgressListener(object:UtteranceProgressListener(){
   override fun onStart(utteranceId:String?){pauseMusicForSpeech()}
   override fun onDone(utteranceId:String?){if(utteranceId=="praise")resumeMusicAfterSpeech()}
   override fun onError(utteranceId:String?){resumeMusicAfterSpeech()}
  })
 }
 override fun onResume(){super.onResume();startStudyMusic()}
 override fun onPause(){stopStudyMusic();super.onPause()}
 private fun startStudyMusic(){
  if(musicRunning)return
  try{
   val rate=22050
   val min=AudioTrack.getMinBufferSize(rate,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT)
   val chunk=1024
   val track=AudioTrack.Builder()
    .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
     .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
    .setAudioFormat(AudioFormat.Builder().setSampleRate(rate)
     .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
     .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
    .setBufferSizeInBytes(maxOf(min,chunk*4))
    .setTransferMode(AudioTrack.MODE_STREAM).build()
   musicTrack=track
   musicRunning=true
   track.setVolume(0.10f)
   track.play()
   musicThread=Thread{
    val buffer=ShortArray(chunk)
    val melody=doubleArrayOf(523.25,659.25,783.99,659.25,587.33,698.46,783.99,698.46,
     523.25,587.33,659.25,523.25,440.00,523.25,587.33,523.25)
    val samplesPerNote=(rate*0.48).toInt()
    var sample=0L
    while(musicRunning){
     for(i in buffer.indices){
      val local=(sample%samplesPerNote).toInt()
      val noteIndex=((sample/samplesPerNote)%melody.size).toInt()
      val fade=minOf(1.0,local/(rate*0.045), (samplesPerNote-local)/(rate*0.07))
      val melodyWave=sin(2.0*PI*melody[noteIndex]*sample/rate)
      val padFreq=when((sample/(samplesPerNote*4))%4){
       0L->261.63;1L->293.66;2L->329.63;else->293.66
      }
      val pad=sin(2.0*PI*padFreq*sample/rate)
      val value=(melodyWave*0.12*fade+pad*0.035)*32767.0
      buffer[i]=value.toInt().coerceIn(-32768,32767).toShort()
      sample++
     }
     if(musicRunning)try{track.write(buffer,0,buffer.size)}catch(_:Exception){musicRunning=false}
    }
   }.apply{isDaemon=true;start()}
   if(musicPausedBySpeech)track.pause()
  }catch(_:Exception){musicRunning=false}
 }
 private fun pauseMusicForSpeech(){
  musicPausedBySpeech=true
  try{musicTrack?.pause()}catch(_:Exception){}
 }
 private fun resumeMusicAfterSpeech(){
  musicPausedBySpeech=false
  try{if(musicRunning)musicTrack?.play()}catch(_:Exception){}
 }
 private fun stopStudyMusic(){
  musicRunning=false
  musicThread?.interrupt()
  musicThread=null
  try{musicTrack?.pause();musicTrack?.flush();musicTrack?.release()}catch(_:Exception){}
  musicTrack=null
 }
 override fun onDestroy(){stopStudyMusic();tts?.stop();tts?.shutdown();super.onDestroy()}
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
