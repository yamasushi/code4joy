require "vector"
require "matrix"
require "frame"
require "geometry"
require "gd_bitmap"

data_min = Vector:new(100.,100.)
data_max = Vector:new(200.,300.)
print (data_min:xy())

m = Matrix:new(data_min,data_max)
print (m)
print (m:uv())

data_frame = Frame:new( data_min , data_max )
img_frame  = Frame:new( Vector:new(0.,0.) , Vector:new(5,50.) )

data_geom = Geometry:new(data_frame)
img_geom = Geometry:new(img_frame)

print(data_geom)
print(img_geom)

img_filename = "a.png"
tf = img_geom:canvas_transform(data_geom)
make_bitmap_png(img_filename ,
			img_geom.size.x+1 ,
			img_geom.size.y+1 ,
			function(im)
				local white = im:colorAllocate(255,255,255)
				local black = im:colorAllocate(0, 0, 0)

				local s = {}
				local e = {}
				s = tf(Vector:new(100.,100.))
				e = tf(Vector:new(100.,300.))
				print(s,e)
				im:line(s.x,s.y,e.x,e.y , black)
				--
				s = tf(Vector:new(100.,100.))
				e = tf(Vector:new(200.,100.))
				print(s,e)
				im:line(s.x,s.y,e.x,e.y , black)
				--
				s = tf(Vector:new(200.,100.))
				e = tf(Vector:new(200.,300.))
				print(s,e)
				im:line(s.x,s.y,e.x,e.y , black)
			end )

