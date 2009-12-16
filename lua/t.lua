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
local star={}
star.inner = {
	{{ 0, 0},{ 2, 0},{ 3, 1},{ 1, 1}} ,
	{{ 0, 0},{ 1, 1},{ 0, 2},{-1, 1}} ,
	{{ 0, 0},{-1, 1},{-3, 1},{-2, 0}} ,
	{{ 0, 0},{-2, 0},{-3,-1},{-1,-1}} ,
	{{ 0, 0},{-1,-1},{ 0,-2},{ 1,-1}} ,
	{{ 0, 0},{ 1,-1},{ 3,-1},{ 2, 0}} }

star.outer = {
	{{ 4, 0},{ 3, 1},{ 2, 0},{ 3,-1}} ,
	{{ 2, 2},{ 0, 2},{ 1, 1},{ 3, 1}} ,
	{{-2, 2},{-3, 1},{-1, 1},{ 0, 2}} ,
	{{-4, 0},{-3,-1},{-2, 0},{-3, 1}} ,
	{{-2,-2},{ 0,-2},{-1,-1},{-3,-1}} ,
	{{ 2,-2},{ 3,-1},{ 1,-1},{ 0,-2}} }

--
star_1={}
star_1.inner = {
	{ star.outer[1][1] , star.inner[6][4] , star.inner[6][2] , star.inner[6][3] } ,
	{ star.outer[1][1] , star.inner[1][3] , star.inner[1][4] , star.inner[1][2] } ,
	star.inner[2] ,
	star.inner[3] ,
	star.inner[4] ,
	star.inner[5] }

star_1.outer = {
	{ {0,0} , star.inner[6][2] , star.outer[1][3] , star.inner[1][4] } ,
	star.outer[2] ,
	star.outer[3] ,
	star.outer[4] ,
	star.outer[5] ,
	star.outer[6] }
--

function draw_star(im,star,origin)
	local col = {}
	col[1] = im:colorAllocate(  0,   0,   0)
	col[2] = im:colorAllocate(255,   0,   0)
	col[3] = im:colorAllocate(  0, 255,   0)
	col[4] = im:colorAllocate(255, 255,   0)
	col[5] = im:colorAllocate(  0,   0, 255)
	col[6] = im:colorAllocate(255,   0, 255)
	--
	for i,r in ipairs(star.inner) do
		local rhombus={}
		for j,ip in ipairs(r) do
			table.insert(rhombus , l:pos(ip) + {250,150} )
		end
		im:filledPolygon(rhombus,col[i])
	end
	for i,r in ipairs(star.outer) do
		local rhombus={}
		for j,ip in ipairs(r) do
			table.insert(rhombus , l:pos(ip) + {250,150} )
		end
		im:filledPolygon(rhombus,col[i])
	end
end

make_bitmap_png("a.png",1000,1000,
	function (im)
		draw_star(im,star_1,{250,150})
	end )
