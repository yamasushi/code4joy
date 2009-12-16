require "Lattice"
require "gd_bitmap"

--~ local l  = Lattice:new(3 ,2.0)
--~ print(l)
--~ local sp = l:pos ( {0,0} )
--~ local ep = l:pos ( {0,0} )

--~ make_bitmap_png("a.png",1000,1000,
--~ 	function (im)
--~ 		local black = im:colorAllocate(  0,   0,   0)
--~ 		local white = im:colorAllocate(255, 255, 255)
--~ 		Lattice:scan(4,{250,150},
--~ 			function(ip)
--~ 				print( ip )
--~ 				sp = ep
--~ 				ep = l:pos(ip)
--~ 				im:line(sp[1],sp[2],ep[1],ep[2],black)
--~ 			end )
--~ 	end )

local l  = Lattice:new(3 ,40.0)
local inner = {
	{{ 0, 0},{ 2, 0},{ 3, 1},{ 1, 1}} ,
	{{ 0, 0},{ 1, 1},{ 0, 2},{-1, 1}} ,
	{{ 0, 0},{-1, 1},{-3, 1},{-2, 0}} ,
	{{ 0, 0},{-2, 0},{-3,-1},{-1,-1}} ,
	{{ 0, 0},{-1,-1},{ 0,-2},{ 1,-1}} ,
	{{ 0, 0},{ 1,-1},{ 3,-1},{ 2, 0}} }

local outer = {
	{{ 4, 0},{ 3, 1},{ 2, 0},{ 3,-1}} ,
	{{ 2, 2},{ 0, 2},{ 1, 1},{ 3, 1}} ,
	{{-2, 2},{-3, 1},{-1, 1},{ 0, 2}} ,
	{{-4, 0},{-3,-1},{-2, 0},{-3, 1}} ,
	{{-2,-2},{ 0,-2},{-1,-1},{-3,-1}} ,
	{{ 2,-2},{ 3,-1},{ 1,-1},{ 0,-2}} }

make_bitmap_png("a.png",1000,1000,
	function (im)
		local col = {}
		col[1] = im:colorAllocate(  0,   0,   0)
		col[2] = im:colorAllocate(255,   0,   0)
		col[3] = im:colorAllocate(  0, 255,   0)
		col[4] = im:colorAllocate(255, 255,   0)
		col[5] = im:colorAllocate(  0,   0, 255)
		col[6] = im:colorAllocate(255,   0, 255)
		--
		for i,r in ipairs(inner) do
			local rhombus={}
			for j,ip in ipairs(r) do
				table.insert(rhombus , l:pos(ip) + {250,150} )
			end
			im:filledPolygon(rhombus,col[i])
		end
		for i,r in ipairs(outer) do
			local rhombus={}
			for j,ip in ipairs(r) do
				table.insert(rhombus , l:pos(ip) + {250,150} )
			end
			im:filledPolygon(rhombus,col[i])
		end
	end )
