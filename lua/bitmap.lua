-- Load the wxLua module, does nothing if running from wxLua, wxLuaFreeze, or wxLuaEdit
package.cpath = package.cpath..";./?.dll;./?.so;../lib/?.so;../lib/vc_dll/?.dll;../lib/bcc_dll/?.dll;../lib/mingw_dll/?.dll;"
require("wx")

op = function(dc)
	dc:DrawLine(0,0,50,50)
end

img_filename = "a.png"
img_width,img_height = 100,200
img_depth = -1
bmp = wx.wxBitmap(img_width,img_height,img_depth)
-- print (Bmp)
dc = wx.wxMemoryDC()
dc:SelectObject(bmp)
dc:SetBackground(wx.wxWHITE_BRUSH)
dc:SetPen(wx.wxBLACK_PEN)
dc:SetTextBackground(wx.wxWHITE)
dc:SetTextForeground(wx.wxBLACK)
dc:Clear()
--
op(dc)
--
img = bmp:ConvertToImage()
result = img:SaveFile( img_filename , wx.wxBITMAP_TYPE_PNG )
dc:SelectObject(wx.wxNullBitmap) -- always release bitmap
dc:delete() -- ALWAYS delete() any wxDCs created when done

