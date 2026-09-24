package com.example.fbinviteautoscroller
object AutomationState { @Volatile var running=false; @Volatile var invites=0; @Volatile var scrolls=0; @Volatile var batchLimit=50
 fun reset(){running=false;invites=0;scrolls=0}
 fun inviteReached():Boolean{invites++;return invites>=batchLimit} }