require "Lattice"
require "gd_bitmap"

local ldim =10
local l   = Lattice:new(3 ,ldim)
local l2  = Lattice:new(3 ,ldim/2)

print(l)
local sp = l:pos ( {0,0} )
local ep = l:pos ( {0,0} )

local i = 0
make_bitmap_png("a.png",2000,2400,
	function (im)
		local black = im:colorAllocate(  0,   0,   0)
		--
		local gray0 = im:colorAllocate( 50,  50,  50)
		local gray1 = im:colorAllocate(100, 100, 100)
		local gray2 = im:colorAllocate(150, 150, 150)
		local gray3 = im:colorAllocate(200, 200, 200)
		--
		local red   = im:colorAllocate(255,   0,   0)
		local white = im:colorAllocate(255, 255, 255)

		Lattice:scan(3,{100,60},
			function(ip)
				local j = 1
				local jp = Vector:new( ip:x()*2 , ip:y()*2 )
				--print(i , ip , jp)
				Lattice:star_neighbor(i,jp,
					function(r)
						rhombus = {}
						for k,p in ipairs(r) do
							--print(p)
							table.insert(rhombus,l2:pos(p))
						end
						if( j <= 6 ) then
							if(i==0) then
								--- outside of star
								im:filledPolygon(rhombus,gray2)
							else
								im:filledPolygon(rhombus,gray1)
							end
						else
							if(i==0) then
								-- star
							else
								im:filledPolygon(rhombus,gray0)
							end
						end
						j = j+1
						--
					end )

--~ 				local nb = {}
--~ 				for k,p in ipairs(Lattice:neighbor(ip)) do
--~ 					table.insert(nb,l:pos(p))
--~ 				end
--~ 				im:polygon(nb,red)
				--
				i = (i+1)%7
			end )
	end )

--~ local l  = Lattice:new(3 , 5.0)
--~ function draw_star(im,star,origin)
--~ 	local in_col = {}
--~ 	local out_col = {}
--~ 	in_col[1] = im:colorAllocate(  0,   0,   0)
--~ 	in_col[2] = im:colorAllocate(255,   0,   0)
--~ 	in_col[3] = im:colorAllocate(  0, 255,   0)
--~ 	in_col[4] = im:colorAllocate(255, 255,   0)
--~ 	in_col[5] = im:colorAllocate(  0,   0, 255)
--~ 	in_col[6] = im:colorAllocate(255,   0, 255)
--~ 	out_col[1] = im:colorAllocate(100, 100, 100)
--~ 	out_col[2] = im:colorAllocate(128,   0,   0)
--~ 	out_col[3] = im:colorAllocate(  0, 128,   0)
--~ 	out_col[4] = im:colorAllocate(128, 128,   0)
--~ 	out_col[5] = im:colorAllocate(  0,   0, 128)
--~ 	out_col[6] = im:colorAllocate(128,   0, 128)
--~ 	--
--~ 	for i,r in ipairs(star.inner) do
--~ 		local rhombus={}
--~ 		for j,ip in ipairs(r) do
--~ 			table.insert(rhombus , l:pos(ip) + origin )
--~ 		end
--~ 		im:filledPolygon(rhombus,in_col[i])
--~ 	end
--~ 	for i,r in ipairs(star.outer) do
--~ 		local rhombus={}
--~ 		for j,ip in ipairs(r) do
--~ 			table.insert(rhombus , l:pos(ip) + origin )
--~ 		end
--~ 		im:filledPolygon(rhombus,out_col[i])
--~ 	end
--~ end

--~ local sp = Vector.zero
--~ local ep = Vector.zero
--~ make_bitmap_png("a.png",1000,1000,
--~ 	function (im)
--~ 		local black = im:colorAllocate(  0,   0,   0)
--~ 		Lattice:star_neighbor(2,{10,10},
--~ 			function(r)
--~ 				sp = ep
--~ 				ep = l:pos( (r[1]+r[2]+r[3]+r[4])*0.25 )
--~ 				im:line(sp.x,sp.y,ep.x,ep.y,black)
--~ 			end )
--~ 	end )
