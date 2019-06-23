Set oShell = CreateObject("WScript.Shell") 
	
  	if WScript.Arguments.Count = 0 then
    		WScript.Echo "Missing parameters"
    		Wscript.quit
   	Else
		'WScript.Echo "Running Dialog Handler Script"
    		PDFName = WScript.Arguments.Item(0) & ".pdf"
    	
    	end if
	do 
    bResult = oShell.AppActivate("Save As") 
	dFound = false
    If bResult = True Then
    	WScript.Echo PDFName
    	If InStr(PDFName,"|") > 0 Then
    		WScript.Echo "Inside InStr"
    		t = Replace(PDFName,"|","\")
    		oShell.SendKeys t
    		t = ""
    		WScript.Sleep 500 
			oShell.SendKeys "{Enter}" ' Alt+N, you may send {Esc} or {Enter} if you want
			dFound = True
		Else
			WScript.Echo "Outside InStr"
			oShell.SendKeys PDFName
			PDFName = ""
			WScript.Sleep 500 
			oShell.SendKeys "{Enter}" ' Alt+N, you may send {Esc} or {Enter} if you want
			dFound = True
		End If
       Exit do 
    End If 
    WScript.Sleep 500 
loop
if Not dFound then
WScript.Echo "Dialog not Found"
End if 
