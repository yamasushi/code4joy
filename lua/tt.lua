require "vector"
require "matrix"
require "frame"
require "geometry"
require "bitmap"

data_frame = Frame:new( Vector:new(100.,100.) , Vector:new(200.,300.) )
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
			function(dc)
				local s = {}
				local e = {}
				s = tf(Vector:new(100.,100.))
				e = tf(Vector:new(100.,300.))
				print(s,e)
				dc:DrawLine(s.x,s.y,e.x,e.y)
				--
				s = tf(Vector:new(100.,100.))
				e = tf(Vector:new(200.,100.))
				print(s,e)
				dc:DrawLine(s.x,s.y,e.x,e.y)
				--
				s = tf(Vector:new(200.,100.))
				e = tf(Vector:new(200.,300.))
				print(s,e)
				dc:DrawLine(s.x,s.y,e.x,e.y)
			end )

