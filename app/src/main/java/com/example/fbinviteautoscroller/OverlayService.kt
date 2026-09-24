package com.example.fbinviteautoscroller
import android.app.Service
import android.content.*
import android.graphics.*
import android.os.*
import android.provider.Settings
import android.view.*
import android.widget.*
class OverlayService:Service(){
private var wm:WindowManager?=null;private var box:LinearLayout?=null;private var stat:TextView?=null
companion object{fun start(c:Context){c.startService(Intent(c,OverlayService::class.java))}}
override fun onCreate(){super.onCreate();if(Settings.canDrawOverlays(this))show()}
override fun onBind(i:Intent?):IBinder?=null
private fun show(){wm=getSystemService(WINDOW_SERVICE) as WindowManager;box=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(10,5,10,5);setBackgroundColor(Color.DKGRAY)}
stat=TextView(this).apply{setTextColor(Color.WHITE);text="Invites: 0/50 | Scrolls: 0"}
val row=LinearLayout(this)
row.addView(Button(this).apply{text="START";setOnClickListener{AutomationState.running=true}})
row.addView(Button(this).apply{text="STOP";setOnClickListener{AutomationState.running=false}})
box!!.addView(stat);box!!.addView(row)
val p=WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,if(Build.VERSION.SDK_INT>=26)WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT).apply{gravity=Gravity.TOP or Gravity.END;x=10;y=100}
wm!!.addView(box,p)
Handler(mainLooper).post(object:Runnable{override fun run(){if(box!=null){stat?.text="Invites: "+AutomationState.invites+"/"+AutomationState.batchLimit+" | Scrolls: "+AutomationState.scrolls;Handler(mainLooper).postDelayed(this,500)}}})}
override fun onDestroy(){box?.let{wm?.removeView(it)};box=null;super.onDestroy()}}