require "vector"
require "geometry"
require "gd_bitmap"

canvas_frame = Frame:new(Vector:new(0,0),Vector:new(500,500))
canvas_geom  = Geometry:new(canvas_frame)

data_frame = Frame:empty()

img_filename = "a.png"
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

-- caluculate frame

pt = Vector:new(0.1,0.1)
for i=1,max_iter do
	data_frame = data_frame:inflate(pt)
	pt = ds(pt)
end

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

				pt = Vector:new(0.1,0.1)
				for i=1,max_iter do
					local x,y = (tf(pt)):xy()
					im:setPixel(x,y,black)
					pt = ds(pt)
				end

			end )

