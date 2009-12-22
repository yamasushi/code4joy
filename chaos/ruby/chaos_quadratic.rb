# quadratic attractor
# http://www.eng.kagawa-u.ac.jp/~kitaji/bunrui/PDF/nolta99.pdf

include Java

def createPictureFile(imgWidth , imgHeight , imgType , fileName , &op)
	bi  = java.awt.image.BufferedImage.new( imgWidth , imgHeight , java.awt.image.BufferedImage::TYPE_BYTE_GRAY )
	g   = bi.createGraphics()
	g.setPaint(java.awt.Color::WHITE)
	g.fillRect(0,0,imgWidth,imgHeight)
	g.setColor(java.awt.Color::BLACK)
	
	yield(g)
	
	outFile	= java.io.File.new(fileName)
	javax.imageio.ImageIO.write(bi,imgType,outFile)
end

class Point
	attr_accessor :x , :y
	def initialize(x,y)
		@x = x
		@y = y
	end
end

num_trajectory           = 1000
num_point_per_trajectory = 1000


paramA =  0.55
paramB = -0.8596
# paramB = -0.8598
# paramB = -0.865
# paramB = -0.8656
# paramB = -0.866
# paramB = -0.8773
# paramB = -0.8774
# paramB = -0.879

ratio     = 200.0
imgWidth  = 500
imgHeight = 500
offsetx   = imgWidth /2
offsety   = imgHeight/2
imgType   = "png"
fileName  = "a.png"

oval_r    = 0.5
num_ring  = 3

limitPositive = 10.0e6
limitNegative =-limitPositive

createPictureFile(imgWidth , imgHeight , imgType , fileName){ |g|
	rand = java.util.Random.new()
	num_trajectory.times{|j|
		iring = (j % num_ring) + 1 
		r     = (oval_r * iring)/num_ring
		theta = rand.nextDouble * Math::PI * 2
		#
		pt = Point.new( r * Math::cos( theta ) , r * Math::sin( theta ) )
		#
		num_point_per_trajectory.times{|i|
			x  = pt.x
			y  = pt.y
			break if (limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y)
			
			#
			#print pt.x ," , ", pt.y , "\n"
			ix =               ratio*x + offsetx
			iy = imgHeight - ( ratio*y + offsety )
			g.drawLine(ix,iy,ix,iy)
			#
			x_x  = x*x
			y_y  = y*y
			x_y  = x*y
			#
			pt.x = paramA * x + y   
			pt.y =        x_x + paramB
			#
		}
	}
}
