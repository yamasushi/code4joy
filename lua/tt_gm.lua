require "vector"
require "geometry"
require "gd_bitmap"
require "chaos_system"

canvas_frame = Frame:new(Vector:new(0,0),Vector:new(500,500))
canvas_geom  = Geometry:new(canvas_frame)

img_filename = "gm.png"
mu      =-0.485
nu      = 1.0
param_a = 0.008
param_b = 0.05

max_iter= 100000

pi  = function(t) return t*t end
psi = function(t) return t*t end

meta_phi = function(pi,psi)
	return function(t) return pi(t)/(1+psi(t)) end
end

phi = meta_phi(pi,psi)

gm_f = function(x)
	return mu * x + 2*(1-mu)*phi(x)
end

gm_g = function(y)
	return nu *y + param_a * (1-param_b*y*y)*y
end

ds = function(pt)
	local x,y = pt:xy()
	local xx = gm_g(y) + gm_f(x)
	local yy = -x + gm_f(xx)
	return Vector:new(xx,yy)
end

chaos_system = ChaosSystem:new(ds,max_iter)


-- caluculate frame

pt0 = Vector:new(0.1,0.1)
data_frame = chaos_system:calc_frame(pt0,nil)

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
				chaos_system:iterate_with(pt0 , tf ,
					function(pt)
						local x,y = pt:xy()
						--print(x,y)
						im:setPixel(x,y,black)
					end )
			end )
