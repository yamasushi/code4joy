require "vector"
require "geometry"
require "gd_bitmap"
require "chaos_system"
require "image_buffer"

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


	local ib = ImageBuffer:new(img_width,img_height)
	local max_h = 0
	local update_max = function(h) max_h = math.max(max_h,h) end
	self:do_iteration(
		function(pt0)
			self.chaos:iterate(pt0,
				function(pt)
					local q = tf(pt)
					local x,y = q:xy()
					ib:update(x,y,1,update_max)
				end )
		end )
	--print("max_h = "..tostring(max_h))
--~ 	ib:smooth( function(h) return (h/max_h >  0.5) end ,update_max)
--~ 	ib:smooth( function(h) return (h/max_h >  0.5) end ,update_max)
--~ 	ib:smooth( function(h) return (h/max_h <= 0.5) end ,update_max)
--~ 	ib:smooth( function(h) return (h/max_h <= 0.5) end ,update_max)
	ib:smooth( function(h) return true end ,update_max)
	make_bitmap_png(self.filename ,
				img_width  ,
				img_height ,
				function(im)
					local white = im:colorAllocate(255,255,255)
					local black = im:colorAllocate(0, 0, 0)
					local red   = im:colorAllocate(255,0,0)
					--
					ib:each_hexcell( function(cell,h)
							local ratio = h/max_h
							local icol  = 255 - math.min( math.floor(ratio*255) , 255 )
							local col   = im:colorAllocate(icol,icol,icol)
							im:filledPolygon(cell,col)
						end )
					--
				end )
end

