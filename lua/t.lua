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
local i_in,j_in = 5,6--4,5--3,4--2,3 --1,2 --6,1
local star_1=star
local i_in_val = { star.outer[j_in][1] , star.inner[i_in][4] , star.inner[i_in][2] , star.inner[i_in][3] }
local j_in_val = { star.outer[j_in][1] , star.inner[j_in][3] , star.inner[j_in][4] , star.inner[j_in][2] }
local j_out_val= { {0,0} , star.inner[i_in][2] , star.outer[j_in][3] , star.inner[j_in][4] }

star_1.inner[j_in] , star_1.inner[i_in] , star_1.outer[j_in]= j_in_val , i_in_val , j_out_val

--

function draw_star(im,star,origin)
	local in_col = {}
	local out_col = {}
	in_col[1] = im:colorAllocate(  0,   0,   0)
	in_col[2] = im:colorAllocate(255,   0,   0)
	in_col[3] = im:colorAllocate(  0, 255,   0)
	in_col[4] = im:colorAllocate(255, 255,   0)
	in_col[5] = im:colorAllocate(  0,   0, 255)
	in_col[6] = im:colorAllocate(255,   0, 255)
	out_col[1] = im:colorAllocate(100, 100, 100)
	out_col[2] = im:colorAllocate(128,   0,   0)
	out_col[3] = im:colorAllocate(  0, 128,   0)
	out_col[4] = im:colorAllocate(128, 128,   0)
	out_col[5] = im:colorAllocate(  0,   0, 128)
	out_col[6] = im:colorAllocate(128,   0, 128)
	--
	for i,r in ipairs(star.inner) do
		local rhombus={}
		for j,ip in ipairs(r) do
			table.insert(rhombus , l:pos(ip) + origin )
		end
		im:filledPolygon(rhombus,in_col[i])
	end
	for i,r in ipairs(star.outer) do
		local rhombus={}
		for j,ip in ipairs(r) do
			table.insert(rhombus , l:pos(ip) + origin )
		end
		im:filledPolygon(rhombus,out_col[i])
	end
end

make_bitmap_png("a.png",1000,1000,
	function (im)
		draw_star(im,Lattice:star_shape(0),{150,150} )
		draw_star(im,Lattice:star_shape(1),{150,450} )
		draw_star(im,Lattice:star_shape(2),{150,750} )
		draw_star(im,Lattice:star_shape(3),{450,150} )
		draw_star(im,Lattice:star_shape(4),{450,450} )
		draw_star(im,Lattice:star_shape(5),{450,750} )
	end )
