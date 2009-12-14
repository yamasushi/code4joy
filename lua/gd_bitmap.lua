require "gd"

function make_bitmap_png(img_filename,img_width,img_height,op)
	local im = gd.createTrueColor(img_width,img_height)
	local white = im:colorAllocate(255,255,255)
	local black = im:colorAllocate(0, 0, 0)
	im:filledRectangle(0,0,img_width,img_height,white)
	--
	op(im)
	--
	im:png(img_filename)
end

