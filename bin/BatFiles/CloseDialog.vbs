Set wshShell = CreateObject("WScript.Shell")
Do
PDFName = WScript.Arguments.Item(0) & ".pdf"
ret = wshShell.AppActivate("Save As")
dFound = false
	wshShell.SendKeys PDFName
	WScript.Sleep 500
	wshShell.SendKeys "{Enter}"
	WScript.Sleep 500
	ret = wshShell.AppActivate("Save As")
	if ret = true Then
		wshShell.SendKeys "{Enter}"
	Else
		EXIT Do
	End IF 
Loop