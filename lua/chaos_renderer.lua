require "vector"
require "geometry"
require "gd_bitmap"
require "chaos_system"

ChaosRenderer = {}
ChaosRenderer_mt = {__index = ChaosRenderer}

function ChaosRenderer:new(chaos_system,num_traj,oval_r)
	assert(chaos_system)
	assert(num_traj)
	local o = {}
	o.chaos    = chaos_system
	o.num_traj = num_traj
	o.oval_r   = oval_r
	o.filename = tostring(o.chaos.sys)..".png"

	return setmetatable(o,ChaosRenderer_mt)
end

ChaosRenderer_mt.__tostring = function(a)
	return string.format("(chaos=%s,num_traj=%d,oval_r=%f)",
				tostring(a.chaos) ,
				a.num_traj  ,
				a.oval_r )
end


function ChaosRenderer:do_iteration(op)
	assert(self)
	assert(self.num_traj)
	assert(self.oval_r)
	for traj=1 , self.num_traj do
		local theta = math.random() * math.pi * 2
		local pt0=(Vector:trig(theta)) * self.oval_r
		--
		op(pt0)
	end
end

-- caluculate frame
function ChaosRenderer:calc_frame()
	assert(self)
	assert(self.chaos)
	local data_frame = nil
	self:do_iteration(function(pt0)
			data_frame = (self.chaos):calc_frame(pt0,data_frame)
		end )
	return data_frame
end

function ChaosRenderer:render(img_width,img_height)
	assert(self)
	assert(self.chaos)
	assert(self.filename)
	assert(img_width)
	assert(img_height)

	local canvas_frame = Frame:new(Vector:new(0,0),Vector:new(img_width-1,img_height-1))
	local canvas_geom = Geometry:new(canvas_frame)
	local data_geom = Geometry:new( self:calc_frame() )

	local tf = canvas_geom:canvas_transform(data_geom)

	local histgram = {}
	self:do_iteration(
		function(pt0)
			(self.chaos):iterate_with(pt0 , tf ,
				function(pt)
					local x,y = pt:xy()
					local ix = math.floor(x+0.5)
					local iy = math.floor(y+0.5)
					--print(x,y)
					if (histgram[ix] == nil) then
						histgram[ix]= {}
					end
					histgram[ix][iy] = 1

				end )
		end )
	--print(histgram)
	make_bitmap_png(self.filename ,
				img_width  ,
				img_height ,
				function(im)
					local white = im:colorAllocate(255,255,255)
					local black = im:colorAllocate(0, 0, 0)
					for ix,row in pairs(histgram) do
						--print("ix--"..ix)
						for iy,h in pairs(row) do
							--print("  iy--"..iy)
							im:setPixel(ix,iy,black)
						end
					end
				end )
end

