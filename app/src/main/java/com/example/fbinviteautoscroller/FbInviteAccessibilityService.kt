package com.example.fbinviteautoscroller
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlin.random.Random
class FbInviteAccessibilityService:AccessibilityService(){
private val h=Handler(mainLooper);private var busy=false
override fun onAccessibilityEvent(e:AccessibilityEvent?){if(!AutomationState.running||e==null)return;val p=e.packageName?.toString()?:"";if(p!="com.facebook.katana"&&p!="com.facebook.lite")return;h.removeCallbacksAndMessages(null);h.postDelayed({scan()},350)}
override fun onInterrupt(){busy=false}
private fun scan(){if(!AutomationState.running||busy)return;if(AutomationState.invites>=AutomationState.batchLimit){AutomationState.running=false;return};val root=rootInActiveWindow?:return;val target=findInvite(root);if(target!=null){busy=true;if(target.performAction(AccessibilityNodeInfo.ACTION_CLICK)){if(AutomationState.inviteReached())AutomationState.running=false;h.postDelayed({busy=false;if(AutomationState.running)scan()},Random.nextLong(1500,3001))}else{busy=false;h.postDelayed({scan()},500)}}else{busy=true;val ok=scroll(root);if(ok){AutomationState.scrolls++;h.postDelayed({busy=false;if(AutomationState.running)scan()},1600)}else{busy=false;AutomationState.running=false}}}
private fun findInvite(r:AccessibilityNodeInfo):AccessibilityNodeInfo?{val q=ArrayDeque<AccessibilityNodeInfo>();q.add(r);while(q.isNotEmpty()){val n=q.removeFirst();if(n.isVisibleToUser&&n.isEnabled){val t=n.text?.toString()?.trim()?:"";val d=n.contentDescription?.toString()?.trim()?:"";if((t.equals("Invite",true)||d.equals("Invite",true))&&!t.contains("Invited",true)&&!d.contains("Invited",true)){if(n.isClickable)return n;var p=n.parent;repeat(6){if(p!=null){if(p.isClickable&&p.isEnabled&&p.isVisibleToUser)return p;p=p.parent}}}};for(i in 0 until n.childCount)n.getChild(i)?.let{q.add(it)}};return null}
private fun scroll(r:AccessibilityNodeInfo):Boolean{if(r.isScrollable&&r.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD))return true;val q=ArrayDeque<AccessibilityNodeInfo>();q.add(r);while(q.isNotEmpty()){val n=q.removeFirst();if(n.isScrollable&&n.isVisibleToUser&&n.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD))return true;for(i in 0 until n.childCount)n.getChild(i)?.let{q.add(it)}};if(android.os.Build.VERSION.SDK_INT>=24){val dm=resources.displayMetrics;val path=Path().apply{moveTo(dm.widthPixels*.5f,dm.heightPixels*.78f);lineTo(dm.widthPixels*.5f,dm.heightPixels*.28f)};return dispatchGesture(GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path,0,500)).build(),null,null)};return false}
}