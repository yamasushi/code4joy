-- Load the wxLua module, does nothing if running from wxLua, wxLuaFreeze, or wxLuaEdit
package.cpath = package.cpath..";./?.dll;./?.so;../lib/?.so;../lib/vc_dll/?.dll;../lib/bcc_dll/?.dll;../lib/mingw_dll/?.dll;"
require("wx")

function make_bitmap_png(img_filename,img_width,img_height,op)
	img_type  = wx.wxBITMAP_TYPE_PNG
	img_depth = -1
	--
	local bmp = wx.wxBitmap(img_width,img_height,img_depth)
	-- print (Bmp)
	local dc = wx.wxMemoryDC()
	dc:SelectObject(bmp)
	dc:SetBackground(wx.wxWHITE_BRUSH)
	dc:SetPen(wx.wxBLACK_PEN)
	dc:SetTextBackground(wx.wxWHITE)
	dc:SetTextForeground(wx.wxBLACK)
	dc:Clear()
	--
	op(dc)
	--
	local img = bmp:ConvertToImage()
	local result = img:SaveFile( img_filename , wx.wxBITMAP_TYPE_PNG )
	dc:SelectObject(wx.wxNullBitmap) -- always release bitmap
	dc:delete() -- ALWAYS delete() any wxDCs created when done
end

