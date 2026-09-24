package com.example.fbinviteautoscroller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
class MainActivity:AppCompatActivity(){override fun onCreate(b:Bundle?){super.onCreate(b)
val l=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(32,32,32,32)}
l.addView(TextView(this).apply{text="Facebook Invite Auto-Scroller";textSize=24f})
l.addView(TextView(this).apply{text="Select a batch limit, enable Accessibility and overlay permission, then open Facebook.";setPadding(0,20,0,20)})
val s=Spinner(this);s.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrayOf("25","50","100"));s.setSelection(1)
s.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{override fun onItemSelected(p:AdapterView<*>?,v:android.view.View?,pos:Int,id:Long){AutomationState.batchLimit=arrayOf(25,50,100)[pos]};override fun onNothingSelected(p:AdapterView<*>?){}}
l.addView(s)
l.addView(Button(this).apply{text="Enable Accessibility";setOnClickListener{startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))}})
l.addView(Button(this).apply{text="Allow Floating Overlay";setOnClickListener{if(!Settings.canDrawOverlays(this@MainActivity))startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:$packageName")))}})
l.addView(Button(this).apply{text="START";setOnClickListener{AutomationState.reset();AutomationState.running=true;OverlayService.start(this@MainActivity)}})
l.addView(Button(this).apply{text="STOP";setOnClickListener{AutomationState.running=false}})
setContentView(l)}}