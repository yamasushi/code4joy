require "vector"
require "geometry"
require "gd_bitmap"
require "chaos_system"
require "ds_gumowski_mira"
canvas_frame = Frame:new(Vector:new(0,0),Vector:new(500,500))
canvas_geom  = Geometry:new(canvas_frame)

mu      =-0.485
nu      = 1.0
param_a = 0.008
param_b = 0.05

max_iter= 2000

header = "A"
pi  = function(t) return math.abs(t) end
psi = function(t) return math.abs(t) end

meta_phi = function(pi,psi)
	return function(t) return pi(t)/(1+psi(t)) end
end

gm = DSGumowskiMira:new(header,param_a,param_b,mu,nu,pi,psi,meta_phi)
print(gm)
chaos_system = ChaosSystem:new(gm.ds,max_iter)
img_filename = tostring(gm)..".png"

-- caluculate frame
num_traj = 100
function do_iteration(op)
	for traj=1,num_traj do
		local theta = math.random() * math.pi * 2
		local pt0=Vector:trig(theta)
		--
		op(pt0)
	end
end

data_frame = nil
do_iteration(function(pt0)
			data_frame = chaos_system:calc_frame(pt0,data_frame)
		end )

print(data_frame)
data_geom = Geometry:new(data_frame)
print(data_geom)

tf = canvas_geom:canvas_transform(data_geom)
make_bitmap_png(img_filename ,
			canvas_geom.size.x+1 ,
			canvas_geom.size.y+1 ,
			function(im)
				local white = im:colorAllocate(255,255,255)
				local black = im:colorAllocate(0, 0, 0)
				do_iteration(
					function(pt0)
						chaos_system:iterate_with(pt0 , tf ,
							function(pt)
								local x,y = pt:xy()
								--print(x,y)
								im:setPixel(x,y,black)
							end )
					end )
			end )
