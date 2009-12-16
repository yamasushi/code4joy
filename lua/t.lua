require "Lattice"
require "gd_bitmap"

local l  = Lattice:new(3 ,2.0)
print(l)
local sp = l:pos ( {0,0} )
local ep = l:pos ( {0,0} )

make_bitmap_png("a.png",1000,1000,
	function (im)
		local black = im:colorAllocate(  0,   0,   0)
		local white = im:colorAllocate(255, 255, 255)
		Lattice:scan(4,{250,150},
			function(ip)
				print( ip )
				sp = ep
				ep = l:pos(ip)
				im:line(sp[1],sp[2],ep[1],ep[2],black)
			end )
	end )
