REM Remove files older than 30 days
forfiles /p "D:\TEMP\TMS\DELIVERY_ORDER" /s /m *.* /c "cmd /c Del @path" /d -30