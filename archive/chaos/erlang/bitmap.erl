-module(bitmap).

-export([test/0,create_picture_file/6]).

-include_lib("wx/include/wx.hrl").

create_picture_file(
			Img_width    ,
			Img_height   ,
			Img_depth    ,
			Imgtype_str  ,
			Img_filename , 
			OP )->
	{ok,Img_type}=imgtype_str2int(Imgtype_str),
	%%
	Wx = wx:new(),
	%%
	Bmp  = wx:batch(fun() -> wxBitmap:new(Img_width,Img_height,[{depth,Img_depth}]) end),
	DC   = wx:batch(fun() -> wxMemoryDC:new() end),
	ok   = wx:batch(fun() -> wxMemoryDC:selectObject(DC,  Bmp)           end ) ,
	ok   = wx:batch(fun() -> wxDC:setBackground     (DC, ?wxWHITE_BRUSH) end ) ,
	ok   = wx:batch(fun() -> wxDC:setPen            (DC, ?wxBLACK_PEN)   end ) ,
	ok   = wx:batch(fun() -> wxDC:setTextBackground (DC, ?wxWHITE)       end ) ,
	ok   = wx:batch(fun() -> wxDC:setTextForeground (DC, ?wxBLACK)       end ) ,
	ok   = wx:batch(fun() -> wxDC:clear             (DC)                 end ) ,
	%%
	ok   = wx:batch(fun() -> OP(DC) end), 
	%%
	Img  = wx:batch(fun() -> wxBitmap:convertToImage(Bmp) end),
	true = wx:batch(fun() -> wxImage:saveFile( Img , Img_filename , Img_type ) end), 
	%%
	wx:batch(fun()-> wxMemoryDC:destroy(DC) end),
	wx:destroy(),
	ok.

imgtype_str2int(Imgtype_str) -> case Imgtype_str of
		"bmp"  -> {ok   ,?wxBITMAP_TYPE_BMP }; 
		"jpg"  -> {ok   ,?wxBITMAP_TYPE_JPEG};
		"jpeg" -> {ok   ,?wxBITMAP_TYPE_JPEG};
		"png"  -> {ok   ,?wxBITMAP_TYPE_PNG }; 
		"pcx"  -> {ok   ,?wxBITMAP_TYPE_PCX }; 
		"pnm"  -> {ok   ,?wxBITMAP_TYPE_PNM };
		%%"tif"  -> {ok   ,?wxBITMAP_TYPE_TIFF};
		%%"tiff" -> {ok   ,?wxBITMAP_TYPE_TIFF};
		"xpm"  -> {ok   ,?wxBITMAP_TYPE_XPM }; 
		"ico"  -> {ok   ,?wxBITMAP_TYPE_ICO }; 
		"cur"  -> {ok   ,?wxBITMAP_TYPE_CUR };
		Other  -> {error,unknown_image_type }
	end.

test()->
	create_picture_file(
			500     ,
			600     ,
			1       ,
			"png"   ,
			"a.png" ,
			fun(DC)->
				wxDC:drawLine(DC,{0,0},{100,100})
			end ),
		ok.

